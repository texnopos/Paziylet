package uz.texnopos.paziylet.data.firebase

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.firestore.FirebaseFirestore
import uz.texnopos.paziylet.data.model.Question
import uz.texnopos.paziylet.data.model.QuestionCategories

class FirebaseHelper(
    private val context: Context,
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    fun getQuestionCategories(
        onSuccess: (list: List<QuestionCategories>) -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        db.collection("questionCategories").get()
            .addOnSuccessListener {
                onSuccess.invoke(it.documents.map { doc ->
                    doc.toObject(QuestionCategories::class.java)!!
                })
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }

    fun getQuestions(
        id: String,
        onSuccess: (list: List<Question>) -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        db.collection("questions").whereEqualTo("categoryId", id).get()
            .addOnSuccessListener {
                onSuccess.invoke(it.documents.map { doc ->
                    doc.toObject(Question::class.java)!!
                })
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }

    fun auth(
        credential: PhoneAuthCredential,
        onSuccess: (boolean: Boolean) -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess.invoke(true)
            } else {
                onFailure.invoke(it.exception?.message)
            }
        }
    }

}