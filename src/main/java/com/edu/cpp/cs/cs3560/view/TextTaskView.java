package com.edu.cpp.cs.cs3560.view;

import com.edu.cpp.cs.cs3560.util.TaskSerializer;
import com.edu.cpp.cs.cs3560.model.tasks.Task;
import com.edu.cpp.cs.cs3560.model.types.PSSOperation;
import com.edu.cpp.cs.cs3560.model.types.PSSOperation.PSSOperationType;
import com.edu.cpp.cs.cs3560.model.types.TaskTypes;
import com.edu.cpp.cs.cs3560.ui.UserInterface;
import com.google.common.collect.ObjectArrays;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class TextTaskView implements TaskView {

    private static final String INVALID_INPUT_ERROR_MESSAGE = "\nInvalid Input. Please Try Again.";

    private static final String CORRECTNESS_PROMPT = "Is this correct? [Y,n]";

    private static final String PROMPT_USER_INPUT_FORMAT = "Enter %s:\t";

    private static final String FILE_PATH_PROMPT = "Enter path of file: ";

    private static final String READ_OPTION = "Read";

    private static final String WRITE_OPTION = "Write";

    private static final String INPUT_NON_APPLICABLE_MESSAGE = "If not applicable, enter N/A";

    private static final String CREATE_TASK_PSS_OPERATION = "Create a task";

    private static final String VIEW_TASK_PSS_OPERATION = "View a task";

    private static final String DELETE_TASK_PSS_OPERATION = "Delete a task";

    private static final String EDIT_TASK_PSS_OPERATION = "Edit a task";

    private static final String WRITE_TO_FILE_PSS_OPERATION = "Write the Schedule to a file";

    private static final String READ_FROM_FILE_PSS_OPERATION = "Read the schedule from a file";

    private static final String WHOLE_SCHEDULE_PSS_OPERATION = "View the schedule";

    private static final String DAY_SCHEDULE_PSS_OPERATION = "View or write the schedule for one day";

    private static final String WEEK_SCHEDULE_PSS_OPERATION = "View or write the schedule for one week";

    private static final String MONTH_SCHEDULE_PSS_OPERATION = "View or write the schedule for one month";

    private static final String[] EXIT_FLAGS = {"QUIT", "Q"};

    private static final String[] DONE_FLAGS = {"DONE", "D"};

    private static final String[] YES_FLAGS = {"YES", "Y"};

    private static final String[] NO_FLAGS = {"NO", "N"};

    private static final String CHANGE_FIELD_OPTION = "Change a field";

    private static final String DISPLAY_TASK_INFO_OPTION = "Display task info";

    private static final String UPDATE_OPTIONS_MENU = "\n1. " + CHANGE_FIELD_OPTION +
                                                        "\n2. " + DISPLAY_TASK_INFO_OPTION +
                                                        "\n3. Done." +
                                                        "\nEnter option to perform: ";

    private static final Map<Integer, String> OPERATIONS = Map.ofEntries(
            Map.entry(1, CREATE_TASK_PSS_OPERATION),
            Map.entry(2, VIEW_TASK_PSS_OPERATION),
            Map.entry(3, DELETE_TASK_PSS_OPERATION),
            Map.entry(4, EDIT_TASK_PSS_OPERATION),
            Map.entry(5, WRITE_TO_FILE_PSS_OPERATION),
            Map.entry(6, READ_FROM_FILE_PSS_OPERATION),
            Map.entry(7, WHOLE_SCHEDULE_PSS_OPERATION),
            Map.entry(8, DAY_SCHEDULE_PSS_OPERATION),
            Map.entry(9, WEEK_SCHEDULE_PSS_OPERATION),
            Map.entry(10, MONTH_SCHEDULE_PSS_OPERATION)
    );

    private static final String PSS_OPERATION_OPTIONS_MENU = generateMenu();

    private static final String SUPPORTED_TASK_TYPES_HEADING = "\nSupported Task Types:";

    private static final String TASK_EDITOR_HEADER = "This is the Task Editor (Enter 'Done' when finished).\n";

    private static final String ORIGINAL_TASK_HEADER = "Here is the Original Task:";

    private static final String FIELD_NAME_ERROR_MESSAGE = "Field entered does not exist. Please make sure field name entered is correct.";

    private static final String CURRENT_TASK_INFO_HEADER = "Current Task Info:";

    private static final String UPDATED_TASK_INFO_HEADER = "Updated Task Info:";

    private static final String UPDATED_VALUE_PROMPT = "updated value";

    private static final String DID_YOU_MEAN_PROMPT_FORMAT = "Did you mean [%s]? [Y/n] ";

    private static final String WHAT_FIELD_PROMPT = "What field would you like to change? ";

    private static final String SCHEDULE_OPERATION_PROMPT = "Do you want to [View] or [Write] the schedule to a file? ";

    private static final String SCHEDULE_START_DATE_PROMPT = "Enter Start Date [yyyyMMdd]: ";

    private static final String OPERATION_OPTIONS_HEADER = "\nOperation options:";

    private static final String MENU_OPTIONS_PROMPT = "\n\nEnter operation to perform: ";


    private static final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .setPrettyPrinting()
            .create();

    private final UserInterface ui;
    private final TaskSerializer serializer;

    public TextTaskView(final UserInterface ui, final TaskSerializer serializer) {
        this.ui = ui;
        this.serializer = serializer;
    }

    @Override
    public String getInput(final String prompt){
        return ui.getInput(prompt);
    }

    public Map<String, String> getInputs(final String ...s){
        return ui.getInput(s);
    }

    @Override
    public PSSOperation getOperation(){
       final Collection<String> options = getValidOperations();

        String operation;
        do {
            String input = ui.getInput(PSS_OPERATION_OPTIONS_MENU);

            operation = parseOperationInput(input);

            if(StringUtils.equalsAnyIgnoreCase(operation, EXIT_FLAGS)){
                return PSSOperation.builder().withType(PSSOperationType.QUIT).create();
            }

            if(!options.contains(operation)){
                ui.displayln(INVALID_INPUT_ERROR_MESSAGE);
            }
        } while(!options.contains(operation));

        final PSSOperation.Builder builder = PSSOperation.builder();
        if(StringUtils.equalsAnyIgnoreCase(operation, CREATE_TASK_PSS_OPERATION, VIEW_TASK_PSS_OPERATION, DELETE_TASK_PSS_OPERATION, EDIT_TASK_PSS_OPERATION)){
            if(operation.equalsIgnoreCase(CREATE_TASK_PSS_OPERATION)){
                builder.withType(PSSOperationType.CREATE_TASK);
            } else if(operation.equalsIgnoreCase(VIEW_TASK_PSS_OPERATION)){
                builder.withType(PSSOperationType.VIEW_TASK);
            } else if(operation.equalsIgnoreCase(DELETE_TASK_PSS_OPERATION)){
                builder.withType(PSSOperationType.DELETE_TASK);
            } else if(operation.equalsIgnoreCase(EDIT_TASK_PSS_OPERATION)){
                builder.withType(PSSOperationType.EDIT_TASK);
            } else {
                throw new RuntimeException(INVALID_INPUT_ERROR_MESSAGE);
            }

            builder.withData(builder.getType() == PSSOperationType.CREATE_TASK ? getTaskInfo() : getTaskName());
        } else if(StringUtils.equalsAnyIgnoreCase(operation, READ_FROM_FILE_PSS_OPERATION, WRITE_TO_FILE_PSS_OPERATION)){
            builder.withType(
                    operation.equalsIgnoreCase(READ_FROM_FILE_PSS_OPERATION)
                            ? PSSOperationType.READ_FROM_FILE
                            : PSSOperationType.WRITE_TO_FILE
            ).withData(getFileInfo());
        } else if(StringUtils.equalsIgnoreCase(operation, WHOLE_SCHEDULE_PSS_OPERATION)){
            builder.withType(PSSOperationType.VIEW_SCHEDULE).withData(OPERATION_KEY, VIEW_OPERATION_VALUE).withData(TYPE_KEY, WHOLE_SCHEDULE_OPERATION_VALUE);
        } else if(StringUtils.equalsAnyIgnoreCase(operation, DAY_SCHEDULE_PSS_OPERATION, WEEK_SCHEDULE_PSS_OPERATION, MONTH_SCHEDULE_PSS_OPERATION)){
            final Map<String, Object> data = getScheduleInfo(operation);

            final Object op = data.get(OPERATION_KEY);
            if(op.equals(VIEW_OPERATION_VALUE)){
                builder.withType(PSSOperationType.VIEW_SCHEDULE);
            } else if(op.equals(WRITE_OPERATION_VALUE)){
                builder.withType(PSSOperationType.WRITE_SCHEDULE);
            } else {
                throw new RuntimeException(INVALID_INPUT_ERROR_MESSAGE);
            }
            builder.withData(data);
        } else if(StringUtils.equalsAnyIgnoreCase(operation, EXIT_FLAGS)){
            builder.withType(PSSOperationType.QUIT);
        } else {
            throw new RuntimeException(INVALID_INPUT_ERROR_MESSAGE);
        }

        return builder.create();
   }

    private Map<String, Object> getTaskInfo(){
        final Collection<String> types = TaskTypes.getTaskTypeValues();

        String type;
        do {
            ui.displayln(SUPPORTED_TASK_TYPES_HEADING);
            types.stream().map(t -> String.format("\t%s", t)).forEach(ui::displayln);

            type = ui.getInput(String.format(PROMPT_USER_INPUT_FORMAT, TYPE_KEY));
            if(!TaskTypes.isSupportedTask(type)){
                displayMessage(INVALID_INPUT_ERROR_MESSAGE);
            }
        } while(!TaskTypes.isSupportedTask(type));

        final Map<String, Object> info = new LinkedHashMap<>(getTaskName());

        info.put(TYPE_KEY, type);

        for(final String field : TaskTypes.getFields(type)){
            info.put(field, ui.getInput(String.format(PROMPT_USER_INPUT_FORMAT, field)));
        }

        return info;
    }

    private Map<String, Object> getTaskName(){
        return Map.of(NAME_KEY, ui.getInput(String.format(PROMPT_USER_INPUT_FORMAT, NAME_KEY)));
    }

    @Override
    public Map<String, Object> getUpdatedInfo(final Map<String, Object> original){
        final Map<String, Object> updated = new LinkedHashMap<>(original);

        ui.displayln(TASK_EDITOR_HEADER);
        ui.displayln(ORIGINAL_TASK_HEADER);
        ui.displayln(gson.toJson(original));

        String input;
        do {
            input = getInput(UPDATE_OPTIONS_MENU);
            if(StringUtils.equalsAnyIgnoreCase(input, "1", CHANGE_FIELD_OPTION)){
                try {
                    final String field = getFieldToUpdate(updated);
                    if(StringUtils.isNotEmpty(field)){
                        final String value = getInput(String.format(PROMPT_USER_INPUT_FORMAT, UPDATED_VALUE_PROMPT));
                        updated.put(field, value);
                    }
                } catch (IllegalArgumentException e){
                    ui.displayError(e.getMessage());
                    ui.displayError(FIELD_NAME_ERROR_MESSAGE);
                }
            } else if(StringUtils.equalsAnyIgnoreCase(input, "2", DISPLAY_TASK_INFO_OPTION)){
                ui.displayln(CURRENT_TASK_INFO_HEADER);
                ui.displayln(gson.toJson(updated));
            } else if(StringUtils.equalsAnyIgnoreCase(input, ObjectArrays.concat("3", DONE_FLAGS))){
                ui.displayln(UPDATED_TASK_INFO_HEADER);
                ui.displayln(gson.toJson(updated));
            } else {
                ui.displayError(INVALID_INPUT_ERROR_MESSAGE);
            }
        } while(!StringUtils.equalsAnyIgnoreCase(input, ObjectArrays.concat("3", DONE_FLAGS)));

        return updated;
    }

    private String getFieldToUpdate(final Map<String, Object> data){
        final String field = getInput(WHAT_FIELD_PROMPT);
        if(data.containsKey(field)){
            return field;
        } else if (StringUtils.equalsAnyIgnoreCase(field, data.keySet().toArray(new String[0]))){
            final Optional<String> possible = data.keySet().stream().filter(k -> k.equalsIgnoreCase(field)).findFirst();
            if(possible.isPresent()){
                final String key = possible.get();

                String input;
                do {
                    input = getInput(String.format(DID_YOU_MEAN_PROMPT_FORMAT, key));

                    if(StringUtils.equalsAnyIgnoreCase(input, YES_FLAGS)){
                        return key;
                    } else if(StringUtils.equalsAnyIgnoreCase(input, NO_FLAGS)){
                        return StringUtils.EMPTY;
                    } else {
                        ui.displayError(INVALID_INPUT_ERROR_MESSAGE);
                    }
                } while(!StringUtils.equalsAnyIgnoreCase(input, ObjectArrays.concat(YES_FLAGS, NO_FLAGS, String.class)));
            }
        }

        throw new IllegalArgumentException(INVALID_INPUT_ERROR_MESSAGE);
    }

    @Override
    public void displayTasks(final Collection<Task> tasks){
        ui.displayln(serializer.serialize(tasks));
    }

    @Override
    public void displayTask(final Task task){
        ui.displayln(task.toString());
    }

    private Map<String, Object> getFileInfo(){
        return Map.of(FILE_KEY, ui.getInput(FILE_PATH_PROMPT));
    }

    private Map<String, Object> getScheduleInfo(final String input){
        final Map<String, Object> options = new LinkedHashMap<>();

        String operation;
        do{
            operation = ui.getInput(SCHEDULE_OPERATION_PROMPT);
            if(operation.equalsIgnoreCase(VIEW_OPERATION_VALUE)){
                options.put(OPERATION_KEY, VIEW_OPERATION_VALUE);
            } else if(operation.equalsIgnoreCase(WRITE_OPERATION_VALUE)){
                options.put(OPERATION_KEY, WRITE_OPERATION_VALUE);
                options.putAll(getFileInfo());
            } else {
                ui.displayln(INVALID_INPUT_ERROR_MESSAGE);
            }
        } while(!StringUtils.equalsAnyIgnoreCase(operation, VIEW_OPERATION_VALUE, WRITE_OPERATION_VALUE));

        options.put(SCHEDULE_START_DATE_KEY, ui.getInput(SCHEDULE_START_DATE_PROMPT));

        String type;
        if(input.equalsIgnoreCase(DAY_SCHEDULE_PSS_OPERATION)){
            type = DAY_SCHEDULE_OPERATION_VALUE;
        } else if(input.equalsIgnoreCase(WEEK_SCHEDULE_PSS_OPERATION)){
            type = WEEK_SCHEDULE_OPERATION_VALUE;
        } else if(input.equalsIgnoreCase(MONTH_SCHEDULE_PSS_OPERATION)){
            type = MONTH_SCHEDULE_OPERATION_VALUE;
        } else {
            throw new RuntimeException(INVALID_INPUT_ERROR_MESSAGE);
        }

        options.put(TYPE_KEY, type);

        return options;
    }

    private String parseOperationInput(final String input){
        return (NumberUtils.isDigits(input) ? OPERATIONS.get(Integer.parseInt(input)) : input).toUpperCase();
    }

    private Collection<String> getValidOperations(){
        return OPERATIONS.values().stream().map(String::toUpperCase).collect(Collectors.toSet());
    }

    @Override
    public void displayMessage(final String message){
        ui.displayln(message);
    }

    @Override
    public void displayError(final String message){
        ui.displayError(message);
    }

    private static String generateMenu(){
        final StringBuilder sb = new StringBuilder(OPERATION_OPTIONS_HEADER);

        final SortedSet<Integer> keys = new TreeSet<>(OPERATIONS.keySet());
        for(final int key : keys){
            sb.append(String.format("\n\t%s. %s", StringUtils.leftPad(String.valueOf(key), 2), OPERATIONS.get(key)));
        }

        return sb.append(MENU_OPTIONS_PROMPT).toString();
    }

}
