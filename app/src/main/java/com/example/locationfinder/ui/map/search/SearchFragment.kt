package com.example.locationfinder.ui.map.search

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.locationfinder.BR
import com.example.locationfinder.R
import com.example.locationfinder.databinding.FragmentSearchBinding
import com.example.locationfinder.models.bridge.BrMcLocationItem
import com.example.locationfinder.ui.base.BaseBottomSheetFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * SearchFragment
 */

class SearchFragment :
    BaseBottomSheetFragment<FragmentSearchBinding, SearchViewModel>(),
    SearchNavigator {

    private val vm: SearchViewModel by viewModels()
    override fun getLayoutId() = R.layout.fragment_search
    override fun getViewModel() = vm
    override fun getBindingVariable(): Int = BR.viewModel
    private lateinit var currentLocation: LatLng
    val searchRecyclerviewAdapter = SearchRecyclerviewAdapter()
    private var searchJob: Job? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //추가
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentLocation = requireArguments().getParcelable<LatLng>("currentLocation") ?: LatLng(
            127.toDouble(),
            127.toDouble()
        )
        initBottomSheet(view)

        searchRecyclerviewAdapter.setOnItemClickListener {
            showAddDialog(it)
        }

        initLoadStateAdapter()

        viewDataBinding.apply {
            rvSearch.apply {
                adapter = searchRecyclerviewAdapter
                layoutManager = LinearLayoutManager(context)
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        LinearLayoutManager.VERTICAL
                    )
                )
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            vm.searchList.collectLatest {
                searchRecyclerviewAdapter.submitData(it)
            }
        }
        setSearchQueryObserver()
    }

    private fun initLoadStateAdapter() {
        searchRecyclerviewAdapter.withLoadStateHeaderAndFooter(
            header = SearchLoadStateAdapter { searchRecyclerviewAdapter.retry() },
            footer = SearchLoadStateAdapter { searchRecyclerviewAdapter.retry() }
        )
        searchRecyclerviewAdapter.addLoadStateListener { loadState ->
            //이부분 줄여보기
            binding().apply {
                rvSearch.isVisible = loadState.source.refresh is LoadState.NotLoading
                pbBar?.isVisible = loadState.source.refresh is LoadState.Loading
                btnRetry?.isVisible = loadState.source.refresh is LoadState.Error
            }

            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    requireContext(),
                    "\uD83D\uDE28 Wooops ${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }

    private fun setSearchQueryObserver() {
        viewDataBinding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    Log.v("search : onQueryTextChange()", query)
                    searchJob?.cancel()
                    searchJob = lifecycleScope.launch {
                        vm.setSearchQuery(
                            query = query,
                            posY = currentLocation.latitude,
                            posX = currentLocation.longitude
                        ).collectLatest {
                            searchRecyclerviewAdapter.submitData(it as PagingData<BrMcLocationItem>)
                        }
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    /**
     * initBottomSheet
     */
    private fun initBottomSheet(view: View) {

        dialog?.let {
            val bottomSheet: View = it.findViewById(R.id.design_bottom_sheet)
            bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        }

        view.post {
            val parent = view.parent as View
            val params = parent.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = params.behavior
            val bottomSheetBehavior = behavior as BottomSheetBehavior<*>?
            bottomSheetBehavior?.peekHeight =
                view.measuredHeight - viewDataBinding.tbSearch.height
            parent.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    /**
     * showAddDialog
     */
    private fun showAddDialog(mcLocationItem: BrMcLocationItem) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(mcLocationItem.placeName + "을 추가하시겠습니까?")
            .setPositiveButton(
                getString(R.string.positive_dialog_button_text)
            ) { dialog, _ ->
                vm.saveItem(mcLocationItem)
                dialog.dismiss()
                this.dismiss()
            }
            .setNegativeButton(
                getString(R.string.negative_dialog_button_text)
            ) { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }

    companion object {
        fun newInstance(currentLocation: LatLng): SearchFragment {
            val searchFragment = SearchFragment()
            val args = Bundle()
            args.putParcelable("currentLocation", currentLocation)
            searchFragment.arguments = args

            return searchFragment
        }
    }
}