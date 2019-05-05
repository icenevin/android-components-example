package com.example.components.architecture.nvice.data.datasource

import androidx.paging.DataSource

abstract class DataSourceFactory<K,V> : DataSource.Factory<K,V>() {
    enum class SortType {
        ASC,
        DESC
    }
}