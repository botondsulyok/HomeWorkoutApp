package hu.bme.aut.android.homeworkoutapp

import androidx.navigation.findNavController
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.PositionAssertions.isCompletelyAbove
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule var activityScenarioRule = activityScenarioRule<MainActivity>()

    private fun navigateToWorkoutsFragment() {
        activityScenarioRule.scenario.onActivity { activity ->
            activity.findNavController(R.id.nav_host_fragment).navigate(R.id.navigation_workouts)
        }
    }

    @Test
    fun whenMainActivityStarts_thenNavHostFragmentVisible() {
        onView(withId(R.id.nav_host_fragment)).check(matches(isDisplayed()))
    }

    @Test
    fun whenMainActivityStarts_thenNavViewVisible() {
        onView(withId(R.id.nav_view)).check(matches(isDisplayed()))
    }

    @Test
    fun navHostFragmentIsAboveOfNavView() {
        onView(withId(R.id.nav_host_fragment)).check(isCompletelyAbove(withId(R.id.nav_view)))
    }

    @Test
    fun whenActionMainSettingsMenuItemClicked_thenPopupMenuVisibleWithItems() {
        // Given
        navigateToWorkoutsFragment()

        // When
        onView(withId(R.id.action_main_settings)).perform(click())

        // Then
        onView(withText(R.string.menu_sign_out)).check(matches(isDisplayed()))
        onView(withText(R.string.menu_settings)).check(matches(isDisplayed()))
    }


    @Test
    fun whenSignOutMenuItemClicked_thenNavigateToWelcomeFragment() {
        // Given
        navigateToWorkoutsFragment()

        // When
        onView(withId(R.id.action_main_settings)).perform(click())
        onView(withText(R.string.menu_sign_out)).perform(click())

        // Then
        activityScenarioRule.scenario.onActivity { activity ->
            Truth.assertThat(
                activity.findNavController(R.id.nav_host_fragment).currentDestination?.id)
                .isEqualTo(R.id.navigation_welcome)
        }
    }

}