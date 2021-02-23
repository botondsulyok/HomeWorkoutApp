package hu.bme.aut.android.homeworkoutapp.ui.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import hu.bme.aut.android.homeworkoutapp.R

class ExercisesFragment : Fragment() {

    private lateinit var exerciseViewModel: ExerciseViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        exerciseViewModel =
                ViewModelProvider(this).get(ExerciseViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_exercises, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        exerciseViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}