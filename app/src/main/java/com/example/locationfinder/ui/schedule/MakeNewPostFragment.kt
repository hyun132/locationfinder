package com.example.locationfinder.ui.schedule

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.locationfinder.BR
import com.example.locationfinder.LocationFinder
import com.example.locationfinder.R
import com.example.locationfinder.databinding.FragmentMakeNewPostBinding
import com.example.locationfinder.databinding.UploadImageItemBinding
import com.example.locationfinder.models.post.McPost
import com.example.locationfinder.ui.base.BaseFragment
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*


class MakeNewPostFragment : BaseFragment<FragmentMakeNewPostBinding, MakeNewPostViewModel>(),
    MakeNewPostNavigator {

    private val vm: MakeNewPostViewModel by viewModels()
    override fun getLayoutId(): Int = R.layout.fragment_make_new_post
    override fun getBindingVariable(): Int = BR.viewModel
    override fun getViewModel(): MakeNewPostViewModel = vm

    private val uploadImageUrlList = mutableListOf<Uri>()

    var storage: FirebaseStorage = FirebaseStorage.getInstance()
    var imgDownloadUrlList = mutableListOf<Uri>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding().btnUploadImage.setOnClickListener {
            checkPermissionGranted()
        }

        binding().tvUploadPost.setOnClickListener {
            if (uploadImageUrlList.size<1) Toast.makeText(
                context,
                "사진을 추가해 주세요",
                Toast.LENGTH_SHORT
            ).show()

            //유저 정보를 가져와야함
            

            val post = McPost(
                System.currentTimeMillis(),
                description = binding().etContents.text.toString(),
                contents = binding().etContents.text.toString(), 0,
                listOf(), Calendar.DATE.toString(),
                url = uploadImageUrlList, 0,
                listOf()
            )

            //사진 업로드
            //게시글 업로드
            //게시글 상세화면으로 이동

            binding().tvUploadPost.setOnClickListener {
                uploadImageFirestore()
            }
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
                    val item2 = UploadImageItemBinding.inflate(
                        LayoutInflater.from(this.context),
                        binding().llUploadList,
                        false
                    )
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

    @SuppressLint("SimpleDateFormat")
    fun uploadImageFirestore(){
        var storageRef = storage.reference.child("images")

        for(img in uploadImageUrlList){
            var imgFileName = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())+img+".png"
            storageRef.child(imgFileName)
            storageRef.putFile(img).addOnSuccessListener {
                storageRef.downloadUrl.result?.let { it1 -> imgDownloadUrlList.add(it1) }
                Log.d("upload file Success: ",img.toString() )
            }.addOnFailureListener {
                Log.d("upload file Fail!!!: ",img.toString() )
            }
        }
    }
}