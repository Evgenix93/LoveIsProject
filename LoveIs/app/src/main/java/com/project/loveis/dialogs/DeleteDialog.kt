package com.project.loveis.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class DeleteDialog(private val callback:() -> Unit): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Удаление")
            .setMessage("Вы хотите удалить фото?")
            .setPositiveButton("Да") { _, _ ->
                 callback()
            }
            .setNegativeButton("Нет") { _, _ ->

            }
            .create()
    }
}