package com.example.components.architecture.nvice.ui.user.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.components.architecture.nvice.BaseViewModel
import com.example.components.architecture.nvice.data.repository.UserRepository
import com.example.components.architecture.nvice.model.User
import com.example.components.architecture.nvice.model.UserPosition
import com.example.components.architecture.nvice.model.UserStatus
import com.example.components.architecture.nvice.scheduler.DefaultScheduler
import com.example.components.architecture.nvice.data.repository.GenerateUserCallback
import com.example.components.architecture.nvice.data.repository.UserGeneratorRepository
import com.example.components.architecture.nvice.ui.LoadingStatus
import com.example.components.architecture.nvice.util.extension.init
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


class UserCreateViewModel @Inject constructor(
        private val userRepository: UserRepository,
        private val userGeneratorRepository: UserGeneratorRepository
) : BaseViewModel() {

    private val _isUserCreated = MutableLiveData<Boolean>().init(false)
    val isUserCreated : LiveData<Boolean>
        get() = _isUserCreated

    val userDataLoadingStatus = MutableLiveData<LoadingStatus>().init(LoadingStatus.IDLE)
    val userAvatarLoadingStatus = MutableLiveData<LoadingStatus>().init(LoadingStatus.IDLE)
    val userCoverLoadingStatus = MutableLiveData<LoadingStatus>().init(LoadingStatus.IDLE)

    // disposable (rxjava)
    private var userCoverService: Disposable? = null

    // coroutines scope
    private val job: Job = Job()
    private val bgScope = CoroutineScope(Dispatchers.IO + job)

    // user information
    val avatar = MutableLiveData<String>().init(null)
    val firstName = MutableLiveData<String>().init("")
    val lastName = MutableLiveData<String>().init("")
    val description = MutableLiveData<String>().init("")
    val dateOfBirth = MutableLiveData<String>().init("")
    val position = MutableLiveData<UserPosition>().init(UserPosition.DEV)
    val status = MutableLiveData<UserStatus>().init(UserStatus.PERMANENT)
    val cover = MutableLiveData<String>().init(null)

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

    fun selectAvatar() {
        // select function in the future
        randomAvatar()
    }

    // use kotlin coroutines
    fun randomAvatar() {
        userAvatarLoadingStatus.value = LoadingStatus.PROCESSING
        bgScope.launch {
            try {
                val result = userGeneratorRepository.generateUserAvatar().await()
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
        if (cover.value.isNullOrEmpty() && userCoverLoadingStatus.value != LoadingStatus.PROCESSING) {
            randomCover()
        }
    }

    // use rxJava
    fun randomCover() {
        userCoverLoadingStatus.postValue(LoadingStatus.PROCESSING)
        userCoverService = userGeneratorRepository.generateUserCover()
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
        avatar.value = ""
    }

    fun clearCover() {
        cover.value = ""
    }

    fun clearDateOfBirth() {
        dateOfBirth.value = ""
    }

    fun clearDescription() {
        description.value = ""
    }

    fun addUser() {
        val user = User(null, "", firstName.value, lastName.value, dateOfBirth.value, description.value, avatar.value, cover.value, status.value, position.value)
        DefaultScheduler.AsyncScheduler.execute {
            user.staffId = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + (1000 + (userRepository.getLatestUserId() + 1))
            userRepository.addUser(user)
            _isUserCreated.postValue(true)
        }
    }

    fun setDateOfBirth(day: Int, month: Int, year: Int) {
        val date = LocalDate.of(year, month + 1, day)
        dateOfBirth.value = date.format(DateTimeFormatter.ofPattern("d MMM yyyy"))
    }

    override fun disposeServices() {
        job.cancel()
        userCoverService?.dispose()
    }
}