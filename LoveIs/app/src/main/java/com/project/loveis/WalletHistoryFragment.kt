package com.project.loveis

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.adapters.OperationAdapter
import com.project.loveis.databinding.FragmentWalletHistoryBinding
import com.project.loveis.models.OperationsWrapper
import com.project.loveis.util.autoCleared
import com.project.loveis.viewmodels.WalletViewModel

class WalletHistoryFragment: Fragment(R.layout.fragment_wallet_history) {
    private val binding: FragmentWalletHistoryBinding by viewBinding()
    private val viewModel: WalletViewModel by viewModels()
    private var operationAdapter: OperationAdapter by autoCleared()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeState()
        getOperations()
    }

    private fun initRecyclerView(){
        operationAdapter = OperationAdapter()
        with (binding.operationsRecyclerView){
            adapter = operationAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun getOperations(){
       binding.loadingProgressBar
       viewModel.getOperations()
    }

    private fun observeState(){
        viewModel.state.observe(viewLifecycleOwner){state ->
            when(state){
                is State.LoadedSingleState -> {
                  operationAdapter.updateList((state.result as OperationsWrapper).list)
                }
                is State.ErrorState -> Toast.makeText(requireContext(), "Ошибка ${state.code}", Toast.LENGTH_LONG).show()
            }
        }
    }
}