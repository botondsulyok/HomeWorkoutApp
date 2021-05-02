package hu.bme.aut.android.homeworkoutapp.utils

import android.app.ActionBar
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.NestedScrollView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.util.HumanReadables
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.Matcher
import org.hamcrest.Matchers

class NestedScrollViewScrollToAction : ViewAction {
    override fun getConstraints(): Matcher<View> {
        return Matchers.allOf(
            ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE), ViewMatchers.isDescendantOfA(
                anyOf(
                    isAssignableFrom(NestedScrollView::class.java)
                )
            )
        )
    }

    override fun perform(uiController: UiController, view: View) {
        if (ViewMatchers.isDisplayingAtLeast(90).matches(view)) {
            Log.i(TAG, "View is already displayed. Returning.")
            return
        }
        val parentScrollView = findScrollView(view)
        parentScrollView.requestLayout()
        uiController.loopMainThreadUntilIdle()
        val rect = Rect()
        view.getDrawingRect(rect)
        if (!view.requestRectangleOnScreen(rect, true /* immediate */)) {
            Log.w(TAG, "Scrolling to view was requested, but none of the parents scrolled.")
        }
        uiController.loopMainThreadUntilIdle()
        if (!ViewMatchers.isDisplayingAtLeast(90).matches(view)) {
            throw PerformException.Builder()
                .withActionDescription(this.description)
                .withViewDescription(HumanReadables.describe(view))
                .withCause(
                    RuntimeException(
                        "Scrolling to view was attempted, but the view is not displayed"
                    )
                )
                .build()
        }
    }

    private fun findScrollView(view: View): View {
        val parent = view.parent as View
        return parent as? NestedScrollView ?: findScrollView(parent)
    }

    override fun getDescription(): String {
        return "scroll to"
    }

    companion object {
        private val TAG = NestedScrollViewScrollToAction::class.java.simpleName
        fun scrollTo(): NestedScrollViewScrollToAction {
            return NestedScrollViewScrollToAction()
        }
    }
}