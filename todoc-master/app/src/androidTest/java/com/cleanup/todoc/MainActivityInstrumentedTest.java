package com.cleanup.todoc;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.RootMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.cleanup.todoc.TestUtils.childAtPosition;
import static com.cleanup.todoc.TestUtils.withRecyclerView;

import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @author Gaëtan HERFRAY
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {

    private TodocDatabase database;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void addAndRemoveTask() throws InterruptedException{
        MainActivity activity = rule.getActivity();
        TextView lblNoTask = activity.findViewById(R.id.lbl_no_task);
        RecyclerView listTasks = activity.findViewById(R.id.list_tasks);

        int listSize;

        if (lblNoTask.getVisibility() == View.VISIBLE)
        {
            listSize =0;
        }
        else
        {
            listSize = listTasks.getAdapter().getItemCount();
        }

        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("Tâche example"));
        onView(withId(android.R.id.button1)).perform(click());

        // Check that lblTask is not displayed anymore
        assertThat(lblNoTask.getVisibility(), equalTo(View.GONE));
        // Check that recyclerView is displayed
        assertThat(listTasks.getVisibility(), equalTo(View.VISIBLE));
        // Check that it contains one element only
        assertEquals(listTasks.getAdapter().getItemCount(), listSize+1);


        onView(withId(R.id.list_tasks)).perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteViewAction()));
        listSize = listTasks.getAdapter().getItemCount();

        if (listSize == 0)
        {
            // Check that lblTask is displayed
            assertThat(lblNoTask.getVisibility(), equalTo(View.VISIBLE));
            // Check that recyclerView is not displayed anymore
            assertThat(listTasks.getVisibility(), equalTo(View.GONE));
        }
        else
        {
            // Check that lblTask is not displayed anymore
            assertThat(lblNoTask.getVisibility(), equalTo(View.GONE));
            // Check that recyclerView is displayed
            assertThat(listTasks.getVisibility(), equalTo(View.VISIBLE));
            // Check that it contains one element only
            assertEquals(listTasks.getAdapter().getItemCount(), listSize);
        }

    }

    @Test
    public void sortTasks() {
        MainActivity activity = rule.getActivity();

        RecyclerView listTasks = activity.findViewById(R.id.list_tasks);
        int countList = listTasks.getAdapter().getItemCount();
        for (int i = 0 ; i < countList; i++){
            onView(withId(R.id.list_tasks)).perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteViewAction()));
        }

        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("CCC Tâche example"));
        ViewInteraction appCompatSpinner1 = onView(allOf(withId(R.id.project_spinner),
                        childAtPosition(allOf(withId(R.id.add_task_dialog),
                                childAtPosition(withId(R.id.custom), 0)), 1),
                        isDisplayed()));
        appCompatSpinner1.perform(click());
        onData(anything()).inRoot(RootMatchers.isPlatformPopup()).atPosition(2).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("LLL Tâche example"));
        ViewInteraction appCompatSpinner2 = onView(allOf(withId(R.id.project_spinner),
                childAtPosition(allOf(withId(R.id.add_task_dialog),
                        childAtPosition(withId(R.id.custom), 0)), 1),
                isDisplayed()));
        appCompatSpinner2.perform(click());
        onData(anything()).inRoot(RootMatchers.isPlatformPopup()).atPosition(1).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("TTT Tâche example"));
        ViewInteraction appCompatSpinner3 = onView(allOf(withId(R.id.project_spinner),
                childAtPosition(allOf(withId(R.id.add_task_dialog),
                        childAtPosition(withId(R.id.custom), 0)), 1),
                isDisplayed()));
        appCompatSpinner3.perform(click());
        onData(anything()).inRoot(RootMatchers.isPlatformPopup()).atPosition(0).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("CCC Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("LLL Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("TTT Tâche example")));

        // Sort alphabetical
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_alphabetical)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("CCC Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("LLL Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("TTT Tâche example")));

        // Sort alphabetical inverted
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_alphabetical_invert)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("TTT Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("LLL Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("CCC Tâche example")));

        // Sort old first
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_oldest_first)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("CCC Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("LLL Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("TTT Tâche example")));

        // Sort recent first
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_recent_first)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("TTT Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("LLL Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("CCC Tâche example")));

        countList = listTasks.getAdapter().getItemCount();
        for (int i = 0 ; i < countList; i++){
            onView(withId(R.id.list_tasks)).perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteViewAction()));
        }
    }
}
