package uz.texnopos.paziylet.ui.questions.myQuestions

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_question.view.*
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.data.model.Question

class MyQuestionsAdapter : RecyclerView.Adapter<MyQuestionsAdapter.MyQuestionsViewHolder>() {

    inner class MyQuestionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun populateModel(model: Question) {
            itemView.tvSoraw.text = Html.fromHtml(model.soraw)
        }
    }

    var models: List<Question> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyQuestionsViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_myquestions, parent, false)
        return MyQuestionsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyQuestionsViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    override fun getItemCount() = models.size
}