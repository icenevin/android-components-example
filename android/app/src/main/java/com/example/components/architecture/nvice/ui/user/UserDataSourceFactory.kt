package com.example.components.architecture.nvice.ui.user

import androidx.paging.DataSource
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.components.architecture.nvice.dao.UserDao
import com.example.components.architecture.nvice.data.datasource.DataSourceFactory
import com.example.components.architecture.nvice.model.user.User
import com.example.components.architecture.nvice.util.regex.RegexUtil
import javax.inject.Inject
import kotlin.collections.HashMap

class UserDataSourceFactory @Inject constructor(
        private val dao: UserDao
) : DataSourceFactory<Int, User>() {

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
        } else {
            queryConditions.remove(UserDataSourceRequest.SEARCH_BY_STATUS)
        }
    }

    fun orderBy(value: String) {
        queryOrderBy = value
    }

    fun orderBy(value: String, desc: Boolean) {
        orderBy(value)
        setDescendingOrder(desc)
    }

    fun searchByREGEX(regex: String){
        if (regex.isNotEmpty()) {
            queryConditions[UserDataSourceRequest.SEARCH_BY_REGEX] = "lower(user.first_name || ' ' || user.last_name || ' ' || user.id) REGEXP '${RegexUtil.generate(regex).toLowerCase()}'"
        } else {
            queryConditions.remove(UserDataSourceRequest.SEARCH_BY_REGEX)
        }
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
    SEARCH_BY_ID,
    SEARCH_BY_REGEX
}
