package com.edu.cpp.cs.cs3560.ui;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class TextUserInterface implements UserInterface {
    private static final Scanner input = new Scanner(System.in);



    public void display(String s) {
        System.out.print(s);
    }


    public void displayln(String s) {
        System.out.println(s);
    }


    public String getInput() {
        return StringUtils.defaultString(input.nextLine()).trim();
    }


    public String getInput(String s) {
        display(s);
        return getInput();
    }


    public Map<String, String> getInput(String... s) {
        Map<String, String> inputs = new LinkedHashMap<>();
        for(String k : s){
            inputs.put(k, getInput(k));
        }

        return Collections.unmodifiableMap(inputs);
    }

    public Map<String, String> getInput(String p, String... s) {
        Map<String, String> inputs = new LinkedHashMap<>();
        for(String k : s){
            inputs.put(k, getInput(p + k));
        }

        return Collections.unmodifiableMap(inputs);
    }

    @Override
    public void displayError(String s){
        System.err.println(s);
    }

}
