package com.example.sort.impl;

import com.example.sort.Sort;

import java.util.Arrays;

/**
 * 计数排序 <br><br>
 * <p>
 * 计数排序（Counting Sort）是一种线性时间复杂度的排序算法，适用于排序一定范围内的非负整数。<br>
 * 计数排序的核心思想是通过统计每个元素的出现次数，然后根据这些统计信息将元素按照顺序排列。<br><br>
 * <p>
 * 1.确定数据范围： 首先，找出待排序数据中的最大值和最小值，以确定排序的范围。<br>
 * 2.统计元素频次： 创建一个计数数组（counting array），其长度等于待排序范围的大小。遍历待排序数组，统计每个元素出现的次数，并将次数记录在计数数组对应的索引位置上。<br>
 * 3.计算累加频次： 对计数数组进行累加操作，使得计数数组中的每个元素变为前面所有元素频次之和。这将帮助我们确定每个元素在排序后的位置。<br>
 * 4.构建排序结果： 创建一个与待排序数组等长的结果数组。从后向前遍历待排序数组，根据每个元素的频次在计数数组中找到它在排序后的位置，并将它放置在结果数组中。同时，减少计数数组中对应元素的频次。<br>
 * 5.复制结果： 最后，将结果数组中的元素复制回待排序数组，完成排序。<br><br>
 * <p>
 * 计数排序的优点是它对于一定范围内的整数排序具有很高的效率，尤其在数据范围不大的情况下。然而，它的局限性在于它只适用于非负整数排序，且对于数据范围过大时，计数数组可能变得过于庞大。<br><br>
 * <p>
 * 需要注意的是，计数排序不是一种基于比较的排序算法，因此其时间复杂度可以达到 O(n+k)，其中 n 为待排序元素数量，k 为数据范围。<br><br>
 * <p>
 * 缺陷<br>
 * 1.计数排序只能适用待排序元素为整数(负数不行)的场景<br>
 * 2.待排序元素的数值范围(极差)过大的情况下，计数排序会浪费大量空间，故一般不推荐使用计数排序<br>
 */
public class Counting implements Sort {
    public static void main(String[] args) {
        new Counting().test();
    }

    @Override
    public int[] sort(int[] targetArray) {

        // 找出最大值
        int max = Arrays.stream(targetArray).max().getAsInt();
        // 找出最小值
        int min = Arrays.stream(targetArray).min().getAsInt();

        int range = max - min + 1;
        // 创建一个计数数组
        int[] countingArray = new int[range];

        for (int num : targetArray) {
            /*
             * 统计每个元素出现的次数,  num - min为计算原数组元素在统计数组中的索引
             * num - min的操作其实就是对数据进行排序了, 然后计数功能又可以应对重复数据的情况
             */
            countingArray[num - min]++;
        }

        // 以下一行代码仅做日志打印, 与排序无关
        addLog4Process("统计数组:" + Arrays.toString(countingArray));

        /*
         * 计算统计数组的累计值，即计算 统计数组中指定索引位置上的元素在排序后的位置
         * 如果该索引位置上有重复元素，则为重复元素所占的最大排序位置
         * 解决计数排序不稳定问题
         */
        for (int i = 1; i < range; i++) {
            /*
             * 因为统计数组中存放的是原始数组中值出现的次数, 并且统计数组本身就是对原始数组排序后的一个映射
             * 因此, 将次数累加刚好就可以是原始数组的下标, 并且最大值也不会超过原始数组的长度
             */
            countingArray[i] += countingArray[i - 1];
        }

        // 以下一行代码仅做日志打印, 与排序无关
        addLog4Process("统计数组累加后:" + Arrays.toString(countingArray));

        int size = targetArray.length;
        int[] sortedArray = new int[size];  // 排序结果数组
        // 倒序遍历原数组，保证稳定性
        for (int i = size - 1; i >= 0; i--) {
            // 计算原数组元素在统计数组中的索引
            int dataInCountIndex = targetArray[i] - min;
            /*
             * 计算其排序后的位置, 因为数组索引从0开始计算，故应对排序位置减1
             * 例如，排在最前面的元素，排序位置为1，则其在数组中的位置索引应为0
             */
            int sortIndex = countingArray[dataInCountIndex] - 1;
            // 将原数组元素放入排序后的位置上
            sortedArray[sortIndex] = targetArray[i];
            /*
             * 下一个重复的元素，应排前一个位置，以保证稳定性
             * 因为是倒序遍历原始数组的元素, 所以重复元素第一次遍历到之后, 将统计数组中代表该重复元素的值减一, 以保证下次遍历时存放到"前一个位置"
             * 也不用担心这里减一后的下标会不会冲突的问题, 因为在之前的累加代码中, 累加的是"出现次数",
             */
            countingArray[dataInCountIndex]--;
        }

        return sortedArray;
    }
}
