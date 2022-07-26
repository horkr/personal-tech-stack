package com.horkr.util;

import com.horkr.util.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Slf4j
public class DefaultTreeComparetor{

    private static final String KEY_DELIMITER = "&&&";

    private static final String DEFAULT_ROOT_KEY = "DEFAULT_ROOT_KEY";

    /**
     * 对比树结构变更
     *
     * @param treeCompareParam 树对比参数
     * @return TreeItem
     */
    public ComparedTreeItem compare(TreeCompareParam treeCompareParam) throws InvocationTargetException, IllegalAccessException {
        TreeItem oldTree = treeCompareParam.getOldTree();
        TreeCompareContext treeCompareContext = buildTreeCompareContext(treeCompareParam);
        List<NewCompareTreeParam> compareTreeParams = treeCompareParam.getCompareTreeParams();
        for (int treeIndex = 0; treeIndex < compareTreeParams.size(); treeIndex++) {
            NewCompareTreeParam compareTreeParam = compareTreeParams.get(treeIndex);
            TreeItem newTree = compareTreeParam.getNewTree();
            treeCompareContext.setNewTree(newTree);
            treeCompareContext.setTreeIndex(treeIndex);
            // 1、对比树，获取到变更map，key为节点在树中的唯一主键（根节点到当前节点的唯一主键累加），value为变更类型
            Map<String, TreeNodeCompareChangeType> allNodeCompareChangeTypeMap = compareTree(oldTree, newTree, treeCompareContext.getUniqueKeyMapping(), treeCompareContext.getUniqueKeyFields());
            treeCompareContext.setAllNodeCompareChangeTypeMap(allNodeCompareChangeTypeMap);
            List<TreeItem> supplementaryTreeNodes = compareTreeParam.getSupplementaryTreeNodes();
            Map<String, TreeItem> supplementaryTreeNodeMap = new HashMap<>();
            supplementaryTreeNodes
                    .forEach(supplementaryTree ->
                            TreeUtil.traverseTreeNodeAndConsume(supplementaryTree, TreeItem::getChildren, node -> supplementaryTreeNodeMap.put(obtainTreeNodeUniqueKey(node, treeCompareContext.getUniqueKeyFields()), node)));
            treeCompareContext.setSupplementaryTreeNodeMap(supplementaryTreeNodeMap);
            // 3、递归结果树，将变更结果赋值到TreeItem的expandData中
            assignAllTreeNodeCompareResult(treeCompareContext);
        }
        return treeCompareContext.getResultTree();
    }

    private TreeCompareContext buildTreeCompareContext(TreeCompareParam treeCompareParam) throws InvocationTargetException, IllegalAccessException {
        TreeCompareContext treeCompareContext = new TreeCompareContext();
        BeanUtils.copyProperties(treeCompareParam, treeCompareContext);
        List<String> uniqueKeyFields = treeCompareParam.getUniqueKeyFields();
        // 克隆老树作为结果树
        ComparedTreeItem resultTree = ComparedTreeItem.createFromTreeItem(treeCompareParam.getOldTree());
        Map<String, TreeItem> oldTreeNodeMap = new HashMap<>();
        // 所有的树节点组成的map
        TreeUtil.traverseTreeNodeAndConsume(resultTree, TreeItem::getChildren, node -> oldTreeNodeMap.put(obtainTreeNodeUniqueKey(node, uniqueKeyFields), node));
        treeCompareContext.setOldTreeNodeMap(oldTreeNodeMap);
        treeCompareContext.setResultTree(ComparedTreeItem.createFromTreeItem(treeCompareParam.getOldTree()));
        Map<String, String> uniqueKeyMapping = treeCompareParam.getUniqueKeyMapping();
        if(isNull(uniqueKeyMapping)){
            return treeCompareContext;
        }
        Map<String, String> finalUniqueKeyMapping = new HashMap<>(uniqueKeyMapping);
        uniqueKeyMapping.forEach((key,value)->{
            finalUniqueKeyMapping.put(value,key);
        });
        treeCompareContext.setUniqueKeyMapping(finalUniqueKeyMapping);
        return treeCompareContext;
    }


