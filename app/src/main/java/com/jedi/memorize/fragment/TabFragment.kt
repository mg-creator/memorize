package com.jedi.memorize.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.jedi.memorize.R
import com.jedi.memorize.adapter.PageAdapter

class TabFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tab, container, false)

        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager = view.findViewById(R.id.viewPager)

        tabLayout.setupWithViewPager(viewPager)

        val adapter = PageAdapter(
                childFragmentManager,
                listOf(LeaderboardFragment(), GlobalLeaderboard())
        )
        viewPager.adapter = adapter

        return view
    }
}