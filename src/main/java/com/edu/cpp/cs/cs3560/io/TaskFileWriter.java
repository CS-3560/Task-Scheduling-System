// =====================================================================================================================
// TaskFileWriter.java
// =====================================================================================================================
/* About:
 *      Interface TaskFileWriterImpl.java.
 * */
// =====================================================================================================================
package com.edu.cpp.cs.cs3560.io;

import com.edu.cpp.cs.cs3560.model.tasks.Task;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public interface TaskFileWriter {

    void write(String file, Task task) throws IOException;

    void write(String file, Collection<Task> tasks) throws IOException;

    void write(File file, Task task) throws IOException;

    void write(File file, Collection<Task> tasks) throws IOException;

}
