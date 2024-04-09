package com.example.calculator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class DisplayFragment : Fragment() {
    lateinit var displayText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_display, container, false)
        displayText = view.findViewById(R.id.display_text)
        return view
    }

    // Append digit to the display
    fun appendDigit(digit: String) {
        displayText.append(digit)
    }

    // Append operator to the display
    fun appendOperator(operator: String) {
        displayText.append("$operator")
    }

    // Evaluate the expression
    fun evaluateExpression() {
        val expression = displayText.text.toString()
        val result = evaluateExpressions(expression)
        displayText.text = result.toString()
    }

    private fun evaluateExpressions(expression: String): Double {
        // Split the expression by individual characters
        val parts = expression.toCharArray()
        var result = StringBuilder()
        var i = 0
        while (i < parts.size) {
            // If the current character is a digit or a decimal point, keep adding it to the result
            if (Character.isDigit(parts[i]) || parts[i] == '.' || (parts[i] == '-' && i == 0)) {
                result.append(parts[i])
            } else {
                // If the current character is an operator, evaluate the current result with the operator and the next number
                val operator = parts[i]
                val nextNumber = getNextNumber(parts, i + 1)
                result = StringBuilder(
                    calculate(
                        result.toString().toDouble(),
                        operator,
                        nextNumber
                    ).toString()
                )
                // Skip the next number in the expression
                i += nextNumber.length
            }
            i++
        }
        return result.toString().toDouble()
    }

    // Add negation sign before the digit
    fun addNegationSign() {
        val currentText = displayText.text.toString()
        // Check if the current text is empty or already has a minus sign
        if (currentText.isEmpty() || currentText.startsWith("-")) {
            // If it's empty or already has a minus sign, remove it
            if (currentText.startsWith("-")) {
                displayText.text = currentText.substring(1)
            } else {
                // If it's empty, add a minus sign
                displayText.text = "-$currentText"
            }
        } else {
            // If it doesn't have a minus sign, add it before the digit
            displayText.text = "-$currentText"
        }
    }


    // Function to calculate the result of an expression with a given operator and operand
    private fun calculate(currentResult: Double, operator: Char, nextNumber: String): Double {
        if (nextNumber.isNotEmpty()) {
            val bigDecimalCurrentResult = currentResult.toBigDecimal()
            val bigDecimalNextNumber = nextNumber.toDouble().toBigDecimal()
            return when (operator) {
                '+' -> (bigDecimalCurrentResult + bigDecimalNextNumber).toDouble()
                '-' -> (bigDecimalCurrentResult - bigDecimalNextNumber).toDouble()
                '*' -> (bigDecimalCurrentResult * bigDecimalNextNumber).toDouble()
                '/' -> (bigDecimalCurrentResult / bigDecimalNextNumber).toDouble()
                '%' -> (bigDecimalCurrentResult % bigDecimalNextNumber).toDouble()
                'E' -> {
                    val exponentIndex = nextNumber.indexOf("E^")
                    if (exponentIndex != -1) {
                        val base = nextNumber.substring(0, exponentIndex).toDouble()
                        val power = nextNumber.substring(exponentIndex + 2).toDouble()
                        Math.pow(base, power)
                    } else {
                        val resultInDouble = currentResult.toDouble()
                        val nextNumberInDouble = nextNumber.toDouble()
                        resultInDouble * Math.pow(10.0, nextNumberInDouble)
                    }
                }
                else -> throw IllegalArgumentException("Unknown operator")
            }
        } else {
            // Handle the case where nextNumber is empty
            return currentResult
        }
    }

    // Function to get the next number in the expression
    private fun getNextNumber(expression: CharArray, startIndex: Int): String {
        val number = StringBuilder()
        var i = startIndex
        while (i < expression.size && (Character.isDigit(expression[i]) || expression[i] == '.')) {
            number.append(expression[i])
            i++
        }
        return number.toString()
    }

}