package com.edu.cpp.cs.cs3560.model.tasks.recurring;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.stream.Collectors;


public enum Frequency {

    DAILY(1, ChronoUnit.DAYS),
    WEEKLY(7, ChronoUnit.WEEKS),
    MONTHLY(30, ChronoUnit.MONTHS);

    private static final BiMap<Frequency, Integer> frequencies = ImmutableBiMap.copyOf(
            Arrays.stream(values()).collect(Collectors.toMap(f -> f, Frequency::getKey))
    );

    public final int key;
    public final TemporalUnit unit;

    Frequency(int key, TemporalUnit unit){
        this.key = key;
        this.unit = unit;
    }

    public int getKey(){ return key; }

    public TemporalUnit getUnit(){ return unit; }

    public static Frequency getFrequency(int key){
        return frequencies.inverse().get(key);
    }

    public static int getFrequencyKey(Frequency frequency){
        return frequencies.get(frequency);
    }

}



