package com.edu.cpp.cs.cs3560.model.tasks.recurring;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;


public enum Frequency {

    DAILY(1, ChronoUnit.DAYS),
    WEEKLY(7, ChronoUnit.WEEKS),
    MONTHLY(30, ChronoUnit.MONTHS);

    private static final Map<Integer, Frequency> frequencies = Arrays.stream(values())
            .collect(Collectors.toMap(Frequency::getKey, f -> f));


    public final int key;
    public final TemporalUnit unit;

    Frequency(int key, TemporalUnit unit){
        this.key = key;
        this.unit = unit;
    }

    public int getKey(){ return key; }

    public TemporalUnit getUnit(){ return unit; }

    public static Frequency getFrequency(int key){
        return frequencies.get(key);
    }

}



