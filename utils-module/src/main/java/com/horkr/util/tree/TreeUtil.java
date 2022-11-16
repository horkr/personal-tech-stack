package com.horkr.util.tree;

import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Objects.isNull;

/**
 * 树工具类
 *
 * @author llh
 */
public class TreeUtil {

    /**
     * 计算树节点总数
     * @param root  root
     * @param obtainChildrenFunction    obtainChildrenFunction
     * @param <T>
     * @return  long
     */
    public static <T> long countTree(T root, Function<T, Collection<T>> obtainChildrenFunction) {
        AtomicLong count = new AtomicLong();
        traverseTreeNodeAndConsume(root, obtainChildrenFunction, node -> count.getAndIncrement());
        return count.get();
    }

    /**
     * 遍历树，并附加操作
     *
     * @param root root
     * @author llh
     */
    public static <T> void traverseTreeNodeAndConsume(T root, Function<T, Collection<T>> obtainChildrenFunction, Consumer<T> consumer) {
        if (isNull(root)) {
            return;
        }
        // 用于遍历树的队列
        Queue<T> nodeQueue = new LinkedList<>();
        nodeQueue.offer(root);
        while (!nodeQueue.isEmpty()) {
            T node = nodeQueue.poll();
            consumer.accept(node);
            if (isNull(obtainChildrenFunction)) {
                return;
            }
            Collection<T> children = obtainChildrenFunction.apply(node);
            if (CollectionUtils.isNotEmpty(children)) {
                for (T childNode : children) {
                    nodeQueue.offer(childNode);
                }
            }
        }
    }

    /**
     * 遍历树，并附加操作
     *
     * @param root root
     * @author llh
     */
    public static <T> T traverseTreeNodeAndConsumeChildren(T root, Function<T, Collection<T>> obtainChildrenFunction, Consumer<Collection<T>> consumer) {
        if (isNull(root)) {
            return root;
        }
        // 用于遍历树的队列
        Queue<T> nodeQueue = new LinkedList<>();
        nodeQueue.offer(root);
        while (!nodeQueue.isEmpty()) {
            T node = nodeQueue.poll();
            if (isNull(obtainChildrenFunction)) {
                return root;
            }
            Collection<T> children = obtainChildrenFunction.apply(node);
            if (CollectionUtils.isNotEmpty(children)) {
                consumer.accept(children);
                for (T childNode : children) {
                    nodeQueue.offer(childNode);
                }
            }
        }
        return root;
    }
}
