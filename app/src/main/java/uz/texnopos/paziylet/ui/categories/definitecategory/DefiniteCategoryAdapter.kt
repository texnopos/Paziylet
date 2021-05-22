package uz.texnopos.paziylet.ui.categories.definitecategory

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_definite_category.view.*
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.core.extentions.inflate
import uz.texnopos.paziylet.core.extentions.onClick
import uz.texnopos.paziylet.data.model.Patwa
import java.text.SimpleDateFormat

class DefiniteCategoryAdapter :
    RecyclerView.Adapter<DefiniteCategoryAdapter.DefiniteCategoryViewHolder>() {

    var models: List<Patwa> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class DefiniteCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun populateModel(model: Patwa) {
            itemView.tvTitle.text = model.title
            val sdf = SimpleDateFormat("dd.MM.yyyy")
            val date = sdf.format(model.createdAt * 1000).toString()
            itemView.tvDate.text = date
            itemView.tvViews.text = "views ${model.views}"
            itemView.onClick {
                onItemClick.invoke(model.text, model.title)
            }

            Glide.with(itemView)
                .load(model.image)
                .centerCrop()
                .into(itemView.ivImg)
        }
    }

    private var onItemClick: (text: String, title: String) -> Unit = { _: String, _: String -> }
    fun onItemClickListener(onItemClick: (text: String, title: String) -> Unit) {
        this.onItemClick = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefiniteCategoryViewHolder {
        val itemView = parent.inflate(R.layout.item_definite_category)
        return DefiniteCategoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DefiniteCategoryViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    override fun getItemCount() = models.size
}