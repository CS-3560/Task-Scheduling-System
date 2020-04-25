package com.edu.cpp.cs.cs3560;

import com.edu.cpp.cs.cs3560.model.manager.TaskModelManager;
import com.edu.cpp.cs.cs3560.model.tasks.trans.TransientTask;
import com.google.common.collect.HashMultimap;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;


public class Scenario1Tests {

    /*
    These Tests are not implemented correctly, this is just a rough draft
     */

    private static final String DELETE_TASK_NAME = "Intern Interview";
    private static final String INVALID_TASK_TYPE_MESSAGE = "Invalid Task Type";
    private static final String OVERLAP_ERROR_MESSAGE_1 = "Task [Watch a movie] overlaps with Task [CS3560-Th]";
    private static final String OVERLAP_ERROR_MESSAGE_2 = "Task [Dinner] overlaps with Task [Intern Interview]";

    private static final TransientTask transientTask1 = ScenarioTestsUtils.createTransientTask(
            "Intern Interview",
            "Appointment",
            20200427,
            17,
            2.5
    );

    private static final TransientTask transientTask2 = ScenarioTestsUtils.createTransientTask(
            "Watch a movie",
            "Movie",
            20200429,
            21.5,
            2
    );

    private static final TransientTask transientTask3 = ScenarioTestsUtils.createTransientTask(
            "Watch a movie",
            "Visit",
            20200430,
            18.5,
            2
    );

    private final TaskModelManager manager = new TaskModelManager();

    @Before
    public void readSet1() throws IOException {
        ScenarioTestsUtils.parseFile(ScenarioTestConfig.TEST_SET_1).forEach(manager::addTask);
    }

    @Test
    public void deleteTask() {
        assertTrue(manager.taskExists(DELETE_TASK_NAME));
        manager.removeTask(DELETE_TASK_NAME);
        assertFalse(manager.taskExists(DELETE_TASK_NAME));
        assertFalse(manager.getAllTasks().stream().allMatch(t -> t.getName().equals(DELETE_TASK_NAME)));
    }

    @Test
    public void addTransientTask1Test(){
        deleteTask();

        manager.addTask(transientTask1);
        assertEquals(manager.getTask(transientTask1.getName()), transientTask1);
    }

    @Test(expected = RuntimeException.class)
    public void addTransientTask2Test(){
        addTransientTask1Test();

        manager.addTask(transientTask2);
    }

    @Test(expected = RuntimeException.class)
    public void addTransientTask3Test(){
        addTransientTask1Test();

        manager.addTask(transientTask3);
    }

    @Test(expected = RuntimeException.class)
    public void readFileSet2Test() throws IOException {
        addTransientTask1Test();

        ScenarioTestsUtils.parseFile(ScenarioTestConfig.TEST_SET_2).forEach(manager::addTask);
    }

    @Test
    public void runScenario1Test() throws IOException {
        //2
        assertTrue(manager.taskExists(DELETE_TASK_NAME));
        manager.removeTask(DELETE_TASK_NAME);
        assertFalse(manager.taskExists(DELETE_TASK_NAME));
        assertFalse(manager.getAllTasks().stream().allMatch(t -> t.getName().equals(DELETE_TASK_NAME)));

        //3
        manager.addTask(transientTask1);
        assertEquals(manager.getTask(transientTask1.getName()), transientTask1);

        //4
        try {
            manager.addTask(transientTask2);
        } catch (RuntimeException e){
            assertEquals(INVALID_TASK_TYPE_MESSAGE, e.getMessage());
        }

        //5
        try {
            manager.addTask(transientTask3);
        } catch (RuntimeException e){
            assertEquals( OVERLAP_ERROR_MESSAGE_1, e.getMessage());
        }

        //6
        try {
            ScenarioTestsUtils.parseFile(ScenarioTestConfig.TEST_SET_2).forEach(manager::addTask);
        } catch (RuntimeException e){
            assertEquals(OVERLAP_ERROR_MESSAGE_2, e.getMessage());
        }

    }

}
