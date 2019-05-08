package com.example.components.architecture.nvice.ui.user.details

import androidx.lifecycle.MutableLiveData
import com.example.components.architecture.nvice.BaseViewModel
import com.example.components.architecture.nvice.data.repository.UserRepository
import com.example.components.architecture.nvice.model.User
import com.example.components.architecture.nvice.util.extension.capitalize
import com.example.components.architecture.nvice.util.extension.init
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class UserDetailsViewModel @Inject constructor(
        private val userRepository: UserRepository
) : BaseViewModel() {

    private var userId: Int? = null

    val fullName = MutableLiveData<String>().init("")
    val positionGroup = MutableLiveData<String>().init("")
    val positionName = MutableLiveData<String>().init("")
    val dateOfBirth = MutableLiveData<String>().init("")
    val statusName = MutableLiveData<String>().init("")
    val statusColor = MutableLiveData<Int>().init(null)
    val description = MutableLiveData<String>().init("")
    val staffId = MutableLiveData<String>().init("")
    val cover = MutableLiveData<String>().init("")
    val avatar = MutableLiveData<String>().init("")

    private val job: Job = Job()
    private val bgScope = CoroutineScope(Dispatchers.IO + job)

    override fun disposeServices() {
        job.cancel()
    }

    fun initUser(userId: Int?) {
        userId?.let {
            this.userId = userId
            bgScope.launch {
                val user = userRepository.getUserById(it)
                setUserDetails(user)
            }
        }
    }

    private fun setUserDetails(user: User?) {
        user?.let {
            fullName.postValue(it.firstName + " " + it.lastName)
            positionGroup.postValue(it.position?.positionGroup)
            positionName.postValue(it.position?.positionName)
            dateOfBirth.postValue(it.birthday)
            statusName.postValue(it.status?.name)
            statusColor.postValue(it.status?.getColorResource())
            description.postValue(it.description)
            staffId.postValue(it.staffId)
            cover.postValue(it.cover)
            avatar.postValue(it.avatar)
        }
    }

    fun updateUserDetails() {
        userId?.let {
            bgScope.launch {
                val user = userRepository.getUserById(it)
                Timber.i("$user")
                setUserDetails(user)
            }
        }
    }

    fun getUserId() = userId
}