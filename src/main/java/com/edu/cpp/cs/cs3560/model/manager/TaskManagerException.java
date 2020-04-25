package com.edu.cpp.cs.cs3560.model.manager;

public class TaskManagerException extends RuntimeException {

    public TaskManagerException(String e){
        super(e);
    }

    public TaskManagerException(Exception e){
        super(e);
    }

}
