package com.example.sort.impl;

import com.example.sort.Sort;

import java.util.Arrays;

/**
 * 归并排序 <br>
 * 归并排序（Merge Sort）是一种基于分治法（Divide and Conquer）的排序算法，其原理可以描述为：<br>
 * 1.分割： 将待排序数组递归地分割为更小的子数组，直到每个子数组只包含一个元素。这些单个元素的子数组被认为是有序的。<br>
 * 2.合并： 将相邻的子数组逐一合并，将其组合成较大的有序数组。在合并过程中，逐个比较两个子数组的元素，并将较小（或较大）的元素放入新的数组中。<br>
 * 3.重复合并： 重复上述合并步骤，直到所有子数组都被合并成一个完整的有序数组。这就是归并排序的最终结果。<br>
 * <br>
 * 归并排序的关键思想是将数组逐步分割为小的子数组，然后通过有序合并的方式将这些子数组逐步组合成更大的有序数组，直到最终获得完整有序数组。<br>
 * 相对于其他排序算法，归并排序具有稳定性（相等元素的相对顺序不会改变）和稳定的 O(n log n) 时间复杂度<br>
 * <br>
 * 尽管归并排序的时间复杂度相对较低，但其实现相对复杂，需要使用额外的存储空间来存储合并后的子数组。在某些情况下，这可能会使得归并排序的空间复杂度较高。<br>
 * 然而，归并排序的可预测性和稳定性使其在某些场景下仍然具有重要的应用价值<br>
 *
 * 迭代实现: <br>
 * 分析: 其实就是循环每次增加排序分割的步长<br>
 * 以[8 3 6 5 1 7 2 4]为例：    <br>
 *     第一次排序：实现步长为1，偏移量为2的归并    <br>
 *     [[3 8] 6 5 1 7 2 4]                  <br>
 *     [3 8 [5 6] 1 7 2 4]                  <br>
 *     [3 8 5 6 [1 7] 2 4]                 <br>
 *     [3 8 5 6 1 7 [2 4]]                  <br>
 *     第二次排序实现步长为2,偏移量为4的归并                 <br>
 *     [[3 5 6 8] 1 7 2 4]                 <br>
 *     [3 5 6 8 [1 2 4 7]]                 <br>
 *     第三次排序实现步长为4，偏移量为8的归并                 <br>
 *     [1 2 3 4 5 6 7 8]                 <br>
 */
public class Merge implements Sort {
    public static void main(String[] args) {
        new Merge().test();
    }

    @Override
    public int[] sort(int[] targetArray) {
        sort(targetArray, 0, targetArray.length - 1);
        return targetArray;
    }

    private void sort(int[] targetArray, int start, int end) {
        if (start >= end) {
            return;
        }

        // 计算中间下标
        int min = (start + end) / 2;

        // 对min左边进行排序(包括min)
        sort(targetArray, start, min);
        // 对min右边进行排序
        sort(targetArray, min + 1, end);
        // 合并 min左边(包括min) 和 min右边的数据
        merge(targetArray, start, min, end);
    }

    /**
     * 合并
     *
     * @param array 原数组
     * @param start 归并处理的开始下标
     * @param min   归并处理范围下标的中间值
     * @param end   归并处理的结束下标
     */
    private void merge(int[] array, int start, int min, int end) {
        // 计算min左边数量(包括min)
        int leftNum = min - start + 1;
        // 计算min右边数量
        int rightNum = end - min;

        // 将min左边的数据拷贝至临时数组, 包含min
        int[] leftTempArray = copyArray(array, start, leftNum);
        //  将min右边的数据拷贝至临时数组, 不包含min
        int[] rightTempArray = copyArray(array, min + 1, rightNum);

        int leftIndex = 0;
        int rightIndex = 0;
        // 将临时数组中的数据合并回原始数组, 注意等号
        // 这个循环就是对 [start, end]这个区间内的数据进行排序
        for (int i = start; i <= end; i++) {
            if (leftIndex < leftNum && (
                    rightIndex >= rightNum || leftTempArray[leftIndex] <= rightTempArray[rightIndex]
            )) {
                array[i] = leftTempArray[leftIndex];
                leftIndex++;
            } else {
                array[i] = rightTempArray[rightIndex];
                rightIndex++;
            }
        }

        // 记录排序过程, 下面的代码跟排序无关
        countTimes();
        addLog4Process("数组下标范围:" + start + "~" + end);
        addLog4Process("分割的子数组: " + Arrays.toString(leftTempArray) + "  " + Arrays.toString(rightTempArray));
        addLog4Process("归并到原数组:" + Arrays.toString(array));
    }

    /**
     * 数组拷贝
     *
     * @param sourceArray      原始数组
     * @param sourceStartIndex 开始拷贝的原始数组下标
     * @param length           拷贝长度
     * @return
     */
    private int[] copyArray(int[] sourceArray, int sourceStartIndex, int length) {
        int[] newArray = new int[length];
        for (int i = 0; i < length; i++) {
            int sourceIndex = i + sourceStartIndex;
            newArray[i] = sourceArray[sourceIndex];
        }

        return newArray;
    }
}
