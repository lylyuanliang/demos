# 0. 可视化算法网站

> [https://visualgo.net/zh/sorting](https://visualgo.net/zh/sorting)

# 1.冒泡排序（Bubble Sort）

> 逐步比较相邻元素并进行交换，将最大（或最小）的元素逐步“冒泡”到数组的末尾。时间复杂度为O(n^2)。

> ```
> com.example.sort.impl.Bubble
> ```

# 2.插入排序（Insertion Sort）

> 将数组分为已排序和未排序两部分，逐个将未排序元素插入到已排序部分的正确位置。时间复杂度为O(n^2)，在部分已排序的情况下性能较好。

> ```
> com.example.sort.impl.Insertion
> ```

# 3.选择排序（Selection Sort）

> 每次选择未排序部分的最小（或最大）元素，并放置在已排序部分的末尾。时间复杂度为O(n^2)，不论输入数据是否有序，性能表现相似。

> ```
> com.example.sort.impl.Selection
> ```

# 4.快速排序（Quick Sort）

> 使用分治法的思想，选择一个基准元素，将数组分为小于基准和大于基准的两部分，递归地对这两部分进行排序。平均情况下的时间复杂度为O(n log n)。

> ```
> com.example.sort.impl.Quick
> ```

# 5.归并排序（Merge Sort）

> 将数组递归地分为较小的子数组，然后将子数组合并以得到已排序的数组。时间复杂度为O(n log n)，需要额外的存储空间。

> ```
> com.example.sort.impl.Merge
> ```

# 6.堆排序（Heap Sort）

> 将数组看作二叉堆，构建最大（或最小）堆，然后逐步将堆顶元素取出并进行调整，得到有序数组。时间复杂度为O(n log n)，具有较好的性能。

> ```
> com.example.sort.impl.Heap
> ```


# 7.希尔排序（Shell Sort）

> 对数组进行多次分组排序，每次分组的间隔逐步减小，直到间隔为1，然后进行最后一次排序。时间复杂度在理想情况下为O(n log n)。

> ```
> com.example.sort.impl.Shell
> ```

# 8.计数排序（Counting Sort）

> 适用于具有确定范围的整数数据，统计每个元素出现的次数，然后根据计数重建有序数组。时间复杂度为O(n + k)，其中k为数据范围。

> > ```
> com.example.sort.impl.Counting
> ```

# 9.基数排序（Radix Sort）

> 适用于整数数据，按照低位到高位的顺序进行排序，每次按某个位数进行桶排序。时间复杂度为O(nk)，其中k为数字的位数