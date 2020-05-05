package com.edu.cpp.cs.cs3560;

import com.edu.cpp.cs.cs3560.model.manager.TaskModelManager;
import com.edu.cpp.cs.cs3560.model.tasks.anti.AntiTask;
import com.google.common.collect.HashMultimap;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


import java.io.IOException;

public class Scenario2Tests {

    /*
    These Tests are not implemented correctly, this is just a rough draft
     */

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



}
