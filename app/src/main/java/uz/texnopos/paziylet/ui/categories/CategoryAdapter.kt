package uz.texnopos.paziylet.ui.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_category.view.*
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.core.extentions.onClick
import uz.texnopos.paziylet.data.model.QuestionCategories

class CategoryAdapter:RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    var models:List<QuestionCategories> = listOf()
    set(value){
        field=value
        notifyDataSetChanged()
    }

    inner class CategoryViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun populateModel(model:QuestionCategories){
            itemView.tvCategory.text=model.name
            itemView.onClick {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.item_category,parent,false)
        return CategoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    override fun getItemCount(): Int =models.size
}