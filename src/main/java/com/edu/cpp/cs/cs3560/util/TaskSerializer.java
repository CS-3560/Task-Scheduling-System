// =====================================================================================================================
// TaskSerializer.java
// =====================================================================================================================
/* About:
 *      Interface for TaskTypeSerializer.java.
 * */
// =====================================================================================================================
package com.edu.cpp.cs.cs3560.util;

import com.edu.cpp.cs.cs3560.model.tasks.Task;

import java.util.Collection;

public interface TaskSerializer {

    String serialize(Task task);

    String serialize(Collection<Task> tasks);

}
