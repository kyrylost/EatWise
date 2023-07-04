package kyrylost.apps.eatwise.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

abstract class FirebaseDatabase {

    lateinit var authInstance: FirebaseAuth
    lateinit var currentUserDb: DatabaseReference

    private fun getFirebaseInstance(): FirebaseDatabase {
        return FirebaseDatabase.getInstance(
            "https://eatwise-0-default-rtdb.europe-west1.firebasedatabase.app"
        )
    }

    private fun getDatabaseReference(): DatabaseReference {
        return getFirebaseInstance()
            .reference
            .child("users")
    }

    fun getFirebaseAuthInstance(): FirebaseAuth {
        authInstance = FirebaseAuth.getInstance()
        return authInstance
    }

    fun getCurrentUserDatabase(uid: String): DatabaseReference {
        return getDatabaseReference()
            .child(uid)
    }

}