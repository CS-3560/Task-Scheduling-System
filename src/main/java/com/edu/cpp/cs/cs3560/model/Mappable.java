package com.edu.cpp.cs.cs3560.model;

import java.util.Map;

@FunctionalInterface
public interface Mappable {

    Map<String, ?> toMap();

}
