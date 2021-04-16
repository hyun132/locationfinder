package com.example.locationfinder.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.locationfinder.api.McLocationService
import com.example.locationfinder.models.McLocationItemDto
import java.io.IOException

/**
 * SearchDataSource
 */
class SearchDataSource(
    private val posX: Double,
    private val posY: Double,
    private val query: String
) : PagingSource<Int, McLocationItemDto>() {

    override suspend fun load(params: PagingSource.LoadParams<Int>): PagingSource.LoadResult<Int, McLocationItemDto> {
        try {
            Log.d("search : ", "SearchPagingSource.load")
            val nextPage = params.key ?: 1
            val response = McLocationService.mcApi.searchLocation(
                posX = posX.toString(),
                posY = posY.toString(),
                query = query,
                display = nextPage
            ).body()

            return if (response != null) {
                LoadResult.Page(
                    data = response.documents,
                    prevKey = if (nextPage == 1) null else nextPage - 1,
                    nextKey = nextPage + 1
                )
            } else {
                return LoadResult.Error(IOException())
            }
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, McLocationItemDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}