package com.horkr.algorithm.base.trie.right;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * ac自动机变种字典树
 */
public class TrieTree {
    /**
     * 根节点
     */
    private final TrieTreeNode root = new TrieTreeNode();

    /**
     * 将一个词插入到字典树中
     *
     * @param word word
     */
    public void insert(String word) {
        if (StringUtils.isBlank(word)) {
            return;
        }
        TrieTreeNode currentNode = root;
        char[] chars = word.toCharArray();
        for (char item : chars) {
            TrieTreeNode child = currentNode.findChild(item);
            // 如果不在当前节点下，添加一个
            if (isNull(child)) {
                currentNode.addChild(item);
            }
            // 处理下一个字母
            currentNode = currentNode.findChild(item);
        }
        // 此处word已添加完,尾部填充word length
        currentNode.addWordLengths(word.length());
    }

    /**
     * 构建AC字典树
     *
     * @param words words
     */
    public void build(List<String> words) {
        for (String word : words) {
            insert(word);
        }
        LinkedList<TrieTreeNode> queue = new LinkedList<>();
        // 设置第一层的fail指针
        root.childMap().values().forEach(trieNode -> {
            trieNode.setFailNode(root);
            queue.push(trieNode);
        });
        while (!queue.isEmpty()) {
            TrieTreeNode parent = queue.pop();
            Collection<TrieTreeNode> childList = parent.childMap().values();
            /**
             * 下层fail指针设置,利用队列实现层序遍历，挨个设置
             * 设当前节点为node,找到父节点的fail指针faFail,在faFail的子节点中查找和node同值的节点expect,如果expect不为空，那么node.fail=expect.否则node.fail=root
             */
            for (TrieTreeNode child : childList) {
                TrieTreeNode failNode = parent.failNode();
                TrieTreeNode fail = failNode.findChild(child.data());
                if (nonNull(fail)) {
                    child.setFailNode(fail);
                    if (!fail.wordLengths().isEmpty()) {
                        //存储fail指针数据的索引
                        child.addWordLengths(fail.wordLengths());
                    }
                } else {
                    child.setFailNode(root);
                }
                queue.push(child);
            }
        }
    }

    /**
     * 搜索关键词命中
     *
     * @param text text
     * @return Set<KeywordHit>
     */
    public Set<KeywordHit> searchKeywordHits(String text) {
        Set<KeywordHit> result = new HashSet<>();
        TrieTreeNode node = root;
        char[] chars = text.toCharArray();
        for (int charIndex = 0; charIndex < chars.length; charIndex++) {
            //1. 开始查找字符
            char aChar = chars[charIndex];
            //2. 未找到时转移到失败节点
            while (isNull(node.findChild(aChar)) && nonNull(node.failNode())) {
                node = node.failNode();
            }
            //3. 找到后节点向下继续
            if (nonNull(node.findChild(aChar))) {
                node = node.findChild(aChar);
            } else {
                continue;
            }
            List<Integer> wordLengths = node.wordLengths();
            if (!wordLengths.isEmpty()) {
                for (Integer wordLength : wordLengths) {
                    result.add(new KeywordHit(charIndex - wordLength + 1, charIndex + 1));
                }
            }
        }
        return result;
    }
}
