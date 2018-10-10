package com.example.mb.comp7082;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import android.support.test.rule.ActivityTestRule;

import java.lang.reflect.Array;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
@RunWith(AndroidJUnit4.class)
public class ImageInstrumentedTest {
    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);
    @Before
    public void init(){
        activityActivityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
    }
    int listSize;
    int index;
    String photo;
    @Test
    public void testButtons() throws Exception{
        listSize = activityActivityTestRule.getActivity().imageList.size();
        index = activityActivityTestRule.getActivity().currentIndex;
        photo = activityActivityTestRule.getActivity().currentPhoto;
        onView(withId(R.id.navRight)).perform(click());
        assert (activityActivityTestRule.getActivity().currentIndex == index + 1);
        assert (activityActivityTestRule.getActivity().currentPhoto != photo);
        index = activityActivityTestRule.getActivity().currentIndex;
        onView(withId(R.id.navLeft)).perform(click());
        assert (activityActivityTestRule.getActivity().currentIndex == index - 1);
        assert (activityActivityTestRule.getActivity().currentPhoto == photo);
        onView(withId(R.id.searchPhoto)).perform(click());
        onView(withId(R.id.search)).perform(click());
        assert (activityActivityTestRule.getActivity().imageList.size() == listSize);
        onView(withId(R.id.searchPhoto)).perform(click());
        onView(withId(R.id.startDate)).perform(typeText("2018/09/27"), closeSoftKeyboard());
        onView(withId(R.id.endDate)).perform(typeText("2018/09/27"), closeSoftKeyboard());
        onView(withId(R.id.search)).perform(click());
        assert (activityActivityTestRule.getActivity().imageList.size() != listSize);
        assert (activityActivityTestRule.getActivity().imageList.size() == 1);
        onView(withId(R.id.searchPhoto)).perform(click());
        onView(withId(R.id.startDate)).perform(typeText("2018/09/01"), closeSoftKeyboard());
        onView(withId(R.id.endDate)).perform(typeText("2018/09/30"), closeSoftKeyboard());
        onView(withId(R.id.search)).perform(click());
        assert (activityActivityTestRule.getActivity().imageList.size() != listSize);
        assert (activityActivityTestRule.getActivity().imageList.size() == 4);
        onView(withId(R.id.searchPhoto)).perform(click());
        onView(withId(R.id.latSearch)).perform(typeText("49"), closeSoftKeyboard());
        onView(withId(R.id.lngSearch)).perform(typeText("122"), closeSoftKeyboard());
        onView(withId(R.id.search)).perform(click());
        assert (activityActivityTestRule.getActivity().imageList.size() != listSize);
        assert (activityActivityTestRule.getActivity().imageList.size() == 1);
        onView(withId(R.id.searchPhoto)).perform(click());
        onView(withId(R.id.back)).perform(click());
        assert (activityActivityTestRule.getActivity().imageList.size() == 1);
        onView(withId(R.id.uploadPhoto)).perform(click());
        pressBack();
        onView(withId(R.id.snapPhoto)).perform(click());
        pressBack();

    }
}
