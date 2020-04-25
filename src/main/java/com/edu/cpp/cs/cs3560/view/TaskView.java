package com.edu.cpp.cs.cs3560.view;

import com.edu.cpp.cs.cs3560.model.tasks.Task;
import com.edu.cpp.cs.cs3560.model.types.PSSOperation;

import java.util.Collection;

public interface TaskView {

    String getInput(String prompt);

    PSSOperation getOperation();

    void displayTask(Task task);

    void displayTasks(Collection<Task> task);

    void displayMessage(String message);

    void displayError(String message);

}
