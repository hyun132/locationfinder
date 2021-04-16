package com.example.locationfinder.ui.location

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.locationfinder.BR
import com.example.locationfinder.R
import com.example.locationfinder.constant.McConstants
import com.example.locationfinder.constant.McConstants.DIALOG_WIDTH
import com.example.locationfinder.constant.McConstants.KAKAO_PACKAGE_NAME
import com.example.locationfinder.constant.McConstants.MARKET_BASE_URL
import com.example.locationfinder.databinding.FragmentLocationBinding
import com.example.locationfinder.models.bridge.BrMcLocationItem
import com.example.locationfinder.ui.base.BaseFragment
import kotlinx.android.synthetic.main.location_item.view.*
import kotlinx.android.synthetic.main.random_pick_dialog.*
import kotlin.random.Random

/**
 * LocationFragment
 */
class LocationFragment :
    BaseFragment<FragmentLocationBinding, LocationViewModel>(),
    LocationNavigator {

    private val vm: LocationViewModel by viewModels()
    override fun getLayoutId() = R.layout.fragment_location
    override fun getViewModel(): LocationViewModel = vm
    override fun getBindingVariable(): Int = BR.viewModel

    private val locationRecyclerviewAdapter = LocationRecyclerviewAdapter()
    private lateinit var dialog: Dialog
    private val anim: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.dialog_text_anim
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLocationItemClickListener()
        initRecyclerview()
        initRandomPickDialog()
        vm.getLocationPagedList()
        setObserver()
        initRandomPick()
        setCategoryAdapter()

        binding().apply {
            this.viewModel = vm
            rvLocation.layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setCategoryAdapter() {
        val categoryAdapter = CategoryAdapter(requireContext())
        binding().spCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent != null) {
                    var code: String? = null
                    if (parent.getItemAtPosition(position) != null) {
                        val category =
                            parent.getItemAtPosition(position) as McConstants.LocationCategory
                        code = category.categoryCode
                    }
                    Log.d("clicked item : ", code.toString())
                    if (code == null) code = ""
                    vm.setCategory(code)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        binding().spCategory.adapter = categoryAdapter
    }

    private fun setObserver() {
        vm.locationPagedLiveData.observe(viewLifecycleOwner, Observer { list ->
            Log.d("ItemChanged : ", "onViewCreated()")
            binding().bgEmptyList.visibility = (
                    if (list.isNullOrEmpty()) View.VISIBLE
                    else View.GONE)
            locationRecyclerviewAdapter.submitList(list)
            Log.d("locationRecyclerviewAdapter ", list.toString())
        })

        vm.category.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) Log.d("category changed", it)
            vm.getLocationPagedList()
            Log.d("category observer: ", vm.locationPagedLiveData.value.toString())
        })
    }

    private fun setLocationItemClickListener() {
        val listener = object : LocationRecyclerviewAdapter.ItemClickListener {
            override fun onFavoriteClicked(locationItem: BrMcLocationItem) {
                locationItem.favorite = !locationItem.favorite
                vm.updateFavoriteMcItemEntity(locationItem)
            }

            override fun onItemClicked(locationItem: BrMcLocationItem) {
                Log.d("placeUrl: ", locationItem.placeUrl)
                val packageName = KAKAO_PACKAGE_NAME
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(locationItem.placeUrl))
                    startActivity(intent)
                } catch (e: Exception) {
                    val intent =
                        Intent(Intent.ACTION_VIEW, Uri.parse("$MARKET_BASE_URL$packageName"))
                    startActivity(intent)
                }
            }

            override fun onItemLongClicked(locationItem: BrMcLocationItem) {
                println(">>>>>LongClicked>>>>>>>")
            }
        }
        locationRecyclerviewAdapter.setLocationItemClickListener(listener)
    }

    private fun initRandomPick() {
        binding().btnRandomPick.setOnClickListener {
            val savedItems = vm.locationPagedLiveData.value
            Log.d("initRandomPick() : ", savedItems.toString())
            if (savedItems.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.inform_no_saved_list),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val num = savedItems.let { Random.nextInt(it.size) }
                Log.d("random Pick : ", "$num : ${num.let { savedItems[it]?.placeName }}")
                dialog.et_place_name.apply {
                    text = num.let { savedItems[it]?.placeName }
                    animation = anim
                }
                dialog.show()
                dialog.dialog_image.playAnimation()
                anim.start()
            }
        }
    }

    /**
     * initRecyclerview
     */
    private fun initRecyclerview() {
        binding().rvLocation.apply {
            adapter = locationRecyclerviewAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ) = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    locationRecyclerviewAdapter.currentList?.get(viewHolder.absoluteAdapterPosition)
                        ?.let { vm.deleteMcItemEntity(it) }
                    Log.d(
                        "log : ",
                        "onSwiped ${
                            locationRecyclerviewAdapter.currentList?.get(viewHolder.absoluteAdapterPosition)
                                ?.let { vm.deleteMcItemEntity(it) }
                        }"
                    )
                }

                override fun clearView(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ) {
                    getDefaultUIUtil().clearView(getView(viewHolder))
                }

                override fun onSelectedChanged(
                    viewHolder: RecyclerView.ViewHolder?,
                    actionState: Int
                ) {
                    viewHolder?.let {
                        getDefaultUIUtil().onSelected(getView(it))
                    }
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                        getDefaultUIUtil().onDraw(
                            c, recyclerView,
                            getView(viewHolder), dX, dY, actionState, isCurrentlyActive
                        )
                    }
                }

                private fun getView(viewHolder: RecyclerView.ViewHolder): View {
                    return viewHolder.itemView.swipe_view
                }
            }).attachToRecyclerView(this)
        }
    }

    /**
     * initSearchDialog
     */
    @SuppressLint("ResourceType")
    private fun initRandomPickDialog() {
        dialog = Dialog(requireContext())
        dialog.apply {
            setContentView(R.layout.random_pick_dialog)
            window?.setLayout(DIALOG_WIDTH, DIALOG_WIDTH)
            btn_ok.setOnClickListener {
                dialog.dismiss()
            }
        }
    }
}