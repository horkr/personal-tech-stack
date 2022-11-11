package com.horkr.algorithm.base.trie;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Ac {
    private static final Logger log = LogManager.getLogger(Ac.class);


    public static class TrieNode {
        private final Map<Character, TrieNode> childMap = new HashMap<>();
        private Character data;
        private TrieNode failNode;
        private List<Integer> wordLengths = new ArrayList<>();
        private TrieNode parent;

        public String word() {
            List<Character> result = new ArrayList<>();
            result.add(data);
            TrieNode parentNode = this.parent;
            while (nonNull(parentNode)&&nonNull(parentNode.data)) {
                result.add(parentNode.data);
                parentNode = parentNode.parent;
            }
            Collections.reverse(result);
            StringBuffer buffer = new StringBuffer();
            for (Character character : result) {
                buffer.append(character);
            }
            return buffer.toString();
        }

        public TrieNode(Character data) {
            this.data = data;
        }

        public TrieNode() {
        }

        public Map<Character, TrieNode> childMap() {
            return childMap;
        }

        public Character data() {
            return data;
        }

        public TrieNode getFailNode() {
            return failNode;
        }

        public void setFailNode(TrieNode failNode) {
            this.failNode = failNode;
        }

        public List<Integer> getWordLengths() {
            return wordLengths;
        }

        public void setWordLengths(List<Integer> wordLengths) {
            this.wordLengths = wordLengths;
        }

        public TrieNode findChild(char data) {
            return childMap.get(data);
        }

        public void addChild(char data) {
            TrieNode node = new TrieNode(data);
            node.setParent(this);
            childMap.put(data,node);
        }


        public void addWordLengths(int length) {
            wordLengths.add(length);
        }

        public TrieNode getParent() {
            return parent;
        }

        public void setParent(TrieNode parent) {
            this.parent = parent;
        }
    }

    public static void main(String[] args) {
        TrieTree trieTree = new TrieTree();
        List<String> words = Arrays.asList("he", "she", "hers", "his");
//        List<String> words = Arrays.asList("华人", "人民共", "共和", "民共");
        acBuild(trieTree, words);
        List<String> ahishers = acQuery(trieTree, "ahishers");
//        for (String ahisher : ahishers) {
//            System.out.println(ahisher);
//        }
    }


    public static List<String> acQuery(TrieTree trieTree, String word) {
        List<String> result = new ArrayList<>();
        TrieNode node = trieTree.root;
        char[] chars = word.toCharArray();
        for (int charIndex = 0; charIndex < chars.length; charIndex++) {
            char aChar = chars[charIndex];
            log.info("------开始查找字符 [{}]------------", aChar);
            while (isNull(node.findChild(aChar)) && nonNull(node.getFailNode())) {
                log.info("当前节点：{}无法找到：{}",node.word(),aChar);
                node = node.getFailNode();
                log.info("转移到失败节点：{}",node.word());
            }
            if (nonNull(node.findChild(aChar))) {
                node = node.findChild(aChar);
                log.info("找到了字符 [{}], 现在查找的起始节点为 [{}]", aChar, node.word());
            } else {
                log.info("未查找到 [{}]", aChar);
            }
            List<Integer> wordLengths = node.getWordLengths();
            if (!wordLengths.isEmpty()) {
                for (Integer wordLength : wordLengths) {
                    String findWord = word.substring(charIndex - wordLength + 1, charIndex + 1);
                    result.add(findWord);
                    log.info("当前字符对应如下结果 :{}", findWord);
                }
            }
            log.info("结束查找 [{}]", aChar);
        }
        return result;
    }



    public static void acBuild(TrieTree trieTree, List<String> words) {
        for (String word : words) {
            trieTree.insert(word);
        }
        LinkedList<TrieNode> queue = new LinkedList<>();
        // 设置第一层的fail指针
        trieTree.root.childMap.values().forEach(trieNode -> {
            trieNode.setFailNode(trieTree.root);
            queue.push(trieNode);
        });
        while (!queue.isEmpty()) {
            TrieNode parent = queue.pop();
            Collection<TrieNode> childList = parent.childMap().values();
            /**
             * 下层fail指针设置,利用队列实现层序遍历，挨个设置
             * 设当前节点为node,找到父节点的fail指针faFail,在faFail的子节点中查找和node同值的节点expect,如果expect不为空，那么node.fail=expect.否则node.fail=root
             */
            for (TrieNode child : childList) {
                TrieNode failNode = parent.failNode;
                TrieNode fail = failNode.findChild(child.data);
                if (nonNull(fail)) {
                    child.setFailNode(fail);
                    if (!fail.getWordLengths().isEmpty()) {
                        //存储fail指针数据的索引
                        child.getWordLengths().addAll(fail.getWordLengths());
                    }
                } else {
                    child.setFailNode(trieTree.root);
                }
                queue.push(child);
            }
        }
    }

    public static class TrieTree {
        private final TrieNode root = new TrieNode();

        public void insert(String word) {
            if (StringUtils.isBlank(word)) {
                return;
            }
            TrieNode currentNode = root;
            char[] chars = word.toCharArray();
            for (char item : chars) {
                TrieNode child = currentNode.findChild(item);
                if (isNull(child)) {
                    currentNode.addChild(item);
                }
                currentNode = currentNode.findChild(item);
            }
            currentNode.addWordLengths(word.length());
        }


        public boolean contains(String word) {
            if (StringUtils.isBlank(word)) {
                return false;
            }
            TrieNode currentNode = root;
            char[] chars = word.toCharArray();
            for (char item : chars) {
                TrieNode child = currentNode.findChild(item);
                if (nonNull(child)) {
                    currentNode = child;
                } else {
                    return false;
                }
            }
            return !currentNode.getWordLengths().isEmpty();
        }
    }
}
