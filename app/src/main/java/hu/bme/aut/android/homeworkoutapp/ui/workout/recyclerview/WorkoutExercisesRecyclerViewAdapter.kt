package hu.bme.aut.android.homeworkoutapp.ui.workout.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.homeworkoutapp.databinding.WorkoutExercisesRowBinding
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise

class WorkoutExercisesRecyclerViewAdapter(private val context: Context) :
    ListAdapter<UiExercise, WorkoutExercisesRecyclerViewAdapter.ViewHolder>(ExercisesDiffCallback) {

    interface ExerciseItemClickListener {
        fun onItemClick(exercise: UiExercise?): Boolean
        fun onItemLongClick(exercise: UiExercise?): Boolean
        fun onDeleteClick(exercise: UiExercise?): Boolean
        fun onStartClick(exercise: UiExercise?): Boolean
    }

    var exerciseClickListener: ExerciseItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            WorkoutExercisesRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: WorkoutExercisesRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                exerciseClickListener?.onItemClick(binding.exercise)
            }

            itemView.setOnLongClickListener {
                exerciseClickListener?.onItemLongClick(binding.exercise)
                true
            }

            binding.ibDelete.setOnClickListener {
                exerciseClickListener?.onDeleteClick(binding.exercise)
            }

            binding.ibStart.setOnClickListener {
                exerciseClickListener?.onStartClick(binding.exercise)
            }

        }

        fun bind(exercise: UiExercise) {
            binding.exercise = exercise
        }

    }

    object ExercisesDiffCallback : DiffUtil.ItemCallback<UiExercise>() {
        override fun areItemsTheSame(oldItem: UiExercise, newItem: UiExercise): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UiExercise, newItem: UiExercise): Boolean {
            return oldItem == newItem
        }
    }

}