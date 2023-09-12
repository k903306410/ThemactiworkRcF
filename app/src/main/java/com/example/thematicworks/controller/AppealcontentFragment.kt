package com.example.thematicworks.controller

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.yalantis.ucrop.UCrop
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.thematicworks.viewmodel.AppealcontentViewModel
import com.example.thematicworks.R

import com.example.thematicworks.databinding.FragmentAppealcontentBinding
import com.example.thematicworks.model.Appeal
import com.google.firebase.storage.FirebaseStorage

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import java.io.File

class AppealcontentFragment : Fragment() {


    private var ImageUri: Uri? = null
    private lateinit var binding: FragmentAppealcontentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().title = "新增罰單申訴"
        val viewModel: AppealcontentViewModel by viewModels()
        binding = FragmentAppealcontentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            viewModel?.appeal?.value = Appeal()

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
            formmenu.setSelection(0, true)
            formmenu.onItemSelectedListener = onItemSelectedListener


            goout.setOnClickListener {
                with(viewModel!!) vm@{
                    this.setappealId()
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
                        Toast.makeText(requireContext(), "已新增罰單的申訴", Toast.LENGTH_SHORT)
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

        }
    }

    private var pickPicture =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri -> crop(uri) }
            }
        }

    private fun crop(sourceImageUri: Uri) {
        val file = File(requireContext().getExternalFilesDir(null), "picture_cropped.jpg")
        val destinationUri = Uri.fromFile(file)
        val cropIntent: Intent = UCrop.of(
            sourceImageUri,
            destinationUri
        )
            .getIntent(requireContext())
        cropPictureLauncher.launch(cropIntent)
    }

    private var cropPictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                var bitmap: Bitmap? = null
                result.data?.let { intent ->
                    UCrop.getOutput(intent)?.let { uri ->
                        ImageUri = uri
                        bitmap = BitmapFactory.decodeStream(
                            requireContext().contentResolver.openInputStream(uri)
                        )
                    }
                }
                with(binding) {
                    if (bitmap != null) {
                        imageView3.setImageBitmap(bitmap)
                    } else {
                        imageView3.setImageResource(R.drawable.carview)
                    }
                }
            }


        }
}