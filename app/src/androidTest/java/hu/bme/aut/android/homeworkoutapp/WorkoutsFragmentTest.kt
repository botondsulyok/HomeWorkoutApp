package hu.bme.aut.android.homeworkoutapp

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import hu.bme.aut.android.homeworkoutapp.ui.workouts.WorkoutsFragment
import org.hamcrest.Matchers.allOf
import org.junit.Test
import org.junit.runner.RunWith
import com.google.common.truth.Truth.assertThat
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class WorkoutsFragmentTest {

    @Test
    fun testNavigationToInGameScreen() {
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext())

        val scenario = launchFragmentInContainer<WorkoutsFragment>()

        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        scenario.recreate()
        onView(allOf(withId(R.id.btnCreateWorkout), withText(R.string.btn_create_new_workout))).perform(click())
        assertThat(navController.currentDestination?.id).isEqualTo(R.id.newWorkoutFragment)
    }


}