package com.example.sort.impl;

import com.example.sort.Sort;

import java.util.Arrays;

/**
 * 冒泡排序<br>
 * 1. 遍历待排序的数组，从第一个元素开始比较相邻的两个元素。<br>
 * 2. 如果前面的元素大于后面的元素（不满足排序顺序），则交换这两个元素的位置，使得较大的元素“冒泡”到数组的后面。<br>
 * 3. 继续进行相邻元素的比较和交换，直到遍历整个数组。这一轮遍历结束后，最大的元素已经被移动到数组的最后。<br>
 * 4. 重复上述步骤，但这次不再考虑已经排好序的最后一个元素，而是考虑剩余的未排序部分，继续执行比较和交换操作，将次大的元素“冒泡”到倒数第二个位置。<br>
 * 5. 持续进行多轮的遍历，每次都将未排序部分中的最大元素移动到已排序部分的末尾。<br>
 * 6. 当所有元素都被放置在正确的位置上，排序完成。<br><br>
 * 时间复杂度O(n^2)
 */
public class Bubble implements Sort {
    @Override
    public int[] sort(int[] targetArray) {
        int length = targetArray.length;
        if (length <= 1) {
            return targetArray;
        }
        for (int i = 0; i < length; i++) {
            boolean flag = false;
            // 每一轮遍历将最大元素冒泡到末尾
            for (int j = 0; j < length - i - 1; j++) {
                // 比较并交换
                if (targetArray[j] > targetArray[j + 1]) {
                    exchange(targetArray, j, j+1);
                    flag = true;
                }
            }
            if (!flag) {
                // 无交换, 比较结束
                break;
            }
        }

        return targetArray;
    }

    public static void main(String[] args) {
        new Bubble().test();
    }
}
