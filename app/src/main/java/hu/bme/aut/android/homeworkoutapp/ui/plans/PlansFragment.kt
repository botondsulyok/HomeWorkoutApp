package hu.bme.aut.android.homeworkoutapp.ui.plans

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import hu.bme.aut.android.homeworkoutapp.MainActivity
import hu.bme.aut.android.homeworkoutapp.R

class PlansFragment : Fragment() {

    private lateinit var plansViewModel: PlansViewModel

    private var mainActivity: MainActivity? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        plansViewModel =
                ViewModelProvider(this).get(PlansViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_plans, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        plansViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainActivity = activity as? MainActivity
        mainActivity?.supportActionBar?.show()
        mainActivity?.binding?.navView?.visibility = View.VISIBLE
    }

}