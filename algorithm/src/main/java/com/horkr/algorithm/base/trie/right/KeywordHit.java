package com.horkr.algorithm.base.trie.right;

/**
 * 关键词命中数据
 */
public class KeywordHit {
    /**
     * 在搜索文本中的起始位置
     */
    private final int start;
    /**
     * 在搜索文本中的结束位置
     */
    private final int end;

    public String keyword(String text) {
        return text.substring(start, end);
    }

    public KeywordHit(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int start() {
        return start;
    }

    public int end() {
        return end;
    }
}
