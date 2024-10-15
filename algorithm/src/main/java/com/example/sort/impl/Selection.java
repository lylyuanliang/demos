package com.example.sort.impl;

import com.example.sort.Sort;

/**
 * 选择排序<br>
 * 1.将数组分为已排序和未排序两个部分。初始状态下，已排序部分为空，未排序部分包含整个数组。<br>
 * 2.在未排序部分找到最小（或最大）的元素，并将其与未排序部分的第一个元素交换位置，将最小（或最大）元素放入已排序部分的末尾。<br>
 * 3.从未排序部分剩余元素中继续寻找最小元素，并将其交换到未排序部分的第一个位置，继续迭代。<br>
 * 4.重复上述步骤，每次迭代将最小（或最大）元素交换到未排序部分的开始，直到所有元素都被放置在已排序部分，排序完成<br>
 * <br>
 * 选择排序的核心思想是从未排序部分中选择最小（或最大）的元素，并将其交换到已排序部分的末尾，逐渐构建有序数组。<br>
 * 选择排序的关键特点是每一轮迭代只进行一次交换，因此总共会进行 n-1 次交换，其中 n 是数组的长度。<br><br>
 * 时间复杂度始终为 O(n^2)
 */
public class Selection implements Sort {
    @Override
    public int[] sort(int[] targetArray) {
        int length = targetArray.length;
        if (length <= 1) {
            return targetArray;
        }

        for (int i = 0; i < length - 1; i++) {
            // 记录最小元素的下标, 初始为i (i是已排序部分的末尾, 也是未排序部分的第一个元素)
            int minIndex = i;
            // 在未排序部分中找到最小值, 从i+1的位置开始找, 因为已经假设i为最小值
            for (int j = i+1; j < length; j++) {
                // 比较并记录
                if(targetArray[j] < targetArray[minIndex]) {
                    minIndex = j;
                }
            }

            if(minIndex != i) {
                // 交换位置
                exchange(targetArray, minIndex, i);
            }
        }

        return targetArray;
    }

    public static void main(String[] args) {
        new Selection().test();
    }
}
