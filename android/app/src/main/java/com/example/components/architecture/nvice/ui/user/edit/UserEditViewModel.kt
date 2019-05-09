package com.example.components.architecture.nvice.ui.user.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.components.architecture.nvice.BaseViewModel
import com.example.components.architecture.nvice.data.repository.UserGeneratorRepository
import com.example.components.architecture.nvice.data.repository.UserRepository
import com.example.components.architecture.nvice.model.User
import com.example.components.architecture.nvice.model.UserPosition
import com.example.components.architecture.nvice.model.UserStatus
import com.example.components.architecture.nvice.scheduler.DefaultScheduler
import com.example.components.architecture.nvice.ui.LoadingStatus
import com.example.components.architecture.nvice.util.extension.init
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class UserEditViewModel @Inject constructor(
        private val userRepository: UserRepository,
        private val userGeneratorRepository: UserGeneratorRepository
) : BaseViewModel() {

    private val _isProfileChangeCompleted = MutableLiveData<Boolean>().init(false)
    val isProfileChangeCompleted: LiveData<Boolean>
        get() = _isProfileChangeCompleted

    private lateinit var user: User

    val userDataLoadingStatus = MutableLiveData<LoadingStatus>().init(LoadingStatus.IDLE)
    val userAvatarLoadingStatus = MutableLiveData<LoadingStatus>().init(LoadingStatus.IDLE)
    val userCoverLoadingStatus = MutableLiveData<LoadingStatus>().init(LoadingStatus.IDLE)

    // disposable (rxjava)
    private var userCoverService: Disposable? = null

    // coroutines scope
    private val job: Job = Job()
    private val bgScope = CoroutineScope(Dispatchers.IO + job)

    // user information
    val avatar = MutableLiveData<String?>().init(null)
    val firstName = MutableLiveData<String?>().init("")
    val lastName = MutableLiveData<String?>().init("")
    val description = MutableLiveData<String?>().init("")
    val dateOfBirth = MutableLiveData<String?>().init("")
    val position = MutableLiveData<UserPosition?>().init(UserPosition.DEV)
    val status = MutableLiveData<UserStatus?>().init(UserStatus.PERMANENT)
    val cover = MutableLiveData<String?>().init(null)

    override fun disposeServices() {
        job.cancel()
        userCoverService?.dispose()
    }

    fun selectAvatar() {
        randomAvatar()
    }

    fun initUser(userId: Int?) {
        userId?.let {
            bgScope.launch {
                user = userRepository.getUserById(it)
                setUserDetails(user)
            }
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
        }.run {
            userRepository.updateUser(this)
            _isProfileChangeCompleted.postValue(true)
        }
    }

    fun setDateOfBirth(day: Int, month: Int, year: Int) {
        val date = LocalDate.of(year, month + 1, day)
        dateOfBirth.value = date.format(DateTimeFormatter.ofPattern("d MMM yyyy"))
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
        }
    }

    // use kotlin coroutines
    private fun randomAvatar() {
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
}
