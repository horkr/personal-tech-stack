package com.horkr.algorithm.base.trie;

import java.util.*;

public class AcAuto {
    public static class HitResult {
        /**
         * 命中关键词
         */
        private final String keyword;

        /**
         * 命中位置
         */
        private final int index;

        //关键词
        public HitResult(int index, String keyword) {
            this.index = index;
            this.keyword = keyword;
        }

        public int index() {
            return index;
        }

        public String keyword() {
            return keyword;
        }
    }

    /**
     * <p>Title: MyOneACSearchTest.java</p>
     * <p>Description:ac自动机 </p>
     * <p>Copyright: Copyright (c) 2017</p>
     * <p>Company: Sage</p>
     *
     * @author 五虎将
     * @version 1.0
     * @date 2017年1月5日上午8:19:34
     */
    public static class MyOneACSearchTest {
        public static void main(String[] args) {
            String[] keywords = new String[]{"我是好人", "我是坏人", "好人", "坏人", "世界", "那么大", "世界那么大", "大"};
            MyOneACSearchTest search = new MyOneACSearchTest(keywords);
            HitResult[] findAll = search.findAll("我是好人吗?这事需要问问自己,人能分成好人坏人吗?这恐怕谁也无法解答.世界那么大,给你的想法那么大,我们世界里,只能想想大而已");
            for (HitResult result : findAll) {
                System.out.println(result.keyword() + " : " + result.index());
            }

        }

        //构建树
        //设置失败指针
        //搜索过程

        public MyOneACSearchTest(String[] keywords) {
            buildTree(keywords);
            addFailure();
        }

        private TreeNode root;

        //查找全部的模式串
        public HitResult[] findAll(String text) {
            //可以找到 转移到下个节点 不能找到在失败指针节点中查找直到为root节点
            ArrayList<HitResult> results = new ArrayList<>();
            int index = 0;
            TreeNode mid = root;
            while (index < text.length()) {

                TreeNode temp = null;

                while (temp == null) {
                    temp = mid.getSonNode(text.charAt(index));
                    if (mid == root) {
                        break;
                    }
                    if (temp == null) {
                        mid = mid.failure;
                    }
                }
                //mid为root 再次进入循环 不需要处理  或者 temp不为空找到节点 节点位移
                if (temp != null) mid = temp;

                for (String result : mid.getResults()) {
                    results.add(new HitResult(index - result.length() + 1, result));
                }
                index++;
            }
            return results.toArray(new HitResult[results.size()]);
        }


        private void addFailure() {
            //设置二层失败指针为根节点 收集三层节点

            //DFA遍历所有节点 设置失败节点 原则: 节点的失败指针在父节点的失败指针的子节点中查找 最大后缀匹配
            ArrayList<TreeNode> mid = new ArrayList<TreeNode>();//过程容器
            for (TreeNode node : root.getSonsNode()) {
                node.failure = root;
                for (TreeNode treeNode : node.getSonsNode()) {
                    mid.add(treeNode);
                }
            }

            //广度遍历所有节点设置失败指针 1.存在失败指针 2.不存在到root结束
            while (mid.size() > 0) {
                ArrayList<TreeNode> temp = new ArrayList<TreeNode>();//子节点收集器

                for (TreeNode node : mid) {

                    TreeNode r = node.getParent().failure;

                    while (r != null && !r.containNode(node.getChar())) {
                        r = r.failure;//没有找到,保证最大后缀 (最后一个节点字符相同)
                    }

                    //是根结
                    if (r == null) {
                        node.failure = root;
                    } else {
                        node.failure = r.getSonNode(node.getChar());
                        //重叠后缀的包含
                        for (String result : node.failure.getResults()) {
                            node.addResult(result);
                        }
                    }

                    //收集子节点
                    for (TreeNode treeNode : node.getSonsNode()) {
                        temp.add(treeNode);
                    }

                }
                mid = temp;
            }
            root.failure = root;
        }

        private void buildTree(String[] keywords) {
            root = new TreeNode(null, ' ');
            //判断节点是否存在 存在转移 不存在添加
            for (String word : keywords) {
                TreeNode currentNode = root;
                for (char ch : word.toCharArray()) {
                    if (currentNode.containNode(ch)) {
                        currentNode = currentNode.getSonNode(ch);
                    } else {
                        TreeNode newNode = new TreeNode(currentNode, ch);
                        currentNode.addSonNode(newNode);
                        currentNode = newNode;
                    }
                }
                currentNode.addResult(word);
            }
        }


        class TreeNode {

            private TreeNode parent;

            private TreeNode failure;

            private char ch;

            private ArrayList<String> results;

            private Map<Character, TreeNode> childMap;

            private TreeNode[] childArr;


            public TreeNode(TreeNode parent, char ch) {
                this.parent = parent;
                this.ch = ch;
                results = new ArrayList<String>();
                childMap = new HashMap<>();
                childArr = new TreeNode[]{};
            }

            //添加子节点
            public void addSonNode(TreeNode node) {
                childMap.put(node.ch, node);
                childArr = new TreeNode[childMap.size()];
                Iterator<TreeNode> iterator = childMap.values().iterator();
                for (int i = 0; i < childArr.length; i++) {
                    if (iterator.hasNext()) {
                        childArr[i] = iterator.next();
                    }
                }
            }

            //获取子节点中指定字符节点
            public TreeNode getSonNode(char ch) {
                return childMap.get(ch);
            }

            //判断子节点中是否存在该字符
            public boolean containNode(char ch) {
                return getSonNode(ch) != null;
            }

            //添加一个结果到结果字符中
            public void addResult(String result) {
                if (!results.contains(result)) results.add(result);
            }

            //获取字符
            public char getChar() {
                return ch;
            }

            //获取父节点
            public TreeNode getParent() {
                return parent;
            }

            //设置失败指针并且返回
            public TreeNode setFailure(TreeNode failure) {
                this.failure = failure;
                return this.failure;
            }

            //获取所有的孩子节点
            public TreeNode[] getSonsNode() {
                return childArr;
            }

            //获取搜索的字符串
            public List<String> getResults() {
                return results;
            }
        }
    }

}
