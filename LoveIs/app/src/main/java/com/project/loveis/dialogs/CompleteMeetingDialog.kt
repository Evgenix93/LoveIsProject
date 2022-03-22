package com.project.loveis.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class CompleteMeetingDialog(val callBack: (Boolean) -> Unit): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return AlertDialog.Builder(requireContext())
            .setTitle("Завершить")
            .setMessage("Ваше событие состоялось?")
            .setPositiveButton("Да"){_, _ ->
                callBack(true)

            }
            .setNegativeButton("Нет"){ _, _ ->
                callBack(false)

            }
            .create()
    }
}