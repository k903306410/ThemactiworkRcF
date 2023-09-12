package com.example.thematicworks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material3.NavigationBar
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.thematicworks.databinding.ItemViewBinding
import com.example.thematicworks.model.Appeal
import com.example.thematicworks.viewmodel.AppealcontentViewModel

class AppealAdapter(private var appeals: MutableList<Appeal>) :
    RecyclerView.Adapter<AppealAdapter.AppealViewHolder>() {

    class AppealViewHolder(val itemViewBinding: ItemViewBinding) :
        RecyclerView.ViewHolder(itemViewBinding.root)


    fun updataAppeals(appeals: MutableList<Appeal>) {
        this.appeals = appeals
        notifyDataSetChanged()

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppealViewHolder {
        val itemViewBinding = ItemViewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        itemViewBinding.viewModel = AppealcontentViewModel()
        itemViewBinding.lifecycleOwner = parent.findViewTreeLifecycleOwner()
        return AppealViewHolder(itemViewBinding)
    }

    override fun getItemCount(): Int {
        return appeals.size
    }

    override fun onBindViewHolder(holder: AppealViewHolder, position: Int) {
        val appeal = appeals[position]
        with(holder) {
            with(itemViewBinding) {
                viewModel?.appeal?.value = appeal
                val bundle = Bundle()
                bundle.putSerializable("appeal", appeal)
                itemView.setOnClickListener {
                    Navigation.findNavController(it).navigate(R.id.appealUpdataFragment, bundle)

                }
                carddelete.setOnClickListener {
                    viewModel?.delete(appeal)
                    appeals.remove(appeal)
                    notifyItemRemoved(adapterPosition)
                    Toast.makeText(carddelete.context, "已刪除該申訴", Toast.LENGTH_SHORT)
                        .show()
                    true
                }
            }
        }
    }

}
