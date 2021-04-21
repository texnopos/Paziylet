package uz.texnopos.paziylet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val id = UUID.randomUUID().toString()
        Log.d("myid", id)
        val db = FirebaseFirestore.getInstance()
        val data = mutableMapOf<String, Any>()
        data["question"] = "Alibek jillime?"
        data["answer"] = "Albette"
        data["createdDate"] = Timestamp.now()
        data["userId"] = "sdasdasfdfasda"
        data["id"] = id
        db.collection("questions").document(id).set(data).addOnSuccessListener {
            Toast.makeText(this, "success", Toast.LENGTH_LONG).show()
        }
    }
}