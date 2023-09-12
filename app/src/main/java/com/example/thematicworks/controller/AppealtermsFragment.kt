package com.example.thematicworks.controller

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.thematicworks.viewmodel.AppealtermsViewModel
import com.example.thematicworks.R
import com.example.thematicworks.databinding.FragmentAppealtermsBinding

class AppealtermsFragment : Fragment() {



    private lateinit var binding: FragmentAppealtermsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val viewModel:AppealtermsViewModel by viewModels()
        binding= FragmentAppealtermsBinding.inflate(inflater,container,false)
        binding.viewModel=viewModel
        binding.lifecycleOwner=this
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding){
            val inputStream1 = requireContext().assets.open("TermsofService")
            inputStream1.bufferedReader().useLines {
                lines->
                val text = lines.fold(""){accum,line->"$accum$line\n"}
                viewModel?.ttthing1?.value=text
            }
            tttnook.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }



}