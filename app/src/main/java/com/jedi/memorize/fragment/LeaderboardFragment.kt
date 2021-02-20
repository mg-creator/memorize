package com.jedi.memorize.fragment

import android.os.Bundle
import android.provider.BaseColumns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jedi.memorize.MainActivity.Companion.dbHelper
import com.jedi.memorize.R
import com.jedi.memorize.adapter.StatAdapter
import com.jedi.memorize.database.StatisticSQLiteHelper.Companion.StatisticColumns.COLUMN_DATE
import com.jedi.memorize.database.StatisticSQLiteHelper.Companion.StatisticColumns.COLUMN_NAME
import com.jedi.memorize.database.StatisticSQLiteHelper.Companion.StatisticColumns.COLUMN_TIME
import com.jedi.memorize.database.StatisticSQLiteHelper.Companion.StatisticColumns.TABLE_NAME
import com.jedi.memorize.model.Stat

class LeaderboardFragment : Fragment() {

    private lateinit var dataset: MutableList<Stat>
    private lateinit var adapter: StatAdapter
    private lateinit var statEmptyView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_leaderboard, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.stats_list)
        statEmptyView = view.findViewById(R.id.emptyStatisticsView)
        statEmptyView.visibility = View.INVISIBLE

        recyclerView.addItemDecoration(
            DividerItemDecoration(this.activity, DividerItemDecoration.VERTICAL)
        )

        adapter = StatAdapter(this::onStatClick)

        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = adapter

        dataset = getDbStatistics()
        adapter.setData(dataset, statEmptyView)

        return view
    }

    private fun onStatClick(stat: Stat): Unit {
        null
    }

    private fun getDbStatistics(): MutableList<Stat> {
        val readDb = dbHelper.readableDatabase
        val projection =
                arrayOf(BaseColumns._ID, COLUMN_NAME, COLUMN_TIME, COLUMN_DATE)

        val cursor = readDb.query(
                TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        )

        var result = mutableListOf<Stat>()

        with(cursor) {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(COLUMN_NAME))
                val time = getString(getColumnIndexOrThrow(COLUMN_TIME))
                val date = getString(getColumnIndexOrThrow(COLUMN_DATE))
                result.add(Stat(name, time, date))
            }
        }

        return result
    }
}