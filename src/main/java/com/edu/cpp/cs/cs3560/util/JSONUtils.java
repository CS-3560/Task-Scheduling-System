// =====================================================================================================================
// JSONUtils.java
// =====================================================================================================================
/* About:
 *      A collection of JSON utilities.
 *
 *      Unimplemented
 * */
// =====================================================================================================================
package com.edu.cpp.cs.cs3560.util;

import java.util.Map;
import java.util.stream.Collectors;

public final class JSONUtils {

    private JSONUtils(){}

    public static String toJSON(Map<String, ?> map){
        return toString(map, "\":\"", "\",\"", "{\"", "\"}");
    }

    public static String toPrettyJSON(Map<String, ?> map){
        return toString(map, "\" : \"", "\",\n\"", "{\n\"", "\"\n}");
    }

    public static String toString(Map<String, ?> map){
        return toString(map, "=", ", ", "{", "}");
    }

    public static String toPrettyString(Map<String, ?> map){
        return toString(map, " = ", ",\n", "{\n", "\n}");
    }

    private static String toString(Map<String, ?> map, String separator, String delimiter, String prefix, String suffix){
        return map.keySet().stream()
                .map(key -> key + separator + map.get(key))
                .collect(Collectors.joining(delimiter, prefix, suffix));
    }

}
