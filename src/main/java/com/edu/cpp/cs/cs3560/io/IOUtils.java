package com.edu.cpp.cs.cs3560.io;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class IOUtils {

    private IOUtils(){
        throw new UnsupportedOperationException();
    }

    public static String readFileToString(String file) throws IOException {
        return new String(Files.readAllBytes(Paths.get(file)));
    }

    public static String readFileToString(File file) throws IOException {
        return FileUtils.readFileToString(file, "UTF-8");
    }

    public static JsonElement parseFileToJson(String file) throws IOException {
        return parseJson(readFileToString(file));
    }

    public static JsonElement parseFileToJson(File file) throws IOException {
        return parseJson(readFileToString(file));
    }

    public static JsonElement parseJson(String json){
        return JsonParser.parseString(json);
    }



}
