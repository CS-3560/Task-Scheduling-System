package com.edu.cpp.cs.cs3560.model.tasks.anti;

import com.edu.cpp.cs.cs3560.model.tasks.AbstractTask;
import com.edu.cpp.cs.cs3560.model.tasks.Task;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;


public class AntiTask extends AbstractTask implements Task {

    public AntiTask(String name, String type, LocalDate date, LocalTime startTime, Duration duration) {
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
                .append("Date", date)
                .append("StartTime", startTime)
                .append("Duration", duration)
                .toString();
    }


}
