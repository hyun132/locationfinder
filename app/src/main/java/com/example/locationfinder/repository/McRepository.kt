package com.example.locationfinder.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.toLiveData
import com.example.locationfinder.db.McDatabase
import com.example.locationfinder.db.McLocationItemEntity

/**
 * McRepository
 */
class McRepository(db: McDatabase) {
    private val dao = db.getDao()
    private val es = McExecutors()

    fun saveLocation(mcItemEntity: McLocationItemEntity): Long {
        var savedResult = 0L
        es.single {
            savedResult = dao.saveLocation(mcItemEntity)
        }
        return savedResult
    }

    fun getAllSavedLocation(category: String?): LiveData<PagedList<McLocationItemEntity>> {
        val config = PagedList.Config.Builder().setPageSize(15).setEnablePlaceholders(false).build()
        return if (category.isNullOrEmpty()) {
            Log.d("update : ", "getSavedLocation() category : $category")
            dao.getSavedLocation().toLiveData(config)
        } else {
            Log.d("update : ", "getFilteredLocation() category : $category")
            dao.getFilteredLocation(category).toLiveData(config)
        }
    }

    fun updateLocation(mcItemEntity: McLocationItemEntity): LiveData<Int> {
        Log.d("ItemChanged : ", "updateLocation()")
        val updateResult = MutableLiveData(0)
        es.single {
            updateResult.postValue(dao.updateLocation(mcItemEntity))
        }
        return updateResult
    }

    fun deleteLocation(mcItemEntity: McLocationItemEntity): LiveData<Int> {
        val deleteResult = MutableLiveData(0)
        es.single {
            deleteResult.postValue(dao.deleteLocation(mcItemEntity))
        }
        return deleteResult
    }

    fun deleteAllSavedLocation(): LiveData<Int> {
        val deleteAllResult = MutableLiveData(0)
        es.single {
            deleteAllResult.postValue(dao.deleteAllSavedLocation())
        }
        return deleteAllResult
    }

    fun getSearchResult(
        posY: Double,
        posX: Double,
        query: String
    ) = Pager(PagingConfig(pageSize = 15)) {
        Log.d("search : ", "SearchPagingSource.load")
        SearchDataSource(posY = posY, posX = posX, query = query)
    }.flow
}