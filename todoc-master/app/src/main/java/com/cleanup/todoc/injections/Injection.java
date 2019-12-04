package com.cleanup.todoc.injections;

import android.content.Context;

import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection
{
    public static ProjectDataRepository provideProjectDataSource(Context c)
    {
        TodocDatabase database = TodocDatabase.getInstance(c);
        return new ProjectDataRepository(database.projectDao());
    }

    public static TaskDataRepository provideTaskDataSource(Context c)
    {
        TodocDatabase database = TodocDatabase.getInstance(c);
        return new TaskDataRepository(database.taskDao());
    }

    public static Executor provideExecutor()
    {
        return Executors.newSingleThreadExecutor();
    }

    public static ViewModelFactory provideViewModelFactory (Context c)
    {
        ProjectDataRepository projectData = provideProjectDataSource(c);
        TaskDataRepository taskData = provideTaskDataSource(c);
        Executor executor = provideExecutor();

        return new ViewModelFactory(projectData, taskData, executor);
    }
}
