package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast


class MainActivity : AppCompatActivity() {
    private var resultDisplayed: Boolean = false
    var opertator: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.display_fragment_container, DisplayFragment())
                .replace(R.id.button_fragment_container, ButtonFragment())
                .commit()
        }
    }

    // Function for handling digit clicks
    fun handleDigitClick(digit: Int) {
        val displayFragment =
            supportFragmentManager.findFragmentById(R.id.display_fragment_container) as DisplayFragment
        val currentText = displayFragment.displayText.text.toString()

        if (currentText.isEmpty() || currentText.startsWith("0") && currentText.length == 1) {

            displayFragment.displayText.text = ""
        } else if (resultDisplayed) {
            displayFragment.displayText.text = ""
            resultDisplayed = false
        }

        displayFragment.appendDigit(digit.toString())
    }

    // Function for handling operator clicks
    fun handleOperatorClick(operator: String) {
        val displayFragment =
            supportFragmentManager.findFragmentById(R.id.display_fragment_container) as DisplayFragment
        //displayFragment.appendOperator(operator)
        val currentText = displayFragment.displayText.text.toString()
        if (operator == "+" || operator == "-" || operator == "/" || operator == "*" || operator == "%") {
            if (currentText.isEmpty() || currentText.startsWith("0") && currentText.length == 1) {

                Toast.makeText(this, "please enter a number", Toast.LENGTH_SHORT).show()

            } else {
                if (!opertator) {
                    if (!resultDisplayed) {
                        // If result is not displayed, just append the operator
                        //     displayFragment.appendOperator(operator)
                        // If result is not displayed, just append the operator
                        displayFragment.appendOperator(operator)
                    } else {
                        // If result is displayed, append the operator and clear the result
                        displayFragment.appendOperator(operator)
                        resultDisplayed = false

                    }
                    opertator = true
                }
            }

        } else {
           // if (operator == "%" || operator == "√") {
                if (operator == "√" ) {

                    // Handle modulus and square root operations
                handleSpecialOperator(operator)
            } else if (operator == "+/-") {
                // Add negation sign before the digit
                displayFragment.addNegationSign()
            }
        }
    }


    private fun handleSpecialOperator(operator: String) {
        val displayFragment =
            supportFragmentManager.findFragmentById(R.id.display_fragment_container) as DisplayFragment
        val currentText = displayFragment.displayText.text.toString()

        // Check if the operator is the square root symbol
        if (operator == "√") {
            try {
                // Ensure that the input for the square root operation is non-negative
                val number = currentText.toDouble()
                if (number < 0) {
                    Toast.makeText(
                        this,
                        "Cannot calculate square root of a negative number",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                // Perform the square root operation
                val result = Math.sqrt(number)

                displayFragment.displayText.text = result.toString()
                resultDisplayed = true
            } catch (e: NumberFormatException) {
                // Handle the case where the input cannot be parsed to a double
                Toast.makeText(this, "Invalid input for square root operation", Toast.LENGTH_SHORT)
                    .show()
            }
            return
        }
    }

    // Logic for handling equals click
    fun handleEqualsClick() {
        val displayFragment =
            supportFragmentManager.findFragmentById(R.id.display_fragment_container) as DisplayFragment
        val expression = displayFragment.displayText.text.toString().trim()

        if (expression.isNotEmpty()) {
            displayFragment.evaluateExpression()
            resultDisplayed = true
            opertator = false
        } else {
            Toast.makeText(this, "Expression is empty", Toast.LENGTH_SHORT).show()
        }
    }
}