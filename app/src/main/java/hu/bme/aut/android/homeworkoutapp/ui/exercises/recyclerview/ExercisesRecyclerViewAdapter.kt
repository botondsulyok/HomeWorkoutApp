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
        fun onItemClick(exercise: UiExercise): Boolean
        fun onItemLongClick(exercise: UiExercise): Boolean
    }

    var exerciseClickListener: ExerciseItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ExercisesRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exercise = getItem(position)
        holder.bind(exercise)
    }

    inner class ViewHolder(private val binding: ExercisesRowBinding) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var exercise: UiExercise

        init {
            itemView.setOnClickListener {
                exerciseClickListener?.onItemClick(exercise)
            }
            itemView.setOnLongClickListener {
                exerciseClickListener?.onItemLongClick(exercise)
                true
            }
        }

        fun bind(e: UiExercise) {
            exercise = e
            binding.tvExerciseName.text = exercise.name
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