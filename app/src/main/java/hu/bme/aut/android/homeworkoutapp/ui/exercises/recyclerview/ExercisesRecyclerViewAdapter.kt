package hu.bme.aut.android.homeworkoutapp.ui.exercises.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hu.bme.aut.android.homeworkoutapp.R
import hu.bme.aut.android.homeworkoutapp.databinding.ExercisesRowBinding
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise

class ExercisesRecyclerViewAdapter(private val context: Context) : ListAdapter<UiExercise, ExercisesRecyclerViewAdapter.ViewHolder>(ExercisesDiffCallback) {

    interface ExerciseItemClickListener {
        fun onItemLongClick(exerciseRowBinding: ExercisesRowBinding?): Boolean
        fun onDeleteClick(exercise: UiExercise?): Boolean
        fun onStartClick(exercise: UiExercise?): Boolean
        fun onEditClick(exercise: UiExercise?): Boolean
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
                if(binding.motionLayoutExercisesRow.currentState == R.id.exercisesRowSceneStart) {
                    binding.motionLayoutExercisesRow.transitionToState(R.id.exercisesRowSceneEnd)
                }
                else {
                    binding.motionLayoutExercisesRow.transitionToState(R.id.exercisesRowSceneStart)
                }
            }

            itemView.setOnLongClickListener {
                exerciseClickListener?.onItemLongClick(binding)
                true
            }

            binding.ibDelete.setOnClickListener {
                exerciseClickListener?.onDeleteClick(binding.exercise)
            }

            binding.ibStart.setOnClickListener {
                exerciseClickListener?.onStartClick(binding.exercise)
            }

            binding.ibEdit.setOnClickListener {
                exerciseClickListener?.onEditClick(binding.exercise)
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