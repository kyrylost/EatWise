package kyrylost.apps.eatwise.viewmodel

import androidx.databinding.ObservableDouble
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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


    private val _firstScreenFieldsSuccessfullySetted = MutableSharedFlow<Boolean>()
    val firstScreenFieldsSuccessfullySetted = _firstScreenFieldsSuccessfullySetted.asSharedFlow()

    private val _firstScreenFieldsSetError = MutableSharedFlow<String>()
    val firstScreenFieldsSetError = _firstScreenFieldsSetError.asSharedFlow()


    private val _secondScreenFieldsSuccessfullySetted = MutableSharedFlow<Boolean>()
    val secondScreenFieldsSuccessfullySetted = _secondScreenFieldsSuccessfullySetted.asSharedFlow()

    private val _secondScreenFieldsSetError = MutableSharedFlow<String>()
    val secondScreenFieldsSetError = _secondScreenFieldsSetError.asSharedFlow()


    private val _thirdScreenFieldsSuccessfullySetted = MutableSharedFlow<Boolean>()
    val thirdScreenFieldsSuccessfullySetted = _thirdScreenFieldsSuccessfullySetted.asSharedFlow()

    private val _thirdScreenFieldsSetError = MutableSharedFlow<String>()
    val thirdScreenFieldsSetError = _thirdScreenFieldsSetError.asSharedFlow()


    private val _fourthScreenFieldsSetError = MutableSharedFlow<String>()
    val fourthScreenFieldsSetError = _fourthScreenFieldsSetError.asSharedFlow()


    // Success triggered after successful user creation,
    // failure when failed registration or login, when there is no user data in ROOM
    private val _userAuthAndCreatingSuccess = MutableSharedFlow<Boolean>()
    val userAuthAndCreatingSuccess = _userAuthAndCreatingSuccess.asSharedFlow()

    private val _userAuthAndCreatingFailure = MutableSharedFlow<Boolean>()
    val userAuthAndCreatingFailure = _userAuthAndCreatingFailure.asSharedFlow()


    // Triggered under two conditions:
    // 1. When a field on the personal page hasn't been filled.
    // 2. When there is an issue with accessing the Firebase repository, or when updating a field in it fails.
    private val _fieldUpdateInfo = MutableSharedFlow<String>()
    val fieldUpdateInfo = _fieldUpdateInfo.asSharedFlow()


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

    fun setEmailAndPassword(email: String, password: String) = viewModelScope.launch {
        if (email.isEmpty()) {
            _firstScreenFieldsSetError.emit("Input an Email!")
            return@launch
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _firstScreenFieldsSetError.emit("Incorrect Email format!")
            return@launch
        }


        if (password.isEmpty()) {
            _firstScreenFieldsSetError.emit("Input a Password!")
            return@launch
        }

        if (password.length < 8) {
            _firstScreenFieldsSetError.emit("Minimum password length is 8!")
            return@launch
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

            _firstScreenFieldsSetError.emit(
                invalidPasswordMessage.replace(Regex(", $"), "")
            )
            return@launch
        }


        userEmail = email
        userPassword = password
        _firstScreenFieldsSuccessfullySetted.emit(true)
    }

    fun setBirthdayAndWeight(birthday: String, weight: String) = viewModelScope.launch {
        if (birthday.length != 10) {
            _secondScreenFieldsSetError.emit("Input Your birthday date!")
            return@launch
        } else {
            val dateValues = birthday.split("/")
            if(!isDateValid(dateValues[2].toInt(), dateValues[1].toInt(), dateValues[0].toInt())) {
                _secondScreenFieldsSetError.emit("Input correct birthday date!")
                return@launch
            }
        }

        if (weight.isEmpty()) {
            _secondScreenFieldsSetError.emit("Input Your weight!")
            return@launch
        }

        userBirthday = birthday
        userWeight.set(weight.toDouble())
        _secondScreenFieldsSuccessfullySetted.emit(true)
    }

    fun setSexAndWorkAndTrainings(
        sexId: Int, workId: Int, trainings: Int,
        maleButtonId: Int, femaleButtonId: Int,
        activeButtonId: Int, sedentaryButtonId: Int
    ) = viewModelScope.launch {
        when (sexId) {
            -1 -> {
                _thirdScreenFieldsSetError.emit("Select Your sex!")
                return@launch
            }

            maleButtonId -> userSex.set(0)
            femaleButtonId -> userSex.set(1)
        }

        when (workId) {
            -1 -> {
                _thirdScreenFieldsSetError.emit("Select Your work type!")
                return@launch
            }

            activeButtonId -> userWork.set(0)
            sedentaryButtonId -> userWork.set(1)
        }

        userTrainings.set(trainings)
        _thirdScreenFieldsSuccessfullySetted.emit(true)
    }

    fun setDiet(diet: String) = viewModelScope.launch {
        if (diet.isEmpty()) {
            _fourthScreenFieldsSetError.emit("Select diets from the list!")
            return@launch
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

    private fun createUser(user: User = buildUser()) =
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.createUser(user)
            _userAuthAndCreatingSuccess.emit(true)
        }



    private fun registerUser() = viewModelScope.launch(Dispatchers.IO) {
        firebaseRepository.register(
            userEmail, userPassword,
            userBirthday, userWeight.get(),
            userSex.get(), userWork.get(),
            userTrainings.get(), userDiet.get(),
            onSuccess = {
                createUser()
                firebaseUserRepositoryIsAccessible = true
            },
            onFailure = {
                launch{_userAuthAndCreatingFailure.emit(true)}
            }
        )
    }


    fun login() = viewModelScope.launch {
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
                onFailure = { launch{_userAuthAndCreatingFailure.emit(true)} }
            )
        }
    }


    fun updateUserData(
        newWeightString: String, newSex: Int,
        newWork: Int, newTrainings: Int,
        newDiet: Int
    ) = viewModelScope.launch(Dispatchers.IO) {
        if (newWeightString == "") {
            _fieldUpdateInfo.emit("Please, input Your weight!")
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
                        launch{_fieldUpdateInfo.emit(String.format(failMessage, "Weight"))}
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
                        launch{_fieldUpdateInfo.emit(String.format(failMessage, "Sex"))}
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
                        launch{_fieldUpdateInfo.emit(String.format(failMessage, "Work"))}
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
                        launch{_fieldUpdateInfo.emit(String.format(failMessage, "Trainings"))}
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
                        launch{_fieldUpdateInfo.emit(String.format(failMessage, "Diet"))}
                    }
                )

                userRepository.updateDiet(newDiet)
                userDiet.set(newDiet)
            }

        } else {

            _fieldUpdateInfo.emit("Data will be saved to cloud after launch with an Internet!")

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
        if (year in 1907..LocalDate.now().year) {
            LocalDate.of(year, month, day)
            true
        }
        else false
    } catch (e: Exception) {
        false
    }
}