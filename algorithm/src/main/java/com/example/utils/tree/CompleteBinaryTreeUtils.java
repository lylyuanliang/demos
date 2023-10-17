package com.example.utils.tree;

import lombok.Data;

import java.util.*;

/**
 * 完全二叉树工具类
 */
public class CompleteBinaryTreeUtils {
    public static TreeNode constructTree(int[] nums) {
        if (nums.length == 0) return new TreeNode(0);
        Deque<TreeNode> nodeQueue = new LinkedList<>();
        // 创建一个根节点
        TreeNode root = new TreeNode(nums[0]);
        nodeQueue.offer(root);
        TreeNode cur;
        // 记录当前行节点的数量（注意不一定是2的幂，而是上一行中非空节点的数量乘2）
        int lineNodeNum = 2;
        // 记录当前行中数字在数组中的开始位置
        int startIndex = 1;
        // 记录数组中剩余的元素的数量
        int restLength = nums.length - 1;

        while (restLength > 0) {
            for (int i = startIndex; i < startIndex + lineNodeNum; i = i + 2) {
                // 说明已经将nums中的数字用完，此时应停止遍历，并可以直接返回root
                if (i == nums.length) return root;
                cur = nodeQueue.poll();
                cur.left = new TreeNode(nums[i]);
                nodeQueue.offer(cur.left);
                // 同上，说明已经将nums中的数字用完，此时应停止遍历，并可以直接返回root
                if (i + 1 == nums.length) return root;
                cur.right = new TreeNode(nums[i + 1]);
                nodeQueue.offer(cur.right);
            }
            startIndex += lineNodeNum;
            restLength -= lineNodeNum;
            lineNodeNum = nodeQueue.size() * 2;
        }

        return root;
    }

    /**
     * 用于获得树的层数
     *
     * @param root
     * @return
     */
    public static int getTreeDepth(TreeNode root) {
        return root == null ? 0 : (1 + Math.max(getTreeDepth(root.left), getTreeDepth(root.right)));
    }

    private static void writeArray(TreeNode currNode, int rowIndex, int columnIndex, String[][] res, int treeDepth) {
        // 保证输入的树不为空
        if (currNode == null) return;
        // 先将当前节点保存到二维数组中
        res[rowIndex][columnIndex] = String.valueOf(currNode.val);

        // 计算当前位于树的第几层
        int currLevel = ((rowIndex + 1) / 2);
        // 若到了最后一层，则返回
        if (currLevel == treeDepth) return;
        // 计算当前行到下一行，每个元素之间的间隔（下一行的列索引与当前元素的列索引之间的间隔）
        int gap = treeDepth - currLevel ;

        // 对左儿子进行判断，若有左儿子，则记录相应的"/"与左儿子的值
        if (currNode.left != null) {
            res[rowIndex + 1][columnIndex - gap] = "/";
            writeArray(currNode.left, rowIndex + 2, columnIndex - gap * 2, res, treeDepth);
        }

        // 对右儿子进行判断，若有右儿子，则记录相应的"\"与右儿子的值
        if (currNode.right != null) {
            res[rowIndex + 1][columnIndex + gap] = "\\";
            writeArray(currNode.right, rowIndex + 2, columnIndex + gap * 2, res, treeDepth);
        }
    }

    /**
     * 利用二叉树的层次遍历来打印
     * @param root 根节点
     * @return
     */
    public static StringBuilder levelOrder(TreeNode root) {

        ArrayDeque<TreeNode> deque = new ArrayDeque<>();
        deque.addLast(root);

        int treeDepth = getTreeDepth(root);

        // 最有一层每个节点之间间隔 6个空格
        int interval4LastRow = 6;
        // 最后一层节点个数(填满的情况下)
        int lastNodeNum = treeDepth << (treeDepth - 1);
        // 每个节点中间以3个空格间隔, 节点默认宽度为4, 整个二叉树的最大宽度就是这个
        int maxRowWidth = (lastNodeNum - 1)*interval4LastRow + lastNodeNum * 4;

        StringBuilder builder = new StringBuilder();
        // 记录处理的层数
        int level =1;

        while (!deque.isEmpty()) {
            int num = deque.size();
            int interval = (treeDepth - level ) * interval4LastRow + interval4LastRow ;

            // 计算每一层开始需要填充的字符数
            int fillBlankNum4Begin = (interval4LastRow / 2) * (treeDepth - level );

            // 计算每一层打印的开始位置
            int startPos = maxRowWidth - fillBlankNum4Begin;

            builder.append(fillBlank(fillBlankNum4Begin));
            for (int i = 0; i < num; i++) {
                TreeNode node = deque.removeFirst();
                if (node.left != null) deque.addLast(node.left);
                if (node.right != null) deque.addLast(node.right);

                int valLength = String.valueOf(node.val).length();
                // 每个节点之间填充空格
                builder.append(node.val).append(fillBlank(interval-valLength));
            }
            builder.append(System.lineSeparator());
            level ++ ;
        }
        return builder;
    }

    public static String show(int[] array) {
        TreeNode treeNode = constructTree(array);
        return show(treeNode);
    }

    public static String show(TreeNode root) {
        if (root == null) System.out.println("EMPTY!");
        // 得到树的深度
        int treeDepth = getTreeDepth(root);

        // 最后一行的宽度为2的（n - 1）次方乘3，再加1
        // 作为整个二维数组的宽度
        int arrayHeight = treeDepth * 2 - 1;
        // 最后一层节点个数(填满的情况下)
        int lastNodeNum = treeDepth << (treeDepth - 1);
        // 每个节点中间以3个空格间隔, 节点默认宽度为4
        int lastRowWidth = (lastNodeNum - 1)*3 + lastNodeNum * 4;
        // 用一个字符串数组来存储每个位置应显示的元素
        String[][] res = new String[arrayHeight][lastRowWidth];
        // 对数组进行初始化，默认为一个空格
        for (int i = 0; i < arrayHeight; i++) {
            for (int j = 0; j < lastRowWidth; j++) {
                res[i][j] = " ";
            }
        }

        // 从根节点开始，递归处理整个树
        writeArray(root, 0, lastRowWidth / 2, res, treeDepth);

        // 此时，已经将所有需要显示的元素储存到了二维数组中，将其拼接并打印即可
        StringBuilder sb = new StringBuilder();
        for (String[] line : res) {
            for (int i = 0; i < line.length; i++) {
                sb.append(line[i]);
                if (line[i].length() > 1 && i <= line.length - 1) {
                    i += line[i].length() > 4 ? 2 : line[i].length() - 1;
                }
            }
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }

    private static String fillBlank(int num) {
        if (num <= 0) {
            return "";
        }
        return String.join("", Collections.nCopies(num, "*"));
    }

    public static void main(String[] args) {
        TreeNode treeNode = constructTree(new int[]{12, 1000, 333, 11, 2, 3, 1, 0, 9, 8, 15, 44, 56});

        System.out.println(levelOrder(treeNode));
    }

    @Data
    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int x) {
            val = x;
        }
    }
}
