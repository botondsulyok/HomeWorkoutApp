package hu.bme.aut.android.homeworkoutapp

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.matcher.ViewMatchers.withChild
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import hu.bme.aut.android.homeworkoutapp.ui.exercises.ExercisesFragment
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StartExerciseBottomSheetDialogFragmentTestLaunchedFromExercisesFragment {

    private lateinit var navController: TestNavHostController

    private lateinit var scenario: FragmentScenario<ExercisesFragment>

    @Before
    fun initFragments() {
        navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )

        scenario = launchFragmentInContainer(
            themeResId = R.style.Theme_HomeWorkoutApp
        ) {
            ExercisesFragment()
        }

        scenario.onFragment { fragment ->
            // init graph and set destination to ExercisesFragment
            navController.setGraph(R.navigation.mobile_navigation)
            Navigation.setViewNavController(fragment.requireView(), navController)
            navController.setCurrentDestination(R.id.navigation_exercises)
        }

        // todo recyclerview click
        //onView(withId())

    }

    @Test
    fun whenStartButtonClicked_thenNavigateToDoingExerciseFragment() {
        // When
        onView(withId(R.id.btnStart)).perform(click())

        // Then
        onView(withChild(withId(R.id.btnStart))).check(doesNotExist())
    }

}