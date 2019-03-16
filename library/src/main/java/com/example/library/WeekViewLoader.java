package com.example.library;

import java.util.Calendar;
import java.util.List;

public interface WeekViewLoader {
    /**
     *将日期转换为双精度值，以便在加载数据时用于引用。
     *     *
           *所有具有相同整数部分的期间，定义一个期间。晚些时候的日期
           *应该有更大的返回值。
     * Convert a date into a double that will be used to reference when you're loading data.
     *      *
     *      * All periods that have the same integer part, define one period. Dates that are later in time
     *      * should have a greater return value.
     *
     * @param instance the date
     * @return The period index in which the date falls (floating point number).
     */
    double toWeekViewPeriodIndex(Calendar instance);

    /**
     * Load the events within the period
     * @param periodIndex the period to load
     * @return A list with the events of this period
     */
    List<? extends WeekViewEvent> onLoad(int periodIndex);
}
