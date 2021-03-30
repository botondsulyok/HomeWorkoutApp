package hu.bme.aut.android.homeworkoutapp.domain.interactors

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import com.bumptech.glide.Glide
import hu.bme.aut.android.homeworkoutapp.data.Result
import hu.bme.aut.android.homeworkoutapp.data.firebase.FirebaseDataSource
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainExercise
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainNewExercise
import hu.bme.aut.android.homeworkoutapp.utils.ImageCompressor
import java.io.ByteArrayOutputStream
import java.io.FileDescriptor
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ExercisesInteractor @Inject constructor(
        private val firebaseDataSource: FirebaseDataSource,
        private val imageCompressor: ImageCompressor
) {

    suspend fun getExercises(): Result<List<DomainExercise>, Exception> {
        return firebaseDataSource.getExercises()
    }

    suspend fun addExercise(exercise: DomainNewExercise): Result<Unit, Exception> {
        return if(exercise.videoUri != null) {
            val imageInBytes = imageCompressor.compressImage(exercise.videoUri)
            firebaseDataSource.addExercise(exercise.copy(thumbnailInBytes = imageInBytes))
        } else {
            firebaseDataSource.addExercise(exercise)
        }
    }

    suspend fun deleteExercise(exercise: DomainExercise): Result<Unit, Exception> {
        return firebaseDataSource.deleteExercise(exercise)
    }
    suspend fun updateExercise(exercise: DomainExercise): Result<Unit, Exception> {
        return firebaseDataSource.updateExercise(exercise)
    }



}