package com.horkr.algorithm.base.trie.right;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Set;

public class Demo {
    public static final Logger log = LoggerFactory.getLogger(Demo.class);
    public static void main(String[] args) {
        TrieTree trieTree = new TrieTree();
        trieTree.build(Arrays.asList("he", "she", "hers", "his"));
        String text = "ahishers";
        Set<KeywordHit> searchKeywordHits = trieTree.searchKeywordHits(text);
        for (KeywordHit hit : searchKeywordHits) {
            String keyword = hit.keyword(text);
            int start = hit.start();
            log.info("命中关键词：{},位置：{}",keyword,start);
        }
    }
}
