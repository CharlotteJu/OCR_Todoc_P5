package com.cleanup.todoc.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cleanup.todoc.R;
import com.cleanup.todoc.Utils;
import com.cleanup.todoc.injections.Injection;
import com.cleanup.todoc.injections.ViewModelFactory;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * <p>Home activity of the application which is displayed when the user opens the app.</p>
 * <p>Displays the list of tasks.</p>
 *
 * @author Gaëtan HERFRAY
 */
public class MainActivity extends AppCompatActivity implements TasksAdapter.DeleteTaskListener {

    /**
     * List of all projects available in the application
     */
    private /*final*/ Project[] allProjects  = Project.getAllProjects();
    //private List<Project> allProjects = new ArrayList<>();

    /**
     * List of all current tasks of the application
     */
    @NonNull
    private /*final*/ ArrayList<Task> tasks = new ArrayList<>();

    /**
     * The adapter which handles the list of tasks
     */
    private /*final*/ TasksAdapter adapter; // = new TasksAdapter(tasks, this);

    /**
     * The sort method to be used to display tasks
     */
    @NonNull
    private Utils.SortMethod sortMethod = Utils.SortMethod.NONE;

    /**
     * Dialog to create a new task
     */
    @Nullable
    public AlertDialog dialog = null;

    /**
     * EditText that allows user to set the name of a task
     */
    @Nullable
    private EditText dialogEditText = null;

    /**
     * Spinner that allows the user to associate a project to a task
     */
    @Nullable
    private Spinner dialogSpinner = null;

    /**
     * The RecyclerView which displays the list of tasks
     */
    // Suppress warning is safe because variable is initialized in onCreate
    @SuppressWarnings("NullableProblems")
    @NonNull
    private RecyclerView listTasks;

    /**
     * The TextView displaying the empty state
     */
    // Suppress warning is safe because variable is initialized in onCreate
    @SuppressWarnings("NullableProblems")
    @NonNull
    private TextView lblNoTasks;

    private TaskViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        listTasks = findViewById(R.id.list_tasks);
        lblNoTasks = findViewById(R.id.lbl_no_task);


