package com.cleanup.todoc.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.cleanup.todoc.model.Task;

import java.util.List;

@Dao
public interface TaskDao
{
    @Query("SELECT * FROM Task WHERE id = :id")
    LiveData<Task> getTask (long id);

    @Query("SELECT * FROM Task")
    LiveData<List<Task>> getAllTasks ();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertTask(Task task);

    @Delete
    int deleteTask(Task task);

}
