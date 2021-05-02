package com.example.locationfinder.ui.schedule

import android.app.ActionBar
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.locationfinder.BR
import com.example.locationfinder.LocationFinder
import com.example.locationfinder.R
import com.example.locationfinder.databinding.FragmentMakeNewPostBinding
import com.example.locationfinder.databinding.UploadImageItemBinding
import com.example.locationfinder.ui.base.BaseFragment

class MakeNewPostFragment : BaseFragment<FragmentMakeNewPostBinding, MakeNewPostViewModel>(),
    MakeNewPostNavigator {

    private val vm: MakeNewPostViewModel by viewModels()
    override fun getLayoutId(): Int = R.layout.fragment_make_new_post
    override fun getBindingVariable(): Int = BR.viewModel
    override fun getViewModel(): MakeNewPostViewModel = vm

    private val uploadImageUrlList = mutableListOf<Uri>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding().btnUploadImage.setOnClickListener {
            checkPermissionGranted()
        }
    }

    private fun checkPermissionGranted() {
        when {
            ContextCompat.checkSelfPermission(
                LocationFinder.appContext,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED ->
                getUploadImage()
            shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) ->
                showPermissionAlertDialog()
            else -> requestPermissions(
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                200
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 200) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) getUploadImage()
            else Toast.makeText(
                LocationFinder.appContext,
                "파일을 업로드 하기 위해 권한 설정이 필요합니다.",
                Toast.LENGTH_SHORT
            ).show()
        }
        Toast.makeText(LocationFinder.appContext, "파일을 업로드 하기 위해 권한 설정이 필요합니다.", Toast.LENGTH_SHORT)
            .show()
    }

    private fun showPermissionAlertDialog() {
        TODO("Not yet implemented")
    }

    private fun getUploadImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 300)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return // 사진을 가져오는데 문제가 생겼을 때

        when (requestCode) {
            300 -> {
                data?.data?.let { it ->
                    uploadImageUrlList.add(it)
                    //아이템 수동으로 넣어주기
                    val item =
                        UploadImageItemBinding.inflate(
                            LayoutInflater.from(this.context),
                            binding().llUploadList,
                            false
                        )
                    val item2 = UploadImageItemBinding.inflate(LayoutInflater.from(this.context),binding().llUploadList,false)
                    item2.ivUpload.setImageURI(it)
                    //추가한 아이템 리스트에 저장하고, 레이아웃에 뷰 추가0
                    // x 버튼 클릭하면 아이템과 저장목록 지우기.
                    item2.ivCancelUpload.setOnClickListener {
                        binding().llUploadList.removeView(item2.uploadItem)
                    }
                    binding().llUploadList.addView(item2.uploadItem)
                }
            }
        }
    }
}