        findViewById(R.id.fab_add_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTaskDialog();
            }
        });

        this.configViewModel();
       // this.configProjects();
        this.getAllTasks();

        this.configRecyclerView();
    }

    ///////////// MENU /////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.filter_alphabetical) {
            sortMethod = Utils.SortMethod.ALPHABETICAL;
        } else if (id == R.id.filter_alphabetical_inverted) {
            sortMethod = Utils.SortMethod.ALPHABETICAL_INVERTED;
        } else if (id == R.id.filter_oldest_first) {
            sortMethod = Utils.SortMethod.OLD_FIRST;
        } else if (id == R.id.filter_recent_first) {
            sortMethod = Utils.SortMethod.RECENT_FIRST;
        }

        updateTasks(tasks);/////////////////////////

        return super.onOptionsItemSelected(item);
    }



    ///////////// CONFIGURATION /////////////

    private void configViewModel()
    {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.viewModel = ViewModelProviders.of(this, viewModelFactory).get(TaskViewModel.class);
    }

    private void configRecyclerView()
    {
        adapter = new TasksAdapter(tasks, this);
        listTasks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listTasks.setAdapter(adapter);
    }

    ///////////// PROJECT /////////////

    private void getAllProjects()
    {
        this.viewModel.getAllProjects().observe(this, new Observer<List<Project>>() {
            @Override
            public void onChanged(@Nullable List<Project> projects) {
                updateProjects(projects);
            }
        });
    }

    private void updateProjects(List<Project> projects)
    {
        Project[] p = new Project[projects.size()];
        for (int i = 0; i < p.length; i ++)
        {
            p[i] = projects.get(i);
        }
        //this.allProjects = p;
    }

    private void getProject(long id)
    {
        this.viewModel.getProject(id).observe(this, new Observer<Project>() {
            @Override
            public void onChanged(@Nullable Project project) {

            }
        });
    }

    private void insertProject(Project project)
    {
        this.viewModel.insertProject(project);
    }

    private void configProjects()
    {
        List<Project> p = new ArrayList<>();
        for (int i =0; i < Project.getAllProjects().length; i ++)
        {
            p.add(Project.getAllProjects()[i]);
        }
        for (int i = 0; i < p.size(); i ++)
        {
            insertProject(p.get(i));
        }
        this.getAllProjects();
    }

    ///////////// TASK /////////////


    private void getAllTasks()
    {
        this.viewModel.getAllTasks().observe(this, this::updateTasks);
        this.viewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable List<Task> tasks) {
                adapter.updateTasks(tasks);
            }
        });
    }


    private void getTask(long id)
    {
        this.viewModel.getTask(id).observe(this, new Observer<Task>() {
            @Override
            public void onChanged(@Nullable Task tasks) {

            }
        });
    }

    private void insertTask(Task task)
    {
        this.getAllProjects();
        this.viewModel.insertTask(task);
    }

    private void deleteTask(Task task)
    {
        this.viewModel.deleteTask(task);
    }

    /**
     * Adds the given task to the list of created tasks.
     *
     * @param task the task to be added to the list
     */
    private void addTask(@NonNull Task task) {
       /* tasks.add(task);
        updateTasks(tasks);*/

        /////////////////////////
        this.insertTask(task);
        this.getAllTasks();
        /////////////////////////
    }

    /**
     * Updates the list of tasks in the UI
     */
    private void updateTasks(List<Task> tasks) {
        /////////////////////////
        this.tasks = (ArrayList<Task>) tasks;
        /////////////////////////

        if (tasks.size() == 0)
        {
            lblNoTasks.setVisibility(View.VISIBLE);
            listTasks.setVisibility(View.GONE);
        } else {
            lblNoTasks.setVisibility(View.GONE);
            listTasks.setVisibility(View.VISIBLE);
            /////////////////////////
            adapter.updateTasks(Utils.sortTasks(tasks, sortMethod));
            /////////////////////////
        }
    }

    @Override
    public void onDeleteTask(Task task) {
        /*tasks.remove(task);
        updateTasks(tasks);/////////////////////////*/

        /////////////////////////
        this.deleteTask(task);
        this.updateTasks(tasks);
        /////////////////////////
    }

    ///////////// DIALOG /////////////

    /**
     * Called when the user clicks on the positive button of the Create Task Dialog.
     *
     * @param dialogInterface the current displayed dialog
     */
    private void onPositiveButtonClick(DialogInterface dialogInterface) {
        // If dialog is open
        if (dialogEditText != null && dialogSpinner != null) {
            // Get the name of the task
            String taskName = dialogEditText.getText().toString();

            // Get the selected project to be associated to the task
            Project taskProject = null;
            if (dialogSpinner.getSelectedItem() instanceof Project) {
                taskProject = (Project) dialogSpinner.getSelectedItem();
            }

            // If a name has not been set
            if (taskName.trim().isEmpty()) {
                dialogEditText.setError(getString(R.string.empty_task_name));
            }
            // If both project and name of the task have been set
            else if (taskProject != null) {
                // TODO: Replace this by id of persisted task
                long id = (long) (Math.random() * 50000);


                Task task = new Task(
                        id,
                        taskProject.getId(),
                        taskName,
                        new Date().getTime()
                );

                addTask(task);

                dialogInterface.dismiss();
            }
            // If name has been set, but project has not been set (this should never occur)
            else{
                dialogInterface.dismiss();
            }
        }
        // If dialog is aloready closed
        else {
            dialogInterface.dismiss();
        }
    }

    /**
     * Shows the Dialog for adding a Task
     */
    private void showAddTaskDialog() {
        final AlertDialog dialog = getAddTaskDialog();

        dialog.show();

        dialogEditText = dialog.findViewById(R.id.txt_task_name);
        dialogSpinner = dialog.findViewById(R.id.project_spinner);

        populateDialogSpinner();
    }

    /**
     * Returns the dialog allowing the user to create a new task.
     *
     * @return the dialog allowing the user to create a new task
     */
    @NonNull
    private AlertDialog getAddTaskDialog() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.Dialog);

        alertBuilder.setTitle(R.string.add_task);
        alertBuilder.setView(R.layout.dialog_add_task);
        alertBuilder.setPositiveButton(R.string.add, null);
        alertBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dialogEditText = null;
                dialogSpinner = null;
                dialog = null;
            }
        });

        dialog = alertBuilder.create();

        // This instead of listener to positive button in order to avoid automatic dismiss
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        onPositiveButtonClick(dialog);
                    }
                });
            }
        });

        return dialog;
    }

    /**
     * Sets the data of the Spinner with projects to associate to a new task
     */
    private void populateDialogSpinner() {
        final ArrayAdapter<Project> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allProjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (dialogSpinner != null) {
            dialogSpinner.setAdapter(adapter);
        }
    }


}
