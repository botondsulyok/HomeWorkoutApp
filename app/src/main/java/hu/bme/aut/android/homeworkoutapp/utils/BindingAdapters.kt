package hu.bme.aut.android.homeworkoutapp.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import hu.bme.aut.android.homeworkoutapp.R

@BindingAdapter(value = ["setImageUrl"])
fun bindImageUrl(imageView: ImageView, imageUrl: String?) {
    if(!imageUrl.isNullOrEmpty()) {
        Glide
                .with(imageView.context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_baseline_image_placeholder_24)
                .dontTransform()
                .dontAnimate()
                .into(imageView)
    }
    else {
        imageView.visibility = View.GONE
    }
}