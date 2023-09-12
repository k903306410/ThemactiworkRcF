package com.example.thematicworks.controller

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.thematicworks.viewmodel.AppealtermstwoViewModel
import com.example.thematicworks.R
import com.example.thematicworks.databinding.FragmentAppealtermstwoBinding

class AppealtermstwoFragment : Fragment() {



    private lateinit var binding: FragmentAppealtermstwoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModel:AppealtermstwoViewModel by viewModels()
        binding= FragmentAppealtermstwoBinding.inflate(inflater,container,false)
        binding.viewModel=viewModel
        binding.lifecycleOwner=this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding){
val inputStream1 =requireContext().assets.open("PrivacyPolicy")
            inputStream1.bufferedReader().useLines {
lines->
                val text=lines.fold(""){accm,line->"$accm$line\n"}
                viewModel?.ttthing2?.value=text
            }
tttnook2.setOnClickListener {
findNavController().popBackStack()
}
        }
    }

}