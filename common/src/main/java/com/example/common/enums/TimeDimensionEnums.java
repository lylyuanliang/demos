package com.example.common.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author liurl
 * @Description 日期时间维度转换枚举类
 * @Date 2024/12/31 16:20
 * @Created by liurl
 */
@AllArgsConstructor
@Getter
public enum TimeDimensionEnums {
    YEAR(1, "年") {
        @Override
        public TimeRange calculateTimeRange() {
            LocalDate today = LocalDate.now();
            // 当前年份的第一天开始
            LocalDateTime startDateTime = today.withDayOfYear(1).atStartOfDay();
            // 当前年份的最后一天结束
            LocalDateTime endDateTime = today.withDayOfYear(today.lengthOfYear())
                    .atTime(23, 59, 59);
            return new TimeRange(startDateTime, endDateTime);
        }
    },
    MONTH(2, "月") {
        @Override
        public TimeRange calculateTimeRange() {
            LocalDate today = LocalDate.now();
            // 当前月的第一天开始
            LocalDateTime startDateTime = today.withDayOfMonth(1).atStartOfDay();
            // 当前月的最后一天结束
            LocalDateTime endDateTime = today.withDayOfMonth(today.lengthOfMonth())
                    .atTime(23, 59, 59);

            return new TimeRange(startDateTime, endDateTime);
        }
    },
    WEEK(3, "周") {
        @Override
        public TimeRange calculateTimeRange() {
            LocalDate today = LocalDate.now();
            // 获取当前周的开始日期 (ISO-8601 周从星期一开始)
            LocalDate weekStart = today.with(DayOfWeek.MONDAY);
            // 获取当前周的结束日期 (ISO-8601 周从星期日结束)
            LocalDate weekEnd = weekStart.plusDays(6);
            // 当前周的开始时间
            LocalDateTime startDateTime = weekStart.atStartOfDay();
            // 当前周的结束时间
            LocalDateTime endDateTime = weekEnd.atTime(23, 59, 59);

            return new TimeRange(startDateTime, endDateTime);
        }
    },
    DAY(4, "日") {
        @Override
        public TimeRange calculateTimeRange() {
            LocalDate today = LocalDate.now();
            // 获取昨天的日期
            LocalDate yesterday = today.minusDays(1);
            // 昨天的开始时间
            LocalDateTime startDateTime = yesterday.atStartOfDay();
            // 昨天的结束时间
            LocalDateTime endDateTime = yesterday.atTime(23, 59, 59);

            return new TimeRange(startDateTime, endDateTime);
        }
    };

    private final Integer key;
    private final String desc;

    public static TimeRange getTimeRange(Integer dimension) throws IllegalAccessException {
        TimeDimensionEnums[] values = TimeDimensionEnums.values();
        for (TimeDimensionEnums value : values) {
            if (Objects.equals(value.getKey(), dimension)) {
                return value.calculateTimeRange();
            }
        }

        throw new IllegalAccessException("不匹配的dimension=[" + dimension + "]");
    }

    public abstract TimeRange calculateTimeRange();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeRange {
        private LocalDateTime startDateTime;
        private LocalDateTime endDateTime;
    }
}