package com.edu.cpp.cs.cs3560.model.types;

import com.google.common.collect.ImmutableMap;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class PSSOperation {
    private final PSSOperationType type;
    private final Map<String, Object> data;

    private PSSOperation(final PSSOperationType type, final Map<String, Object> data) {
        this.type = Objects.requireNonNull(type);
        this.data = Objects.requireNonNull(data);
    }

    public final PSSOperationType getType() {
        return type;
    }

    public final Map<String, Object> getData() {
        return ImmutableMap.copyOf(data);
    }

    public static Builder builder(){
        return new Builder();
    }

    public static final class Builder {
        private final Map<String, Object> data = new LinkedHashMap<>();

        private PSSOperationType type;


        private Builder(){}

        public final PSSOperationType getType(){ return type; }

        public final void setType(final PSSOperationType type){
            this.type = type;
        }

        public final Builder withType(final PSSOperationType type){
            setType(type);
            return this;
        }

        public final Map<String, Object> getData(){
            return ImmutableMap.copyOf(data);
        }

        public final void addData(final Map<String, Object> data){
            this.data.putAll(data);
        }

        public final Builder withData(final Map<String, Object> data){
            addData(data);
            return this;
        }

        public final void addData(final String key, final Object value){
            data.put(key, value);
        }

        public final Builder withData(final String key, final Object value){
            addData(key, value);
            return this;
        }

        public final PSSOperation create(){
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
        QUIT
    }

}
