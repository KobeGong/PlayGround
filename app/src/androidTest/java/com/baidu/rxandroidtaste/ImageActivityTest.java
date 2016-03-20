package com.baidu.rxandroidtaste;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


/**
 * Created by gonggaofeng on 16/1/5.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ImageActivityTest {
    @Rule
    public ActivityTestRule<ImageActivity> mActivityRule = new ActivityTestRule<ImageActivity>(ImageActivity.class);

    @Test
    public void imageClick() {
        onView(withId(R.id.image)).perform(ViewActions.click());
    }
}
