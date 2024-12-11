package com.example.projecthope

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.selects.SelectInstance

class LL : AppCompatActivity() {
    private lateinit var tvHistory: TextView
    private lateinit var tvResult: TextView
    private lateinit var tvCurrentInput: TextView

    private var history: String = ""
    private var previousResult: String = ""
    private var operator: Char? = null
    private var currentInput: String = ""
    private var isResultShown: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ll)

        tvHistory = findViewById(R.id.tvPrev)
        tvResult = findViewById(R.id.tvSum)
        tvCurrentInput = findViewById(R.id.tvInput)

        if (savedInstanceState != null) {
            history = savedInstanceState.getString("HISTORY", "")
            previousResult = savedInstanceState.getString("PREVIOUS_RESULT", "")
            currentInput = savedInstanceState.getString("CURRENT_INPUT", "")
        }

        val buttons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )
        buttons.forEach { id ->
            findViewById<Button>(id).setOnClickListener { onNumberClicked((it as Button).text.toString()) }
        }

        val operators = mapOf(
            R.id.btnPlus to '+',
            R.id.btnMinus to '-',
            R.id.btnMul to '*',
            R.id.btnDiv to '/'
        )
        operators.forEach { (id, op) ->
            findViewById<Button>(id).setOnClickListener { onOperatorClicked(op) }
        }

        findViewById<Button>(R.id.btnEqual).setOnClickListener { onEqualClicked() }
        findViewById<Button>(R.id.btnClear).setOnClickListener { onClearClicked() }


    }

    private fun onNumberClicked(number: String) {
        if (isResultShown) {
            currentInput = ""
            isResultShown = false
        }
        currentInput += number
        updateUI()
    }

    private fun onOperatorClicked(op: Char) {
        if (currentInput.isNotEmpty()) {
            if (operator != null) {
                previousResult = calculate(previousResult, currentInput, operator!!)
            } else {
                // First operand
                previousResult = currentInput
            }
            operator = op
            currentInput = ""
        }
        updateUI()
    }

    private fun onEqualClicked() {
        if (operator != null && currentInput.isNotEmpty()) {
            val result = calculate(previousResult, currentInput, operator!!)
            history = if (history.isEmpty()) {
                "$previousResult $operator $currentInput = $result"
            } else {
                "$history\n$previousResult $operator $currentInput = $result"
            }
            previousResult = result
            currentInput = ""
            operator = null
            isResultShown = true
        }
        updateUI()
    }

    private fun onClearClicked() {
        history = ""
        previousResult = ""
        currentInput = ""
        operator = null
        isResultShown = false
        updateUI()
    }

    private fun calculate(operand1: String, operand2: String, operator: Char): String {
        val num1 = operand1.toDoubleOrNull() ?: 0.0
        val num2 = operand2.toDoubleOrNull() ?: 0.0
        val result = when (operator) {
            '+' -> num1 + num2
            '-' -> num1 - num2
            '*' -> num1 * num2
            '/' -> if (num2 != 0.0) num1 / num2 else Double.NaN
            else -> 0.0
        }
        return result.toString()
    }

    private fun updateUI() {
        tvHistory.text = history
        tvResult.text = if (previousResult.isNotEmpty()) previousResult else ""
        tvCurrentInput.text = if (currentInput.isNotEmpty()) currentInput else "0"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save values
        outState.putString("HISTORY", history)
        outState.putString("PREVIOUS_RESULT", previousResult)
        outState.putString("CURRENT_INPUT", currentInput)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Restore values
        history = savedInstanceState.getString("HISTORY", "")
        previousResult = savedInstanceState.getString("PREVIOUS_RESULT", "")
        currentInput = savedInstanceState.getString("CURRENT_INPUT", "")
        updateUI()
    }
}