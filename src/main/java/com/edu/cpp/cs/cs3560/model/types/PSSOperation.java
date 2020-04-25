package com.edu.cpp.cs.cs3560.model.types;

import com.google.common.collect.ImmutableMap;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class PSSOperation {
    private final PSSOperationType type;
    private final Map<String, Object> data;

    private PSSOperation(PSSOperationType type, Map<String, Object> data) {
        this.type = Objects.requireNonNull(type);
        this.data = Objects.requireNonNull(data);
    }

    public PSSOperationType getType() {
        return type;
    }

    public Map<String, Object> getData() {
        return ImmutableMap.copyOf(data);
    }

    public static Builder builder(){
        return new Builder();
    }

    public static final class Builder {
        private final Map<String, Object> data = new LinkedHashMap<>();

        private PSSOperationType type;

        private Builder(){}

        public PSSOperationType getType(){ return type; }

        public void setType(PSSOperationType type){
            this.type = type;
        }

        public Builder withType(PSSOperationType type){
            setType(type);
            return this;
        }

        public Map<String, Object> getData(){
            return ImmutableMap.copyOf(data);
        }

        public void addData(Map<String, Object> data){
            this.data.putAll(data);
        }

        public Builder withData(Map<String, Object> data){
            addData(data);
            return this;
        }

        public void addData(String key, Object value){
            data.put(key, value);
        }

        public Builder withData(String key, Object value){
            addData(key, value);
            return this;
        }

        public PSSOperation create(){
            return new PSSOperation(type, data);
        }

    }





    public enum PSSOperationType {

        CREATE_TASK,
        VIEW_TASK,
        DELETE_TASK,
        EDIT_TASK,
        WRITE_TO_FILE,
        READ_FROM_FILE,
        VIEW_SCHEDULE,
        WRITE_SCHEDULE,
        EXECUTE_INSTRUCTIONS,
        QUIT

    }

}
