package com.example.sort;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

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
        if (PROCESS_BUILDER.length() > 0) {
            System.out.println("排序过程");
            System.out.println(PROCESS_BUILDER);
        }
    }

    default void test() {
        test(DEFAULT_TEST_ARRAY);
    }

    /**
     * 交换数组中的两个元素(index1位置的元素和index2位置的元素交换)
     *
     * @param array  数组
     * @param index1 下标1
     * @param index2 下标2
     */
    default void exchange(int[] array, int index1, int index2) {
        swap2(array, index1, index2);
    }

    /**
     * 交换数组中的两个元素(index1位置的元素和index2位置的元素交换)
     *
     * @param array  数组
     * @param index1 下标1
     * @param index2 下标2
     */
    default void swap(int[] array, int index1, int index2) {
        if (index1 == index2) {
            return;
        }
        array[index1] = array[index1] + array[index2];
        array[index2] = array[index1] - array[index2];
        array[index1] = array[index1] - array[index2];
    }

    /**
     * 交换数组中的两个元素(index1位置的元素和index2位置的元素交换) <br>
     * 前置知识: <br>
     * a ^ 0 = a; a ^ a = 0; a ^ b = b ^ a; (a ^ b) ^ c = a ^ (b ^ c); <br>
     * 所以a,b通过异或交换: <br>
     * 假设 a = 1, b = 2; <br>
     * a = a ^ b = 1 ^ 2 <br>
     * b = a ^ b = (1 ^ 2) ^ 2 = 1 ^ (2 ^ 2) = 1 ^ 0 = 1; <br>
     * a = a ^ b = (1 ^ 2) ^ 1 = (1 ^ 1) ^ 2 = 0 ^ 2 = 2; <br>
     *
     * @param array  数组
     * @param index1 下标1
     * @param index2 下标2
     */
    default void swap2(int[] array, int index1, int index2) {
        if (index1 == index2) {
            return;
        }
        array[index1] = array[index1] ^ array[index2];
        array[index2] = array[index1] ^ array[index2];
        array[index1] = array[index1] ^ array[index2];
    }

    /**
     * 排序过程日志记录
     *
     * @param logRow 日志行
     */
    default void addLog4Process(String... logRow) {
        if (logRow != null) {
            for (String s : logRow) {
                int times = count.get();
                if(times > 0) {
                    PROCESS_BUILDER.append("第").append(times).append("轮");
                }
                PROCESS_BUILDER
                        .append(s).append("\n");
            }
        }
    }

    /**
     * 统计次数(排序次数累加)
     *
     * @return
     */
    default int countTimes() {
        return count.addAndGet(1);
    }

    int[] DEFAULT_TEST_ARRAY = new int[]{12, 1000, 333, 11, 2, 3, 1, 0, 9, 8, 11, 44, 56};
    StringBuffer PROCESS_BUILDER = new StringBuffer();

    AtomicInteger count = new AtomicInteger(0);
}
