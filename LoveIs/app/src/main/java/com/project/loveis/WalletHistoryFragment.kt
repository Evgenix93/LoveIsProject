package com.project.loveis

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentWalletHistoryBinding

class WalletHistoryFragment: Fragment(R.layout.fragment_wallet_history) {
    private val binding: FragmentWalletHistoryBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView(){
        with (binding.operationsRecyclerView){
            adapter = OperationAdapter()
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}