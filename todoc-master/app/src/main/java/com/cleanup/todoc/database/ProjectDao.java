package com.cleanup.todoc.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.cleanup.todoc.model.Project;

import java.util.List;

@Dao
public interface ProjectDao
{

    @Query("SELECT * FROM Project WHERE id = :id")
    LiveData<Project> getProject(long id);

    @Query("SELECT * FROM Project")
    LiveData<List<Project>> getAllProjects();

    @Insert
    long insertProject(Project project);

}
