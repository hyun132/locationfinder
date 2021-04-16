package com.example.locationfinder.ui.map.search

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.locationfinder.db.McLocationItemEntity.Companion.getBrAsEntity
import com.example.locationfinder.models.bridge.BrMcLocationItem
import com.example.locationfinder.ui.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * SearchViewModel
 */
class SearchViewModel(application: Application) : BaseViewModel<SearchNavigator>(application) {

    var searchList: Flow<PagingData<BrMcLocationItem>> =
        mcRepository.getSearchResult(posY = 126.734086, posX = 127.269311, query = "query")
            .map { item -> item as PagingData<BrMcLocationItem> }.cachedIn(viewModelScope)

    fun setSearchQuery(
        posY: Double,
        posX: Double,
        query: String
    ): Flow<PagingData<BrMcLocationItem>> {
        Log.d("search : setSearchQuery() qyery: ", query)
        searchList = mcRepository.getSearchResult(posY = posY, posX = posX, query = query)
            .map { item -> item as PagingData<BrMcLocationItem> }.cachedIn(viewModelScope)
        return searchList
    }

    fun saveItem(BrMcLocationItem: BrMcLocationItem) {
        mcRepository.saveLocation(getBrAsEntity(BrMcLocationItem))
    }
}