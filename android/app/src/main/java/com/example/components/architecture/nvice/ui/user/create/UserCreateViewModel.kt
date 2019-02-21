package com.example.components.architecture.nvice.ui.user.create

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.components.architecture.nvice.data.repository.UserRepository
import com.example.components.architecture.nvice.model.User
import com.example.components.architecture.nvice.model.UserPosition
import com.example.components.architecture.nvice.model.UserStatus
import com.example.components.architecture.nvice.scheduler.DefaultScheduler
import com.example.components.architecture.nvice.util.GenerateUserCallback
import com.example.components.architecture.nvice.util.UserGenerator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton


class UserCreateViewModel @Inject constructor(
        private val userRepository: UserRepository,
        private val userGenerator: UserGenerator
) : ViewModel() {

    // loading status
    enum class LoadingStatus { FINISHED, PROCESSING, IDLE }

    val userCreateStatus = MutableLiveData<LoadingStatus>()
    val userDataLoadingStatus = MutableLiveData<LoadingStatus>()
    val userAvatarLoadingStatus = MutableLiveData<LoadingStatus>()
    val userCoverLoadingStatus = MutableLiveData<LoadingStatus>()

    // disposable (rxjava)
    private var userCoverService: Disposable? = null

    // coroutines scope
    private val job: Job = Job()
    private val bgScope = CoroutineScope(Dispatchers.IO + job)

    // user information
    val avatar = MutableLiveData<String>()
    val firstName = MutableLiveData<String>()
    val lastName = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val dateOfBirth = MutableLiveData<String>()
    val position = MutableLiveData<UserPosition>()
    val status = MutableLiveData<UserStatus>()
    val cover = MutableLiveData<String>()

    // date picker trigger
    val showDatePicker = MutableLiveData<Boolean>()

    init {
        avatar.postValue(null)
        firstName.postValue("")
        lastName.postValue("")
        description.postValue("")
        dateOfBirth.postValue("")
        position.postValue(UserPosition.DEV)
        status.postValue(UserStatus.PERMANENT)
        cover.postValue(null)
        userCreateStatus.postValue(LoadingStatus.PROCESSING)
        userDataLoadingStatus.postValue(LoadingStatus.IDLE)
        userAvatarLoadingStatus.postValue(LoadingStatus.IDLE)
        userCoverLoadingStatus.postValue(LoadingStatus.IDLE)
    }

    // use custom callback
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
                    selectCover()
                    userDataLoadingStatus.postValue(LoadingStatus.FINISHED)
                }
            }

            override fun onFailure(t: Throwable) {
            }
        })
    }

    fun selectAvatar() {
        // select function in the future
        randomAvatar()
    }

    // use kotlin coroutines
    fun randomAvatar() {
        userAvatarLoadingStatus.postValue(LoadingStatus.PROCESSING)
        bgScope.launch {
            try {
                val result = userGenerator.generateUserAvatar().await()
                result[0].photo?.let {
                    avatar.postValue(it)
                }
            } catch (e: Exception) {
                Timber.e("Exception $e")
            }
            userAvatarLoadingStatus.postValue(LoadingStatus.FINISHED)
        }
    }

    fun selectCover() {
        if (cover.value.isNullOrEmpty() && userCoverLoadingStatus != LoadingStatus.PROCESSING) {
            randomCover()
        }
    }

    // use rxJava
    fun randomCover() {
        userCoverLoadingStatus.postValue(LoadingStatus.PROCESSING)
        userCoverService = userGenerator.generateUserCover()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { response ->
                            cover.postValue(response[0].urls.regular)
                        },
                        { error ->
                            Timber.e(error)
                        },
                        { userCoverLoadingStatus.postValue(LoadingStatus.FINISHED) }
                )
    }

    fun clearAvatar() {
        avatar.postValue("")
    }

    fun clearCover() {
        cover.postValue("")
    }

    fun clearDateOfBirth() {
        dateOfBirth.postValue("")
    }

    fun clearDescription() {
        description.postValue("")
    }

    fun addUser() {
        val user = User(null, "", firstName.value, lastName.value, dateOfBirth.value, description.value, avatar.value, cover.value, status.value, position.value)
        DefaultScheduler.AsyncScheduler.execute {
            user.staffId = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + (1000 + (userRepository.getLatestUserId() + 1))
            userRepository.addUser(user)
            userCreateStatus.postValue(LoadingStatus.FINISHED)
        }
    }

    fun requestDateOfBirthPicker() {
        showDatePicker.postValue(true)
    }

    fun setDateOfBirth(day: Int, month: Int, year: Int) {
        val date = LocalDate.of(year, month + 1, day)
        dateOfBirth.postValue(date.format(DateTimeFormatter.ofPattern("d MMM yyyy")))
    }

    fun disposeServices() {
        userCoverService?.dispose()
    }
}