package com.example.sort;

import java.util.Arrays;

public interface Sort {
    int[] sort(int[] targetArray);

    /**
     * 测试
     *
     * @param targetArray 待排序数组
     */
    default void test(int[] targetArray) {
        System.out.println("待排序数组为:");
        System.out.println(Arrays.toString(targetArray));
        System.out.println("排序后数组为:");
        System.out.println(Arrays.toString(this.sort(targetArray)));
    }

    default void test() {
        test(DEFAULT_TEST_ARRAY);
    }

    int[] DEFAULT_TEST_ARRAY= new int[] {12, 1000, 333, 11, 2, 3, 1, 0, 9, 8, 11, 44, 56};
}
