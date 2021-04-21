package hu.bme.aut.android.homeworkoutapp.ui.plans

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import hu.bme.aut.android.homeworkoutapp.R
import hu.bme.aut.android.homeworkoutapp.databinding.*
import hu.bme.aut.android.homeworkoutapp.utils.daysOfWeekFromLocale
import hu.bme.aut.android.homeworkoutapp.utils.setTextColorRes
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*


class PlansFragment : Fragment() {

    val typedValue = TypedValue()

    private var _binding: FragmentPlansBinding? = null
    private val binding get() = _binding!!

    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()

    private val titleSameYearFormatter = DateTimeFormatter.ofPattern("MMMM")
    private val titleFormatter = DateTimeFormatter.ofPattern("MMM yyyy")
    private val selectionFormatter = DateTimeFormatter.ofPattern("d MMM yyyy")

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // todo
        /*binding.eventsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = eventsAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        }*/

        val daysOfWeek = daysOfWeekFromLocale()
        val currentMonth = YearMonth.now()
        binding.plansCalendar.apply {
            setup(currentMonth.minusMonths(10), currentMonth.plusMonths(10), daysOfWeek.first())
            scrollToMonth(currentMonth)
        }

        if (savedInstanceState == null) {
            binding.plansCalendar.post {
                // Show today's events initially.
                selectDate(today)
            }
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
                        selectedDate -> {
                            requireContext().theme.resolveAttribute(R.attr.colorOnSecondary, typedValue, true)
                            textView.setTextColorRes(typedValue.resourceId)
                            textView.setBackgroundResource(R.drawable.plans_selected_bg)
                            dotView.visibility = View.INVISIBLE
                        }
                        else -> {
                            requireContext().theme.resolveAttribute(R.attr.colorOnPrimary, typedValue, true)
                            textView.setTextColorRes(typedValue.resourceId)
                            textView.background = null
                            // todo ez az egész a viewmodel state-je alapján az ott tárol listából szedje ki
                            // dotView.isVisible = events[day.date].orEmpty().isNotEmpty()
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
            selectDate(it.yearMonth.atDay(1))
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

        binding.fabAddWorkoutToEvent.setOnClickListener {
            Toast.makeText(requireContext(), "inputDialog.show()", Toast.LENGTH_SHORT).show()
        }
    }

    private fun selectDate(date: LocalDate) {
        if (selectedDate != date) {
            val oldDate = selectedDate
            selectedDate = date
            oldDate?.let { binding.plansCalendar.notifyDateChanged(it) }
            binding.plansCalendar.notifyDateChanged(date)
            // todo viewmodel call, viewmodel-ben eltárolni esetleg a kiválasztott napot, de ez nem biztos, hogy kell
            updateAdapterForDate(date)
        }
    }

    /*private fun saveEvent(text: String) {
        if (text.isBlank()) {
            Toast.makeText(requireContext(), "R.string.example_3_empty_input_text", Toast.LENGTH_LONG).show()
        } else {
            selectedDate?.let {
                events[it] = events[it].orEmpty().plus(Plans(UUID.randomUUID().toString(), text, it))
                updateAdapterForDate(it)
            }
        }
    }

    private fun deleteEvent(event: Plans) {
        val date = event.date
        events[date] = events[date].orEmpty().minus(event)
        updateAdapterForDate(date)
    }*/

    private fun updateAdapterForDate(date: LocalDate) {

        // todo viewmodel-től lekérni a workout-okat az új napra

        binding.plansSelectedDateText.text = selectionFormatter.format(date)
    }

}
