package com.cleanup.todoc.injections;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;
import com.cleanup.todoc.ui.TaskViewModel;

import java.util.concurrent.Executor;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final ProjectDataRepository projectData;
    private final TaskDataRepository taskData;
    private final Executor executor;

    public ViewModelFactory(ProjectDataRepository projectData,
                            TaskDataRepository taskData, Executor executor) {
        this.projectData = projectData;
        this.taskData = taskData;
        this.executor = executor;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(TaskViewModel.class))
        {
            return (T) new TaskViewModel
                    (this.projectData, this.taskData, this.executor);
        }
        throw new IllegalArgumentException("Unkown ViewModel class");
    }
}
