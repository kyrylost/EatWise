package kyrylost.apps.eatwise.firebase

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class FirebaseRepository @Inject constructor(): FirebaseDatabase() {

    fun register(email: String, password: String,
                 birthday: String, weight: Double,
                 sex: Int, work: Int,
                 trainings: Int, diet: Int,
                 onSuccess: () -> Unit,
                 onFailure: () -> Unit
    ) {

        CoroutineScope(Dispatchers.IO).launch {
            getFirebaseAuthInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    if (authInstance.currentUser != null) {
                        currentUserDb = getCurrentUserDatabase(authInstance.currentUser!!.uid)

                        Log.d("test1", currentUserDb.toString())
                        setAllFields(birthday, weight, sex, work, trainings, diet)

                        //Trigger callback to save user to ROOM
                        onSuccess()
                    }
                }
                .addOnFailureListener {
                    onFailure()
                }
        }

    }

    fun login(email: String, password: String,
              onSuccess: () -> Unit) {

        CoroutineScope(Dispatchers.IO).launch {
            getFirebaseAuthInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    if (authInstance.currentUser != null) {
                        currentUserDb = getCurrentUserDatabase(authInstance.currentUser!!.uid)
                        onSuccess()
                    }
                }
        }

    }

    fun loginAndGetData(email: String, password: String,
                        onSuccess: (HashMap<*,*>) -> Unit, onFailure: () -> Unit) {

        CoroutineScope(Dispatchers.IO).launch {
            getFirebaseAuthInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    if (authInstance.currentUser != null) {
                        currentUserDb = getCurrentUserDatabase(authInstance.currentUser!!.uid)
                        currentUserDb.get().addOnSuccessListener { userDataSnapshot ->
                            val userData = userDataSnapshot.value as HashMap<*, *>
                            onSuccess(userData)
                        }
                            .addOnFailureListener {
                                onFailure()
                            }
                    }
                }
        }

    }

    fun setAllFields(birthday: String, weight: Double,
                     sex: Int, work: Int,
                     trainings: Int, diet: Int
    ) {
        Log.d("test", currentUserDb.toString())
        setBirthDate(birthday)
        setWeight(weight)
        setSex(sex)
        setWork(work)
        setTrainings(trainings)
        setDiet(diet)
    }

    private fun setBirthDate(birthday: String) {
        CoroutineScope(Dispatchers.IO).launch {
            currentUserDb.child("birthDate").setValue(birthday)
        }
    }

    private fun setWeight(weight: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            currentUserDb.child("weight").setValue(weight)
        }
    }

    private fun setSex(sex: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            currentUserDb.child("sex").setValue(sex)
        }
    }

    private fun setWork(work: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            currentUserDb.child("work").setValue(work)
        }
    }

    private fun setTrainings(trainings: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            currentUserDb.child("trainings").setValue(trainings)
        }
    }

    private fun setDiet(diet: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            currentUserDb.child("diet").setValue(diet)
        }
    }

}