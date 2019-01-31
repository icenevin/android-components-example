package com.example.components.architecture.nvice.ui.user.create

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.components.architecture.nvice.data.repository.UserRepository
import com.example.components.architecture.nvice.model.User
import com.example.components.architecture.nvice.model.UserPosition
import com.example.components.architecture.nvice.model.UserStatus
import com.example.components.architecture.nvice.scheduler.DefaultScheduler
import com.example.components.architecture.nvice.util.GenerateAvatarCallback
import com.example.components.architecture.nvice.util.GenerateUserCallback
import com.example.components.architecture.nvice.util.UserGenerator
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

class UserCreateViewModel @Inject constructor(
        private val userRepository: UserRepository,
        private val userGenerator: UserGenerator
) : ViewModel() {

    enum class LoadingStatus { FINISHED, PROCESSING, IDLE }

    val userCreateStatus = MutableLiveData<LoadingStatus>()
    val userDataLoadingStatus = MutableLiveData<LoadingStatus>()
    val userAvatarLoadingStatus = MutableLiveData<LoadingStatus>()

    val avatar = MutableLiveData<String>()
    val firstName = MutableLiveData<String>()
    val lastName = MutableLiveData<String>()
    val position = MutableLiveData<UserPosition>()
    val status = MutableLiveData<UserStatus>()
    val cover = MutableLiveData<String>()

    init {
        avatar.postValue("")
        firstName.postValue("")
        lastName.postValue("")
        position.postValue(UserPosition.DEV)
        status.postValue(UserStatus.PERMANENT)
        cover.postValue("")
        userCreateStatus.postValue(LoadingStatus.PROCESSING)
        userDataLoadingStatus.postValue(LoadingStatus.IDLE)
        userAvatarLoadingStatus.postValue(LoadingStatus.IDLE)
    }

    fun randomUser() {
        userDataLoadingStatus.postValue(LoadingStatus.PROCESSING)
        userGenerator.generateUser(object : GenerateUserCallback {
            override fun onSuccess(user: User) {
                DefaultScheduler.AsyncScheduler.execute {
                    user.staffId = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + (1000 + (userRepository.getLatestUserId() + 1))
                    avatar.postValue(user.avatar)
                    firstName.postValue(user.firstName)
                    lastName.postValue(user.lastName)
                    position.postValue(user.position)
                    status.postValue(user.status)
                    cover.postValue(user.cover)
                    userDataLoadingStatus.postValue(LoadingStatus.FINISHED)
                }
            }

            override fun onFailure(t: Throwable) {
            }
        })
    }

    fun randomAvatar() {
        userAvatarLoadingStatus.postValue(LoadingStatus.PROCESSING)
        userGenerator.generateUserAvatar(object : GenerateAvatarCallback {
            override fun onSuccess(url: String?) {
                avatar.postValue(url)
                userAvatarLoadingStatus.postValue(LoadingStatus.FINISHED)
            }

            override fun onFailure(t: Throwable) {
                userAvatarLoadingStatus.postValue(LoadingStatus.FINISHED)
            }
        })
    }

    fun randomCover() {
        cover.postValue("")
        cover.postValue("https://picsum.photos/1280/?random")
    }

    fun clearAvatar() {
        avatar.postValue(null)
    }

    fun addUser() {
        val user = User(null, "", firstName.value, lastName.value, avatar.value, cover.value, status.value, position.value)
        DefaultScheduler.AsyncScheduler.execute {
            user.staffId = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + (1000 + (userRepository.getLatestUserId() + 1))
            userRepository.addUser(user)
            userCreateStatus.postValue(LoadingStatus.FINISHED)
        }
    }
}