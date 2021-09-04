package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ArithmeticException

class MainActivity : AppCompatActivity() {
    var lastNumeric = false
    var lastDot = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clearButton.setOnLongClickListener {
            tvInput.text = ""
            lastNumeric = false
            lastDot = false
            false
        }
    }

    fun onDigit(view: View) {
        tvInput.append((view as Button).text)
        lastNumeric = true
    }

    fun onEqual(view: View) {
        if(lastNumeric) {
            var tvValue = tvInput.text.toString()
            var prefix = ""

            try {
                if(tvValue.startsWith("-")) {
                    prefix = "-"
                    tvValue = tvValue.substring(1)
                }

                val index = tvValue.indexOfAny("-+/x".toCharArray())
                if(index > -1) {
                    val op = tvValue[index]
                    var valueOne = tvValue.substring(0, index)
                    var valueTwo = tvValue.substring(index + 1)

                    if(!prefix.isEmpty()) {
                        valueOne = prefix + valueTwo
                    }

                    tvInput.text = removeZeroAfterDot(when(op) {
                        '+' -> (valueOne.toDouble() + valueTwo.toDouble()).toString()
                        '-' -> (valueOne.toDouble() - valueTwo.toDouble()).toString()
                        'x' -> (valueOne.toDouble() * valueTwo.toDouble()).toString()
                        '/' -> (valueOne.toDouble() / valueTwo.toDouble()).toString()
                        else -> ""
                    })
                }

            } catch (e: ArithmeticException) {
                e.printStackTrace()
            }
        }
    }

    private fun removeZeroAfterDot(value: String) : String {
        if(value.substring(value.length-2) == ".0")
            return value.replace(".0", "")
        return value
    }

    fun onOperator(view: View) {
        if(lastNumeric && !isOperatorAdded(tvInput.text.toString())) {
            tvInput.append((view as Button).text)
            lastNumeric = false
            lastDot = false
        }
    }

    private fun isOperatorAdded(value: String) : Boolean {
        return if(value.startsWith("-")) {
            false
        } else {
            value.contains("/") || value.contains("*") ||
            value.contains("+") || value.contains("-")
        }
    }

    fun onDecimal(view: View) {
        if(lastNumeric && !lastDot) {
            tvInput.append(".")
            lastNumeric = false
            lastDot = true
        }
    }

    fun delete(view: View) {
        tvInput.text = tvInput.text.dropLast(1)
    }
}