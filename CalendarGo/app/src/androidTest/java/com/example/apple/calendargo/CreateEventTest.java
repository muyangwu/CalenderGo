package com.example.apple.calendargo;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CreateEventTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void createEventTest() throws InterruptedException {
        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.more_item),
                        withParent(allOf(withId(R.id.bb_bottom_bar_item_container),
                                withParent(withId(R.id.bb_bottom_bar_outer_container)))),
                        isDisplayed()));
        linearLayout.perform(click());

        Thread.sleep(1000);

        ViewInteraction linearLayout2 = onView(
                allOf(withId(R.id.list_item),
                        withParent(allOf(withId(R.id.bb_bottom_bar_item_container),
                                withParent(withId(R.id.bb_bottom_bar_outer_container)))),
                        isDisplayed()));
        linearLayout2.perform(click());

        Thread.sleep(1000);

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.profile_image),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()));
        appCompatImageView.perform(click());

        Thread.sleep(1000);

        if(!MainActivity.hasLoggedIn) {
            ViewInteraction appCompatCheckedTextView = onView(
                    allOf(withId(R.id.design_menu_item_text), withText("Log in"), isDisplayed()));
            appCompatCheckedTextView.perform(click());


            ViewInteraction appCompatEditText = onView(
                    allOf(withId(R.id.etEmailLogin),
                            withParent(allOf(withId(R.id.activity_login),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatEditText.perform(click());

            ViewInteraction appCompatEditText2 = onView(
                    allOf(withId(R.id.etEmailLogin),
                            withParent(allOf(withId(R.id.activity_login),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatEditText2.perform(replaceText("827377925@qq.com"), closeSoftKeyboard());

            ViewInteraction appCompatEditText3 = onView(
                    allOf(withId(R.id.etEmailLogin), withText("827377925@qq.com"),
                            withParent(allOf(withId(R.id.activity_login),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatEditText3.perform(pressImeActionButton());

            ViewInteraction appCompatEditText4 = onView(
                    allOf(withId(R.id.etPasswordLogin),
                            withParent(allOf(withId(R.id.activity_login),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatEditText4.perform(replaceText("gwt83537298"), closeSoftKeyboard());

            ViewInteraction appCompatEditText5 = onView(
                    allOf(withId(R.id.etPasswordLogin), withText("gwt83537298"),
                            withParent(allOf(withId(R.id.activity_login),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatEditText5.perform(pressImeActionButton());

            Thread.sleep(1000);

            ViewInteraction appCompatButton = onView(
                    allOf(withId(R.id.bLogin), withText("Log in"),
                            withParent(allOf(withId(R.id.activity_login),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatButton.perform(click());

            Thread.sleep(3000);
        }
        ViewInteraction linearLayout3 = onView(
                allOf(withId(R.id.more_item),
                        withParent(allOf(withId(R.id.bb_bottom_bar_item_container),
                                withParent(withId(R.id.bb_bottom_bar_outer_container)))),
                        isDisplayed()));
        linearLayout3.perform(click());

        Thread.sleep(1000);

        ViewInteraction textView = onView(
                allOf(withId(R.id.type_list), withText("Create event"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.listViewType),
                                        0),
                                1),
                        isDisplayed()));
        textView.check(matches(withText("Create event")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.type_list), withText("Manage events"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.listViewType),
                                        1),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("Manage events")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.type_list), withText("Manage account"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.listViewType),
                                        2),
                                1),
                        isDisplayed()));
        textView3.check(matches(withText("Manage account")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.type_list), withText("Manage account"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.listViewType),
                                        2),
                                1),
                        isDisplayed()));
        textView4.check(matches(withText("Manage account")));

        ViewInteraction relativeLayout = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.listViewType),
                                withParent(withId(R.id.activity_main))),
                        0),
                        isDisplayed()));
        relativeLayout.perform(click());

        ViewInteraction appCompatEditText6 = onView(
                withId(R.id.organizer));
        appCompatEditText6.perform(scrollTo(), replaceText("albert"), closeSoftKeyboard());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.organizer), withText("albert")));
        appCompatEditText7.perform(pressImeActionButton());

        ViewInteraction appCompatEditText8 = onView(
                withId(R.id.event_name));
        appCompatEditText8.perform(scrollTo(), replaceText("concert"), closeSoftKeyboard());

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.event_name), withText("concert")));
        appCompatEditText9.perform(pressImeActionButton());

        ViewInteraction appCompatEditText10 = onView(
                withId(R.id.address));
        appCompatEditText10.perform(scrollTo(), replaceText("UCSD"), closeSoftKeyboard());

        ViewInteraction appCompatEditText11 = onView(
                allOf(withId(R.id.address), withText("UCSD")));
        appCompatEditText11.perform(pressImeActionButton());

        ViewInteraction appCompatSpinner = onView(
                withId(R.id.category));
        appCompatSpinner.perform(scrollTo(), click());

        Thread.sleep(1000);

        ViewInteraction appCompatCheckedTextView2 = onView(
                allOf(withId(android.R.id.text1), withText("Music"), isDisplayed()));
        appCompatCheckedTextView2.perform(click());

        Thread.sleep(1000);

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.button3), withText("Create")));
        appCompatButton2.perform(scrollTo());

        Thread.sleep(3000);

        appCompatButton2.perform(click());


        ViewInteraction appCompatButton3 = onView(
                allOf(withId(android.R.id.button1), withText("Continue"),
                        withParent(allOf(withId(R.id.buttonPanel),
                                withParent(withId(R.id.parentPanel)))),
                        isDisplayed()));
        appCompatButton3.perform(click());

        Thread.sleep(3000);

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.location_confirm), withText("confirm location"),
                        withParent(allOf(withId(R.id.drag_map_layout),
                                withParent(withId(R.id.frame)))),
                        isDisplayed()));
        appCompatButton4.perform(click());

        Thread.sleep(3000);

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(android.R.id.button1), withText("Publish event"),
                        withParent(allOf(withId(R.id.buttonPanel),
                                withParent(withId(R.id.parentPanel)))),
                        isDisplayed()));
        appCompatButton5.perform(click());

        Thread.sleep(1000);

        ViewInteraction linearLayout4 = onView(
                allOf(withId(R.id.calendar_item),
                        withParent(allOf(withId(R.id.bb_bottom_bar_item_container),
                                withParent(withId(R.id.bb_bottom_bar_outer_container)))),
                        isDisplayed()));
        linearLayout4.perform(click());

        Thread.sleep(3000);

        ViewInteraction linearLayout5 = onView(
                allOf(withId(R.id.map_item),
                        withParent(allOf(withId(R.id.bb_bottom_bar_item_container),
                                withParent(withId(R.id.bb_bottom_bar_outer_container)))),
                        isDisplayed()));
        linearLayout5.perform(click());

        Thread.sleep(2000);

        ViewInteraction linearLayout6 = onView(
                allOf(withId(R.id.list_item),
                        withParent(allOf(withId(R.id.bb_bottom_bar_item_container),
                                withParent(withId(R.id.bb_bottom_bar_outer_container)))),
                        isDisplayed()));
        linearLayout6.perform(click());

        ViewInteraction relativeLayout2 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.listViewType),
                                withParent(withId(R.id.activity_main))),
                        2),
                        isDisplayed()));
        relativeLayout2.perform(click());

        Thread.sleep(3000);

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
