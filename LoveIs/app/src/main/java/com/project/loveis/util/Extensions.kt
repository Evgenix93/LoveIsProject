package com.project.loveis.util

import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun EditText.textChangedFlow(): Flow<Int> {

    return callbackFlow {
        val textChangeListener = object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val age = p0?.toString()?.toIntOrNull()
                if (age != null)
                sendBlocking(age)
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        }
        addTextChangedListener(textChangeListener)
        awaitClose {
            addTextChangedListener(textChangeListener)
        }
    }
}

