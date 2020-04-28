package com.edu.cpp.cs.cs3560.util;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.TemporalAmount;

public final class TaskParser {
    private static final DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE.withResolverStyle(ResolverStyle.STRICT);

    public static LocalDate parseDate(final int date){
        return parseDate(String.valueOf(date));
    }

    public static LocalDate parseDate(final String date){
        return LocalDate.parse(date, formatter);
    }

    public static LocalTime parseTime(final String time){
        return parseTime(Double.parseDouble(time));
    }

    public static LocalTime parseTime(final double time){
        final int hour = (int) time;
        final int minute = (int) ((time - hour) * 60);

        return LocalTime.of(hour, minute);
    }

    public static TemporalAmount parseDuration(final String duration){
        return parseDuration(Double.parseDouble(duration));
    }

    public static TemporalAmount parseDuration(final double duration){
        final long hours = (long) duration;
        final long minutes = (long) ((duration - hours) * 60);

        return Duration.ofHours(hours).plusMinutes(minutes);
    }

    public static int parseDateToInteger(final LocalDate date){
        return Integer.parseInt(parseDateToString(date));
    }

    public static String parseDateToString(final LocalDate date){
        return date.format(formatter);
    }

    public static double parseTimeToDouble(final LocalTime time){
        return calculateDuration(time.getHour(), time.getMinute());
    }

    public static double parseDuration(final TemporalAmount amount){
        final Duration duration = Duration.from(amount);

        return calculateDuration(duration.toHoursPart(), duration.toMinutesPart());
    }

    public static boolean isValidDateFormat(final String date){
        try {
            parseDate(date);
        } catch (DateTimeParseException e){
            return false;
        }

        return true;
    }

    public static DateTimeFormatter getFormatter(){ return formatter; }

    private static double calculateDuration(final double hour, final double minute){
        return hour + (minute / 60.0);
    }

}
