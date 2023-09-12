package com.example.thematicworks.controller

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.thematicworks.viewmodel.AppealUpdataViewModel
import com.example.thematicworks.R
import com.example.thematicworks.databinding.FragmentAppealUpdataBinding
import com.example.thematicworks.databinding.FragmentAppealcontentBinding
import com.example.thematicworks.model.Appeal
import com.example.thematicworks.viewmodel.AppealcontentViewModel
import com.google.firebase.storage.FirebaseStorage
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class AppealUpdataFragment : Fragment() {

    private var ImageUri: Uri? = null
    private lateinit var binding: FragmentAppealUpdataBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        requireActivity().title = "編輯/顯示內容"
        val viewModel: AppealcontentViewModel by viewModels()
        binding = FragmentAppealUpdataBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            arguments?.let {bundle ->
                viewModel?.appeal?.value= if(Build.VERSION.SDK_INT<Build.VERSION_CODES.TIRAMISU){
                    bundle.getSerializable("appeal")as Appeal
                }else{
                    bundle.getSerializable("appeal",Appeal::class.java)!!
                }
            }
            val onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val selectedValue = parent?.getItemAtPosition(position).toString()
                    viewModel?.appeal?.value?.regulations=selectedValue

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    viewModel?.text?.value = "請選擇對應條例"
                }
            }
            formmenu.onItemSelectedListener = onItemSelectedListener
            goupdata.setOnClickListener {
                with(viewModel!!) vm@{

                    val appeal = appeal.value!!
                    lifecycleScope.launch {
                        withContext(Dispatchers.Main) {
                            if (ImageUri == null) {
                                this@vm.addAppealUp(appeal)
                                Navigation.findNavController(it).navigate(R.id.appealListFragment)
                            } else {
                                val imagePath = "${"申訴照片"}/image/${appeal.id}"
                                appeal.imagePath = imagePath
                                this@vm.addAppealUp(appeal)
                                FirebaseStorage.getInstance()
                                    .reference.child(imagePath)
                                    .putFile(ImageUri!!).addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Navigation.findNavController(it)
                                                .navigate(R.id.appealListFragment)
                                        }
                                    }
                            }
                        }
                        Toast.makeText(requireContext(),"已修改罰單的申訴", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            }
            btpoto.setOnClickListener {
                val add = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                pickPicture.launch(add)
            }
            back.setOnClickListener {
                findNavController().popBackStack()
            }

        }
    }
    private  var pickPicture =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
            if(result.resultCode== Activity.RESULT_OK){
                result.data?.data?.let {uri->crop(uri)  }
            }
        }
    private fun crop(sourceImageUri: Uri){
        val file = File(requireContext().getExternalFilesDir(null),"picture_cropped.jpg")
        val destinationUri = Uri.fromFile(file)
        val cropIntent: Intent = UCrop.of(
            sourceImageUri,
            destinationUri
        )
            .getIntent(requireContext())
        cropPictureLauncher.launch(cropIntent)
    }
    private var cropPictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode== Activity.RESULT_OK){
                var bitmap: Bitmap?=null
                result.data?.let { intent->
                    UCrop.getOutput(intent)?.let { uri->
                        ImageUri = uri
                        bitmap = BitmapFactory.decodeStream(
                            requireContext().contentResolver.openInputStream(uri)
                        )
                    }
                }
                with(binding){
                    if(bitmap!=null){
                        imageView3.setImageBitmap(bitmap)
                    }else{
                        imageView3.setImageResource(R.drawable.carview)
                    }
                }
            }


        }
}