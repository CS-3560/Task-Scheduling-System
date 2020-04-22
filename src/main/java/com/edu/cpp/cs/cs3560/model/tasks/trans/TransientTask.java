package com.edu.cpp.cs.cs3560.model.tasks.trans;

import com.edu.cpp.cs.cs3560.model.tasks.AbstractTask;
import com.edu.cpp.cs.cs3560.model.tasks.NonRecurringTask;
import com.edu.cpp.cs.cs3560.model.tasks.Task;
import com.edu.cpp.cs.cs3560.model.tasks.anti.AntiTask;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;
import java.util.Objects;

public class TransientTask extends NonRecurringTask implements Task {


    public TransientTask(String name, String type, LocalDate date, LocalTime startTime, TemporalAmount duration) {
        super(name, type, date, startTime, duration);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString(){
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("Name", name)
                .append("Type", type)
                .append("Date", parseDateToInteger(date))
                .append("StartTime", parseTimeToDouble(startTime))
                .append("Duration", duration)
                .toString()
                .replace("{", "{\n")
                .replace("}", "\n}")
                .replace(",", ",\n");
    }

}
