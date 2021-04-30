package uz.texnopos.paziylet.ui.questions.question

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.paziylet.data.model.Question

class QuestionAdapter: RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    var models: List<Question> = listOf()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    inner class QuestionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun populateModel(model: Question){
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuestionAdapter.QuestionViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: QuestionAdapter.QuestionViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}