package uz.texnopos.paziylet.ui.questions.myQuestions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_myquestions.view.*
import kotlinx.android.synthetic.main.item_question.view.tvSoraw
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.core.extentions.onClick
import uz.texnopos.paziylet.data.model.Question

class MyQuestionsAdapter(val context: Context) :
    RecyclerView.Adapter<MyQuestionsAdapter.MyQuestionsViewHolder>() {

    inner class MyQuestionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun populateModel(model: Question) {
            itemView.tvSoraw.text =
                HtmlCompat.fromHtml(model.soraw, HtmlCompat.FROM_HTML_MODE_LEGACY)
            if (model.rejected) {
                itemView.tvCheck.setBackgroundResource(R.drawable.background_no_answer)
                itemView.tvCheck.text = context.getString(R.string.answer_no)
            } else {
                if (model.juwap.isNotEmpty()) {
                    itemView.tvCheck.setBackgroundResource(R.drawable.background_yes_answer)
                    itemView.tvCheck.text = context.getString(R.string.answer_show)
                    itemView.onClick {
                        onClickQuestion.invoke(model.soraw, model.juwap)
                    }
                } else {
                    itemView.tvCheck.setBackgroundResource(R.drawable.background_waiting_answer)
                    itemView.tvCheck.text = context.getString(R.string.answer_waiting)
                }
            }
        }
    }

    private var onClickQuestion: (question: String, answer: String) -> Unit =
        { _, _ -> }

    fun setOnClickQuestion(onQuestionMoreClicked: (question: String, answer: String) -> Unit) {
        this.onClickQuestion = onQuestionMoreClicked
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