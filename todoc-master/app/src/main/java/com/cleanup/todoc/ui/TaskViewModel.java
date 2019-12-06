package com.cleanup.todoc.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.cleanup.todoc.Utils;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class TaskViewModel extends ViewModel
{
    private final ProjectDataRepository projectData;
    private final TaskDataRepository taskData;
    private final Executor executor;

    public TaskViewModel(ProjectDataRepository projectData, TaskDataRepository taskData, Executor executor) {
        this.projectData = projectData;
        this.taskData = taskData;
        this.executor = executor;
    }

    ///////////// PROJECT ////////////
    public LiveData<List<Project>> getAllProjects()
    {
        return this.projectData.getAllProjects();
    }

    public void insertProject(Project project) {
        executor.execute(() -> {
            this.projectData.insertProject(project);
        });
    }

    ///////////// TASK ////////////
    public LiveData<List<Task>> getAllTasks()
    {
        return this.taskData.getAllTasks();
    }

    public void insertTask(Task task) {
        executor.execute(() -> {
            this.taskData.insertTask(task);
        });
    }

    public void deleteTask(Task task) {
        executor.execute(() -> {
            this.taskData.deleteTask(task);
        });
    }

}
