package com.example.components.architecture.nvice.ui.user.profile.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.components.architecture.nvice.data.exception.ValidatorException
import com.example.components.architecture.nvice.data.repository.UserRepository
import com.example.components.architecture.nvice.model.user.User
import com.example.components.architecture.nvice.util.scheduler.DefaultScheduler
import com.example.components.architecture.nvice.data.repository.GenerateUserCallback
import com.example.components.architecture.nvice.data.repository.UserGeneratorRepository
import com.example.components.architecture.nvice.ui.LoadingStatus
import com.example.components.architecture.nvice.ui.user.profile.UserProfileViewModel
import com.example.components.architecture.nvice.util.extension.init
import com.example.components.architecture.nvice.util.validation.UserValidation
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

class UserCreateViewModel @Inject constructor(
        userRepository: UserRepository,
        userGeneratorRepository: UserGeneratorRepository
) : UserProfileViewModel(userRepository, userGeneratorRepository) {

    private val _isUserCreated = MutableLiveData<Boolean>().init(false)
    val isUserCreated: LiveData<Boolean>
        get() = _isUserCreated

    private val _formValidator = MutableLiveData<ValidatorException>()
    val formValidator: LiveData<ValidatorException>
        get() = _formValidator

    init {
        initSkills(skills)
        initExperiences(experiences)
    }

    // use custom callback
    fun randomUser() {
        userDataLoadingStatus.postValue(LoadingStatus.PROCESSING)
        userGeneratorRepository.generateUser(object : GenerateUserCallback {
            override fun onSuccess(user: User) {
                DefaultScheduler.AsyncScheduler.execute {
                    user.staffId = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + (1000 + (userRepository.getLatestUserId() + 1))
                    avatar.postValue(user.avatar)
                    firstName.postValue(user.firstName)
                    lastName.postValue(user.lastName)
                    position.postValue(user.position)
                    status.postValue(user.status)
                    selectCover()
                    userDataLoadingStatus.postValue(LoadingStatus.FINISHED)
                }
            }

            override fun onFailure(t: Throwable) {
            }
        })
    }

    fun addUser() {
        val vm = this@UserCreateViewModel
        User().apply {
            firstName = vm.firstName.value
            lastName = vm.lastName.value
            birthday = vm.dateOfBirth.value
            description = vm.description.value
            avatar = vm.avatar.value
            cover = vm.cover.value
            status = vm.status.value
            position = vm.position.value
            validateSkills()?.apply {
                skills?.addAll(this)
            }
            validateExperiences()?.apply {
                experiences?.addAll(this)
            }
        }.run {
            DefaultScheduler.AsyncScheduler.execute {
                staffId = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + (1000 + (userRepository.getLatestUserId() + 1))
                validateUser(this)
            }
        }
    }

    private fun validateUser(user: User?) = UserValidation.validateUser(user,
            success = {
                userRepository.addUser(it)
                _isUserCreated.postValue(true)
            },
            failure = {
                _formValidator.postValue(it)
            })
}