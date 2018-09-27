package com.example.mb.comp7082;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
@RunWith(AndroidJUnit4.class)
public class ImageUnitTest {
    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);
    @Before
    public void init(){
        activityActivityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
    }
    @Test
    public void testButtons() throws Exception{
        onView(withId(R.id.searchPhoto)).perform(click());
        onView(withId(R.id.startDate)).perform(typeText("2018/09/01"), closeSoftKeyboard());
        onView(withId(R.id.endDate)).perform(typeText("2018/09/30"), closeSoftKeyboard());
        onView(withId(R.id.search)).perform(click());
        onView(withId(R.id.searchPhoto)).perform(click());
        onView(withId(R.id.back)).perform(click());
        onView(withId(R.id.snapPhoto)).perform(click());

    }
}
