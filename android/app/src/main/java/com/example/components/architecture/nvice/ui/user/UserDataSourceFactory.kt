package com.example.components.architecture.nvice.ui.user

import android.arch.paging.DataSource
import android.arch.persistence.db.SimpleSQLiteQuery
import com.example.components.architecture.nvice.dao.UserDao
import com.example.components.architecture.nvice.data.datasource.DataSourceFactory
import com.example.components.architecture.nvice.model.User
import timber.log.Timber
import javax.inject.Inject
import kotlin.collections.HashMap

class UserDataSourceFactory @Inject constructor(private val dao: UserDao) : DataSourceFactory<Int, User>() {
    private var statusList = emptyList<Int>().toMutableList()

    private val queryMain = "SELECT * FROM user"
    private var queryConditions = HashMap<UserDataSourceRequest, String>()
    private var queryOrderBy = "user.id"
    var queryOrderSortType = SortType.ASC

    override fun create(): DataSource<Int, User> {
        val query = "$queryMain ${if (queryConditions.size > 0) "WHERE ${this.queryConditionsToString(queryConditions)}" else ""} ORDER BY $queryOrderBy ${queryOrderSortType.name}"
        return dao.rawQueryForPaging(SimpleSQLiteQuery(query)).create()
    }

    fun searchByStatus(statusList: List<Int>) {
        this.statusList = statusList.toMutableList()
        queryConditions.clear()
        if (!this.statusList.isEmpty()) {
            queryConditions[UserDataSourceRequest.SEARCH_BY_STATUS] = "user.status IN ${statusList.toString().replace("[", "(").replace("]", ")")}"
            UserDataSourceRequest.SEARCH_BY_STATUS
        } else {
            queryConditions.remove(UserDataSourceRequest.SEARCH_BY_STATUS)
            UserDataSourceRequest.DEFAULT
        }
    }

    fun orderBy(value: String) {
        queryOrderBy = value
    }

    fun setDescendingOrder(desc: Boolean) {
        queryOrderSortType = if (desc) SortType.DESC else SortType.ASC
    }

    private fun queryConditionsToString(conditions: HashMap<UserDataSourceRequest, String>): String {
        var result = ""
        var round = conditions.size
        conditions.forEach {
            round--
            result += it.value
            if (round != 0) result += " AND "
        }
        return result
    }
}

enum class UserDataSourceRequest {
    DEFAULT,
    SEARCH_BY_STATUS,
    SEARCH_BY_NAME,
    SEARCH_BY_ID
}
