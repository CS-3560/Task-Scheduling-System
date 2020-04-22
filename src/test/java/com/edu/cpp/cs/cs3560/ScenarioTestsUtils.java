package com.edu.cpp.cs.cs3560;

import com.edu.cpp.cs.cs3560.io.tasks.TaskParser;
import com.edu.cpp.cs.cs3560.model.tasks.Task;
import com.edu.cpp.cs.cs3560.model.tasks.anti.AntiTask;
import com.edu.cpp.cs.cs3560.model.tasks.recurring.Frequency;
import com.edu.cpp.cs.cs3560.model.tasks.recurring.RecurringTask;
import com.edu.cpp.cs.cs3560.model.tasks.trans.TransientTask;

import java.io.IOException;
import java.util.List;

public final class ScenarioTestsUtils {
    private static final TaskParser parser = TaskParser.getInstance();

    private ScenarioTestsUtils(){
        throw new UnsupportedOperationException();
    }

    public static TransientTask createTransientTask(
            String name,
            String type,
            int date,
            double startTime,
            double duration
    ){
        return new TransientTask(name, type, parser.parseDate(date), parser.parseTime(startTime), parser.parseDuration(duration));
    }

    public static AntiTask createAntiTask(
            String name,
            String type,
            int date,
            double startTime,
            double duration
    ){
        return new AntiTask(name, type, parser.parseDate(date), parser.parseTime(startTime), parser.parseDuration(duration));
    }

    public static RecurringTask createRecurringTask(
            String name,
            String type,
            int startDate,
            double startTime,
            double duration,
            int endDate,
            int frequency
    ){
        return new RecurringTask(name, type, parser.parseDate(startDate), parser.parseTime(startTime), parser.parseDuration(duration), parser.parseDate(endDate), Frequency.getFrequency(frequency));
    }

    public static List<Task> parseFile(String file) throws IOException {
        return parser.parseTasks(parser.parseFile(file));
    }

}
