package com.edu.cpp.cs.cs3560;

import com.edu.cpp.cs.cs3560.model.manager.TaskParser;
import com.edu.cpp.cs.cs3560.model.manager.TaskParser.ParsedTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TaskParserTests {

    private static final String TEST_SET_1 = "/Users/bryanayala/Downloads/Homework4/Set1.json";
    private static final String TEST_SET_2 = "/Users/bryanayala/Downloads/Homework4/Set2.json";

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            //.setDateFormat("yyyyMMdd")
            .create();

    private final TaskParser parser = TaskParser.getInstance();

    @Test
    public void testReadFile() throws IOException {


        List<ParsedTask> tasks = parser.parseFile(TEST_SET_1);

        ParsedTask task = tasks.get(0);

        System.out.println(gson.toJson(task));
    }






}
