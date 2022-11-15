package com.horkr.algorithm.base.tree;


import com.horkr.algorithm.base.stack.Stack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

//创建二叉搜索树（坐小右大），二叉搜索树的中序遍历是有序的
//二叉搜索树定义：1.左边所有节点小于根节点  2.右边所有节点大于根节点    3.左右子树也是二叉搜索树
public class BinTreeUtil<E> {
    private static final Logger log = LogManager.getLogger(BinTreeUtil.class);


    public BinaryTreeNode<String> init() {
        BinaryTreeNode<String> root = new BinaryTreeNode<>();
        root.setData("A");
        BinaryTreeNode<String> B = new BinaryTreeNode<>("B");
        BinaryTreeNode<String> C = new BinaryTreeNode<>("C");
        BinaryTreeNode<String> D = new BinaryTreeNode<>("B1");
        BinaryTreeNode<String> E = new BinaryTreeNode<>("B2");
        BinaryTreeNode<String> F = new BinaryTreeNode<>("C1");
        BinaryTreeNode<String> G = new BinaryTreeNode<>("C2");
        root.setLeft(B);
        root.setRight(C);
        B.setLeft(D);
        B.setRight(E);
        C.setRight(G);
        C.setLeft(F);
        return root;
    }

    public void pre(BinaryTreeNode<E> root) {
        if (root != null) {
            log.info(root.getData() + " ");
            if (root.getLeft() != null) {
                pre(root.getLeft());
            }
            if (root.getRight() != null) {
                pre(root.getRight());
            }
        }
    }

    public void mid(BinaryTreeNode<E> root) {
        if (root != null) {
            if (root.getLeft() != null) {
                mid(root.getLeft());
            }
            log.info(root.getData() + " ");
            if (root.getRight() != null) {
                mid(root.getRight());
            }
        }
    }

    public void after(BinaryTreeNode<E> root) {
        if (root != null) {
            if (root.getLeft() != null) {
                after(root.getLeft());
            }
            if (root.getRight() != null) {
                after(root.getRight());
            }
            log.info(root.getData() + " ");
        }
    }

    //
    public void midByStack(BinaryTreeNode<E> root) {
        Stack<BinaryTreeNode<E>> stack = new Stack<>();
        BinaryTreeNode<E> binaryTreeNode = root;
        //栈和节点都空了说明遍历结束了，任何一个不满足都还没遍历完，可以在调试过程中看出。
        // 也可思考，若是栈没空，肯定还要没出来的，节点没空肯定没入过栈
        while (binaryTreeNode != null || !stack.isEmpty()) {
            while (binaryTreeNode != null) {
                stack.push(binaryTreeNode);
                binaryTreeNode = binaryTreeNode.getLeft();
            }
            if (!stack.isEmpty()) {
                binaryTreeNode = stack.pop();
                log.info(binaryTreeNode.getData());
                binaryTreeNode = binaryTreeNode.getRight();
            }
        }
    }

    //按照前序遍历是中左右的顺序，则先压根节点，直接出栈，再先压右后压左，依次循环
    public void preByStack(BinaryTreeNode<E> root) {
        Stack<BinaryTreeNode<E>> s = new Stack<>();
        BinaryTreeNode<E> t = root;
        if (t != null) {
            s.push(root);
        }
        while (!s.isEmpty()) {
            t = s.pop();
            log.info(t.getData());
            if (isNull(t.getLeft()) && isNull(t.getRight())) {
                log.info("当前路径遍历结束");
            }
            if (t.getRight() != null) s.push(t.getRight());
            if (t.getLeft() != null) s.push(t.getLeft());
        }
    }

    //按照前序遍历是中左右的顺序，则先压根节点，直接出栈，再先压右后压左，依次循环
    public void preEveryPath(BinaryTreeNode<E> root) {
        Stack<BinaryTreeNode<E>> s = new Stack<>();
        BinaryTreeNode<E> t = root;
        if (t != null) {
            s.push(root);
        }
        List<BinaryTreeNode<E>> path = new ArrayList<>();
        while (!s.isEmpty()) {
            t = s.pop();
            path.add(t);
            log.info(t.getData());
//            if(isNull(t.getLeft())&&isNull(t.getRight())){
//                System.out.println("当前路径遍历结束:"+path.stream().map(treeNode -> treeNode.getData()).collect(Collectors.toList()));
//                path.remove(t);
//                path.removeIf(node->node.isLastChild());
//            }
            if (t.getRight() != null) {
                t.setLastChild(true);
                s.push(t.getRight());
            }
            if (t.getLeft() != null) s.push(t.getLeft());
        }
    }

    public static void main(String[] args) {
        BinTreeUtil<String> tree = new BinTreeUtil<>();
        BinaryTreeNode<String> root = tree.init();
        tree.preEveryPath(root);
    }

    public void afterByStack(BinaryTreeNode<E> root) {
        //进出栈用
        Stack<BinaryTreeNode<E>> s1 = new Stack<>();
        //保存出栈结果
        Stack<BinaryTreeNode<E>> s2 = new Stack<>();
        BinaryTreeNode<E> t = root;
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
            log.info(t.getData());
        }
    }


}
