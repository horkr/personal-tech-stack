package com.horkr.algorithm.base.tree;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MutiChildNode {
    private String data;
    private List<MutiChildNode> childNodes;

    public static void main(String[] args) {
        MutiChildNode build = build();
//        List<List<MutiChildNode>> lists = binaryTreePaths1(build);
//        for (List<MutiChildNode> list : lists) {
//            List<String> collect = list.stream().map(MutiChildNode::getData).collect(Collectors.toList());
//            System.out.println(collect);
//        }
    }

    private static MutiChildNode build(){
        List<MutiChildNode> children = new ArrayList<>();
        MutiChildNode root = new MutiChildNode(UUID.randomUUID().toString().substring(0, 3), children);
        for (int i = 1; i < 4; i++) {
            children.add(new MutiChildNode("1_"+i, new ArrayList<>()));
        }
        for (MutiChildNode child : children) {
            for (int i = 1; i < 4; i++) {
                child.getChildNodes().add(new MutiChildNode(child.getData()+"_"+i, new ArrayList<>()));
            }
        }
        return root;
    }

    //按照前序遍历是中左右的顺序，则先压根节点，直接出栈，再先压右后压左，依次循环
    public static void preEveryPath(MutiChildNode root) {
        Stack<MutiChildNode> s = new Stack<>();
        MutiChildNode t = root;
        if (t != null) {
            s.push(root);
        }
        List<MutiChildNode> path = new ArrayList<>();
        while (!s.isEmpty()) {
            t = s.pop();
            path.add(t);
            System.out.println(t.getData());
//            if(isNull(t.getLeft())&&isNull(t.getRight())){
//                System.out.println("当前路径遍历结束:"+path.stream().map(treeNode -> treeNode.getData()).collect(Collectors.toList()));
//                path.remove(t);
//                path.removeIf(node->node.isLastChild());
//            }
            for (MutiChildNode childNode : t.getChildNodes()) {
                s.push(childNode);
            }
        }
    }
        public static List<String> binaryTreePaths(MutiChildNode root) {
            List<String> paths = new ArrayList<>();
            if (root == null) {
                return paths;
            }
            Queue<MutiChildNode> nodeQueue = new LinkedList<>();
            Queue<String> pathQueue = new LinkedList<>();

            nodeQueue.offer(root);
            pathQueue.offer(root.getData());

            while (!nodeQueue.isEmpty()) {
                MutiChildNode node = nodeQueue.poll();
                String path = pathQueue.poll();

                if (node.getChildNodes().isEmpty()) {
                    paths.add(path);
                } else {
                    List<MutiChildNode> childNodes = node.getChildNodes();
                    for (MutiChildNode childNode : childNodes) {
                        nodeQueue.offer(childNode);
                        pathQueue.offer(new StringBuffer(path).append("->").append(childNode.getData()).toString());
                    }
                }
            }
            return paths;
        }
    public static List<List<MutiChildNode>> binaryTreePaths1(MutiChildNode root) {
        List<List<MutiChildNode>> paths = new ArrayList<>();
        if (root == null) {
            return paths;
        }
        Queue<MutiChildNode> nodeQueue = new LinkedList<>();
        Queue<List<MutiChildNode>> pathQueue = new LinkedList<>();

        nodeQueue.offer(root);
        List<MutiChildNode> path = new ArrayList<>();
        path.add(root);
        pathQueue.offer(path);

        while (!nodeQueue.isEmpty()) {
            MutiChildNode node = nodeQueue.poll();
            List<MutiChildNode> pollPath = pathQueue.poll();
            if (node.getChildNodes().isEmpty()) {
                paths.add(pollPath);
            } else {
                List<MutiChildNode> childNodes = node.getChildNodes();
                for (MutiChildNode childNode : childNodes) {
                    nodeQueue.offer(childNode);
                    List<MutiChildNode> newPath = new ArrayList<>(pollPath);
                    newPath.add(childNode);
                    pathQueue.offer(newPath);
                }
            }
        }
        return paths;
    }

    /**
     * 非递归方法输出所有路径
     */
    public static List<List<MutiChildNode>> listAllPathByNotRecursion(MutiChildNode root){
        List<List<MutiChildNode>> pathList = new ArrayList<>();
        List<MutiChildNode> path = new ArrayList<>();
        //主栈，用于计算处理路径
        Deque<MutiChildNode> majorStack = new ArrayDeque();
        //副栈，用于存储待处理节点
        Deque<MutiChildNode> minorStack = new ArrayDeque();
        minorStack.addLast(root);

        HashMap<String,Integer> popCount = new HashMap<>();
        String curString  = "";

        while(!minorStack.isEmpty()){
            //出副栈，入主栈
            MutiChildNode minLast = minorStack.pollLast();
            majorStack.addLast(minLast);
            path.add(minLast);
//            curString+=minLast.getData()+"->";
            //将该节点的子节点入副栈
            if(!minLast.getChildNodes().isEmpty()){
                List<MutiChildNode> childs = minLast.getChildNodes();
                Iterator<MutiChildNode> iterator = childs.iterator();
                while(iterator.hasNext()){
                    minorStack.addLast(iterator.next());
                }
            }
            //出主栈
            MutiChildNode majLast = majorStack.peekLast();
            //循环条件：栈顶为叶子节点 或 栈顶节点孩子节点遍历完了（需要注意空指针问题）
            while(majLast.getChildNodes().size() ==0 ||
                    (popCount.get(majLast.getData())!=null && popCount.get(majLast.getData()).equals(majLast.getChildNodes().size()))){

                MutiChildNode last = majorStack.pollLast();
                majLast = majorStack.peekLast();
                //此时主栈为空，运算完毕
                if(majLast == null){
                    return pathList;
                }
                //第一次弹出孩子节点，弹出次数设为1
                if(popCount.get(majLast.getData())==null){
                    popCount.put(majLast.getData(),1);
                }else{
                    //非第一次弹出孩子节点，在原有基础上加1
                    popCount.put(majLast.getData(),popCount.get(majLast.getData())+1);
                }
                String lastContent = last.getData();
                //如果是叶子节点才将结果加入路径集中
                if(last.getChildNodes().isEmpty()){
                    pathList.add(path);
//                    pathList.add(curString.substring(0,curString.length()-2));
                    path = new ArrayList<>();
                }
                //调整当前curString，减去2是减的“->”这个符号
//                curString = curString.substring(0,curString.length()-lastContent.length()-2);
            }
        }
        return pathList;
    }
}


