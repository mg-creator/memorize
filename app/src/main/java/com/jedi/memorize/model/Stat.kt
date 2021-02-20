package com.jedi.memorize.model

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class Stat(val name: String, val score: String, val date: String = getCurrentTime()): Serializable {

    companion object {
        fun getCurrentTime(): String {
            val date = Date()
            val pattern = "dd/MM/yyyy HH:mm:ss"
            val simpleDateFormat = SimpleDateFormat(pattern)
            return simpleDateFormat.format(date)
        }
    }
}