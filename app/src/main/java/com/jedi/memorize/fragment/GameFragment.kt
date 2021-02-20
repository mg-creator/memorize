package com.jedi.memorize.fragment

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import android.widget.ImageView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.jedi.memorize.MainActivity
import com.jedi.memorize.MainActivity.Companion.dbHelper
import com.jedi.memorize.R
import com.jedi.memorize.database.StatisticSQLiteHelper.Companion.StatisticColumns.COLUMN_DATE
import com.jedi.memorize.database.StatisticSQLiteHelper.Companion.StatisticColumns.COLUMN_NAME
import com.jedi.memorize.database.StatisticSQLiteHelper.Companion.StatisticColumns.COLUMN_TIME
import com.jedi.memorize.database.StatisticSQLiteHelper.Companion.StatisticColumns.TABLE_NAME
import com.jedi.memorize.dialog.GameEnd
import com.jedi.memorize.model.Stat
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class GameFragment : Fragment() {

    private lateinit var cards: List<ImageView>
    private lateinit var pics: List<Int>
    private lateinit var cardStates: BooleanArray
    private lateinit var content: View
    private var playing = false
    private lateinit var c: Chronometer
    private val reverse = R.drawable.hearth_cardback
    private lateinit var restart: Button
    private var selected: Pair<Int, ImageView>? = null
    private val ip = "localhost"
    private val client = OkHttpClient()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_game, container, false)

        restart = view.findViewById(R.id.startGame)
        c = view.findViewById(R.id.chronometer)
        content = activity!!.findViewById(android.R.id.content)

        cards = listOf(
            view.findViewById(R.id.card1),
            view.findViewById(R.id.card2),
            view.findViewById(R.id.card3),
            view.findViewById(R.id.card4),
            view.findViewById(R.id.card5),
            view.findViewById(R.id.card6),
            view.findViewById(R.id.card7),
            view.findViewById(R.id.card8),
            view.findViewById(R.id.card9),
            view.findViewById(R.id.card10),
            view.findViewById(R.id.card11),
            view.findViewById(R.id.card12)
        )

        pics = listOf(
                R.drawable.alex,
                R.drawable.alex,
                R.drawable.dr_boom,
                R.drawable.dr_boom,
                R.drawable.hex,
                R.drawable.hex,
                R.drawable.leeroy,
                R.drawable.leeroy,
                R.drawable.lord,
                R.drawable.lord,
                R.drawable.mana,
                R.drawable.mana
        )

        // False meaning backwards
        cardStates = BooleanArray(cards.size)

        restart.setOnClickListener {
            if (playing) { gameplay() } else {
                pics = pics.shuffled()
                for ((i, card) in cards.withIndex()) {
                    card.setImageResource(pics[i])
                    Handler(Looper.getMainLooper()).postDelayed({
                        card.setImageResource(reverse)
                    }, 1000)
                }
                Handler(Looper.getMainLooper()).postDelayed({
                    gameplay()
                }, 1000)
            }
        }

        return view
    }

    private fun gameplay() {
        if (playing) {
                (activity as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.mainLayout, GameFragment()).commit()
        } else {
            playing = !playing
            restart.text = "Restart"
            c.base = SystemClock.elapsedRealtime();
            c.start()

            for((i, card) in cards.withIndex()) {
                card.setOnClickListener {
                    if (!cardStates[i]) {
                        if (selected == null) {
                            cardStates[i] = !cardStates[i]
                            card.setImageResource(pics[i])
                            selected = Pair(i, card)
                        } else if (selected!!.first != i && pics[selected!!.first] == pics[i]) {
                            cardStates[i] = !cardStates[i]
                            card.setImageResource(pics[i])
                            selected = null
                        } else if (selected!!.first != i) {
                            cardStates[i] = !cardStates[i]
                            card.setImageResource(pics[i])
                            Handler(Looper.getMainLooper()).postDelayed({
                                card.setImageResource(reverse)
                                selected!!.second.setImageResource(reverse)
                                cardStates[selected!!.first] = false
                                selected = null
                                cardStates[i] = !cardStates[i]
                            }, 500)
                        }
                    }

                    if (cardStates.all { value -> value }) {
                        c.stop()
                        val dialog = GameEnd.buildDialog((this.activity as Context), c.text)
                        dialog.show()
                        val stat = Stat(MainActivity.username!!, c.text.toString())
                        insertStatistic(stat)
                        updateServer(stat)
                    }
                }
            }
        }
    }

    private fun insertStatistic(stat: Stat) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, stat.name)
            put(COLUMN_TIME, stat.score)
            put(COLUMN_DATE, stat.date.toString())
        }

        db?.insert(TABLE_NAME, null, values)
    }

    private fun winAlert() {
        Snackbar
            .make(
                content,
                "Se ha añadido el resultado en el servidor.",
                Snackbar.LENGTH_SHORT
            )
            .show()
    }

    private fun errorAlert() {
        Snackbar
            .make(
                content,
                "Ha habido un problema al connectar con el servidor.",
                Snackbar.LENGTH_SHORT
            )
            .show()
    }

    private fun updateServer(stat: Stat) {
        val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val statJson =
            Gson().toJson(stat).toRequestBody(jsonMediaType)

        val requestPost = Request
            .Builder()
            .post(statJson)
            .url("http://$ip:8080/scores")
            .build()

        client.newCall(requestPost).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Error al añadir nuevo record!")
                println(e.toString())
                e.printStackTrace()
                errorAlert()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    println("Nuevo record añadido correctamente!")
                    winAlert()
                } else {
                    println("Error al añadir nuevo record!")
                    println(response.toString())
                    println(response.body?.string())
                    errorAlert()
                }
            }
        })
    }
}
