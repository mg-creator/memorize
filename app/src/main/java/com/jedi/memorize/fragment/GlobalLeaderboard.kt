package com.jedi.memorize.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.jedi.memorize.R
import com.jedi.memorize.adapter.StatAdapter
import com.jedi.memorize.model.Stat
import okhttp3.*
import java.io.IOException

class GlobalLeaderboard : Fragment() {
    private lateinit var dataset: MutableList<Stat>
    private lateinit var adapter: StatAdapter
    private lateinit var statEmptyView: View
    private lateinit var emptyMessage: TextView
    private val ip = "localhost"
    private val client = OkHttpClient()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_global_leaderboard, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.global_stats_list)
        emptyMessage = view.findViewById(R.id.global_stats_empty_message)
        statEmptyView = view.findViewById(R.id.emptyGlobalStatisticsView)
        statEmptyView.visibility = View.VISIBLE

        recyclerView.addItemDecoration(
            DividerItemDecoration(this.activity, DividerItemDecoration.VERTICAL)
        )

        adapter = StatAdapter(this::onStatClick)

        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = adapter

        getAllRecords()

        return view
    }

    private fun onStatClick(stat: Stat): Unit {
        null
    }

    private fun getAllRecords() {

        val requestGet = Request.Builder().get().url("http://$ip:8080/scores").build()

        client.newCall(requestGet).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body!!

                val json = Gson().fromJson(responseBody.string(), JsonObject::class.java)
                val jsonScores = json.getAsJsonArray("records")

                val newScores = mutableListOf<Stat>()
                for (scoreJson in jsonScores) {
                    newScores.add(Gson().fromJson(scoreJson, Stat::class.java))
                }

                activity!!.runOnUiThread{ adapter.setData(newScores, statEmptyView) }
            }

            override fun onFailure(call: Call, e: IOException) {
                activity!!.runOnUiThread {
                    emptyMessage.text = getString(R.string.connection_failed)
                }
                activity!!.runOnUiThread{ statEmptyView.visibility = View.VISIBLE }
            }
        })
    }
}
