package uz.texnopos.paziylet.data.model

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.experimental.property.inject

class FirebaseHelper(private val context: Context,private val db: FirebaseFirestore) {
    fun getQuestionCategories(onSuccess: (list: List<QuestionCategories>)-> Unit,onFailure: (msg: String?)-> Unit){
        db.collection("questionCategories").get()
            .addOnSuccessListener {
                if (it.documents.isNotEmpty()){
                    onSuccess.invoke(it.documents.map {doc->
                        doc.toObject(QuestionCategories::class.java)!!
                    })
                }else{
                    onFailure.invoke("qatelik")
                }
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }
}