package com.edu.cpp.cs.cs3560.io.tasks;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.TemporalAmount;

public final class TaskParser {
    private static final DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE.withResolverStyle(ResolverStyle.STRICT);

    public static LocalDate parseDate(int date){
        return parseDate(String.valueOf(date));
    }

    public static LocalDate parseDate(String date){
        return LocalDate.parse(date, formatter);
    }

    public static LocalTime parseTime(String time){
        return parseTime(Double.parseDouble(time));
    }

    public static LocalTime parseTime(double time){
        int hour = (int) time;
        int minute = (int) ((time - hour) * 60);

        return LocalTime.of(hour, minute);
    }

    public static TemporalAmount parseDuration(String duration){
        return parseDuration(Double.parseDouble(duration));
    }

    public static TemporalAmount parseDuration(double duration){
        long hours = (long) duration;
        long minutes = (long) ((duration - hours) * 60);

        return Duration.ofHours(hours).plusMinutes(minutes);
    }

    public static int parseDateToInteger(LocalDate date){
        return Integer.parseInt(parseDateToString(date));
    }

    public static String parseDateToString(LocalDate date){
        return date.format(formatter);
    }

    public static double parseTimeToDouble(LocalTime time){
        return calculateDuration(time.getHour(), time.getMinute());
    }

    public static double parseDuration(TemporalAmount amount){
        Duration duration = Duration.from(amount);

        return calculateDuration(duration.toHoursPart(), duration.toMinutesPart());
    }

    public static boolean isValidDateFormat(String date){
        try {
            parseDate(date);
        } catch (DateTimeParseException e){
            return false;
        }

        return true;
    }

    public static DateTimeFormatter getFormatter(){ return formatter; }

    private static double calculateDuration(double hour, double minute){
        return hour + (minute / 60.0);
    }

}
