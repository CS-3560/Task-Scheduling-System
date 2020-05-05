// =====================================================================================================================
// TaskFileReaderImpl.java
// =====================================================================================================================
/* About:
 *      This class navigates along the user provided file path to read the JSON file specified.
 *
 *      This class interacts with TaskTypeDeserializer to convert the stream of bits into an object that is usuable with
 *      the projects various methods.
 * */
// =====================================================================================================================
package com.edu.cpp.cs.cs3560.io;

import com.edu.cpp.cs.cs3560.util.TaskDeserializer;
import com.edu.cpp.cs.cs3560.model.tasks.Task;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TaskFileReaderImpl implements TaskFileReader {
    private static final String IO_ERROR_MESSAGE_FORMAT = "There was an issue reading file in path [%s]. " +
                                                            "Please make sure path is correct and the file exists.";

    private final TaskDeserializer deserializer;

    public TaskFileReaderImpl(final TaskDeserializer deserializer) {
        this.deserializer = deserializer;
    }

    @Override
    public List<Task> read(final String file) throws IOException {
        try {
            return deserializer.deserialize(new String(Files.readAllBytes(Paths.get(file))));
        } catch (IOException e){
            throw new IOException(String.format(IO_ERROR_MESSAGE_FORMAT, file), e);
        }
    }

    @Override
    public List<Task> read(final File file) throws IOException {
        try {
            return deserializer.deserialize(FileUtils.readFileToString(file, StandardCharsets.UTF_8));
        } catch (IOException e){
            throw new IOException(String.format(IO_ERROR_MESSAGE_FORMAT, file.getName()), e);
        }
    }

}
