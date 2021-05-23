package uz.texnopos.paziylet.ui.questions.question

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_question.view.*
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.data.model.Question

class QuestionAdapter : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    var models: List<Question> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun populateModel(model: Question) {
            itemView.tvSoraw.text = Html.fromHtml(model.soraw)
            itemView.setOnClickListener {
                onQuestionMoreClicked.invoke(model.id, model.soraw, model.juwap)
            }

        }
    }

    private var onQuestionMoreClicked: (questionMoreClicked: String, question: String, answer: String) -> Unit =
        { _, _, _ -> }

    fun setOnQuestionMoreClicked(onQuestionMoreClicked: (questionMoreClicked: String, question: String, answer: String) -> Unit) {
        this.onQuestionMoreClicked = onQuestionMoreClicked
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuestionAdapter.QuestionViewHolder {
        val itemView =
              LayoutInflater.from(parent.context).inflate(R.layout.item_question, parent, false)
        return QuestionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: QuestionAdapter.QuestionViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    override fun getItemCount(): Int = models.size
}