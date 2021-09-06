package uz.texnopos.paziylet.ui.categories

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_category.view.*
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.core.extentions.inflate
import uz.texnopos.paziylet.core.extentions.onClick
import uz.texnopos.paziylet.data.model.QuestionCategories
import java.io.IOException

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    var models: List<QuestionCategories> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun populateModel(model: QuestionCategories) {
            itemView.tvCategory.text = model.nameCyr
            itemView.onClick {
                onItemClick.invoke(model.nameCyr)
            }
        }
    }


    private var onItemClick: (path: String) -> Unit = {}
    fun onItemClickListener(onItemClick: (path: String) -> Unit) {
        this.onItemClick = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView = parent.inflate(R.layout.item_category)
        return CategoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    override fun getItemCount(): Int = models.size
}