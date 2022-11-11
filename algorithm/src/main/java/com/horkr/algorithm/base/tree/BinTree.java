package com.horkr.algorithm.base.tree;


import stack.Stack;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

//创建二叉搜索树（坐小右大），二叉搜索树的中序遍历是有序的
//二叉搜索树定义：1.左边所有节点小于根节点  2.右边所有节点大于根节点    3.左右子树也是二叉搜索树
public class BinTree<E> {
    public TreeNode init() {
        TreeNode root = new TreeNode();
        root.setData("A");
        TreeNode<String> B = new TreeNode<>("B");
        TreeNode<String> C = new TreeNode<>("C");
        TreeNode<String> D = new TreeNode<>("B1");
        TreeNode<String> E = new TreeNode<>("B2");
        TreeNode<String> F = new TreeNode<>("C1");
        TreeNode<String> G = new TreeNode<>("C2");
        root.setLeft(B);
        root.setRight(C);
        B.setLeft(D);
        B.setRight(E);
        C.setRight(G);
        C.setLeft(F);
        return root;
    }

    public void pre(TreeNode root) {
        if (root != null) {
            System.out.print(root.getData() + " ");
            if (root.getLeft() != null) {
                pre(root.getLeft());
            }
            if (root.getRight() != null) {
                pre(root.getRight());
            }
        }
    }

    public void mid(TreeNode root) {
        if (root != null) {
            if (root.getLeft() != null) {
                mid(root.getLeft());
            }
            System.out.print(root.getData() + " ");
            if (root.getRight() != null) {
                mid(root.getRight());
            }
        }
    }

    public void after(TreeNode root) {
        if (root != null) {
            if (root.getLeft() != null) {
                after(root.getLeft());
            }
            if (root.getRight() != null) {
                after(root.getRight());
            }
            System.out.print(root.getData() + " ");
        }
    }

    //
    public void midByStack(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        TreeNode<Integer> treeNode = root;
        //栈和节点都空了说明遍历结束了，任何一个不满足都还没遍历完，可以在调试过程中看出。
        // 也可思考，若是栈没空，肯定还要没出来的，节点没空肯定没入过栈
        while (treeNode != null || !stack.isEmpty()) {
            while (treeNode != null) {
                stack.push(treeNode);
                treeNode = treeNode.getLeft();
            }
            if (!stack.isEmpty()) {
                treeNode = stack.pop();
                System.out.println(treeNode.getData());
                treeNode = treeNode.getRight();
            }
        }
    }

    //按照前序遍历是中左右的顺序，则先压根节点，直接出栈，再先压右后压左，依次循环
    public void preByStack(TreeNode root) {
        Stack<TreeNode> s = new Stack<>();
        TreeNode<Integer> t = root;
        if (t != null) {
            s.push(root);
        }
        while (!s.isEmpty()) {
            t = s.pop();
            System.out.println(t.getData());
            if(isNull(t.getLeft())&&isNull(t.getRight())){
                System.out.println("当前路径遍历结束");
            }
            if (t.getRight() != null) s.push(t.getRight());
            if (t.getLeft() != null) s.push(t.getLeft());
        }
    }

    //按照前序遍历是中左右的顺序，则先压根节点，直接出栈，再先压右后压左，依次循环
    public void preEveryPath(TreeNode root) {
        Stack<TreeNode> s = new Stack<>();
        TreeNode<Integer> t = root;
        if (t != null) {
            s.push(root);
        }
        List<TreeNode> path = new ArrayList<>();
        while (!s.isEmpty()) {
            t = s.pop();
            path.add(t);
            System.out.println(t.getData());
//            if(isNull(t.getLeft())&&isNull(t.getRight())){
//                System.out.println("当前路径遍历结束:"+path.stream().map(treeNode -> treeNode.getData()).collect(Collectors.toList()));
//                path.remove(t);
//                path.removeIf(node->node.isLastChild());
//            }
            if (t.getRight() != null){
                t.setLastChild(true);
                s.push(t.getRight());
            }
            if (t.getLeft() != null) s.push(t.getLeft());
        }
    }

    public static void main(String[] args) {
        BinTree<Integer> tree = new BinTree<>();
        TreeNode root = tree.init();
        tree.preEveryPath(root);
    }

    public void afterByStack(TreeNode root) {
        //进出栈用
        Stack<TreeNode> s1 = new Stack();
        //保存出栈结果
        Stack<TreeNode> s2 = new Stack();
        TreeNode t = root;
        while (t != null || !s1.isEmpty()) {
            if (t != null) {
                s2.push(t);
                s1.push(t);
                t = t.getRight();
            } else {
                t = s1.pop();
                t = t.getLeft();
            }
        }
        while (!s2.isEmpty()) {
            t = s2.pop();
            System.out.println(t.getData());
        }
    }



}
