package uz.texnopos.paziylet.ui.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.item_news.view.*
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.core.extentions.onClick
import uz.texnopos.paziylet.data.model.News
import uz.texnopos.paziylet.data.model.UpdatedNews

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    var models: List<News> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun populateModel(news: News) {
            itemView.tvTitle.text = news.titleCyr
            itemView.tvViews.text = news.views.toString()
            itemView.tvCategory.text = news.category
            news.views=news.views+1
            val gsonPretty = GsonBuilder().setPrettyPrinting().create()
            itemView.onClick {
                val gsonString = gsonPretty.toJson(news)
                onItemClicked.invoke(gsonString)
            }
            Glide.with(itemView.context)
                .load(news.img)
                .centerCrop()
                .into(itemView.ivNewsImg)
        }
    }

    private var onItemClicked: (objectToJson: String) -> Unit = {}
    fun onItemClicked(onItemClick: (objectToJson: String) -> Unit) {
        this.onItemClicked = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    override fun getItemCount(): Int = models.size
}