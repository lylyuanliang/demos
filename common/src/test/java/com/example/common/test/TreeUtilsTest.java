package com.example.common.test;

import com.alibaba.fastjson.JSONObject;
import com.example.common.util.TreeUtils;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TreeUtilsTest {
    public static void main(String[] args) {
        List<Node> testDataList = getTestData();

        List<Node> subList = TreeUtils.tileTree(0L, testDataList, Node::getId, Node::getPid);
        System.out.println(JSONObject.toJSONString(subList));

        System.out.println(JSONObject.toJSONString(
                        TreeUtils.merge2Tree(getTestData(), Node::getId, Node::getPid, (parentNode, node) -> {
                            List<Node> child = parentNode.getChild();
                            if (child == null) {
                                child = new ArrayList<>();
                            }
                            child.add(node);
                            parentNode.setChild(child);
                        })
                )
        );
    }

    /**
     * 查找所有下级
     *
     * @param id          主键
     * @param allDataList 所有部门信息
     * @return
     */
    private static List<Node> getSubList(Long id, List<Node> allDataList) {
        // 提前根据pid分组
        Map<Long, List<Node>> pidMap = allDataList.stream()
                .collect(Collectors.groupingBy(Node::getPid));

        if (!pidMap.containsKey(id)) {
            // 没有下级
            return null;
        }

        // 先获取直接下属节点
        List<Node> subList = pidMap.get(id);
        List<Node> resultList = new ArrayList<>(subList);
        while (!subList.isEmpty()) {

            subList = subList.stream()
                    // 获取当前层级所有节点id
                    .map(Node::getId)
                    // 如果map中没有这个id,则表示该id没有子节点(map的key为pid)
                    .filter(pidMap::containsKey)
                    // 获取id的所有直接子节点信息, 准备下一次循环
                    .flatMap(subId -> pidMap.get(subId).stream())
                    .collect(Collectors.toList());

            resultList.addAll(subList);

        }

        return resultList;
    }

    private static List<Node> getTestData() {
        List<Node> list = new ArrayList<>();
        Node node1 = Node.builder().id(1L).name("华傲数据").pid(0L).build();
        Node node2 = Node.builder().id(10000L).name("th").pid(1L).build();
        Node node3 = Node.builder().id(10002L).name("yy").pid(1L).build();
        Node node4 = Node.builder().id(99999999999L).name("自级联级联部").pid(1L).build();
        Node node41 = Node.builder().id(9999999999988L).name("自级联级联部").pid(99999999999L).build();
        Node node5 = Node.builder().id(10003L).name("gyx").pid(1L).build();
        Node node6 = Node.builder().id(10001L).name("编码和订阅").pid(10000L).build();
        Node node7 = Node.builder().id(100011L).name("编码和订阅").pid(10001L).build();

        list.add(node1);
        list.add(node2);
        list.add(node3);
        list.add(node4);
        list.add(node5);
        list.add(node6);
        list.add(node7);
        list.add(node41);

        return list;
    }

    @Data
    @Builder
    public static class Node {
        private Long id;
        private String name;
        private Long pid;

        private List<Node> child;

    }
}
