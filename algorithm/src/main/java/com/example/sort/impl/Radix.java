package com.example.sort.impl;

import com.example.sort.Sort;

import java.util.Arrays;

/**
 * 基数排序<br><br>
 *
 * 基数排序（Radix Sort）是一种非比较的整数排序算法，它将待排序的数字按照个位、十位、百位等位数进行排序。基数排序的核心思想是将整数从低位到高位依次排序，最终得到有序序列<br><br>
 *
 * 1.按位排序： 从最低位开始，对待排序的数字按照当前位上的数值进行排序。可以使用计数排序、桶排序等稳定的排序算法来实现。<br>
 * 2.依次排序： 从低位到高位，依次对所有位数进行排序。每次排序后，数字在当前位上的相对顺序被保留，从而逐步使得整个序列越来越有序。<br>
 * 3.重复操作： 重复上述排序过程，直到对所有位数都进行了排序。最终得到完全有序的序列。<br><br>
 *
 * 基数排序的关键在于按位排序，而每次按位排序都需要使用稳定的排序算法，以确保之前排序的结果不会被破坏。<br>
 * 由于基数排序不涉及元素之间的比较，因此它是一种线性时间复杂度的排序算法。然而，在实际应用中，基数排序可能需要大量的内存空间，尤其当数字的位数很大时。<br>
 */
public class Radix implements Sort {
    public static void main(String[] args) {
        new Radix().test();
    }
    @Override
    public int[] sort(int[] targetArray) {
        int max = Arrays.stream(targetArray).max().getAsInt();

        // 从最低位开始，依次对每个位数进行排序
        for (int exp = 1; max / exp > 0; exp *= 10) {
            sort(targetArray, exp);
        }

        return targetArray;
    }

    private void sort(int[] arr, int exp) {
        int n = arr.length;
        // 用于存放排序结果
        int[] output = new int[n];
        // 因为每个位数的值范围为 0-9
        int range = 10;
        int[] countingArray = new int[range];

        // 统计每个位数上的频次
        for (int num : arr) {
            int digit = (num / exp) % 10;
            countingArray[digit]++;
        }

        // 计算累加频次
        for (int i = 1; i < range; i++) {
            countingArray[i] += countingArray[i - 1];
        }

        // 根据位数排序
        for (int i = n - 1; i >= 0; i--) {
            int num = arr[i];
            int digit = (num / exp) % 10;
            // 元素应放置的位置
            int index = countingArray[digit] - 1;
            output[index] = num;
            countingArray[digit]--;
        }

        // 将排序结果复制回原数组
        System.arraycopy(output, 0, arr, 0, n);
    }
}
