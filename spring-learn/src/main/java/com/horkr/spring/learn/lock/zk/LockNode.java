package com.horkr.spring.learn.lock.zk;

public class LockNode {
    private String path;
    private boolean active;

    public LockNode( String path) {
        this.path = path;
    }

    public LockNode() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
