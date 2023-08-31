package kyrylost.apps.eatwise.viewmodel

import androidx.databinding.ObservableDouble
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kyrylost.apps.eatwise.SingleLiveEvent
import kyrylost.apps.eatwise.firebase.FirebaseRepository
import kyrylost.apps.eatwise.model.User
import kyrylost.apps.eatwise.room.user.UserRepository
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private var firebaseUserRepositoryIsAccessible = false

    val userAuthAndCreatingSuccessMutableLiveData = SingleLiveEvent<Boolean>()
    val userAuthAndCreatingFailureMutableLiveData = SingleLiveEvent<Boolean>()

    val emailAndPasswordSuccessfullySetted = SingleLiveEvent<Boolean>()
    val emailAndPasswordSetError = SingleLiveEvent<String>()

    val secondScreenFieldsSuccessfullySetted = SingleLiveEvent<Boolean>()
    val secondScreenFieldsSetError = SingleLiveEvent<String>()

    val thirdScreenFieldsSuccessfullySetted = SingleLiveEvent<Boolean>()
    val thirdScreenFieldsSetError = SingleLiveEvent<String>()

    val fourthScreenFieldsSetError = SingleLiveEvent<String>()

    val fieldUpdateFailure = SingleLiveEvent<String>()


    private var userEmail = ""
    private var userPassword = ""
    private var userBirthday = ""
    var userWeight = ObservableDouble()
    var userSex = ObservableInt()
    var userWork = ObservableInt()
    var userTrainings = ObservableInt()
    var userDiet = ObservableInt()


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

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailAndPasswordSetError.value = "Incorrect Email format!"
            return
        }


        if (password.isEmpty()) {
            emailAndPasswordSetError.value = "Input a Password!"
            return
        }

        if (password.length < 8) {
            emailAndPasswordSetError.value = "Minimum password length is 8!"
            return
        }

        val lower = "[a-z]".toRegex()
        val upper = "[A-Z]".toRegex()
        val digits = "\\d".toRegex()

        val lowersIn = lower.containsMatchIn(password)
        val uppersIn = upper.containsMatchIn(password)
        val digitsIn = digits.containsMatchIn(password)

        if (!lowersIn || !uppersIn || !digitsIn) {
            var invalidPasswordMessage = "Password doesn't contain "

            invalidPasswordMessage += if(!lowersIn) "lowercase letter, " else ""
            invalidPasswordMessage += if(!uppersIn) "uppercase letter, " else ""
            invalidPasswordMessage += if(!digitsIn) "digit" else ""

            emailAndPasswordSetError.value = invalidPasswordMessage
                .replace(Regex(", $"), "")
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
        userWeight.set(weight.toDouble())
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

            maleButtonId -> userSex.set(0)
            femaleButtonId -> userSex.set(1)
        }

        when (workId) {
            -1 -> {
                thirdScreenFieldsSetError.value = "Select Your work type!"
                return
            }

            activeButtonId -> userWork.set(0)
            sedentaryButtonId -> userWork.set(1)
        }

        userTrainings.set(trainings)
        thirdScreenFieldsSuccessfullySetted.value = true
    }

    fun setDiet(diet: String) {
        if (diet.isEmpty()) {
            fourthScreenFieldsSetError.value = "Select diets from the list!"
            return
        }

        when (diet) {
            "maintenance" -> userDiet.set(0)
            "gaining" -> userDiet.set(1)
            "losing" -> userDiet.set(2)
            "cutting" -> userDiet.set(3)
        }

        registerUser()
    }


    private fun buildUser() : User {
        return User(
            1, userBirthday, userWeight.get(),
            userSex.get(), userWork.get(),
            userTrainings.get(), userDiet.get()
        )
    }

    private fun createUser(user: User = buildUser()) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.createUser(user)
            userAuthAndCreatingSuccessMutableLiveData.postValue(true)
        }
    }


    private fun registerUser() {
        viewModelScope.launch {
            firebaseRepository.register(
                userEmail, userPassword,
                userBirthday, userWeight.get(),
                userSex.get(), userWork.get(),
                userTrainings.get(), userDiet.get(),
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
                userWeight.set(user.weight)
                userSex.set(user.sex)
                userWork.set(user.work)
                userTrainings.set(user.trainings)
                userDiet.set(user.diet)

                firebaseRepository.login(
                    userEmail, userPassword,
                    onSuccess = {
                        firebaseUserRepositoryIsAccessible = true
                        firebaseRepository.setAllFields(
                            userBirthday, userWeight.get(),
                            userSex.get(), userWork.get(),
                            userTrainings.get(), userDiet.get()
                        )
                    }
                )
            }
            else {
                firebaseRepository.loginAndGetData(
                    userEmail, userPassword,
                    onSuccess = { firebaseUserData ->
                        userBirthday = firebaseUserData["birthDate"] as String
                        userWeight.set(firebaseUserData["weight"].anyToDouble())
                        userSex.set(firebaseUserData["sex"].anyToInt())
                        userWork.set(firebaseUserData["work"].anyToInt())
                        userTrainings.set(firebaseUserData["trainings"].anyToInt())
                        userDiet.set(firebaseUserData["diet"].anyToInt())

                        createUser(buildUser())
                        firebaseUserRepositoryIsAccessible = true
                    },
                    onFailure = { userAuthAndCreatingFailureMutableLiveData.postValue(true) }
                )
            }
        }

    }


    fun updateUserData(newWeightString: String, newSex: Int,
                       newWork: Int, newTrainings: Int,
                       newDiet: Int) {

        viewModelScope.launch(Dispatchers.IO) {

            if (newWeightString == "") {
                fieldUpdateFailure.postValue("Please, input Your weight!")
                return@launch
            }

            val newWeight = newWeightString.replace(",", ".").toDouble()
            val failMessage = "%s field can't be updated"

            if(firebaseUserRepositoryIsAccessible) {
                if (newWeight != userWeight.get()) {
                    firebaseRepository.updateWeight(
                        newWeight,
                        onFailure = {
                            userWeight.notifyChange()
                            fieldUpdateFailure.postValue(String.format(failMessage, "Weight"))
                        }
                    )

                    userRepository.updateWeight(newWeight)
                    userWeight.set(newWeight)
                }

                if (newSex != userSex.get()) {
                    firebaseRepository.updateSex(
                        newSex,
                        onFailure = {
                            userSex.notifyChange()
                            fieldUpdateFailure.postValue(String.format(failMessage, "Sex"))
                        }
                    )

                    userRepository.updateSex(newSex)
                    userSex.set(newSex)
                }

                if (newWork != userWork.get()) {
                    firebaseRepository.updateWork(
                        newWork,
                        onFailure = {
                            userWork.notifyChange()
                            fieldUpdateFailure.postValue(String.format(failMessage, "Work"))
                        }
                    )

                    userRepository.updateWork(newWork)
                    userWork.set(newWork)
                }

                if (newTrainings != userTrainings.get()) {
                    firebaseRepository.updateTrainings(
                        newTrainings,
                        onFailure = {
                            userTrainings.notifyChange()
                            fieldUpdateFailure.postValue(String.format(failMessage, "Trainings"))
                        }
                    )

                    userRepository.updateTrainings(newTrainings)
                    userTrainings.set(newTrainings)
                }

                if (newDiet != userDiet.get()) {
                    firebaseRepository.updateDiet(
                        newDiet,
                        onFailure = {
                            userDiet.notifyChange()
                            fieldUpdateFailure.postValue(String.format(failMessage, "Diet"))
                        }
                    )

                    userRepository.updateDiet(newDiet)
                    userDiet.set(newDiet)
                }

            } else {

                fieldUpdateFailure.postValue("Data will be saved to cloud after launch with an Internet!")

                if (newWeight != userWeight.get()) {
                    userRepository.updateWeight(newWeight)
                    userWeight.set(newWeight)
                }

                if (newSex != userSex.get()) {
                    userRepository.updateSex(newSex)
                    userSex.set(newSex)
                }

                if (newWork != userWork.get()) {
                    userRepository.updateWork(newWork)
                    userWork.set(newWork)
                }

                if (newTrainings != userTrainings.get()) {
                    userRepository.updateTrainings(newTrainings)
                    userTrainings.set(newTrainings)
                }

                if (newDiet != userDiet.get()) {
                    userRepository.updateDiet(newDiet)
                    userDiet.set(newDiet)
                }
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