package com.example.components.architecture.nvice.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.components.architecture.nvice.BaseViewModel
import com.example.components.architecture.nvice.model.user.User
import com.example.components.architecture.nvice.data.repository.UserRepository
import com.example.components.architecture.nvice.model.user.UserStatus
import timber.log.Timber
import javax.inject.Inject

private const val PREFETCH_DISTANCE = 10
private const val DATABASE_PAGE_SIZE = 20

class UserViewModel @Inject constructor(
        private val userRepository: UserRepository
) : BaseViewModel() {

    private var statusList = emptyList<Int>().toMutableList()

    val userPagedList: LiveData<PagedList<User>> by lazy {
        val dataSourceFactory = userRepository.getUserList()
        val config = PagedList.Config.Builder()
                .setPageSize(DATABASE_PAGE_SIZE)
                .setPrefetchDistance(PREFETCH_DISTANCE)
                .setEnablePlaceholders(true)
                .build()
        LivePagedListBuilder(dataSourceFactory, config).build()
    }

    fun getUserStatus() = userRepository.getUserStatusList()

    fun searchUser(query: String) {
        userRepository.searchUser(query)
        userPagedList.value?.dataSource?.invalidate()
    }

    fun deleteUser(user: User) = userRepository.deleteUser(user)

    fun addStatusFilter(status: UserStatus) = statusList.let {
        if (!it.contains(status.getValue())) {
            Timber.i("addStatusFilter(${status.name})")
            it.add(status.getValue()!!)
            userRepository.getUserPagedListByStatus(it)
            userPagedList.value?.dataSource?.invalidate()
        }
    }

    fun removeStatusFilter(status: UserStatus) = statusList.let {
        if (it.contains(status.getValue())) {
            Timber.i("removeStatusFilter(${status.name})")
            it.remove(status.getValue()!!)
            userRepository.getUserPagedListByStatus(it)
            userPagedList.value?.dataSource?.invalidate()
        }
    }

    fun clearStatusFilter(status: UserStatus) = statusList.clear()

    fun toggleSort() {
        userRepository.toggleSort()
        userPagedList.value?.dataSource?.invalidate()
    }
}