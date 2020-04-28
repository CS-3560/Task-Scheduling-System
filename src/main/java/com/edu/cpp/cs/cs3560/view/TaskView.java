package com.edu.cpp.cs.cs3560.view;

import com.edu.cpp.cs.cs3560.model.tasks.Task;
import com.edu.cpp.cs.cs3560.model.types.PSSOperation;

import java.util.Collection;
import java.util.Map;

public interface TaskView {

    String OPERATION_KEY = "Operation";

    String NAME_KEY = "Name";

    String TYPE_KEY = "Type";

    String FILE_KEY = "File";

    String SCHEDULE_START_DATE_KEY = "StartDate";

    String VIEW_OPERATION_VALUE = "VIEW";

    String WRITE_OPERATION_VALUE = "WRITE";

    String DAY_SCHEDULE_OPERATION_VALUE = "DAY";

    String WEEK_SCHEDULE_OPERATION_VALUE = "WEEK";

    String MONTH_SCHEDULE_OPERATION_VALUE = "MONTH";

    String WHOLE_SCHEDULE_OPERATION_VALUE = "WHOLE";

    String getInput(String prompt);

    Map<String, String> getInputs(String ...s);

    Map<String, Object> getUpdatedInfo(Map<String, Object> original);

    PSSOperation getOperation();

    void displayTask(Task task);

    void displayTasks(Collection<Task> task);

    void displayMessage(String message);

    void displayError(String message);

}
