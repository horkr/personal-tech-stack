package com.horkr.algorithm.base.trie.right;

import java.util.*;

/**
 * 字典树节点
 */
public class TrieTreeNode {
    /**
     * 所有子节点（因为会存储中文，所以不使用数组存储）
     */
    private final Map<Character, TrieTreeNode> childMap = new HashMap<>();
    /**
     * 当前节点存储的数据
     */
    private char data;
    /**
     * 当查找失败后转移的失败节点（最大后缀）
     * 最大后缀：
     * 如果i节点的fail指针是j,那么word[j]是word[i]在字典树上能找到的最大后缀。其中word[i],为根节点到i节点的所有字符组成的字符串
     */
    private TrieTreeNode failNode;
    /**
     * 如果当前节点i是叶子节点,那么word[i]为一个完整组成树的关键词，而length则表示从root到word[i]的距离。而这里是list,是将失败节点的wordLength也存储了（如果有的话）
     */
    private List<Integer> wordLengths = new ArrayList<>();


    public TrieTreeNode(Character data) {
        this.data = data;
    }

    public TrieTreeNode() {
    }

    public Map<Character, TrieTreeNode> childMap() {
        return childMap;
    }

    public Character data() {
        return data;
    }

    public TrieTreeNode failNode() {
        return failNode;
    }

    public void setFailNode(TrieTreeNode failNode) {
        this.failNode = failNode;
    }

    public List<Integer> wordLengths() {
        return wordLengths;
    }

    public void setWordLengths(List<Integer> wordLengths) {
        this.wordLengths = wordLengths;
    }

    public TrieTreeNode findChild(char data) {
        return childMap.get(data);
    }

    public void addChild(char data) {
        childMap.put(data, new TrieTreeNode(data));
    }


    public void addWordLengths(int length) {
        wordLengths.add(length);
    }

    public void addWordLengths(Collection<Integer> lengths) {
        wordLengths.addAll(lengths);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrieTreeNode that = (TrieTreeNode) o;
        return data == that.data &&
                Objects.equals(childMap, that.childMap) &&
                Objects.equals(failNode, that.failNode) &&
                Objects.equals(wordLengths, that.wordLengths);
    }

    @Override
    public int hashCode() {
        return Objects.hash(childMap, data, failNode, wordLengths);
    }
}
