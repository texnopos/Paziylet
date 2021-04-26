package uz.texnopos.paziylet.ui.questions.viewPager1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_view_pager1.view.*
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.data.model.QuestionCategories

class ViewPager1Adapter: RecyclerView.Adapter<ViewPager1Adapter.ViewPager1ViewHolder>() {

    var models: List<QuestionCategories> = listOf()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    inner class ViewPager1ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun populateModel(model: QuestionCategories){
            itemView.tvCategoryName.text = model.name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPager1ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_view_pager1,parent,false)
        return ViewPager1ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewPager1ViewHolder, position: Int) {
       holder.populateModel(models[position])
    }

    override fun getItemCount(): Int  = models.size
}