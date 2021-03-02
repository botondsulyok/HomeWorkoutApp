package hu.bme.aut.android.homeworkoutapp.ui.workouts.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.homeworkoutapp.databinding.WorkoutsRowBinding
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout

class WorkoutsRecyclerViewAdapter : ListAdapter<UiWorkout, WorkoutsRecyclerViewAdapter.ViewHolder>(WorkoutsDiffCallback) {

    interface WorkoutItemClickListener {
        fun onItemClick(workout: UiWorkout): Boolean
        fun onItemLongClick(workout: UiWorkout): Boolean
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

        private lateinit var workout: UiWorkout

        init {
            itemView.setOnClickListener {
                workoutClickListener?.onItemClick(workout)
            }
            itemView.setOnLongClickListener {
                workoutClickListener?.onItemLongClick(workout)
                true
            }
        }

        fun bind(w: UiWorkout) {
            workout = w
            binding.tvWorkoutNameWorkoutsRow.text = workout.name
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