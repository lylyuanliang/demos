package com.example.common.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

/**
 * 连续元素分割工具类
 */
public class ContinuousListUtils {
    /**
     * 取出连续的部分作为一个新list
     *
     * @param sortedList 有序原始集合
     * @return 多个list, 每个list里面的值都是连续的
     */
    public static List<List<Integer>> getContinuousLists(List<Integer> sortedList) {
        return getContinuousLists(sortedList, 1);
    }

    /**
     * 取出连续的部分作为一个新list
     *
     * @param sortedList 有序原始集合
     * @param interval   连续间隔
     * @return 多个list, 每个list里面的值都是连续的
     */
    public static List<List<Integer>> getContinuousLists(List<Integer> sortedList, int interval) {

        List<Integer> sortetdList = sortedList.stream().sorted().collect(Collectors.toList());

        return getContinuousLists(sortetdList, value -> value, interval);
    }

    /**
     * 分割出连续数据作为一个新list
     *
     * @param sortedList 有序原始集合
     * @param value2Int  将集合中的对象转成int, 做连续判断
     * @param <T>        目标值(原始值)
     * @return
     */
    public static <T> List<List<T>> getContinuousLists(List<T> sortedList, ToIntFunction<T> value2Int) {
        return getContinuousLists(sortedList, value2Int, value -> value, 1);
    }

    /**
     * 分割出连续数据作为一个新list
     *
     * @param sortedList 有序原始集合
     * @param value2Int  将集合中的对象转成int, 做连续判断
     * @param interval   连续间隔
     * @param <T>        目标值(原始值)
     * @return
     */
    public static <T> List<List<T>> getContinuousLists(List<T> sortedList, ToIntFunction<T> value2Int, int interval) {
        return getContinuousLists(sortedList, value2Int, value -> value, interval);
    }

    /**
     * 分割出连续数据作为一个新list
     *
     * @param sortedList  有序原始集合
     * @param value2Int   将集合中的对象转成int, 做连续判断
     * @param intervalFun 连续间隔
     * @param <T>         目标值(原始值)
     * @return
     */
    public static <T> List<List<T>> getContinuousLists(List<T> sortedList, ToIntFunction<T> value2Int, ToIntFunction<T> intervalFun) {
        return getContinuousLists(sortedList, value2Int, value -> value, intervalFun);
    }

    /**
     * 分割出连续数据作为一个新list, 并将原始对象转换成另一个对象
     *
     * @param sortedList 有序原始集合
     * @param value2Int  将集合中的对象转成int的实现, 做连续判断
     * @param value2Rtn  原始对象转换成新对象的实现
     * @param interval   连续间隔
     * @param <T>        目标值类型(原始值)
     * @param <R>        返回值类型
     * @return
     */
    public static <T, R> List<List<R>> getContinuousLists(List<T> sortedList, ToIntFunction<T> value2Int, Function<T, R> value2Rtn, int interval) {
        return getContinuousLists(sortedList, value2Int, value2Rtn, null, interval);
    }

    /**
     * 分割出连续数据作为一个新list, 并将原始对象转换成另一个对象
     *
     * @param sortedList  有序原始集合
     * @param value2Int   将集合中的对象转成int的实现, 做连续判断
     * @param value2Rtn   原始对象转换成新对象的实现
     * @param intervalFun 连续间隔
     * @param <T>         目标值类型(原始值)
     * @param <R>         返回值类型
     * @return
     */
    public static <T, R> List<List<R>> getContinuousLists(List<T> sortedList, ToIntFunction<T> value2Int, Function<T, R> value2Rtn, ToIntFunction<T> intervalFun) {
        return getContinuousLists(sortedList, value2Int, value2Rtn, null, intervalFun);
    }


    /**
     * 分割出连续数据作为一个新list, 并将原始对象转换成另一个对象
     *
     * @param list      原始集合(需要有序或者传一个比较器的实现, 见comparing)
     * @param value2Int 将集合中的对象转成int的实现, 做连续判断
     * @param value2Rtn 原始对象转换成新对象的实现
     * @param comparing 比较器, 用来排序, 如果为空则不排序(这种情况需要保证list有序)
     * @param interval  连续间隔
     * @param <T>       目标值类型(原始值)
     * @param <R>       返回值类型
     * @return
     */
    public static <T, R> List<List<R>> getContinuousLists(List<T> list, ToIntFunction<T> value2Int, Function<T, R> value2Rtn, Comparator<T> comparing, int interval) {
        // 这里的间隔是恒定的, 所以ToIntFunction这个参数直接 传参 anyEle -> interval, 这样就会始终返回interval了
        return getContinuousLists(list, value2Int, value2Rtn, comparing, anyEle -> interval);
    }

    /**
     * 分割出连续数据作为一个新list, 并将原始对象转换成另一个对象
     *
     * @param list        原始集合(需要有序或者传一个比较器的实现, 见comparing)
     * @param value2Int   将集合中的对象转成int的实现, 做连续判断
     * @param value2Rtn   原始对象转换成新对象的实现
     * @param comparing   比较器, 用来排序, 如果为空则不排序(这种情况需要保证list有序)
     * @param intervalFun 连续间隔
     * @param <T>         目标值类型(原始值)
     * @param <R>         返回值类型
     * @return
     */
    public static <T, R> List<List<R>> getContinuousLists(List<T> list, ToIntFunction<T> value2Int, Function<T, R> value2Rtn, Comparator<T> comparing, ToIntFunction<T> intervalFun) {
        return getContinuousLists(list, value2Rtn, comparing,
                (current, previous) -> {
                    int currentInt = value2Int.applyAsInt(current);
                    int previousInt = value2Int.applyAsInt(previous);

                    // 间隔由上一个元素决定
                    int interval = intervalFun.applyAsInt(previous);

                    return currentInt == previousInt + interval;
                });
    }

    /**
     * 分割出连续数据作为一个新list, 并将原始对象转换成另一个对象
     *
     * @param list        原始集合(需要有序或者传一个比较器的实现, 见comparing)
     * @param value2Rtn   原始对象转换成新对象的实现
     * @param comparing   比较器, 用来排序, 如果为空则不排序(这种情况需要保证list有序)
     * @param intervalFun 连续间隔
     * @param <T>         目标值类型(原始值)
     * @param <R>         返回值类型
     * @return
     */
    public static <T, R> List<List<R>> getContinuousLists(List<T> list, Function<T, R> value2Rtn, Comparator<T> comparing, BiPredicate<T, T> intervalFun) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        if (comparing != null) {
            list.sort(comparing);
        }

        List<List<R>> result = new ArrayList<>();
        List<R> continuousList = new ArrayList<>();
        T firstValue = list.get(0);
        R firstRtnValue = value2Rtn.apply(firstValue);
        continuousList.add(firstRtnValue);

        int size = list.size();
        for (int i = 1; i < size; i++) {
            T current = list.get(i);
            T previous = list.get(i - 1);

            R returnValue = value2Rtn.apply(current);

            boolean isContinuous = intervalFun.test(current, previous);

            if (isContinuous) {
                continuousList.add(returnValue);
            } else {
                result.add(continuousList);
                continuousList = new ArrayList<>();
                continuousList.add(returnValue);
            }
        }

        result.add(continuousList);

        return result;
    }
}
