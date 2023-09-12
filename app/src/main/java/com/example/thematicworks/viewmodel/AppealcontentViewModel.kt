package com.example.thematicworks.viewmodel

import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thematicworks.R
import com.example.thematicworks.model.Appeal
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


@BindingAdapter("imagePath")
fun downloadImage(imageView: ImageView, imagePath: String?) {
    if (imagePath == null) {
        imageView.setImageResource(R.drawable.carview)
    } else {
        val oneMegabyte: Long = 2048*2048
        val imageRef = FirebaseStorage.getInstance().reference.child(imagePath)
        imageRef.getBytes(oneMegabyte).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result?.let { bytes ->
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    imageView.setImageBitmap(bitmap)
                }
            }
        }
    }
}


class AppealcontentViewModel : ViewModel() {
    val appeal: MutableLiveData<Appeal> by lazy { MutableLiveData<Appeal>() }
val text : MutableLiveData<String> by lazy { MutableLiveData<String>() }

    var menu: List<String> = listOf(
        "--請選擇--",
        "第五十五條-1,在交岔路口十公尺內臨時停車",
        "第四十八條,轉彎或變換車道不依標誌、標線、號誌指示",
        "第四十二條,汽車駕駛人未依規定使用方向燈",
        "第五十五條-4,併排臨時停車",
        "第四十條,汽車駕駛人行車速度，超過規定之最高時速或低於規定之最低時速"
    )

    fun setappealId(){
        appeal.value?.id=FirebaseFirestore.getInstance().collection("appeals").document().id
}

    fun addAppealUp(appeal:Appeal){
        viewModelScope.launch {
            FirebaseFirestore.getInstance().collection("appeals").document(appeal.id).set(appeal).await()
        }

    }

    fun delete(appeal: Appeal) {
        viewModelScope.launch {
            FirebaseFirestore.getInstance().collection("appeals").document(appeal.id).delete()
                .await()
            appeal.imagePath?.let { imagePath ->
                FirebaseStorage.getInstance().reference.child(imagePath).delete().await()
            }
        }


    }

}