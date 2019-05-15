package com.example.components.architecture.nvice.ui.user

import com.example.components.architecture.nvice.BaseViewModel
import com.example.components.architecture.nvice.model.User
import com.example.components.architecture.nvice.data.repository.UserRepository
import com.example.components.architecture.nvice.model.UserStatus
import timber.log.Timber
import javax.inject.Inject


class UserViewModel @Inject constructor(
        private val userRepository: UserRepository
) : BaseViewModel() {

    private var statusList = emptyList<Int>().toMutableList()

    fun getUserList() = userRepository.result

    fun getUserStatus() = userRepository.getUserStatusList()

    fun searchUser(query: String) {
        userRepository.searchUser(query)
    }

    fun deleteUser(user: User) = userRepository.deleteUser(user)

    fun addStatusFilter(status: UserStatus) = statusList.let {
        if (!it.contains(status.getValue())) {
            Timber.i("addStatusFilter(${status.name})")
            it.add(status.getValue()!!)
            userRepository.getUserPagedListByStatus(it)
        }
    }

    fun removeStatusFilter(status: UserStatus) = statusList.let {
        if (it.contains(status.getValue())) {
            Timber.i("removeStatusFilter(${status.name})")
            it.remove(status.getValue()!!)
            userRepository.getUserPagedListByStatus(it)
        }
    }

    fun clearStatusFilter(status: UserStatus) = statusList.clear()

    fun toggleSort() {
        userRepository.toggleSort()
    }
}