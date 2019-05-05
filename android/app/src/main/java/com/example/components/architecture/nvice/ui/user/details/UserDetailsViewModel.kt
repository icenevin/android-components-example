package com.example.components.architecture.nvice.ui.user.details

import com.example.components.architecture.nvice.BaseViewModel
import com.example.components.architecture.nvice.data.repository.UserRepository
import com.example.components.architecture.nvice.model.User
import com.example.components.architecture.nvice.util.extension.capitalize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject

class UserDetailsViewModel @Inject constructor(
        private val userRepository: UserRepository
) : BaseViewModel() {

    private lateinit var user: User

    private val job: Job = Job()
    private val bgScope = CoroutineScope(Dispatchers.IO + job)

    override fun disposeServices() {
        job.cancel()
    }

    fun initUser(user: User?) {
        this.user = user?: User()
    }

    fun getFullName() = user.firstName + " " + user.lastName
    fun getPositionGroup() = user.position?.positionGroup
    fun getPositionName() = user.position?.positionName
    fun getDateOfBirth() = user.birthday
    fun getStatusName() = user.status?.name
    fun getStatusColor() = user.status?.getColorResource()
    fun getDescription() = user.description
    fun getStaffId() = user.staffId
    fun getCover() = user.cover
    fun getAvatar() = user.avatar
}