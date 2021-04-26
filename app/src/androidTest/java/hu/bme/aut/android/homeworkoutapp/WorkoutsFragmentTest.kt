package hu.bme.aut.android.homeworkoutapp

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import hu.bme.aut.android.homeworkoutapp.ui.workouts.WorkoutsFragment
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout
import hu.bme.aut.android.homeworkoutapp.ui.workouts.recyclerview.WorkoutsRecyclerViewAdapter
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WorkoutsFragmentTest {

    private lateinit var navController: TestNavHostController

    private lateinit var scenario: FragmentScenario<WorkoutsFragment>

    val listItemNumberInTest = 2

    val workoutInTest = UiWorkout("2", "Workout2")

    val workoutsList = MutableList(listItemNumberInTest + 2) {
        UiWorkout(it.toString(), "Workout${it}")
    }.apply {
        this[listItemNumberInTest] = workoutInTest
    }

    @Before
    fun initFragment() {
        navController = TestNavHostController(
            ApplicationProvider.getApplicationContext())

        scenario = launchFragmentInContainer(themeResId = R.style.Theme_HomeWorkoutApp) {
            WorkoutsFragment()
        }

        scenario.onFragment { fragment ->
            // init graph and set destination to WorkoutsFragment
            navController.setGraph(R.navigation.mobile_navigation)
            Navigation.setViewNavController(fragment.requireView(), navController)
            navController.setCurrentDestination(R.id.navigation_workouts)

            // add dummy data to recyclerview
            (fragment.view?.findViewById<RecyclerView>(R.id.workoutsRecyclerView)?.adapter
                    as? ListAdapter<UiWorkout, WorkoutsRecyclerViewAdapter.ViewHolder>)
                ?.submitList(workoutsList)
        }
    }


    @Test
    fun whenCreateWorkoutButtonClicked_thenNewWorkoutFragmentVisible() {
        // When
        onView(allOf(withId(R.id.btnCreateWorkout), withText(R.string.btn_create_new_workout))).perform(click())

        // Then
        assertThat(navController.currentDestination?.id).isEqualTo(R.id.newWorkoutFragment)
    }

    @Test
    fun whenWorkoutClicked_thenWorkoutFragmentVisible() {
        // When
        onView(withId(R.id.workoutsRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<WorkoutsRecyclerViewAdapter.ViewHolder>(
                    listItemNumberInTest, click()
                )
            )

        // Then
        assertThat(navController.currentDestination?.id).isEqualTo(R.id.workoutFragment)
    }

    // Todo nem működik
    @Test
    fun whenWorkoutLongClicked_thenAlertDialogVisible() {
        // When
        onView(withId(R.id.workoutsRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<WorkoutsRecyclerViewAdapter.ViewHolder>(
                    listItemNumberInTest, longClick()
                )
            )

        // Then
        onView(withText(R.string.txt_sure_to_delete))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

}