    /**
     * 对比树，获取到对比结果（可重载）
     * 1.遍历新老树，将所有的树节点唯一主键整合到list中
     * 2、将新老树的主键集合放在一块对比
     * 3、将对比出来的新增、删除、无变化的数据put到map，便于后续获取指定结果的变更类型
     *
     * @param oldTree          老树
     * @param newTree          新树
     * @param uniqueKeyMapping 唯一主键映射
     * @param uniqueKeyFields  树节点唯一主键字段集合
     * @return Map<String, TreeNodeCompareChangeType>
     */
    public Map<String, TreeNodeCompareChangeType> compareTree(TreeItem oldTree, TreeItem newTree, Map<String, String> uniqueKeyMapping, List<String> uniqueKeyFields) {
        // 1、遍历新老树，将所有的树节点唯一主键整合到list中
        List<String> oldTreeKeys = new ArrayList<>();
        recursiveObtainTreeNodeKey(oldTree, DEFAULT_ROOT_KEY, oldTreeKeys, uniqueKeyFields);
        List<String> newTreeKeys = new ArrayList<>();
        recursiveObtainTreeNodeKey(newTree, DEFAULT_ROOT_KEY, newTreeKeys, uniqueKeyFields);
        // 2、将新老树的主键集合放在一块对比
        ListCompareResult<String> listCompareResult = CollectionCommonUtils.compareList(oldTreeKeys, newTreeKeys);
        // 3.通过映射关系调整增删数据
        resetCompareResult(listCompareResult, uniqueKeyMapping, new HashSet<>(oldTreeKeys), new HashSet<>(newTreeKeys));
        // 4、将对比出来的新增、删除、无变化的数据put到map，便于后续获取指定结果的变更类型
        Map<String, TreeNodeCompareChangeType> changeResult = new HashMap<>();
        listCompareResult.getNoChangeList().forEach(key -> changeResult.put(key, TreeNodeCompareChangeType.NO_CHANGE));
        listCompareResult.getAddList().forEach(key -> changeResult.put(key, TreeNodeCompareChangeType.NEW));
        listCompareResult.getRemoveList().forEach(key -> changeResult.put(key, TreeNodeCompareChangeType.REMOVE));
        return changeResult;
    }

    /**
     * 重新调整对比结果
     *
     * @param listCompareResult 对比结果
     * @param uniqueKeyMapping  主键映射关系
     * @param oldUniqueKeySet   基准树的唯一key
     * @param newUniqueKeySet   对比树的唯一key
     */
    public void resetCompareResult(ListCompareResult<String> listCompareResult, Map<String, String> uniqueKeyMapping, Set<String> oldUniqueKeySet, Set<String> newUniqueKeySet) {
        if (isNull(uniqueKeyMapping) || uniqueKeyMapping.isEmpty()) {
            return;
        }
        List<String> noChangeList = listCompareResult.getNoChangeList();
        List<String> addList = listCompareResult.getAddList();
        List<String> adjustAddKeys = computeNeedAdjustKeys(addList, oldUniqueKeySet, uniqueKeyMapping);
        addList.removeAll(adjustAddKeys);
        noChangeList.addAll(adjustAddKeys);
        List<String> removeList = listCompareResult.getRemoveList();
        List<String> adjustRemoveKeys = computeNeedAdjustKeys(removeList, newUniqueKeySet, uniqueKeyMapping);
        removeList.removeAll(adjustRemoveKeys);
        noChangeList.addAll(adjustRemoveKeys);
    }

    /**
     * 通过映射关系调整删除和新增的key
     *
     * @param changeKeys       新增或者删除的key
     * @param originalKeySet   原始树或者对比树的key
     * @param uniqueKeyMapping 主键映射关系
     * @return List<String>
     */
    private List<String> computeNeedAdjustKeys(List<String> changeKeys, Set<String> originalKeySet, Map<String, String> uniqueKeyMapping) {
        List<String> needAdjustKeys = new ArrayList<>();
        // 将key分割成每个节点的key
        for (String newKey : changeKeys) {
            String[] splitKeyArray = newKey.split(KEY_DELIMITER);
            for (int i = 0; i < splitKeyArray.length; i++) {
                String singleKey = splitKeyArray[i];
                if (isNull(uniqueKeyMapping.get(singleKey))) {
                    continue;
                }
                // 树路径上的节点不可能会重复,直接替换即可
                for (int replaceIndex = i; replaceIndex < splitKeyArray.length; replaceIndex++) {
                    String needReplaceKey = splitKeyArray[replaceIndex];
                    String mappingKey = uniqueKeyMapping.get(needReplaceKey);
                    if (isNull(mappingKey)) {
                        continue;
                    }
                    String replacedKey = newKey.replace(needReplaceKey, mappingKey);
                    if (originalKeySet.contains(replacedKey)) {
//                        log.info("节点：{}通过映射关系映射到{},将从增删节点移动到无变化节点", newKey, replacedKey);
                        needAdjustKeys.add(newKey);
                        break;
                    }
                }
            }
        }
        return needAdjustKeys;
    }


