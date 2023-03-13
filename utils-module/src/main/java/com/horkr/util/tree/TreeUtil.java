package com.horkr.util.tree;

import com.horkr.util.CollectionCommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * 树工具类
 *
 * @author llh
 */
public class TreeUtil {

    /**
     * 组树
     *
     * @param root      根节点
     * @param relations 树结构中所有关系集合
     * @return R   根节点
     */
    private static <T extends Relation<R>, R extends Node<T>> R buildTree(R root, Collection<T> relations) {
        // 1. 按照父件id对关系进行分组
        Map<String, List<T>> relationMap = CollectionCommonUtils.groupCollection(relations, Relation::getFromId, Function.identity());
        for (T relation : relations) {
            // 2. 遍历关系中的子件，获取children,塞进去
            R childNode = relation.getChildNode();
            List<T> children = relationMap.get(childNode.getId());
            if (CollectionUtils.isNotEmpty(children)) {
                childNode.addChildren(children);
            }
        }
        // 3. 添加根节点子件
        root.addChildren(relationMap.get(root.getId()));
        return root;
    }


    /**
     * 查找树节点上的循环引用
     *
     * @param node                   根节点
     * @param obtainChildrenFunction 获取子件的方法
     * @param uniqueKeyFunction      获取节点唯一key的方法
     * @param path                   路径字符串
     * @return T  循环的节点
     */
    public static <T> T checkCircleInTree(T node, Function<T, Collection<T>> obtainChildrenFunction, Function<T, String> uniqueKeyFunction, String path) {
        String uniqueKey = uniqueKeyFunction.apply(node);
        if (path.contains(uniqueKey)) {
            return node;
        }
        String newPath = StringUtils.joinWith("-", path, uniqueKey);
        Collection<T> children = obtainChildrenFunction.apply(node);
        if (CollectionUtils.isNotEmpty(children)) {
            for (T child : children) {
                T circleNode = checkCircleInTree(child, obtainChildrenFunction, uniqueKeyFunction, newPath);
                if (nonNull(circleNode)) {
                    return circleNode;
                }
            }
        }
        return null;
    }


    /**
     * 计算树节点总数
     *
     * @param root                   root
     * @param obtainChildrenFunction obtainChildrenFunction
     * @param <T>
     * @return long
     */
    public static <T> long countTree(T root, Function<T, Collection<T>> obtainChildrenFunction) {
        AtomicLong count = new AtomicLong();
        traverseTreeNodeAndConsume(root, obtainChildrenFunction, node -> count.getAndIncrement());
        return count.get();
    }

    /**
     * 遍历树，并附加操作
     *
     * @param root root
     * @author llh
     */
    public static <T> void traverseTreeNodeAndConsume(T root, Function<T, Collection<T>> obtainChildrenFunction, Consumer<T> consumer) {
        if (isNull(root)) {
            return;
        }
        // 用于遍历树的队列
        Queue<T> nodeQueue = new LinkedList<>();
        nodeQueue.offer(root);
        while (!nodeQueue.isEmpty()) {
            T node = nodeQueue.poll();
            consumer.accept(node);
            if (isNull(obtainChildrenFunction)) {
                return;
            }
            Collection<T> children = obtainChildrenFunction.apply(node);
            if (CollectionUtils.isNotEmpty(children)) {
                for (T childNode : children) {
                    nodeQueue.offer(childNode);
                }
            }
        }
    }

    /**
     * 遍历树，并附加操作
     *
     * @param root root
     * @author llh
     */
    public static <T> T traverseTreeNodeAndConsumeChildren(T root, Function<T, Collection<T>> obtainChildrenFunction, Consumer<Collection<T>> consumer) {
        if (isNull(root)) {
            return root;
        }
        // 用于遍历树的队列
        Queue<T> nodeQueue = new LinkedList<>();
        nodeQueue.offer(root);
        while (!nodeQueue.isEmpty()) {
            T node = nodeQueue.poll();
            if (isNull(obtainChildrenFunction)) {
                return root;
            }
            Collection<T> children = obtainChildrenFunction.apply(node);
            if (CollectionUtils.isNotEmpty(children)) {
                consumer.accept(children);
                for (T childNode : children) {
                    nodeQueue.offer(childNode);
                }
            }
        }
        return root;
    }
}
