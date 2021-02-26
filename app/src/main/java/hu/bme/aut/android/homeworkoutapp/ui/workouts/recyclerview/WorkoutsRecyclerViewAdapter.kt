package hu.bme.aut.android.homeworkoutapp.ui.workouts.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginTop
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.homeworkoutapp.databinding.WorkoutsRowBinding
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.Workout

class WorkoutsRecyclerViewAdapter : ListAdapter<Workout, WorkoutsRecyclerViewAdapter.ViewHolder>(WorkoutsDiffCallback) {

    interface WorkoutItemClickListener {
        fun onItemClick(position: Int, view: View, workout: Workout?, viewHolder: WorkoutsRecyclerViewAdapter.ViewHolder): Boolean
    }

    var workoutClickListener: WorkoutItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = WorkoutsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val workout = getItem(position)
        holder.bind(workout)
    }

    inner class ViewHolder(private val binding: WorkoutsRowBinding) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var workout: Workout

        init {
            itemView.setOnClickListener {view ->
                workoutClickListener?.onItemClick(adapterPosition, view, workout, this)
                true
            }
        }

        fun bind(w: Workout) {
            workout = w
            binding.tvWorkoutNameWorkoutsRow.text = workout.name
        }

    }

    object WorkoutsDiffCallback: DiffUtil.ItemCallback<Workout>() {
        override fun areItemsTheSame(oldItem: Workout, newItem: Workout): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Workout, newItem: Workout): Boolean {
            return oldItem == newItem
        }
    }

}