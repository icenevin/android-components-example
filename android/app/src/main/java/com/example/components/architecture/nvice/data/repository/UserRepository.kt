package com.example.components.architecture.nvice.data.repository

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.example.components.architecture.nvice.dao.UserDao
import com.example.components.architecture.nvice.data.datasource.DataSourceFactory
import com.example.components.architecture.nvice.model.User
import com.example.components.architecture.nvice.scheduler.DefaultScheduler
import com.example.components.architecture.nvice.ui.user.UserDataSourceFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
        private val userDao: UserDao,
        private val userDataSourceFactory: UserDataSourceFactory) {

    companion object {
        private const val PREFETCH_DISTANCE = 10
        private const val DATABASE_PAGE_SIZE = 20
    }

    var result = MutableLiveData<PagedList<User>>()

    private val pagedListConfig = PagedList.Config.Builder()
            .setPageSize(DATABASE_PAGE_SIZE)
            .setPrefetchDistance(PREFETCH_DISTANCE)
            .setEnablePlaceholders(true)
            .build()

    init {
        getUserList().observeForever {
            result.postValue(it)
        }
    }

    private fun getUserList() = LivePagedListBuilder(userDataSourceFactory, pagedListConfig).build()

    fun getUserStatusList() = userDao.selectAllUserStatus()

    fun getUserPagedListByStatus(statusList: List<Int>) {
        userDataSourceFactory.searchByStatus(statusList)
        result.value?.dataSource?.invalidate()
    }

    fun addUser(user: User) = DefaultScheduler.AsyncScheduler.execute { userDao.insert(user) }

    fun deleteUser(user: User) = DefaultScheduler.AsyncScheduler.execute { userDao.delete(user) }

    fun toggleSort() {
        userDataSourceFactory.setDescendingOrder(userDataSourceFactory.queryOrderSortType == DataSourceFactory.SortType.ASC)
        result.value?.dataSource?.invalidate()
    }
}