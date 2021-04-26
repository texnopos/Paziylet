package uz.texnopos.paziylet.firebase

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import uz.texnopos.paziylet.data.model.QuestionCategories

class FirebaseHelper(private val context: Context,private val db: FirebaseFirestore) {
    fun getQuestionCategories(onSuccess: (list: List<QuestionCategories>)-> Unit, onFailure: (msg: String?)-> Unit){
        db.collection("questionCategories").get()
            .addOnSuccessListener {
                    onSuccess.invoke(it.documents.map {doc->
                        doc.toObject(QuestionCategories::class.java)!!
                    })
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }
}