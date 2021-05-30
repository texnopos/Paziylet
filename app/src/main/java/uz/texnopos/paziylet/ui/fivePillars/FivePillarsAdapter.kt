package uz.texnopos.paziylet.ui.fivePillars

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_category.view.*
import kotlinx.android.synthetic.main.item_definite_category.view.*
import kotlinx.android.synthetic.main.pillars_item.view.*
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.core.extentions.inflate
import uz.texnopos.paziylet.core.extentions.onClick
import uz.texnopos.paziylet.data.model.FivePillars
import java.text.SimpleDateFormat
import com.bumptech.glide.Glide


class FivePillarsAdapter : RecyclerView.Adapter<FivePillarsAdapter.PillarsViewHolder>()   {

    var models: List<FivePillars> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    private var onItemClick: (text: String, title: String) -> Unit = { _: String, _: String -> }
    fun onItemClickListener(onItemClick: (text: String, title: String) -> Unit) {
        this.onItemClick = onItemClick
    }

    inner class PillarsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun populateModel(model: FivePillars) {
            val sdf = SimpleDateFormat("dd.MM.yyyy")
            val date = sdf.format(model.createdAt * 1000).toString()
            itemView.tvTitlePillars.text = model.title
            itemView.tvDatePillars.text = date
            itemView.tvViewsPillars.text = "views ${model.views}"
            itemView.onClick {
                onItemClick.invoke(model.content, model.title)
            }
            Glide.with(itemView)
                .load(model.image)
                .centerCrop()
                .into(itemView.ivImgPillars)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PillarsViewHolder {
        val itemView = parent.inflate(R.layout.pillars_item)
        return PillarsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PillarsViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    override fun getItemCount(): Int = models.size
}