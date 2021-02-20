package com.jedi.memorize.dialog

import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.TextView
import com.jedi.memorize.R

object GameEnd {
    fun buildDialog(
        context: Context,
        time: CharSequence
    ): Dialog {

        val dialog = Dialog(context)
        dialog.setContentView(R.layout.game_end)

        val closeButton = dialog.findViewById<Button>(R.id.end_game_button)
        val resultTextView = dialog.findViewById<TextView>(R.id.end_game)

        resultTextView.text = "Has ganado en $time!"

        closeButton.setOnClickListener { dialog.dismiss() }

        return dialog
    }
}