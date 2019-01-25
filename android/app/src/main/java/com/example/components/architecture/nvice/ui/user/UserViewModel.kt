package com.example.components.architecture.nvice.ui.user

import android.arch.lifecycle.ViewModel
import android.text.format.DateUtils
import com.example.components.architecture.nvice.model.User
import com.example.components.architecture.nvice.data.repository.UserRepository
import com.example.components.architecture.nvice.model.UserStatus
import com.example.components.architecture.nvice.scheduler.DefaultScheduler
import com.example.components.architecture.nvice.util.GenerateUserCallback
import com.example.components.architecture.nvice.util.UserGenerator
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import javax.inject.Inject


class UserViewModel @Inject constructor(
        private val userRepository: UserRepository,
        private val userGenerator: UserGenerator
) : ViewModel() {

    private var statusList = emptyList<Int>().toMutableList()

    fun getUserList() = userRepository.result

    fun getUserStatus() = userRepository.getUserStatusList()

    fun getLatestUserId() = userRepository

    fun addUser(user: User) = userRepository.addUser(user)

    fun addUserForTest() {
        userGenerator.generateUser(object : GenerateUserCallback {

            override fun onSuccess(user: User) {
                DefaultScheduler.AsyncScheduler.execute {
                    user.staffId = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + (1000 + (userRepository.getLatestUserId() + 1))
                    addUser(user)
                }

            }

            override fun onFailure(t: Throwable) {
            }
        })
    }

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