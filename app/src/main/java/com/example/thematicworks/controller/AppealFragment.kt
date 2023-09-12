package com.example.thematicworks.controller

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.thematicworks.viewmodel.AppealViewModel
import com.example.thematicworks.R
import com.example.thematicworks.databinding.FragmentAppealBinding
import com.example.thematicworks.model.Appeal

class AppealFragment : Fragment() {


    private lateinit var binding: FragmentAppealBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModel:AppealViewModel by viewModels()
binding= FragmentAppealBinding.inflate(inflater,container,false)
binding.viewmodel=viewModel
binding.lifecycleOwner=this
return  binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding){

termsbutton1.setOnClickListener{
    Navigation.findNavController(it).navigate(R.id.appealtermsFragment)
}
            termsbutton2.setOnClickListener {
                Navigation.findNavController(it).navigate(R.id.appealtermstwoFragment)
            }
            ido.setOnClickListener {
                if (termsyes.isChecked){
                    Navigation.findNavController(it).navigate(R.id.appealcontentFragment)
                    ttterro.text = ""

                }else{
                    val errorMessage = "請勾選並確認已閱讀網路服務條款和個資隱私條款"
                    ttterro.text = errorMessage
                }

            }
            inot.setOnClickListener {
                // findNavController().popBackStack() "返回上一頁"前一頁是別人寫的,所以這段我註解掉
            }
            tvrecord.setOnClickListener{
                Navigation.findNavController(it).navigate(R.id.appealListFragment)
            }
        }

    }


}