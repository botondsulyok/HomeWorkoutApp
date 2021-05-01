package hu.bme.aut.android.homeworkoutapp

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth
import hu.bme.aut.android.homeworkoutapp.ui.newworkout.NewWorkoutFragment
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewWorkoutFragmentTest {

    private lateinit var navController: TestNavHostController

    private lateinit var scenario: FragmentScenario<NewWorkoutFragment>

    @Before
    fun initFragment() {
        navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )

        scenario = launchFragmentInContainer(
            themeResId = R.style.Theme_HomeWorkoutApp
        ) {
            NewWorkoutFragment()
        }

        scenario.onFragment { fragment ->
            // init graph and set destination to NewWorkoutFragment
            navController.setGraph(R.navigation.mobile_navigation)
            Navigation.setViewNavController(fragment.requireView(), navController)
            navController.setCurrentDestination(R.id.newWorkoutFragment)
        }
    }

    @Test
    fun whenCreateButtonClickedWithEmptyTitleEditText_thenEditTextHasErrorText() {
        // Given
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        // When
        onView(Matchers.allOf(withId(R.id.btnCreate), withContentDescription(R.string.desc_create_a_new_workout)))
            .perform(ViewActions.click())

        // Then
        onView(withId(R.id.etTitle))
            .check(ViewAssertions.matches(ViewMatchers.hasErrorText(appContext.getString(R.string.error_add_a_name))))
    }

    @Test
    fun whenCreateButtonClickedWithEmptyTitleEditText_thenPopBackStackDoesNotHappen() {
        // When
        onView(Matchers.allOf(withId(R.id.btnCreate), withContentDescription(R.string.desc_create_a_new_workout)))
            .perform(ViewActions.click())

        // Then
        Truth.assertThat(navController.currentDestination?.id).isEqualTo(R.id.newWorkoutFragment)
    }

    @Test
    fun whenCreateButtonClickedWithNotEmptyTitleEditText_thenPopBackStack() {
        // When
        onView(withId(R.id.etTitle)).perform(typeText("Workout title"))
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard())
        onView(Matchers.allOf(withId(R.id.btnCreate), withContentDescription(R.string.desc_create_a_new_workout)))
            .perform(ViewActions.click())

        // Then
        Truth.assertThat(navController.currentDestination?.id).isNotEqualTo(R.id.newWorkoutFragment)
    }

}