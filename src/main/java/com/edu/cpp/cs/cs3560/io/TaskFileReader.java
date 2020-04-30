// =====================================================================================================================
// TaskFileReader.java
// =====================================================================================================================
/* About:
 *      Interface for TaskFileReaderImpl.java.
 * */
// =====================================================================================================================
package com.edu.cpp.cs.cs3560.io;

import com.edu.cpp.cs.cs3560.model.tasks.Task;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface TaskFileReader {

    List<Task> read(String file) throws IOException;

    List<Task> read(File file) throws IOException;

}
