package hu.bme.aut.android.homeworkoutapp.ui.exercises.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.homeworkoutapp.databinding.ExercisesRowBinding
import hu.bme.aut.android.homeworkoutapp.databinding.WorkoutsRowBinding
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise

class ExercisesRecyclerViewAdapter : ListAdapter<UiExercise, ExercisesRecyclerViewAdapter.ViewHolder>(ExercisesDiffCallback) {

    interface ExerciseItemClickListener {
        fun onItemClick(exercise: UiExercise?): Boolean
        fun onItemLongClick(exercise: UiExercise?): Boolean
    }

    var exerciseClickListener: ExerciseItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ExercisesRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ExercisesRowBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                exerciseClickListener?.onItemClick(binding.exercise)
            }
            itemView.setOnLongClickListener {
                exerciseClickListener?.onItemLongClick(binding.exercise)
                true
            }
        }

        fun bind(exercise: UiExercise) {
            binding.exercise = exercise
        }

    }

    object ExercisesDiffCallback: DiffUtil.ItemCallback<UiExercise>() {
        override fun areItemsTheSame(oldItem: UiExercise, newItem: UiExercise): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UiExercise, newItem: UiExercise): Boolean {
            return oldItem == newItem
        }
    }

}