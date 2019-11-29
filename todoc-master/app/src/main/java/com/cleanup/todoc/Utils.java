package com.cleanup.todoc;

import android.support.annotation.NonNull;

import com.cleanup.todoc.model.Task;

import java.util.Collections;
import java.util.List;

public abstract class Utils
{
    /**
     * List of all possible sort methods for task
     */
    public enum SortMethod {
        /**
         * Sort alphabetical by name
         */
        ALPHABETICAL,
        /**
         * Inverted sort alphabetical by name
         */
        ALPHABETICAL_INVERTED,
        /**
         * Lastly created first
         */
        RECENT_FIRST,
        /**
         * First created first
         */
        OLD_FIRST,
        /**
         * No sort
         */
        NONE
    }

    /**
     * Sorts the {@link List <  Task  >} in argument and returns it
     * @param tasks a {@link List<Task>}
     * @param sortMethod a {@link SortMethod}
     * @return the {@link List<Task>} but sorted
     */
    @NonNull
    public static List<Task> sortTasks(@NonNull final List<Task> tasks,@NonNull SortMethod sortMethod) {
        if (sortMethod == null) sortMethod = SortMethod.NONE;
        if (tasks.size()!= 0) {
            switch (sortMethod) {
                case ALPHABETICAL:
                    Collections.sort(tasks, new Task.TaskAZComparator());
                    break;
                case ALPHABETICAL_INVERTED:
                    Collections.sort(tasks, new Task.TaskZAComparator());
                    break;
                case RECENT_FIRST:
                    Collections.sort(tasks, new Task.TaskRecentComparator());
                    break;
                case OLD_FIRST:
                    Collections.sort(tasks, new Task.TaskOldComparator());
                    break;
            }
        }
        return tasks;
    }


}
