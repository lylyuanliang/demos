package com.example.sort.impl;

import com.example.sort.Sort;

import java.util.Arrays;

/**
 * 堆排序 <br><br>
 *
 * 前置知识: <br><br>
 *
 * 1.完全二叉树: <br>
 * 1.1**层次结构**： <br>
 * 完全二叉树的所有层级都是满的，除了可能最底层之外。<br>
 * 在最底层，所有节点都尽量靠左排列。换句话说，如果在某个层级上缺少节点，那么这些缺少的节点只能出现在这个层级的最右边。<br>
 * 1.2**节点编号**：<br>
 * 对于完全二叉树中的每个节点，如果其在层级中从左到右的编号为 i（从 0 开始编号），则它的左子节点的编号为 2i + 1，右子节点的编号为 2i + 2。<br><br>
 *
 * 因此，完全二叉树在结构上尽可能地充满，只有最底层可能会出现缺失的情况。这种结构特点使得完全二叉树在存储时能够有效地利用数组来表示，同时也是许多二叉堆（如最小堆和最大堆）的基础。<br>
 * 例如数组 [1, 2, 3, 4, 5, 6]  <br>
 * 对应的二叉树如下                                <br>
 *            1                                 <br>
 *          /   \                               <br>
 *         2     3                              <br>
 *        / \   /                               <br>
 *       4  5  6                                <br>
 * <br><br>
 *
 * 2.堆: <br>
 * 堆是一个树形结构，其实堆的底层是一棵完全二叉树 <br><br>
 * 2.1 大顶堆 <br>
 * 根结点（亦称为堆顶）的关键字是堆里所有结点关键字中最大者，称为大顶堆。大顶堆要求根节点的关键字既大于或等于左子树的关键字值，又大于或等于右子树的关键字值。 <br>
 * 2.2 小顶堆 <br>
 * 根结点（亦称为堆顶）的关键字是堆里所有结点关键字中最小者，称为小顶堆。小顶堆要求根节点的关键字既小于或等于左子树的关键字值，又小于或等于右子树的关键字值。<br>
 *
 * 3.推排序思想  <br>
 * 3.1构建初始堆，将待排序列构成一个大顶堆(或者小顶堆)，升序大顶堆，降序小顶堆；<br>
 * 3.2将堆顶元素与堆尾元素交换，并断开(从待排序列中移除)堆尾元素。<br>
 * 3.3重新构建堆。<br>
 * 3.4重复2~3，直到待排序列中只剩下一个元素(堆顶元素)。<br><br>
 *
 * 堆排序（Heap Sort）是一种基于二叉堆数据结构的排序算法。  <br>
 * 它具有不稳定性（相同元素的相对顺序可能会改变）  <br>
 * 堆排序的优点是不需要额外的存储空间，而且在最坏情况下的时间复杂度依然为 O(n log n)，但它的常数因子较大，因此在实际应用中可能会被一些快速排序等算法取代。<br>
 *
 */
public class Heap implements Sort {

    public static void main(String[] args) {
        new Heap().test();
    }

    @Override
    public int[] sort(int[] targetArray) {
        int n = targetArray.length;

        // 以下一行代码只做排序过程日志记录
        addLog4Process("构建初始大顶堆开始...");
        // 构建最大堆
        for (int i = (n-1) / 2 ; i >= 0; i--) {
            // 从第一个非叶子结点从下至上，从右至左调整结构
            adjustHeap(targetArray, n, i);
        }

        // 以下一行代码只做排序过程日志记录
        addLog4Process("将堆顶元素与堆尾元素交换后, 剩下元素构建大顶堆 开始...");
        // 从堆中一个个取出元素并排序
        for (int i = n - 1; i > 0; i--) {
            // 将堆顶元素（最大值）与数组末尾元素交换
            exchange(targetArray, 0, i);

            // 重新调整堆，将剩余元素重新构建为最大堆
            adjustHeap(targetArray, i, 0);
        }

        return targetArray;
    }

    /**
     * 调整堆，确保以 index 为根的子树是最大堆 <br>
     *  堆是一种重要的数据结构，为一棵完全二叉树,  <br>
     *  底层如果用数组存储数据的话，假设某个元素为序号为i(Java数组从0开始,i为0到n-1), <br>
     *  如果它有左子树，那么左子树的位置是2i+1，<br>
     *  如果有右子树，右子树的位置是2i+2，<br>
     *  如果有父节点，父节点的位置是(n-1)/2取整。<br>
     *  <br>
     *  分为最大堆和最小堆，最大堆的任意子树根节点不小于任意子结点，最小堆的根节点不大于任意子结点<br>
     * @param arr 待排序数组
     * @param lastIndex 待排序列尾元素索引
     * @param parentIndex 最大堆根节点
     */

    public void adjustHeap(int[] arr, int lastIndex, int parentIndex) {
        // 最大值索引, 最开始默认为根节点
        int largestIndex = parentIndex;
        // 根节点i的左节点索引为 2 * i + 1
        int leftIndex = 2 * parentIndex + 1;
        // 根节点i的左节点索引为 2 * i + 2
        int rightIndex = leftIndex + 1;

        // 找出左子节点, 根节点, 右子节点 三个中的最大值 对应的索引
        if(leftIndex < lastIndex && arr[largestIndex] < arr[leftIndex]) {
            largestIndex = leftIndex;
        }
        if(rightIndex < lastIndex && arr[largestIndex] < arr[rightIndex]) {
            largestIndex = rightIndex;
        }

        // 如果根节点不是最大值, 就交换最大值与根节点的位置，然后递归调整子树
        if(largestIndex != parentIndex) {
            exchange(arr, largestIndex, parentIndex);

            adjustHeap(arr, lastIndex, largestIndex);
        }else {

            // 以下一行代码只做排序过程日志记录
            addLog4Process(Arrays.toString(arr));
        }
    }
}
