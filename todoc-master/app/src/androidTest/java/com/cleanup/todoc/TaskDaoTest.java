package com.cleanup.todoc;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TaskDaoTest
{
    private TodocDatabase database;

    // DATA FOR TESTS

    private static Project PROJECT = new Project(1, "Tartampion", 0xFFEADAD1);
    private static long PROJECT_ID = PROJECT.getId();

    private static Task TASK_FIRST = new Task(1,PROJECT_ID, "test1", 0);
    private static Task TASK_SECOND = new Task(2,PROJECT_ID, "test2", 0);


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception
    {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), TodocDatabase.class)
                .allowMainThreadQueries().build();
    }

    @After
    public void closeDb() throws Exception
    {
        this.database.close();
    }

    @Test
    public void getTasksWhenNoTaskInserted() throws InterruptedException
    {
        List<Task> tasks = TestUtils.getValue(this.database.taskDao().getAllTasks());
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void insertOneProjectAndGetIt() throws InterruptedException
    {
        this.database.projectDao().insertProject(PROJECT);
        Project project = TestUtils.getValue(this.database.projectDao().getProject(PROJECT_ID));
        assertEquals(project.getId(), PROJECT_ID);
    }

    @Test
    public void insertOneTaskAndGetTasks() throws InterruptedException
    {
        this.database.projectDao().insertProject(PROJECT);
        this.database.taskDao().insertTask(TASK_FIRST);
        List<Task> tasks = TestUtils.getValue(this.database.taskDao().getAllTasks());
        assertEquals(tasks.size(), 1);
    }

    @Test
    public void insertOneTaskAndGetIt() throws InterruptedException
    {
        this.database.projectDao().insertProject(PROJECT);
        this.database.taskDao().insertTask(TASK_FIRST);
        this.database.taskDao().insertTask(TASK_SECOND);
        List<Task> tasks = TestUtils.getValue(this.database.taskDao().getAllTasks());
        assertEquals(tasks.size(), 2);
        Task task1 = TestUtils.getValue(this.database.taskDao().getTask(TASK_FIRST.getId()));
        assertEquals(1, task1.getId());
    }

    @Test
    public void insertTasksAndDeleteTheSecond() throws InterruptedException
    {
        this.database.projectDao().insertProject(PROJECT);
        this.database.taskDao().insertTask(TASK_FIRST);
        this.database.taskDao().insertTask(TASK_SECOND);
        List<Task> tasks = TestUtils.getValue(this.database.taskDao().getAllTasks());
        assertEquals(tasks.size(), 2);
        this.database.taskDao().deleteTask(TASK_SECOND);
        tasks = TestUtils.getValue(this.database.taskDao().getAllTasks());
        assertEquals(tasks.size(), 1);
    }


}
