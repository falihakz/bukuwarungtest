package falih.example.bukuwarungtestapp.common

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import falih.example.bukuwarungtestapp.ui.common.adapter.BindableAdapter

object BindingAdapters {

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    @BindingAdapter("items")
    fun <SOME_LIST : List<Any>> setRecyclerViewProperties(recyclerView: RecyclerView, data: SOME_LIST?) {
        (recyclerView.adapter as BindableAdapter<*, Any>).setItems(data)
    }

    @JvmStatic
    @BindingAdapter("layoutRes")
    fun setRecyclerViewAdapterLayoutRes(recyclerView: RecyclerView, layoutRes: Int) {
        if (recyclerView.adapter is BindableAdapter<*, *>) {
            (recyclerView.adapter as BindableAdapter<*, *>).setLayoutRes(layoutRes)
        }
    }

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, url: String) {
        if(url.isNotEmpty()) {
            Glide.with(view.context)
                .load(url)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view)
        }
    }

}