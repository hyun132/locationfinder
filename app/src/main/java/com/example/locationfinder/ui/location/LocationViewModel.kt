package com.example.locationfinder.ui.location

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.example.locationfinder.db.McLocationItemEntity
import com.example.locationfinder.models.bridge.BrMcLocationItem
import com.example.locationfinder.ui.base.BaseViewModel

/**
 * LocationViewModel
 */
class LocationViewModel(application: Application) :
    BaseViewModel<LocationNavigator>(application) {
    private val _category: MutableLiveData<String> = MutableLiveData<String>("")
    val category: LiveData<String>
        get() = _category

    private val _locationPagedLiveData = MediatorLiveData<PagedList<BrMcLocationItem>>()
    val locationPagedLiveData: LiveData<PagedList<BrMcLocationItem>>
        get() = _locationPagedLiveData

    fun deleteMcItemEntity(brMcLocationItem: BrMcLocationItem) {
        mcRepository.deleteLocation(McLocationItemEntity.getBrAsEntity(brMcLocationItem))
    }

    fun updateFavoriteMcItemEntity(brMcLocationItem: BrMcLocationItem): LiveData<Int> {
        Log.d("ItemChanged : ", "updateFavoriteMcItemEntity()")
        return mcRepository.updateLocation(McLocationItemEntity.getBrAsEntity(brMcLocationItem))
    }

    fun getLocationPagedList() {
        _locationPagedLiveData.addSource(mcRepository.getAllSavedLocation(category.value)) {
            _locationPagedLiveData.postValue(
                it as PagedList<BrMcLocationItem>
            )
        }
    }

    fun setCategory(inputCategory: String) {
        _category.postValue(inputCategory)
    }
}