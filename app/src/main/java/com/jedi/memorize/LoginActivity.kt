package com.jedi.memorize

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    private lateinit var logIn: Button
    private lateinit var name: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        logIn = findViewById(R.id.logInButton)
        name = findViewById(R.id.logInName)

        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)

        val possibleUsername: String? = sharedPref.getString("USERNAME", null)

        if (possibleUsername != null) {
            name.setText(possibleUsername)
        }

        logIn.setOnClickListener {
            if (name.text.isNotEmpty()) {
                sharedPref.edit().putString("USERNAME", name.text.toString()).apply()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("USERNAME", name.text.toString())
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please introduce your name.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
