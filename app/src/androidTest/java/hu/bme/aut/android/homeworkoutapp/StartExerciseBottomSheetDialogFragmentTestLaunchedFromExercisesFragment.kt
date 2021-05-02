package hu.bme.aut.android.homeworkoutapp

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.schibsted.spain.barista.interaction.BaristaScrollInteractions
import hu.bme.aut.android.homeworkoutapp.core.di.TestDataModule
import hu.bme.aut.android.homeworkoutapp.ui.exercises.ExercisesFragment
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise
import hu.bme.aut.android.homeworkoutapp.ui.workouts.recyclerview.WorkoutsRecyclerViewAdapter
import hu.bme.aut.android.homeworkoutapp.utils.NestedScrollViewScrollToAction
import hu.bme.aut.android.homeworkoutapp.utils.clickOnViewChild
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StartExerciseBottomSheetDialogFragmentTestLaunchedFromExercisesFragment {

    private val listItemNumberInTest = 2
    private val exerciseInTest = UiExercise("2", "Exercise2")

    private lateinit var navController: TestNavHostController

    private lateinit var scenario: FragmentScenario<ExercisesFragment>

    @Before
    fun initFragments() {
        TestDataModule.exercisesList = MutableList(listItemNumberInTest + 4) {
            UiExercise(it.toString(), "Exercise${it}")
        }.apply {
            this[listItemNumberInTest] = exerciseInTest
        }

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

        onView(withId(R.id.exercisesRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<WorkoutsRecyclerViewAdapter.ViewHolder>(
                    listItemNumberInTest, click()
                )
            )
        onView(withId(R.id.exercisesRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<WorkoutsRecyclerViewAdapter.ViewHolder>(
                listItemNumberInTest,
                clickOnViewChild(R.id.ibStart)))

    }

    // todo nem jó
    @Test
    fun whenStartButtonClicked_thenNavigateToDoingExerciseFragment() {
        // When
        //onView(isRoot()).perform(swipeDown())
        onView(withId(R.id.btnStart)).perform(swipeDown(), click())

        // Then
        Truth.assertThat(navController.currentDestination?.id).isEqualTo(R.id.doExerciseFragment)
    }

}