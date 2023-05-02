package com.horkr.algorithm.base.trie;

import java.util.HashMap;
import java.util.Map;

public class EsTireTree {
    static class TrieNode {
        Map<Character, TrieNode> children; // 子节点
        boolean isEndOfWord; // 是否是单词结束节点

        TrieNode() {
            children = new HashMap<>();
            isEndOfWord = false;
        }
    }

    static class Trie {
        private TrieNode root;

        Trie() {
            root = new TrieNode();
        }

        void insert(String word) {
            TrieNode current = root;
            for (char c : word.toCharArray()) {
                // 如果该字符对应的节点不存在，则创建
                current.children.putIfAbsent(c, new TrieNode());
                current = current.children.get(c); // 转移到下一个节点
            }
            current.isEndOfWord = true; // 标记单词结束节点
        }

        boolean search(String word) {
            TrieNode current = root;
            for (char c : word.toCharArray()) {
                if (!current.children.containsKey(c)) {
                    return false; // 如果该字符对应的节点不存在，则单词不存在
                }
                current = current.children.get(c); // 转移到下一个节点
            }
            return current.isEndOfWord; // 检查是否是单词结束节点
        }


    }

    public static void main(String[] args) {
        Trie trie = new Trie();
        trie.insert("apple");
        trie.insert("banana");
        System.out.println(trie.search("apple")); // true
        System.out.println(trie.search("orange")); // false
    }
}
