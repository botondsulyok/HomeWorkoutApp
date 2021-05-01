package hu.bme.aut.android.homeworkoutapp

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import hu.bme.aut.android.homeworkoutapp.core.di.TestDataModule
import hu.bme.aut.android.homeworkoutapp.ui.exercises.ExercisesFragment
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ExercisesFragmentTest {

    companion object {
        private val listItemNumberInTest = 2
        private val exerciseInTest = UiExercise("2", "Exercise2")
    }

    private lateinit var navController: TestNavHostController

    private lateinit var scenario: FragmentScenario<ExercisesFragment>

    @Before
    fun initFragment() {
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
            // init graph and set destination to WorkoutsFragment
            navController.setGraph(R.navigation.mobile_navigation)
            Navigation.setViewNavController(fragment.requireView(), navController)
            navController.setCurrentDestination(R.id.navigation_exercises)
        }
    }

    @Test
    fun whenCreateExerciseButtonClicked_thenNewExerciseFragmentVisible() {
        // When
        onView(withId(R.id.fabCreateExercise)).perform(
            ViewActions.click()
        )

        // Then
        Truth.assertThat(navController.currentDestination?.id).isEqualTo(R.id.newExerciseFragment)
    }


    // todo
    /*
    @Test
    fun whenExerciseClicked_thenImageVisible() {
        // When
        onView(withId(R.id.exercisesRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<WorkoutsRecyclerViewAdapter.ViewHolder>(
                    listItemNumberInTest, ViewActions.scrollTo()
                )
            )
        onView(withId(R.id.exercisesRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<WorkoutsRecyclerViewAdapter.ViewHolder>(
                    listItemNumberInTest, ViewActions.click()
                )
            )

        // Then
        onView(withId(R.id.exercisesRecyclerView))
            .check(matches(withViewAtPosition(listItemNumberInTest, allOf(withId(R.id.ivVideoThumbnail), isDisplayed()))))
    }

    @Test
    fun whenExerciseClicked_thenButtonsVisible() {
    }

    private fun withViewAtPosition(position: Int, itemMatcher: Matcher<View?>): Matcher<View?> {
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description?) {
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(recyclerView: RecyclerView): Boolean {
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                return viewHolder != null && itemMatcher.matches(viewHolder.itemView)
            }
        }
    }
    */

}