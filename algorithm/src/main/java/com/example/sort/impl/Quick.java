package com.example.sort.impl;

import com.example.sort.Sort;

/**
 * 快速排序 <br>
 * 1.选择一个基准元素（pivot），可以是数组中的任意一个元素。<br>
 * 2.将数组分为两个子数组，一个包含所有小于基准元素的元素，另一个包含所有大于基准元素的元素。基准元素本身在两个子数组之间的分界线上。<br>
 * 3.对这两个子数组递归地应用快速排序算法，即在每个子数组中选择新的基准元素，并将子数组继续分割为更小的子数组。<br>
 * 4.递归的结束条件是子数组的长度为1或零，此时数组已经是有序的。<br>
 * 5.最后，将所有的子数组重新合并，得到完整的有序数组。<br>
 * <br>
 * 快速排序的关键思想在于分治法（Divide and Conquer），通过不断地将数组分割为更小的子数组并对其进行排序，最终得到整个数组的有序结果。<br>
 * 选择一个合适的基准元素是快速排序的关键，一般情况下可以选择数组的第一个元素、最后一个元素或者中间元素作为基准。<br>
 * <br>
 * 快速排序具有良好的平均时间复杂度（O(n log n)），但在最坏情况下可能达到O(n^2)，例如当基准选择不合适时，导致每次分割只减少一个元素。<br>
 * 然而，通过合适的基准选择和一些优化技巧，可以避免最坏情况的发生。<br>
 */
public class Quick implements Sort {
    @Override
    public int[] sort(int[] targetArray) {
        int length = targetArray.length;
        if (length <= 1) {
            return targetArray;
        }

        sort(targetArray, 0, length - 1);

        return targetArray;
    }

    /**
     * 快速排序
     *
     * @param targetArray 待排序数组
     * @param start       快排 开始下标
     * @param end         快排 结束下标
     * @return
     */
    private void sort(int[] targetArray, int start, int end) {
        if (start >= end) {
            // 结束排序
            return;
        }
        // 先选取基准值
        int partition = partition(targetArray, start, end);
        // 对基准值左边排序, 递归调用
        sort(targetArray, start, partition - 1);
        // 对基准值右边排序, 递归调用
        sort(targetArray, partition + 1, end);

    }

    /**
     * 选取基准值(并将基准值放在正确的位置)
     *
     * @param array 目标数组
     * @param start 最小下标
     * @param end   最大下标
     * @return
     */
    private int partition(int[] array, int start, int end) {
        // 随便选取一个要素作为基准值, 这里选最后一个
        int pivot = array[end];
        // 小于基准值的下标, 初始为 start-1, 表示还没有发现比基准值小的元素
        int lowerIndex = start - 1;
        // 下面这个循环的逻辑是, 将小于基准值的元素统统往左边放(即lowerIndex++用来遍历(存放)所有小于基准值的元素)
        // i 不能等于end,因为end已经表示基准值了
        for (int i = start; i < end; i++) {
            if (array[i] < pivot) {
                lowerIndex++;

                // 将第i个元素交换到 lowerIndex 的位置上
                exchange(array, i, lowerIndex);
            }
        }

        // 上面循环结束后, lowerIndex后面的元素都 不小于 基准值,
        // 所以将基准值放在 lowerIndex+1 的位置就完成本次基准值选取, end下标是我们一开始就选取的基准值, 所以和end交换即可
        int pivotIndex = lowerIndex + 1;
        exchange(array, pivotIndex, end);

        // 返回最后确定的基准值下标
        return pivotIndex;
    }

    public static void main(String[] args) {
        new Quick().test();
    }
}
