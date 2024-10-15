package com.example.sort.impl;

import com.example.sort.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 希尔排序<br><br>
 *
 * 希尔排序（Shell Sort）是一种插入排序的改进算法，旨在优化插入排序在大规模数据集上的性能。<br>
 * 它通过将数组分割成多个子序列，并对每个子序列进行插入排序，逐步减小子序列的长度，最终完成整个数组的排序。<br>
 * 希尔排序的主要思想是通过交换距离较远的元素，使得比较和交换可以跨越多个元素，从而更快地将较小的元素移动到数组的前部。<br><br>
 *
 * 1. 选择步长（间隔）： 希尔排序的核心是定义一个步长序列，决定分割数组的子序列个数以及每个子序列中的元素间隔。
 * 常用的步长序列有希尔原始序列、Knuth序列等。步长通常会随着迭代逐渐减小，直到步长为1。<br>
 * 2.分组和插入排序： 根据选择的步长，将数组分成若干个子序列。
 * 对每个子序列进行插入排序，即将子序列内的元素按照步长进行比较和插入排序操作。这样做的目的是通过跨越一定距离的元素交换，加快较小元素向数组前部移动的速度。<br>
 * 3.逐步减小步长： 在每一轮排序完成后，逐步减小步长，再次进行分组和插入排序操作，直到步长为1。最后一次排序时，步长为1，相当于对整个数组进行了一次插入排序。<br>
 * 4.合并排序结果： 经过多轮分组和插入排序操作后，数组中的元素逐渐有序。最终步长为1时，整个数组将趋近于有序状态。此时可以对整个数组进行一次插入排序，确保数组的最终有序性。<br><br>
 *
 * 希尔排序通过先处理较远距离的元素，逐步减小距离，最终实现对整个数组的排序。尽管希尔排序的性能依赖于选择的步长序列，但在实践中它通常能够在大规模数据集上表现出比普通插入排序更好的性能。<br><br>
 *
 * 希尔排序的时间复杂度在最坏情况下约为 O(n^2)，但在一般情况下可以达到 O(n log n) 级别，具体取决于步长序列的选择。 <br><br>
 *
 */
public class Shell implements Sort {
    public static void main(String[] args) {
        new Shell().test();
    }
    @Override
    public int[] sort(int[] targetArray) {
        int n = targetArray.length;

        // 使用希尔原始序列：n/2, n/4, n/8, ...
        for (int gap = n / 2; gap > 0; gap /= 2) {
            // 以下一行代码记录日志, 与排序无关
            addLog4Process("gap:" + gap);
            for (int i = gap; i < n; i++) {
                int temp = targetArray[i];
                // 插入位置
                int j;
                // 在子序列内进行插入排序
                for (j = i; j >= gap && targetArray[j - gap] > temp; j -= gap) {
                    /*
                     * 元素后移,
                     * 这里说明一下, 假设已排序部分为0 ~ i-1, 插入位置为n, 那么n ~ i-1这些元素一定大于current, 即下标i的元素
                     * 所以n ~ i-1这些元素都会往后移动一位, (即n~i-1 往后移动一位分别对应 n+1 ~ i), i刚好表示current, 所以不会有元素被覆盖
                     */
                    targetArray[j] = targetArray[j - gap];
                }

                targetArray[j] = temp;

                // 以下代码只做日志记录, 与排序无关
                List<Integer> subIndex = new ArrayList<>();
                for (int k = i; k >= 0; k -= gap) {
                    subIndex.add(k);
                }
                addLog4Process("    插入" + temp + " " + Arrays.toString(targetArray) + ", 子序列下标:" + subIndex.stream().sorted().collect(Collectors.toList()));
            }
        }

        return targetArray;
    }
}
