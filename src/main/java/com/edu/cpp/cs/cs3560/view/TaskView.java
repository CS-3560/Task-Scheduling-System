package com.edu.cpp.cs.cs3560.view;

import com.edu.cpp.cs.cs3560.io.tasks.TaskSerializer;
import com.edu.cpp.cs.cs3560.model.tasks.Task;
import com.edu.cpp.cs.cs3560.model.types.PSSOperation;
import com.edu.cpp.cs.cs3560.model.types.PSSOperation.PSSOperationType;
import com.edu.cpp.cs.cs3560.model.types.TaskTypes;
import com.edu.cpp.cs.cs3560.ui.UserInterface;
import com.google.common.collect.ImmutableSet;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class TaskView {

    private static final String INVALID_INPUT_ERROR_MESSAGE = "Invalid Input. Please Try Again.";

    private static final String CORRECTNESS_PROMPT = "Is this correct? [Y,n]";

    private static final String PROMPT_USER_INPUT_FORMAT = "Enter %s:\t";

    private static final String FILE_PATH_PROMPT = "Enter path of file to write to: ";

    private static final String READ_OPTION = "Read";

    private static final String WRITE_OPTION = "Write";

    private static final String INPUT_NON_APPLICABLE_MESSAGE = "If not applicable, enter N/A";

    private static final String CREATE_TASK_PSS_OPERATION = "Create a task.";

    private static final String VIEW_TASK_PSS_OPERATION = "View a task.";

    private static final String DELETE_TASK_PSS_OPERATION = "Delete a task";

    private static final String EDIT_TASK_PSS_OPERATION = "Edit a task";

    private static final String WRITE_TO_FILE_PSS_OPERATION = "Write the Schedule to a file";

    private static final String READ_FROM_FILE_PSS_OPERATION = "Read the schedule from a file";

    private static final String DAY_SCHEDULE_PSS_OPERATION = "View or write the schedule for one day";

    private static final String WEEK_SCHEDULE_PSS_OPERATION = "View or write the schedule for one week";

    private static final String MONTH_SCHEDULE_PSS_OPERATION = "View or write the schedule for one month";

    private static final String PSS_OPERATION_OPTIONS = "Operation options:" +
            "\n\t1.\t" + CREATE_TASK_PSS_OPERATION +
            "\n\t2.\t" + VIEW_TASK_PSS_OPERATION +
            "\n\t3.\t" + DELETE_TASK_PSS_OPERATION +
            "\n\t4.\t" + EDIT_TASK_PSS_OPERATION +
            "\n\t5.\t" + WRITE_TO_FILE_PSS_OPERATION +
            "\n\t6.\t" + READ_FROM_FILE_PSS_OPERATION +
            "\n\t7.\t" + DAY_SCHEDULE_PSS_OPERATION +
            "\n\t8.\t" + WEEK_SCHEDULE_PSS_OPERATION +
            "\n\t9.\t" + MONTH_SCHEDULE_PSS_OPERATION +
            "\n\nEnter operation to perform: ";

    private static final String[] EXIT_FLAGS = {"QUIT", "Q"};

    private static final String[] DONE_FLAGS = {"DONE", "D"};

    private static final Map<Integer, String> operations = Map.of(
            1, CREATE_TASK_PSS_OPERATION,
            2, VIEW_TASK_PSS_OPERATION,
            3, DELETE_TASK_PSS_OPERATION,
            4, EDIT_TASK_PSS_OPERATION,
            5, WRITE_TO_FILE_PSS_OPERATION,
            6, READ_FROM_FILE_PSS_OPERATION,
            7, DAY_SCHEDULE_PSS_OPERATION,
            8, WEEK_SCHEDULE_PSS_OPERATION,
            9, MONTH_SCHEDULE_PSS_OPERATION
    );

    private static final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .setPrettyPrinting()
            .create();

    private final UserInterface ui;
    private final TaskSerializer serializer;

    public TaskView(UserInterface ui, TaskSerializer serializer) {
        this.ui = ui;
        this.serializer = serializer;
    }

    public String getInput(String prompt){
        return ui.getInput(prompt);
    }

   public PSSOperation getOperation(){
       Set<String> options = ImmutableSet.copyOf(operations.values());

        String operation;
        do {
            String input = ui.getInput(PSS_OPERATION_OPTIONS);

            operation = NumberUtils.isDigits(input) ? operations.get(Integer.parseInt(input)) : input;
            if(options.contains(operation)){
                ui.displayln(INVALID_INPUT_ERROR_MESSAGE);
            }
        } while(!options.contains(operation));

        PSSOperation.Builder builder = PSSOperation.builder();
        if(StringUtils.equalsAny(operation, CREATE_TASK_PSS_OPERATION, VIEW_TASK_PSS_OPERATION, DELETE_TASK_PSS_OPERATION, EDIT_TASK_PSS_OPERATION)){
            switch (operation) {
                case CREATE_TASK_PSS_OPERATION:
                    builder.withType(PSSOperationType.CREATE_TASK);
                    break;
                case VIEW_TASK_PSS_OPERATION:
                    builder.withType(PSSOperationType.VIEW_TASK);
                    break;
                case DELETE_TASK_PSS_OPERATION:
                    builder.withType(PSSOperationType.DELETE_TASK);
                    break;
                case EDIT_TASK_PSS_OPERATION:
                    builder.withType(PSSOperationType.EDIT_TASK);
                    break;
                default:
                    throw new RuntimeException(INVALID_INPUT_ERROR_MESSAGE);
            }

            builder.withData(builder.getType() == PSSOperationType.CREATE_TASK ? getTaskInfo() : getTaskName());
        } else if(StringUtils.equalsAny(operation, READ_FROM_FILE_PSS_OPERATION, WRITE_TO_FILE_PSS_OPERATION)){
            String iio;
            do{
                iio = ui.getInput("[Read] from file or [Write] to file?");
                if(iio.equalsIgnoreCase(READ_OPTION)){
                    builder.withType(PSSOperationType.READ_FROM_FILE);
                } else if(iio.equalsIgnoreCase(WRITE_OPTION)){
                    builder.withType(PSSOperationType.WRITE_TO_FILE);
                } else {
                    ui.displayln(INVALID_INPUT_ERROR_MESSAGE);
                }
            } while(!StringUtils.equalsAnyIgnoreCase(iio, READ_OPTION, WRITE_OPTION));
            builder.withData(getFileInfo());
        } else if(StringUtils.equalsAny(operation, DAY_SCHEDULE_PSS_OPERATION, WEEK_SCHEDULE_PSS_OPERATION, MONTH_SCHEDULE_PSS_OPERATION)){
            Map<String, Object> data = getScheduleInfo();

            Object op = data.get("Operation");
            if(op.equals("VIEW")){
                builder.withType(PSSOperationType.VIEW_SCHEDULE);
            } else if(op.equals("WRITE")){
                builder.withType(PSSOperationType.WRITE_SCHEDULE);
            } else {
                throw new RuntimeException(INVALID_INPUT_ERROR_MESSAGE);
            }
            builder.withData(data);
        } else {
            throw new RuntimeException(INVALID_INPUT_ERROR_MESSAGE);
        }

        return builder.create();
   }

    private Map<String, Object> getTaskInfo(){
        Collection<String> types = TaskTypes.getTaskTypeValues();

        String type;
        do {
            ui.displayln("Supported Task Types");
            types.forEach(ui::displayln);

            type = ui.getInput("Enter Type: ");
        } while(!TaskTypes.isSupportedTask(type));

        Map<String, Object> info = new LinkedHashMap<>(getTaskName());

        info.put("Type", type);

        for(String field : TaskTypes.getFields(type)){
            info.put(field, ui.getInput(String.format(PROMPT_USER_INPUT_FORMAT, field)));
        }

        return info;
    }

    private Map<String, Object> getTaskName(){
        return Map.of("Name", ui.getInput("Enter name: "));
    }

    public void displayTasks(Collection<Task> tasks){
        ui.displayln(serializer.serialize(tasks));
    }

    public void displayTask(Task task){
        ui.displayln(serializer.serialize(task));
    }

    public Map<String, String> updateUserTaskInfo(Map<String, String> original){
        Map<String, String> updated = new LinkedHashMap<>(original);

        List<String> keys = new ArrayList<>(updated.keySet());

        String options = optionCollectionToString(keys);

        String input;
        do {
            ui.displayln(options);
            input = ui.getInput("Which field to update? (Enter 'D' or 'Done' to finish):\t");
            try {
                String key = NumberUtils.isParsable(input) ? keys.get(Integer.parseInt(input) - 1) : input;
                if(!updated.containsKey(key)){
                    throw new IndexOutOfBoundsException();
                }

                input = key;
            } catch (IndexOutOfBoundsException | NullPointerException e){
                ui.displayln(INVALID_INPUT_ERROR_MESSAGE);
                ui.displayln("Enter --h for help\n");
                continue;
            }

            Objects.requireNonNull(updated.replace(input, ui.getInput(String.format(PROMPT_USER_INPUT_FORMAT, input))));
        } while(!(StringUtils.equalsAnyIgnoreCase(input, DONE_FLAGS)));


        return updated;
    }

    private Map<String, Object> getFileInfo(){
        return Map.of("File", ui.getInput(FILE_PATH_PROMPT));
    }

    private Map<String, Object> getScheduleInfo(){
        Map<String, Object> options = new LinkedHashMap<>();

        String type;
        do{
            type = ui.getInput("What kind of schedule? ([Daily, Weekly, Monthly])");
            if(type.equalsIgnoreCase("DAILY")){
                options.put("Type", "DAILY");
            } else if(type.equalsIgnoreCase("WEEKLY")){
                options.put("Type", "WEEKLY");
            } else if(type.equalsIgnoreCase("MONTHLY")){
                options.put("Type", "MONTHLY");
            } else {
                ui.displayln(INVALID_INPUT_ERROR_MESSAGE);
            }
        } while(!StringUtils.equalsAnyIgnoreCase(type, "Daily", "Weekly, Monthly"));

        String operation;
        do{
            operation = ui.getInput("Do you want to [View] or [Write] the schedule to a file? ");
            if(operation.equalsIgnoreCase("VIEW")){
                options.put("Operation", "VIEW");
            } else if(operation.equalsIgnoreCase("WRITE")){
                options.put("Operation", "WRITE");
                options.putAll(getFileInfo());
            } else {
                ui.displayln(INVALID_INPUT_ERROR_MESSAGE);
            }
        } while(!StringUtils.equalsAnyIgnoreCase("View", "Write"));

        options.put("Start", ui.getInput("Enter Date: "));

        return options;
    }



    private boolean isCorrect(Map<String, String> m){
        return isCorrect(gson.toJson(m));
    }

    private boolean isCorrect(String s){
        try {
            ui.displayln(s);

            String input = ui.getInput(CORRECTNESS_PROMPT);
            if(input.equalsIgnoreCase("Y") || input.equalsIgnoreCase("YES")){
                return true;
            } else if(input.equalsIgnoreCase("N") || input.equalsIgnoreCase("NO")){
                return false;
            } else {
                ui.displayln(INVALID_INPUT_ERROR_MESSAGE);
                return isCorrect(s);
            }
        } catch (StackOverflowError | OutOfMemoryError e){
            return false;
        }
    }

    private String optionCollectionToString(Collection<String> collection){
        List<String> options = new ArrayList<>(collection);

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < options.size(); ++i){
            sb.append(i + 1).append(".\t").append(options.get(i)).append("\n");
        }

        return sb.toString();
    }

}
