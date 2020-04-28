package com.edu.cpp.cs.cs3560.ui;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class TextUserInterface implements UserInterface {
    private static final Scanner input = new Scanner(System.in);

    public TextUserInterface(){}

    @Override
    public void display(final String s) {
        print(s);
    }

    @Override
    public void displayln(final String s) {
        println(s);
    }

    @Override
    public String getInput() {
        return StringUtils.defaultString(input.nextLine()).trim();
    }

    @Override
    public String getInput(final String s) {
        display(s);
        return getInput();
    }

    @Override
    public Map<String, String> getInput(final String... s) {
        final Map<String, String> inputs = new LinkedHashMap<>();
        for(final String k : s){
            inputs.put(k, getInput(k));
        }

        return Collections.unmodifiableMap(inputs);
    }

    @Override
    public void displayError(final String s){
        System.err.println(s);
    }

    public void print(final String s){
        System.out.print(s);
    }

    public void println(final String s){
        System.out.println(s);
    }

}
