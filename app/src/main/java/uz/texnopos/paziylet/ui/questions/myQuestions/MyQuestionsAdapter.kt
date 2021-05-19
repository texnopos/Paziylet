package uz.texnopos.paziylet.ui.questions.myQuestions

import android.graphics.Color
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_myquestions.view.*
import kotlinx.android.synthetic.main.item_question.view.tvSoraw
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.core.extentions.onClick
import uz.texnopos.paziylet.data.model.Question

class MyQuestionsAdapter : RecyclerView.Adapter<MyQuestionsAdapter.MyQuestionsViewHolder>() {

    inner class MyQuestionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun populateModel(model: Question) {
            itemView.tvSoraw.text = Html.fromHtml(model.soraw)
            if (model.juwap.isNotEmpty()){
                itemView.tvCheck.setBackgroundResource(R.drawable.background_no_answer)
                itemView.tvCheck.text="Жуап жок"
                itemView.view.setBackgroundColor(Color.parseColor("#B94E4E"))
            }else{
                itemView.tvCheck.setBackgroundResource(R.drawable.background_yes_answer)
                itemView.tvCheck.text="Жуапты кориу"
                itemView.view.setBackgroundColor(Color.parseColor("#4EB9A0"))
                itemView.onClick {
                    onClickQuestion.invoke(model.soraw, model.juwap,model.id)
                }
            }
        }
    }

    private var onClickQuestion: (question: String, answer: String,id:String) -> Unit =
        { _,_, _ -> }

    fun setOnClickQuestion(onQuestionMoreClicked: ( question: String, answer: String,id:String) -> Unit) {
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