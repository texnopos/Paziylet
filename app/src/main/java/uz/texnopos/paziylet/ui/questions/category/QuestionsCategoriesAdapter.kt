package uz.texnopos.paziylet.ui.questions.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_questions_category.view.*
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.data.model.QuestionCategories

class QuestionsCategoriesAdapter :
    RecyclerView.Adapter<QuestionsCategoriesAdapter.QuestionsCategoriesViewHolder>() {

    var models: List<QuestionCategories> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class QuestionsCategoriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun populateModel(model: QuestionCategories) {
            itemView.tvCategoryName.text = model.name
            itemView.setOnClickListener {
                onQuestionCategoryItemClicked.invoke(
                    model.id,
                    model.name
                )
            }
        }

    }

    private var onQuestionCategoryItemClicked: (questionCategoryItemClicked: String, categoryName: String) -> Unit =
        { id, name -> }

    fun setOnQuestionCategoryItemClicked(onQuestionCategoryItemClicked: (questionCategoryItemClicked: String, categoryName: String) -> Unit) {
        this.onQuestionCategoryItemClicked = onQuestionCategoryItemClicked
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuestionsCategoriesViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_questions_category, parent, false)
        return QuestionsCategoriesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: QuestionsCategoriesViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    override fun getItemCount(): Int = models.size
}