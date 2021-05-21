package uz.texnopos.paziylet.ui.categories.definitecategory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_definite_category.view.*
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.data.model.Patwa
import java.text.SimpleDateFormat

class DefiniteCategoryAdapter:RecyclerView.Adapter<DefiniteCategoryAdapter.DefiniteCategoryViewHolder>() {

    var models:List<Patwa> = listOf()
    set(value) {
        field=value
        notifyDataSetChanged()
    }

    inner class DefiniteCategoryViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun populateModel(model: Patwa){
            itemView.tvTitle.text=model.title
            val sdf = SimpleDateFormat("dd.MM.yyyy")
            val date=sdf.format(model.createdAt*1000).toString()
            itemView.tvDate.text=date
            itemView.tvViews.text="views ${model.views}"
            Glide.with(itemView)
                .load(model.image)
                .into(itemView.ivImg)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefiniteCategoryViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(
            R.layout.item_definite_category,
            parent,
            false
        )
        return DefiniteCategoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DefiniteCategoryViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    override fun getItemCount() = models.size
}