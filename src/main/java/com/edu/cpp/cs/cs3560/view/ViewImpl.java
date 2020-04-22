package com.edu.cpp.cs.cs3560.view;

import com.edu.cpp.cs.cs3560.ui.UserInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ViewImpl {
    private static final String INVALID_INPUT_ERROR_MESSAGE = "Invalid Input. Please Try Again.";
    private static final String CORRECTNESS_PROMPT = "Is this correct? [Y,n]";
    private static final String PROMPT_USER_INPUT_FORMAT = "Enter %s:\t";
    private static final String INPUT_NON_APPLICABLE_MESSAGE = "If not applicable, enter N/A";

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private final UserInterface ui;


    public ViewImpl(UserInterface ui) {
        this.ui = ui;
    }

    public String displayOption(String tile, String prompt, String ...options){
        return displayOptions(tile, prompt, Arrays.asList(options));
    }

    public String displayOptions(Collection<String> options){
        return displayOptions(null, options);
    }

    public String displayOptions(String prompt, Collection<String> options){
        return displayOptions(null, prompt, options);
    }

    public String displayOptions(String title, String prompt, Collection<String> options){
        StringBuilder osb = new StringBuilder();
        int i = 0;
        for(String option : options){
            osb.append(++i).append("\t").append(option).append("\n");
        }
        if(Objects.nonNull(title) && !title.isEmpty()) {
            ui.displayln(title);
        }
        ui.display(osb.toString());

        return Objects.nonNull(prompt) ? ui.getInput(prompt) : ui.getInput();
    }

    public Map<String, String> getUserTaskInfo(Collection<String> fields){
        ui.displayln(INPUT_NON_APPLICABLE_MESSAGE);

        Map<String, String> info = new LinkedHashMap<>();
        for(String field : fields){
            info.put(field, ui.getInput(String.format(PROMPT_USER_INPUT_FORMAT, field)));
        }

        while(!isCorrect(info)){
            info = updateUserTaskInfo(info);
        }

        return info;
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
        } while(!(StringUtils.equalsAnyIgnoreCase(input, "DONE", "D")));


        return updated;
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
