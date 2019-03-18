package com.anuraj.weekviewdemo.weekview;

import java.util.Calendar;

public interface DateTimeInterpreter {
    String interpretDate(Calendar date);

    String interpretTime(int hour);

    String interpretQuarter(int hour, int quarter);
}
