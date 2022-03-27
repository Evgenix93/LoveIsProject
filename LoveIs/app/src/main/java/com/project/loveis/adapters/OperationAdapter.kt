package com.project.loveis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.R
import com.project.loveis.databinding.ItemOperationBinding
import com.project.loveis.models.Operation
import com.project.loveis.util.OperationType

class OperationAdapter: RecyclerView.Adapter<OperationAdapter.Holder>() {
     private var operations = listOf<Operation>()

    fun updateList(newList: List<Operation>){
        operations = newList
        notifyDataSetChanged()
    }

    class Holder(view: View): RecyclerView.ViewHolder(view){
        private val binding: ItemOperationBinding by viewBinding()

        fun bind(operation: Operation){
            when(operation.type){
                OperationType.WalletAdd -> {
                    setOperationView(operation.value, "Пополение кошелька", true)
                }
                OperationType.WalletSub -> {
                    setOperationView(operation.value, "Вывод средств", false)
                }
                OperationType.LoveIsCreate -> {
                    setOperationView(operation.value, "Cоздана всреча love is", false)
                }
                OperationType.LoveIsCashback -> {
                    setOperationView(operation.value, "Кэшбек love is", true)
                }
                OperationType.EventIsPayment -> {
                    setOperationView(operation.value, "Взнос за участие в EventIs", false)
                }
                OperationType.EventIsIncome -> {
                    setOperationView(operation.value, "Взнос Event is от Username", true)
                }
                OperationType.Subscription -> {
                    setOperationView(operation.value, "Подписка love is", false)
                }
            }
        }

        private fun setOperationView(amount: Int, info: String, isAdd: Boolean){
            binding.amountTextView.setTextColor(ResourcesCompat.getColor(itemView.resources, if(isAdd) R.color.green else R.color.pink, null))
            binding.amountTextView.text = "${if(isAdd)"+" else ""}$amount"
            binding.infoTextView.text = info
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_operation, parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
          holder.bind(operations[position])
    }

    override fun getItemCount(): Int {
        return operations.size
    }
}