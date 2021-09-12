package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var lastNumeric = false
    private var lastDot = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clearButton.setOnLongClickListener {
            onClear()
            false
        }
    }

    fun onDigit(view: View) {
        if(tvResult.text != "") {
            onClear()
        }
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
                    val valueTwo = tvValue.substring(index + 1)

                    if(prefix.isNotEmpty()) {
                        valueOne = prefix + valueTwo
                    }

                    tvResult.text = removeZeroAfterDot(when(op) {
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

    fun onClear() {
        tvInput.text = ""
        tvResult.text = ""
        lastNumeric = false
        lastDot = false
        false
    }
}