    /**
     * 为每一个节点的对比结果进行赋值
     *
     * @param context context
     */
    private void assignAllTreeNodeCompareResult(TreeCompareContext context) {
        List<String> uniqueKeyFields = context.getUniqueKeyFields();
        Map<String, TreeItem> newTreeNodeMap = new HashMap<>();
        TreeUtil.traverseTreeNodeAndConsume(context.getNewTree(), TreeItem::getChildren, node -> newTreeNodeMap.put(obtainTreeNodeUniqueKey(node, uniqueKeyFields), node));
        context.setNewTreeNodeMap(newTreeNodeMap);
        // 递归对每一个节点进行赋值
        recursiveAssignTreeNodeCompareResult(context.getResultTree(), DEFAULT_ROOT_KEY, context);
        // 根节点对比结果单独赋值
        assignRootNodeCompareResult(context);
    }

    /**
     * 为根节点单独赋值
     *
     * @param context context
     */
    private void assignRootNodeCompareResult(TreeCompareContext context) {
        ComparedTreeItem resultTree = context.getResultTree();
        if (!context.isCompareRoot()) {
            resultTree.addTreeNodeCompareData(TreeNodeCompareData.unKnowResult(), context.getTreeIndex());
        } else {
            TreeNodeCompareData treeNodeCompareData = new TreeNodeCompareData();
            List<InconsistentProperty> inconsistentProperties = obtainInconsistentProperties(context.getOldTree(), context.getNewTree(), context.getCompareProperties());
            treeNodeCompareData.setCompareChangeBody(TreeNodeCompareChangeBody.NODE);
            if (!inconsistentProperties.isEmpty()) {
                treeNodeCompareData.setInconsistentProperties(inconsistentProperties);
                treeNodeCompareData.setCompareChangeType(TreeNodeCompareChangeType.UPDATE);
            } else {
                treeNodeCompareData.setCompareChangeType(TreeNodeCompareChangeType.NO_CHANGE);
            }
            resultTree.addTreeNodeCompareData(treeNodeCompareData);
        }
    }

    /**
     * 递归对每一个节点进行赋值
     * 1。为单个节点的对比结果赋值
     * 2、为节点添加删除的子件
     *
     * @param resultTree          结果树
     * @param nodeUniqueKeyInTree 节点在树中的唯一主键
     */
    private void recursiveAssignTreeNodeCompareResult(ComparedTreeItem resultTree, String nodeUniqueKeyInTree, TreeCompareContext context) {
        // 1。为单个节点的对比结果赋值
        assignCompareBo(resultTree, nodeUniqueKeyInTree, context);
        // 2、处理子件
        List<TreeItem> children = resultTree.getChildren();
        if (CollectionUtils.isNotEmpty(children)) {
            // 这里要将子件替换为对比树
            children.replaceAll(ComparedTreeItem::createFromTreeItem);
            children.forEach(child -> {
                String childUniqueKeyInTree = StringUtils.join(nodeUniqueKeyInTree, KEY_DELIMITER, obtainTreeNodeUniqueKey(child, context.getUniqueKeyFields()));
                recursiveAssignTreeNodeCompareResult((ComparedTreeItem) child, childUniqueKeyInTree, context);
            });
        }
        // 3、从新树中获取新增的子件，添加到结果树中
        // 这里的NEW是指数结构上的NEW
        List<ComparedTreeItem> newChildren = obtainNewChildren(nodeUniqueKeyInTree, context);
        children.addAll(newChildren);
    }

