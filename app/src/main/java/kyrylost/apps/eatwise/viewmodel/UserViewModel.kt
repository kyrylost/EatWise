package kyrylost.apps.eatwise.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kyrylost.apps.eatwise.firebase.FirebaseRepository
import kyrylost.apps.eatwise.model.User
import kyrylost.apps.eatwise.room.UserRepository
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private var firebaseUserRepositoryIsAccessible = false

    val userAuthAndCreatingSuccessMutableLiveData = MutableLiveData<Boolean>()
    val userAuthAndCreatingFailureMutableLiveData = MutableLiveData<Boolean>()

    val emailAndPasswordSuccessfullySetted = MutableLiveData<Boolean>()
    val emailAndPasswordSetError = MutableLiveData<String>()

    val secondScreenFieldsSuccessfullySetted = MutableLiveData<Boolean>()
    val secondScreenFieldsSetError = MutableLiveData<String>()

    val thirdScreenFieldsSuccessfullySetted = MutableLiveData<Boolean>()
    val thirdScreenFieldsSetError = MutableLiveData<String>()

    val fourthScreenFieldsSetError = MutableLiveData<String>()

    private var userEmail = ""
    private var userPassword = ""
    private var userBirthday = ""
    private var userWeight = 0.0
    private var userSex = 0
    private var userWork = 0
    private var userTrainings = 0
    private var userDiet = 0

    fun getEmail(): String {
        return userEmail
    }
    fun getPassword(): String {
        return userPassword
    }

    fun setEmailAndPassword(email: String, password: String) {
        if (email.isEmpty()) {
            emailAndPasswordSetError.value = "Input an Email!"
            return
        }
        if (password.isEmpty()) {
            emailAndPasswordSetError.value = "Input a Password!"
            return
        }

        userEmail = email
        userPassword = password
        emailAndPasswordSuccessfullySetted.value = true
    }

    fun setBirthdayAndWeight(birthday: String, weight: String) {
        if (birthday.length != 10) {
            secondScreenFieldsSetError.value = "Input Your birthday date!"
            return
        } else {
            val dateValues = birthday.split("/")
            if(!isDateValid(dateValues[2].toInt(), dateValues[1].toInt(), dateValues[0].toInt())) {
                secondScreenFieldsSetError.value = "Input correct birthday date!"
                return
            }
        }

        if (weight.isEmpty()) {
            secondScreenFieldsSetError.value = "Input Your weight!"
            return
        }

        userBirthday = birthday
        userWeight = weight.toDouble()
        secondScreenFieldsSuccessfullySetted.value = true
    }

    fun setSexAndWorkAndTrainings(sexId: Int, workId: Int, trainings: Int,
                                  maleButtonId: Int, femaleButtonId: Int,
                                  activeButtonId: Int, sedentaryButtonId: Int) {
        when (sexId) {
            -1 -> {
                thirdScreenFieldsSetError.value = "Select Your sex!"
                return
            }

            maleButtonId -> userSex = 0
            femaleButtonId -> userSex = 1
        }

        when (workId) {
            -1 -> {
                thirdScreenFieldsSetError.value = "Select Your work type!"
                return
            }

            activeButtonId -> userWork = 0
            sedentaryButtonId -> userWork = 1
        }

        userTrainings = trainings
        thirdScreenFieldsSuccessfullySetted.value = true
    }

    fun setDiet(diet: String) {
        if (diet.isEmpty()) {
            fourthScreenFieldsSetError.value = "Select diets from the list!"
            return
        }

        when (diet) {
            "maintenance" -> userDiet = 0
            "gaining" -> userDiet = 1
            "losing" -> userDiet = 2
            "cutting" -> userDiet = 3
        }

        registerUser()
    }


    private fun buildUser() : User {
        return User(
            1, userBirthday, userWeight,
            userSex, userWork,
            userTrainings, userDiet
        )
    }

    // remove log
    private fun buildFirebaseUser(userData: HashMap<*, *>) : User {
        val user = User(
            1, userData["birthDate"] as String,
            userData["weight"].anyToDouble(), userData["sex"].anyToInt(),
            userData["work"].anyToInt(), userData["trainings"].anyToInt(),
            userData["diet"].anyToInt()
        )
        Log.d("FirebaseUserData", user.toString())
        return user
    }

    private fun createUser(user: User = buildUser()) {
        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                userRepository.createUser(user)
                userAuthAndCreatingSuccessMutableLiveData.postValue(true)
            }
        }
    }

    private fun registerUser() {
        viewModelScope.launch {
            firebaseRepository.register(
                userEmail, userPassword,
                userBirthday, userWeight,
                userSex, userWork,
                userTrainings, userDiet,
                onSuccess = {
                    createUser()
                    firebaseUserRepositoryIsAccessible = true
                },
                onFailure = {userAuthAndCreatingFailureMutableLiveData.postValue(true)}
            )
        }
    }

    fun login() {
        viewModelScope.launch {
            val user = withContext(Dispatchers.IO) {
                userRepository.getUser()
            }
            if (user != null) {
                userBirthday = user.birthDate
                userWeight = user.weight
                userSex = user.sex
                userWork = user.work
                userTrainings = user.trainings
                userDiet = user.diet

                firebaseRepository.login(
                    userEmail, userPassword,
                    onSuccess = {
                        firebaseUserRepositoryIsAccessible = true
                        firebaseRepository.setAllFields(
                            userBirthday, userWeight,
                            userSex, userWork,
                            userTrainings, userDiet
                        )
                    }
                )
            }
            else {
                firebaseRepository.loginAndGetData(
                    userEmail, userPassword,
                    onSuccess = {
                        firebaseUserData -> createUser(buildFirebaseUser(firebaseUserData))
                        firebaseUserRepositoryIsAccessible = true
                    },
                    onFailure = { userAuthAndCreatingFailureMutableLiveData.postValue(true) }
                )
            }
        }

    }

}

private fun Any?.anyToDouble(): Double {
    return when (this){
        is Long -> this.toDouble()
        is Double -> this
        else -> 0.0
    }
}

private fun Any?.anyToInt(): Int {
    return when (this) {
        is Long -> this.toInt()
        else -> 0
    }
}

private fun isDateValid(year: Int, month: Int, day: Int): Boolean {
    return try {
        LocalDate.of(year, month, day)
        true
    } catch (e: Exception) {
        false
    }
}