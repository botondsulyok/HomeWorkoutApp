package hu.bme.aut.android.homeworkoutapp

import android.os.Bundle
import androidx.core.os.bundleOf
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
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise
import hu.bme.aut.android.homeworkoutapp.ui.workout.WorkoutFragment
import hu.bme.aut.android.homeworkoutapp.ui.workout.recyclerview.WorkoutExercisesRecyclerViewAdapter
import hu.bme.aut.android.homeworkoutapp.ui.workouts.WorkoutsFragment
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout
import hu.bme.aut.android.homeworkoutapp.ui.workouts.recyclerview.WorkoutsRecyclerViewAdapter
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WorkoutFragmentTest {

    companion object {
        private val listItemNumberInTest = 2
        private val exerciseInTest = UiExercise("2", "Exercise2")
        var exercisesList: List<UiExercise> = listOf()
    }

    private lateinit var navController: TestNavHostController

    private lateinit var scenario: FragmentScenario<WorkoutFragment>

    private fun initFragment() {
        navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )

        val arguments = Bundle().apply {
            putParcelable("workout", UiWorkout())
        }
        scenario = launchFragmentInContainer(
            fragmentArgs = arguments,
            themeResId = R.style.Theme_HomeWorkoutApp) {
            WorkoutFragment()
        }

        scenario.onFragment { fragment ->
            // init graph and set destination to WorkoutsFragment
            navController.setGraph(R.navigation.mobile_navigation)
            Navigation.setViewNavController(fragment.requireView(), navController)
            navController.setCurrentDestination(R.id.workoutFragment)
        }
    }

    @Test
    fun whenStartWorkoutButtonClicked_thenDoingExerciseFragmentVisible() {
        // Given
        exercisesList = MutableList(listItemNumberInTest + 4) {
            UiExercise(it.toString(), "Exercise${it}")
        }.apply {
            this[listItemNumberInTest] = exerciseInTest
        }
        initFragment()

        // When
        onView(withId(R.id.fabStartWorkout)).perform(
            click()
        )

        // Then
        assertThat(navController.currentDestination?.id).isEqualTo(R.id.doExerciseFragment)
    }

    @Test
    fun whenStartWorkoutButtonClickedAndThereAreNoExercises_thenAlertTextShowsUp() {
        // Given
        exercisesList = listOf()
        initFragment()

        // When
        onView(withId(R.id.fabStartWorkout)).perform(
            click()
        )

        // Then
        onView(withText(R.string.txt_first_add_exercises))
            .check(matches(isDisplayed()))
        assertThat(navController.currentDestination?.id).isEqualTo(R.id.workoutFragment)
    }

}