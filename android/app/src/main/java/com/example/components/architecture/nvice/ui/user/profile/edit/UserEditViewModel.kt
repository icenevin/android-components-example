package com.example.components.architecture.nvice.ui.user.profile.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.components.architecture.nvice.data.exception.ValidatorException
import com.example.components.architecture.nvice.data.repository.UserGeneratorRepository
import com.example.components.architecture.nvice.data.repository.UserRepository
import com.example.components.architecture.nvice.model.user.User
import com.example.components.architecture.nvice.ui.user.profile.UserProfileViewModel
import com.example.components.architecture.nvice.util.benchmark.TimeCapture
import com.example.components.architecture.nvice.util.extension.init
import com.example.components.architecture.nvice.util.validation.UserValidation
import kotlinx.coroutines.launch
import javax.inject.Inject


class UserEditViewModel @Inject constructor(
        userRepository: UserRepository,
        userGeneratorRepository: UserGeneratorRepository
) : UserProfileViewModel(userRepository, userGeneratorRepository) {

    private val _isProfileChangeCompleted = MutableLiveData<Boolean>().init(false)
    val isProfileChangeCompleted: LiveData<Boolean>
        get() = _isProfileChangeCompleted

    private val _formValidator = MutableLiveData<ValidatorException>()
    val formValidator: LiveData<ValidatorException>
        get() = _formValidator

    private lateinit var user: User

    fun initUser(user: User?) {
        user?.let {
            this.user = it
            mScope.launch {
                setUserDetails(user)
            }
        }
    }

    fun updateUser() {
        val that = this@UserEditViewModel
        user.apply {
            avatar = that.avatar.value
            cover = that.cover.value
            firstName = that.firstName.value
            lastName = that.lastName.value
            description = that.description.value
            birthday = that.dateOfBirth.value
            status = that.status.value
            position = that.position.value
            validateSkills()?.apply {
                skills?.clear()
                skills?.addAll(this)
            }
            validateExperiences()?.apply {
                experiences?.clear()
                experiences?.addAll(this)
            }
        }.run {
            validateUser(this)
        }
    }

    private fun setUserDetails(user: User?) {
        user?.let {
            avatar.postValue(it.avatar)
            firstName.postValue(it.firstName)
            lastName.postValue(it.lastName)
            description.postValue(it.description)
            dateOfBirth.postValue(it.birthday)
            position.postValue(it.position)
            status.postValue(it.status)
            cover.postValue(it.cover)
            it.skills?.let { list ->
                initSkills(list)
            }
            it.experiences?.let { list ->
                initExperiences(list)
            }
        }
    }

    private fun validateUser(user: User?) = UserValidation.validateUser(user,
            success = {
                userRepository.updateUser(it)
                _isProfileChangeCompleted.postValue(true)
            },
            fail = {
                _formValidator.postValue(it)
            })
}