    /**
     * 获取子件对比结果
     *
     * @param parentUniqueKeyInTree 父件在树中的唯一主键
     * @return Map<String, TreeNodeCompareChangeType>
     */
    private List<ComparedTreeItem> obtainNewChildren(String parentUniqueKeyInTree, TreeCompareContext context) {
        List<ComparedTreeItem> newChildren = new ArrayList<>();
        for (Map.Entry<String, TreeNodeCompareChangeType> entry : context.getAllNodeCompareChangeTypeMap().entrySet()) {
            String childUniqueKeyInTree = entry.getKey();
            TreeNodeCompareChangeType compareChangeType = entry.getValue();
            if (!compareChangeType.equals(TreeNodeCompareChangeType.NEW)) {
                continue;
            }
            // 去除父件的id，和第一个分隔符，如果是子件则只会剩下子件自身的id，不会包含分隔符
            if (childUniqueKeyInTree.startsWith(parentUniqueKeyInTree)) {
                String nodeUniqueKey = childUniqueKeyInTree.replace(parentUniqueKeyInTree, StringUtils.EMPTY).replaceFirst(KEY_DELIMITER, StringUtils.EMPTY);
                if (!nodeUniqueKey.contains(KEY_DELIMITER)) {
                    Map<String, TreeItem> newTreeNodeMap = context.getNewTreeNodeMap();
                    Map<String, String> uniqueKeyMapping = context.getUniqueKeyMapping();
                    TreeItem newChild = isNull(newTreeNodeMap.get(nodeUniqueKey))?newTreeNodeMap.get(uniqueKeyMapping.get(nodeUniqueKey)):newTreeNodeMap.get(nodeUniqueKey);
                    TreeUtil.traverseTreeNodeAndConsumeChildren(newChild, TreeItem::getChildren, children -> {
                        List<TreeItem> childrenList = (List<TreeItem>) children;
                        childrenList.replaceAll(child -> createNewNodeFromNewTreeNode(child, context.getSupplementaryTreeNodeMap(), context.getUniqueKeyFields(), context.getTreeIndex()));
                    });
                    ComparedTreeItem newNodeFromNewTreeNode = createNewNodeFromNewTreeNode(newChild, context.getSupplementaryTreeNodeMap(), context.getUniqueKeyFields(), context.getTreeIndex());
                    newChildren.add(newNodeFromNewTreeNode);
                }
            }
        }
        return newChildren;
    }


    private ComparedTreeItem createNewNodeFromNewTreeNode(TreeItem newNode, Map<String, TreeItem> supplementaryTreeNodeMap, List<String> uniqueKeyFields, int treeIndex) {
        ComparedTreeItem newChildComparedTreeItem = ComparedTreeItem.createFromTreeItem(newNode);
        TreeNodeCompareData treeNodeCompareData = new TreeNodeCompareData();
        treeNodeCompareData.setCompareChangeType(TreeNodeCompareChangeType.NEW);
        treeNodeCompareData.setCompareChangeBody(isNull(supplementaryTreeNodeMap.get(obtainTreeNodeUniqueKey(newNode, uniqueKeyFields))) ? TreeNodeCompareChangeBody.RELATION : TreeNodeCompareChangeBody.RELATION_AND_NOE);
        newChildComparedTreeItem.addTreeNodeCompareData(treeNodeCompareData, treeIndex);
        return newChildComparedTreeItem;
    }


