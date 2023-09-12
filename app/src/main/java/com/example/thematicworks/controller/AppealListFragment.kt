package com.example.thematicworks.controller

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.thematicworks.AppealAdapter
import com.example.thematicworks.MainActivity
import com.example.thematicworks.R
import com.example.thematicworks.databinding.FragmentAppealListBinding
import com.example.thematicworks.model.Appeal
import com.example.thematicworks.viewmodel.AppealListViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage

class AppealListFragment : Fragment() {
    private lateinit var binding: FragmentAppealListBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var appeals: MutableList<Appeal>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        appeals = mutableListOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,

        ): View? {
        (requireActivity() as MainActivity).supportActionBar?.hide()
        val viewModel: AppealListViewModel by viewModels()
        binding = FragmentAppealListBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            rvappeal.layoutManager = LinearLayoutManager(requireContext())
            tvticket.setOnClickListener {
                Navigation.findNavController(it).navigate(R.id.appealFragment)
            }
        }


    }

    private fun showAppeal() {
        with(binding) {
            val appealAdapter: AppealAdapter =
                if (rvappeal.adapter == null) {
                    AppealAdapter(mutableListOf())
                } else {
                    rvappeal.adapter as AppealAdapter
                }
            rvappeal.adapter = appealAdapter
            appealAdapter.updataAppeals(appeals)
        }
    }

    private fun showAll() {

        db.collection("appeals").orderBy("createTime", Query.Direction.DESCENDING).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.let { querySnapshot ->
                        if (appeals.isNotEmpty()) {
                            appeals.clear()
                        }
                        for (document in querySnapshot) {
                            appeals.add(document.toObject(Appeal::class.java))
                        }
                        showAppeal()
                    }
                }
            }
    }

    override fun onStart() {
        super.onStart()
        showAll()
    }

}