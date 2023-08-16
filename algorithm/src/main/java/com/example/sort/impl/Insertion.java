package com.example.sort.impl;

import com.example.sort.Sort;

/**
 * 插入排序
 * 1.将数组分为已排序和未排序两个部分。一开始，已排序部分只包含第一个元素，而未排序部分包含剩余的元素。
 * 2.从未排序部分依次取出元素，并将其插入到已排序部分的正确位置，使得已排序部分始终保持有序状态。
 * 3.对于每个未排序元素，从其前一个元素开始，逐步与已排序部分的元素进行比较。如果当前元素小于已排序元素，就将已排序元素后移一个位置，为当前元素腾出插入位置。
 * 4.重复步骤3，直到找到合适的插入位置，然后将当前元素插入到已排序部分的正确位置。
 * 5.继续处理下一个未排序元素，重复步骤2和3，直到所有元素都被处理完毕。
 * 6.当所有元素都被插入到已排序部分后，整个数组就变成了有序的。
 * <p>
 * 插入排序的关键思想是将未排序部分的元素一个个地插入到已排序部分中，保持已排序部分始终有序。
 * 与冒泡排序和选择排序不同，插入排序每次都在已排序部分中找到合适的插入位置，从而逐渐构建有序数组。
 * 插入排序在部分已排序的情况下表现较好，它的最好情况时间复杂度为O(n)，最坏情况和平均情况时间复杂度均为O(n^2)。
 */
public class Insertion implements Sort {

    @Override
    public int[] sort(int[] targetArray) {
        int length = targetArray.length;
        if (length <= 1) {
            return targetArray;
        }

        for (int i = 1; i < length; i++) {
            // 要插入的元素
            int current = targetArray[i];
            // 在已排序部分找到合适的插入位置
            int j = i - 1;
            for (; j >= 0 && targetArray[j] > current; j--) {
                // 元素后移, 这里说明一下, 假设已排序部分为0 ~ i-1, 插入位置为n, 那么n ~ i-1这些元素一定大于current, 所以就有了下面的元素后移代码
                targetArray[j + 1] = targetArray[j];
            }
            // 插入当前元素到正确的位置
            targetArray[j + 1] = current;
        }
        return targetArray;
    }

    public static void main(String[] args) {
        new Insertion().test();
    }
}