    /**
     * 为单个节点的对比结果赋值
     * 1、NEW类型的区分是节点新建还是结构新建
     * 2、结构无变化的节点判断属性是否变更
     *
     * @param treeNode            结果树节点
     * @param nodeUniqueKeyInTree 节点在树中的唯一主键
     */
    private void assignCompareBo(ComparedTreeItem treeNode, String nodeUniqueKeyInTree, TreeCompareContext context) {
        TreeNodeCompareData treeNodeCompareData = new TreeNodeCompareData();
        Map<String, TreeNodeCompareChangeType> allNodeCompareChangeTypeMap = context.getAllNodeCompareChangeTypeMap();
        TreeNodeCompareChangeType treeNodeCompareChangeType = allNodeCompareChangeTypeMap.get(nodeUniqueKeyInTree);
        treeNodeCompareData.setCompareChangeType(treeNodeCompareChangeType);
        treeNodeCompareData.setCompareChangeBody(TreeNodeCompareChangeBody.RELATION_AND_NOE);
        // 这里的NO_CHANGE是指树结构上无变化
        if (TreeNodeCompareChangeType.NO_CHANGE.equals(treeNodeCompareChangeType)) {
            String nodeUniqueKey = obtainTreeNodeUniqueKey(treeNode, context.getUniqueKeyFields());
            // 这里不处理根节点，因为根节点的默认结构上是无变化的，所有共用了同一主键，在这里是不知道根节点的主键是什么的。后续会单独处理
            if (DEFAULT_ROOT_KEY.equals(nodeUniqueKeyInTree)) {
                return;
            }
            Map<String, String> uniqueKeyMapping = context.getUniqueKeyMapping();
            Map<String, TreeItem> oldTreeNodeMap = context.getOldTreeNodeMap();
            Map<String, TreeItem> newTreeNodeMap = context.getNewTreeNodeMap();
            TreeItem oldNode = isNull(oldTreeNodeMap.get(nodeUniqueKey))?oldTreeNodeMap.get(uniqueKeyMapping.get(nodeUniqueKey)):oldTreeNodeMap.get(nodeUniqueKey);
            TreeItem newTreeNode = isNull(newTreeNodeMap.get(nodeUniqueKey))?newTreeNodeMap.get(uniqueKeyMapping.get(nodeUniqueKey)):newTreeNodeMap.get(nodeUniqueKey);
            // 获取差异属性
            List<InconsistentProperty> inconsistentProperties = obtainInconsistentProperties(oldNode, newTreeNode, context.getCompareProperties());
            if (!inconsistentProperties.isEmpty()) {
                treeNodeCompareData.setInconsistentProperties(inconsistentProperties);
                treeNodeCompareData.setCompareChangeType(TreeNodeCompareChangeType.UPDATE);
                treeNodeCompareData.setCompareChangeBody(TreeNodeCompareChangeBody.NODE);
            }
        }
        if (TreeNodeCompareChangeType.REMOVE.equals(treeNodeCompareChangeType)) {
            treeNodeCompareData.setCompareChangeBody(TreeNodeCompareChangeBody.RELATION);
        }
        treeNode.addTreeNodeCompareData(treeNodeCompareData);
    }


    /**
     * @param oldNode           原始节点
     * @param newNode           新节点
     * @param compareProperties 对比属性
     * @return List<InconsistentProperty>
     */
    private List<InconsistentProperty> obtainInconsistentProperties(TreeItem oldNode, TreeItem newNode, List<String> compareProperties) {
        return compareProperties.stream().map(prop -> {
            // TODO 待改造
//            Object oldData = ReflectUtil.parseObject(oldNode.getData(), prop);
//            Object newData = ReflectUtil.parseObject(newNode.getData(), prop);
            Object oldData = null;
            Object newData = null;
            String oldValue = isNull(oldData) ? null : oldData.toString();
            String newValue = isNull(newData) ? null : newData.toString();
            return Objects.equals(oldValue, newValue) ? null : new InconsistentProperty(prop, oldValue, newValue);
        }).filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    /**
     * 根据主键字段获取单个树节点唯一主键
     *
     * @param treeNode 树节点
     * @return String
     */
    public String obtainTreeNodeUniqueKey(TreeItem treeNode, List<String> uniqueKeyFields) {
        Object treeNodeData = treeNode.getData();
        // TODO 待改造
        return uniqueKeyFields.stream().map(field -> {
//            Object fieldValue = ReflectUtil.parseObject(treeNodeData, field);
            Object fieldValue = null;
            return isNull(fieldValue) ? null : fieldValue.toString();
        }).filter(Objects::nonNull)
                .collect(Collectors.joining());
    }


    /**
     * 递归获取树节点的唯一主键.根节点到当前节点路径的唯一主键累加
     *
     * @param treeNode          树节点
     * @param uniqueKeysInTree  树节点在树中的唯一主键
     * @param allNodeUniqueKeys 存储所有树节点唯一主键的集合
     */
    private void recursiveObtainTreeNodeKey(TreeItem treeNode, String uniqueKeysInTree, List<String> allNodeUniqueKeys, List<String> uniqueKeyFields) {
        allNodeUniqueKeys.add(uniqueKeysInTree);
        List<TreeItem> children = treeNode.getChildren();
        if (CollectionUtils.isNotEmpty(children)) {
            children.forEach(child -> recursiveObtainTreeNodeKey(child, StringUtils.join(uniqueKeysInTree, KEY_DELIMITER, obtainTreeNodeUniqueKey(child, uniqueKeyFields)), allNodeUniqueKeys, uniqueKeyFields));
        }
    }


}
