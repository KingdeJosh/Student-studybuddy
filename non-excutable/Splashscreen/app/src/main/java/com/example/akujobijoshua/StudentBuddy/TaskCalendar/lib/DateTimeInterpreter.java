package com.example.akujobijoshua.StudentBuddy.TaskCalendar.lib;

import java.util.Calendar;


public interface DateTimeInterpreter {
    String interpretDate(Calendar date);
    String interpretTime(int hour);
}
