package com.example.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TreeUtils {
    /**
     * 列表数据，合并成树结构, 使用样例
     * <pre>
     * {@code
     * TreeUtils.merge2Tree(getTestData(), Node::getId, Node::getPid, (parentNode, node) -> {
     *             List<Node> child = parentNode.getChild();
     *             if (child == null) {
     *                 child = new ArrayList<>();
     *             }
     *             child.add(node);
     *             parentNode.setChild(child);
     *         }
     * }
     * </pre>
     *
     * @param list        原始list
     * @param idFun       获取id的Function实例
     * @param parentIdFun 获取父级id的Function实例
     * @param addChildFun 添加子节点的BiConsumer实例
     * @param <T>         节点类型
     * @return 树形结构
     */
    public static <T> List<T> merge2Tree(List<T> list, Function<T, Long> idFun, Function<T, Long> parentIdFun, BiConsumer<T, T> addChildFun) {
        if (list == null || list.isEmpty()) {
            return list;
        }
        // 初始化子节点列表,创建以ID为键的节点映射，便于查找父节点
        Map<Long, T> nodeMap = list.stream().
                collect(Collectors.toMap(idFun, node -> node));

        // 构建树形结构,没有父节点的都列为根
        List<T> tree = list.stream()
                .filter(node -> nodeMap.get(parentIdFun.apply(node)) == null)
                .collect(Collectors.toList());

        // 将list中的节点放入tree结构中
        list.forEach(node -> {
            T parentNode = nodeMap.get(parentIdFun.apply(node));
            if (parentNode != null) {
                addChildFun.accept(parentNode, node);
            }
        });

        return tree;
    }

    /**
     * 展开某个父id下的所有节点(非树形结构)
     *
     * @param parentId    父id, 需要平铺的整个树的父id
     * @param allDataList 所有数据(非树形结构)
     * @param idFun       获取id的Function实例
     * @param parentIdFun 获取父级id的Function实例
     * @param <T>         节点类型
     * @return 父节点以及其子节点组成的集合
     */
    public static <T> List<T> tileTree(Long parentId, List<T> allDataList, Function<T, Long> idFun, Function<T, Long> parentIdFun) {
        // 根据父id分组, 方便获取某个parentId下的所有直接子节点
        Map<Long, List<T>> pidMap = allDataList.stream()
                .collect(Collectors.groupingBy(parentIdFun));

        if (!pidMap.containsKey(parentId)) {
            return null;
        }

        List<T> subList = pidMap.get(parentId);
        List<T> resultList = new ArrayList<>();
        while (!subList.isEmpty()) {
            subList = subList.stream()
                    .map(idFun)
                    .filter(pidMap::containsKey)
                    // 以当前节点id作为新的父id继续查找子节点
                    .flatMap(pid -> pidMap.get(pid).stream())
                    .collect(Collectors.toList());

            resultList.addAll(subList);
        }

        return resultList;
    }
}
