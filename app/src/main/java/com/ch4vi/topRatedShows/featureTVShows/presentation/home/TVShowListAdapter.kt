package com.ch4vi.topRatedShows.featureTVShows.presentation.home

import android.annotation.SuppressLint
import android.content.res.Resources
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.ch4vi.topRatedShows.R
import com.ch4vi.topRatedShows.databinding.RowTvShowBinding
import com.ch4vi.topRatedShows.featureTVShows.domain.model.TVShow

class TVShowListAdapter(
    private val onItemClick: (item: TVShow) -> Unit = {}
) : RecyclerView.Adapter<TVShowListAdapter.TVShowViewHolder>() {

    private var listItems: List<TVShow> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun addShows(items: List<TVShow>) {
        listItems = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TVShowViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val bindingView = RowTvShowBinding.inflate(inflater, parent, false)
        return TVShowViewHolder(bindingView)
    }

    override fun onBindViewHolder(holder: TVShowViewHolder, position: Int) {
        holder.bind(listItems[position])
    }

    override fun getItemCount() = listItems.size

    inner class TVShowViewHolder(
        private val binding: RowTvShowBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: TVShow) {
            with(binding) {
                tvShowTitle.text = data.title
                tvShowImage.load(data.imageUrl) {
                    crossfade(true)
                    placeholder(R.drawable.shape_tv_show_placeholder)
                    error(R.drawable.shape_tv_show_error)
                    transformations(
                        RoundedCornersTransformation(
                            topLeft = 12.toPx,
                            topRight = 12.toPx
                        )
                    )
                }
            }
            itemView.setOnClickListener {
                onItemClick(data)
            }
        }

        private val Int.toPx
            get() = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                this.toFloat(),
                Resources.getSystem().displayMetrics
            )
    }
}
