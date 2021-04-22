package hu.bme.aut.android.homeworkoutapp.ui.plans

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import androidx.navigation.fragment.findNavController
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import co.zsmb.rainbowcake.extensions.exhaustive
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import hu.bme.aut.android.homeworkoutapp.MainActivity
import hu.bme.aut.android.homeworkoutapp.R
import hu.bme.aut.android.homeworkoutapp.databinding.*
import hu.bme.aut.android.homeworkoutapp.ui.exercises.ExercisesFragmentDirections
import hu.bme.aut.android.homeworkoutapp.ui.exercises.UploadSuccess
import hu.bme.aut.android.homeworkoutapp.ui.exercises.Uploading
import hu.bme.aut.android.homeworkoutapp.ui.plans.recyclerview.PlannedWorkoutRecyclerViewAdapter
import hu.bme.aut.android.homeworkoutapp.ui.workoutpicker.WorkoutPickedListener
import hu.bme.aut.android.homeworkoutapp.ui.workoutpicker.WorkoutPickerFragment
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout
import hu.bme.aut.android.homeworkoutapp.utils.daysOfWeekFromLocale
import hu.bme.aut.android.homeworkoutapp.utils.setTextColorRes
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter


class PlannedWorkoutsFragment : RainbowCakeFragment<PlannedWorkoutsViewState, PlannedWorkoutsViewModel>(),
    PlannedWorkoutRecyclerViewAdapter.PlannedWorkoutItemClickListener {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = 0

    val typedValue = TypedValue()

    private var _binding: FragmentPlansBinding? = null
    private val binding get() = _binding!!

    private var mainActivity: MainActivity? = null

    private val today = LocalDate.now()

    private val titleSameYearFormatter = DateTimeFormatter.ofPattern("MMMM")
    private val titleFormatter = DateTimeFormatter.ofPattern("MMM yyyy")
    private val selectionFormatter = DateTimeFormatter.ofPattern("d MMM yyyy")

    private val recyclerViewAdapter = PlannedWorkoutRecyclerViewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlansBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun render(viewState: PlannedWorkoutsViewState) {
        when(viewState) {
            is PlannedWorkoutsLoading -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            is PlannedWorkoutsLoaded -> {
                binding.progressBar.visibility = View.GONE
                recyclerViewAdapter.submitList(viewState.workoutsList)
            }
            is PlannedWorkoutsFailed -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(activity, viewState.message, Toast.LENGTH_LONG).show()
            }
            is PlannedWorkoutUploading -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            is PlannedWorkoutUploadFailed -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(activity, viewState.message, Toast.LENGTH_LONG).show()
            }
        }.exhaustive

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainActivity = activity as? MainActivity
        mainActivity?.setToolbarAndBottomNavigationViewVisible(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewAdapter.plannedWorkoutClickListener = this
        binding.plannedWorkoutsRecyclerView.adapter = recyclerViewAdapter

        val daysOfWeek = daysOfWeekFromLocale()
        val currentMonth = YearMonth.now()
        binding.plansCalendar.apply {
            setup(currentMonth.minusMonths(10), currentMonth.plusMonths(10), daysOfWeek.first())
            scrollToMonth(currentMonth)
        }

        binding.plansCalendar.post {
            // Show today's events initially.
            selectDate(viewModel.selectedDate)
        }

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay // Will be set when this container is bound.
            val binding = PlansCalendarDayBinding.bind(view)

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        selectDate(day.date)
                    }
                }
            }
        }
        binding.plansCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.binding.plansDayText
                val dotView = container.binding.plansDotView

                textView.text = day.date.dayOfMonth.toString()

                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.visibility = View.VISIBLE
                    when (day.date) {
                        today -> {
                            requireContext().theme.resolveAttribute(R.attr.colorOnSecondary, typedValue, true)
                            textView.setTextColorRes(typedValue.resourceId)
                            textView.setBackgroundResource(R.drawable.plans_today_bg)
                            dotView.visibility = View.INVISIBLE
                        }
                        viewModel.selectedDate -> {
                            requireContext().theme.resolveAttribute(R.attr.colorOnSecondary, typedValue, true)
                            textView.setTextColorRes(typedValue.resourceId)
                            textView.setBackgroundResource(R.drawable.plans_selected_bg)
                            dotView.visibility = View.INVISIBLE
                        }
                        else -> {
                            requireContext().theme.resolveAttribute(R.attr.colorOnPrimary, typedValue, true)
                            textView.setTextColorRes(typedValue.resourceId)
                            textView.background = null
                            // todo ez az egész a viewmodel state-je alapján az ott tárolt listából szedje ki
                            // todo valahogy lekérni a kollekció dátumait
                            /*val currentState = viewModel.state.value
                            if(currentState is PlannedWorkoutsLoaded) {

                            }
                            dotView.isVisible = events[day.date].orEmpty().isNotEmpty()*/
                        }
                    }
                } else {
                    textView.visibility = View.INVISIBLE
                    dotView.visibility = View.INVISIBLE
                }
            }
        }

        binding.plansCalendar.monthScrollListener = {
            binding.plansMonthText.text = if (it.year == today.year) {
                titleSameYearFormatter.format(it.yearMonth)
            } else {
                titleFormatter.format(it.yearMonth)
            }

            // Select the first day of the month when
            // we scroll to a new month.
            // todo selectDate(it.yearMonth.atDay(1))
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val legendLayout = PlansCalendarHeaderBinding.bind(view).legendLayout
        }
        binding.plansCalendar.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                // Setup each header day text if we have not done that already.
                if (container.legendLayout.tag == null) {
                    container.legendLayout.tag = month.yearMonth
                    container.legendLayout.children.map { it as TextView }.forEachIndexed { index, tv ->
                        tv.text = daysOfWeek[index].name.subSequence(0, 2).toString()
                        requireContext().theme.resolveAttribute(R.attr.colorOnPrimaryAlpha, typedValue, true)
                        tv.setTextColorRes(typedValue.resourceId)
                    }
                }
            }
        }

        binding.btnAddPlannedWorkout.setOnClickListener {
            val action = PlannedWorkoutsFragmentDirections.actionNavigationPlansToWorkoutPickerFragment(
                WorkoutPickedListener { workouts ->
                    viewModel.addPlannedWorkoutToDate(workouts[0])
                }
            )
            findNavController().navigate(action)
        }
    }

    private fun selectDate(date: LocalDate) {
        binding.plansCalendar.notifyDateChanged(viewModel.selectedDate)
        viewModel.selectedDate = date
        binding.plansCalendar.notifyDateChanged(viewModel.selectedDate)

        viewModel.getPlannedWorkoutsFromDate()
        binding.plansSelectedDateText.text = selectionFormatter.format(viewModel.selectedDate)
    }

    override fun onItemClick(workout: UiWorkout?): Boolean {
        if(workout != null) {
            val action = PlannedWorkoutsFragmentDirections.actionNavigationPlansToWorkoutFragment(workout)
            findNavController().navigate(action)
        }
        return true
    }

    override fun onItemLongClick(workout: UiWorkout?): Boolean {
        if(workout != null) {
            AlertDialog.Builder(context)
                .setTitle(getString(R.string.title_warning))
                .setMessage(getString(R.string.txt_sure_to_delet))
                .setPositiveButton(getString(R.string.btn_yes)) { dialogInterface: DialogInterface, i: Int ->
                    viewModel.deletePlannedWorkoutFromDate(workout)
                }
                .setNegativeButton(getString(R.string.btn_no), null)
                .show()
        }
        return true
    }

}
