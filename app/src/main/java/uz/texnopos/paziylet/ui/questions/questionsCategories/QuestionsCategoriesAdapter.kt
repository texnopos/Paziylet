package uz.texnopos.paziylet.ui.questions.questionsCategories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_questions_categories.view.*
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.data.model.QuestionCategories

class QuestionsCategoriesAdapter: RecyclerView.Adapter<QuestionsCategoriesAdapter.QuestionsCategoriesViewHolder>() {

    var models: List<QuestionCategories> = listOf()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    inner class QuestionsCategoriesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun populateModel(model: QuestionCategories){
            itemView.tvCategoryName.text = model.name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionsCategoriesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_questions_categories,parent,false)
        return QuestionsCategoriesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: QuestionsCategoriesViewHolder, position: Int) {
       holder.populateModel(models[position])
    }

    override fun getItemCount(): Int  = models.size
}