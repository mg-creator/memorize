package com.jedi.memorize.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.widget.Button
import com.jedi.memorize.R

object CloseApp {
    fun buildDialog(
        context: Context
    ): Dialog {

        val dialog = Dialog(context)
        dialog.setContentView(R.layout.close_app)

        val yesButton = dialog.findViewById<Button>(R.id.closeAppButton)
        val noButton = dialog.findViewById<Button>(R.id.cancelCloseApp)

        noButton.setOnClickListener { dialog.dismiss() }
        yesButton.setOnClickListener { (context as Activity).finish()}

        return dialog
    }
}