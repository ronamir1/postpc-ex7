package huji.postpc.y2021.sandwichorder;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.CheckBox;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.junit.Assert.*;
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class MainActivityTest {
    private ActivityController<MainActivity> activityController;
    private MainActivity activityUnderTest;

    @Before
    public void setup(){
        activityController = Robolectric.buildActivity(MainActivity.class);
        Context context = RuntimeEnvironment.application.getApplicationContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        activityUnderTest = activityController.get();
        activityUnderTest.sp = sharedPreferences;
        activityController.create().start().resume();
    }

    @Test
    public void when_activityIsLaunched_then_nameEditText_is_Empty() {
        // setup
        EditText editText = activityUnderTest.findViewById(R.id.nameEditText);
        String userInput = editText.getText().toString();
        // verify
        assertTrue(userInput.isEmpty());
    }

    @Test
    public void when_activityIsLaunched_then_commentEditText_is_Empty() {
        // setup
        EditText editText = activityUnderTest.findViewById(R.id.commentEditText);
        String userInput = editText.getText().toString();
        // verify
        assertTrue(userInput.isEmpty());
    }

    @Test
    public void when_activityIsLaunched_then_hummusCheckBox_is_Unchecked() {
        // setup
        CheckBox checkBox = activityUnderTest.findViewById(R.id.hummusCheckBox);
        assertFalse(checkBox.isChecked());
    }


}

