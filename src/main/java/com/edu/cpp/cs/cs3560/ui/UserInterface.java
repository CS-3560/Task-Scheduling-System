package com.edu.cpp.cs.cs3560.ui;

import java.util.Map;

public interface UserInterface {

    void display(String s);

    void displayln(String s);

    String getInput();

    String getInput(String s);

    Map<String, String> getInput(String ...s);

    void displayError(String s);

}
