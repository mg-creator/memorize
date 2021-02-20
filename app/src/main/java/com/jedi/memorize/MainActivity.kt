package com.jedi.memorize

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.jedi.memorize.database.StatisticSQLiteHelper
import com.jedi.memorize.dialog.CloseApp
import com.jedi.memorize.dialog.GameEnd
import com.jedi.memorize.fragment.*

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigation_view)

        val drawerToggle = ActionBarDrawerToggle(this, drawerLayout,
            R.string.open,
            R.string.close
        )
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener {menuItem ->
            drawerLayout.closeDrawer(GravityCompat.START)

            when(menuItem.itemId) {
                R.id.firstFragment -> {
                    // abrir fragment 1
                    supportFragmentManager.beginTransaction().replace(R.id.mainLayout, MainFragment()).commit()
                    true
                }
                R.id.secondFragment -> {
                    // abrir fragment 2
                    supportFragmentManager.beginTransaction().replace(R.id.mainLayout, GameFragment()).commit()
                    true

                }
                R.id.thirdFragment -> {
                    supportFragmentManager.beginTransaction().replace(R.id.mainLayout, TabFragment()).commit()
                    true
                }
                else -> {
                    supportFragmentManager.beginTransaction().replace(R.id.mainLayout, MapFragment()).commit()
                    true
                }
            }
        }

        dbHelper = StatisticSQLiteHelper(this)

        loadDefaultFragment()

        username = intent.extras!!.get("USERNAME").toString()

        dialog = CloseApp.buildDialog(this)
    }

    companion object {
        lateinit var dbHelper: StatisticSQLiteHelper
        var username: String? = null
    }

    // override android menu clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {

                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    drawerLayout.openDrawer(GravityCompat.START)
                }

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // override android back physical button
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            dialog.show()
        }
    }

    private fun loadDefaultFragment() {
        navigationView.setCheckedItem(R.id.firstFragment)
        supportFragmentManager.beginTransaction().replace(R.id.mainLayout, MainFragment()).commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        dialog.dismiss()
    }
}