package com.jedi.memorize.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.jedi.memorize.R

class MainFragment : Fragment() {

    private lateinit var add: Button
    private lateinit var mult: Button
    private lateinit var op1: EditText
    private lateinit var op2: EditText
    private lateinit var result: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        add = view.findViewById(R.id.sumaButton)
        mult = view.findViewById(R.id.multButton)
        op1 = view.findViewById(R.id.sum1)
        op2 = view.findViewById(R.id.sum2)
        result = view.findViewById(R.id.textResultado)

        add.setOnClickListener {
            if (op1.text.isEmpty() || op2.text.isEmpty()) {
                Toast.makeText(this.activity, getString(R.string.emptyOperand), Toast.LENGTH_SHORT).show()
            } else {
                val sum = op1.text.toString().toInt() + op2.text.toString().toInt()
                result.text = sum.toString()
            }
        }

        mult.setOnClickListener {
            if (op1.text.isEmpty() || op2.text.isEmpty()) {
                Toast.makeText(this.activity, getString(R.string.emptyOperand), Toast.LENGTH_SHORT).show()
            } else {
                val sum = op1.text.toString().toInt() * op2.text.toString().toInt()
                result.text = sum.toString()
            }
        }

        return view
    }
}