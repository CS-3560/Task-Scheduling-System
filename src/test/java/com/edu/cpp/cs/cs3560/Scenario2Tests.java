package com.edu.cpp.cs.cs3560;

import com.edu.cpp.cs.cs3560.model.manager.TaskModelManager;
import com.edu.cpp.cs.cs3560.model.tasks.anti.AntiTask;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


import java.io.IOException;

public class Scenario2Tests {

    private static final String ERROR_MESSAGE_1 = "No tasks match with task [Skip-out]";

    private static final AntiTask antiTask1 = ScenarioTestsUtils.createAntiTask(
            "Skip-out",
            "Cancellation",
            20200430,
            19.25,
            .75
    );

    private static final AntiTask antiTask2 = ScenarioTestsUtils.createAntiTask(
            "Skip a meal",
            "Cancellation",
            20200428,
            17,
            1
    );


    private final TaskModelManager manager = new TaskModelManager();


    @Before
    public void readFileSet2() throws IOException {
        ScenarioTestsUtils.parseFile(ScenarioTestConfig.TEST_SET_2).forEach(manager::addTask);
    }

    @Test(expected = RuntimeException.class)
    public void addAntiTask1Test(){
        manager.addTask(antiTask1);
    }

    @Test
    public void addAntiTask2Test(){
        //System.out.println(antiTask2.getDuration() + "\n");
        //manager.getAllTasks().stream().filter(t -> t.getName().equals("Dinner")).map(Task::getDuration).forEach(System.out::println);
        //manager.getAllTasks().stream().map(Task::getName).forEach(System.out::println);
        manager.addTask(antiTask2);
    }

    @Test
    public void readFileSet1() throws IOException {
        addAntiTask2Test();

        ScenarioTestsUtils.parseFile(ScenarioTestConfig.TEST_SET_1).forEach(manager::addTask);
    }

    @Test
    public void runScenario2Test() throws IOException {
        try {
            manager.addTask(antiTask1);
        } catch (RuntimeException e){
            assertEquals(ERROR_MESSAGE_1, e.getMessage());
        }

        manager.addTask(antiTask2);

        ScenarioTestsUtils.parseFile(ScenarioTestConfig.TEST_SET_1).forEach(manager::addTask);
    }

}
