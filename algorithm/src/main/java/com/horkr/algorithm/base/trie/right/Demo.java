package com.horkr.algorithm.base.trie.right;

import java.util.Arrays;
import java.util.Set;

public class Demo {

    public static void main(String[] args) {
        TrieTree trieTree = new TrieTree();
        trieTree.build(Arrays.asList("he", "she", "hers", "his"));
        String text = "ahishers";
        Set<KeywordHit> searchKeywordHits = trieTree.searchKeywordHits(text);
        for (KeywordHit searchKeywordHit : searchKeywordHits) {
            String keyword = searchKeywordHit.keyword(text);
        }
    }
}
