package hu.bme.aut.android.homeworkoutapp

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth
import hu.bme.aut.android.homeworkoutapp.ui.newexercise.NewExerciseFragment
import hu.bme.aut.android.homeworkoutapp.ui.newexercise.models.UiNewExercise
import hu.bme.aut.android.homeworkoutapp.ui.newworkout.NewWorkoutFragment
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewExerciseFragmentTest {

    private lateinit var navController: TestNavHostController

    private lateinit var scenario: FragmentScenario<NewExerciseFragment>

    @Before
    fun initFragment() {
        navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )

        scenario = launchFragmentInContainer(
            fragmentArgs =  bundleOf("exercise" to UiNewExercise()),
            themeResId = R.style.Theme_HomeWorkoutApp
        ) {
            NewExerciseFragment()
        }

        scenario.onFragment { fragment ->
            // init graph and set destination to WorkoutsFragment
            navController.setGraph(R.navigation.mobile_navigation)
            Navigation.setViewNavController(fragment.requireView(), navController)
            navController.setCurrentDestination(R.id.newExerciseFragment)
        }
    }

    @Test
    fun whenCreateButtonClickedWithEmptyNameEditText_thenEditTextHasErrorText() {
        // Given
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        // When
        onView(allOf(withId(R.id.btnCreate), withContentDescription(R.string.desc_create_a_new_exercise)))
            .perform(click())

        // Then
        onView(withId(R.id.etName))
            .check(matches(hasErrorText(appContext.getString(R.string.error_add_a_name))))
    }

    @Test
    fun whenCreateButtonClickedWithEmptyNameEditText_thenPopBackStackDoesNotHappen() {
        // When
        onView(allOf(withId(R.id.btnCreate), withContentDescription(R.string.desc_create_a_new_exercise)))
            .perform(click())

        // Then
        Truth.assertThat(navController.currentDestination?.id).isEqualTo(R.id.newExerciseFragment)
    }

    @Test
    fun whenCreateButtonClickedWithNotEmptyNameEditText_thenPopBackStack() {
        // When
        onView(withId(R.id.etName)).perform(typeText("Exercise name"))
        onView(isRoot()).perform(closeSoftKeyboard())
        onView(allOf(
            withId(R.id.btnCreate),
            withContentDescription(R.string.desc_create_a_new_exercise)))
            .perform(click())

        // Then
        Truth.assertThat(navController.currentDestination?.id)
            .isNotEqualTo(R.id.newExerciseFragment)
    }

    @Test
    fun whenAttachVideoButtonClicked_thenNavigateToRecordNewExercise() {
        // When
        onView(withId(R.id.btnAttachVideo)).perform(click())

        // Then
        Truth.assertThat(navController.currentDestination?.id)
            .isEqualTo(R.id.recordNewExerciseFragment)
    }

    @Test
    fun whenDurationExpandImageButtonClicked_thenExerciseDurationPickerVisible() {
        // When
        onView(withId(R.id.ibDurationExpand)).perform(click())

        // Then
        onView(allOf(
            withId(R.id.exerciseDurationPicker),
            withParent(withId(R.id.motionLayoutNewExercise))))
            .check(matches(isDisplayed()))
    }

}