package hu.bme.aut.android.homeworkoutapp.ui.plans.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.homeworkoutapp.databinding.PlannedWorkoutRowBinding
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout

class PlannedWorkoutRecyclerViewAdapter :
    ListAdapter<UiWorkout, PlannedWorkoutRecyclerViewAdapter.ViewHolder>(WorkoutsDiffCallback) {

    interface PlannedWorkoutItemClickListener {
        fun onItemClick(workout: UiWorkout?): Boolean
        fun onDeleteClick(workout: UiWorkout?): Boolean
    }

    var plannedWorkoutClickListener: PlannedWorkoutItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PlannedWorkoutRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val workout = getItem(position)
        holder.bind(workout)
    }

    inner class ViewHolder(private val binding: PlannedWorkoutRowBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                plannedWorkoutClickListener?.onItemClick(binding.workout)
            }
            itemView.setOnLongClickListener {
                plannedWorkoutClickListener?.onDeleteClick(binding.workout)
                true
            }
        }

        fun bind(workout: UiWorkout) {
            binding.workout = workout
        }

    }

    object WorkoutsDiffCallback: DiffUtil.ItemCallback<UiWorkout>() {
        override fun areItemsTheSame(oldItem: UiWorkout, newItem: UiWorkout): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UiWorkout, newItem: UiWorkout): Boolean {
            return oldItem == newItem
        }
    }

}