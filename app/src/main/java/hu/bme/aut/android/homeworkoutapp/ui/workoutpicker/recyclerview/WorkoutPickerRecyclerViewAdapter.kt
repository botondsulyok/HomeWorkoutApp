package hu.bme.aut.android.homeworkoutapp.ui.workoutpicker.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.homeworkoutapp.databinding.WorkoutPickerRowBinding
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout

class WorkoutPickerRecyclerViewAdapter : ListAdapter<UiWorkout, WorkoutPickerRecyclerViewAdapter.ViewHolder>(WorkoutsDiffCallback) {

    interface WorkoutItemClickListener {
        fun onItemClick(workout: UiWorkout?): Boolean
        fun onItemLongClick(workout: UiWorkout?): Boolean
    }

    var workoutClickListener: WorkoutItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = WorkoutPickerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val workout = getItem(position)
        holder.bind(workout)
    }

    inner class ViewHolder(private val binding: WorkoutPickerRowBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                workoutClickListener?.onItemClick(binding.workout)
            }
            itemView.setOnLongClickListener {
                workoutClickListener?.onItemLongClick(binding.workout)
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