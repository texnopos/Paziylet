package uz.texnopos.paziylet.firebase

import com.google.firebase.firestore.FirebaseFirestore
import uz.texnopos.paziylet.data.model.Question
import uz.texnopos.paziylet.data.model.QuestionCategories
import java.util.*

class FirebaseHelper(private val db: FirebaseFirestore) {
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
    fun getQuestions(id: String,onSuccess: (list: List<Question>) -> Unit, onFailure: (msg: String?) -> Unit){
        db.collection("questions").whereEqualTo("categoryId", id).get()
            .addOnSuccessListener {
                onSuccess.invoke(it.documents.map { doc->
                    doc.toObject(Question::class.java)!!
                })
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }
    fun addQuestion(question:String,userId:String, onSuccess: (msg: String) -> Unit, onFailure: (msg: String?) -> Unit){
        val map:MutableMap<String,Any> = mutableMapOf()
        map["soraw"]=question
        map["userId"]=userId
        map["id"] = UUID.randomUUID().toString()
        db.collection("questions").document(map["id"].toString()).set(map)
            .addOnSuccessListener {
                onSuccess.invoke("Success")
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }
    fun getPrivateQuestion(userId: String,onSuccess: (list: List<Question>) -> Unit, onFailure: (msg: String?) -> Unit){
        db.collection("questions").whereEqualTo("userId", userId).get()
                .addOnSuccessListener {
                    onSuccess.invoke(it.documents.map { doc->
                        doc.toObject(Question::class.java)!!
                    })
                }
                .addOnFailureListener {
                    onFailure.invoke(it.localizedMessage)
                }
    }
}