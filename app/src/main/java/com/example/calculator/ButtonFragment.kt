package com.example.calculator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout

class ButtonFragment : Fragment() {
    private lateinit var buttons: List<Button>
    private lateinit var buttonszero: ConstraintLayout
    private lateinit var buttonsDecimal: ConstraintLayout
    private lateinit var buttonsAddMinus: ConstraintLayout
    private lateinit var buttonsClearAll: ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_buttons, container, false)
        buttonszero = view.findViewById(R.id.btn_0)
        buttonsDecimal = view.findViewById(R.id.btn_decimal)
        buttonsAddMinus = view.findViewById(R.id.btn_add_minus)
        buttonsClearAll = view.findViewById(R.id.btn_clear_all)

        buttons = listOf(
            view.findViewById(R.id.btn_1),
            view.findViewById(R.id.btn_2),
            view.findViewById(R.id.btn_3),
            view.findViewById(R.id.btn_4),
            view.findViewById(R.id.btn_5),
            view.findViewById(R.id.btn_6),
            view.findViewById(R.id.btn_7),
            view.findViewById(R.id.btn_8),
            view.findViewById(R.id.btn_9),
            view.findViewById(R.id.btn_add),
            view.findViewById(R.id.btn_clear_entry),
            view.findViewById(R.id.btn_subtract),
            view.findViewById(R.id.btn_multiply),
            view.findViewById(R.id.btn_divide),
            view.findViewById(R.id.btn_equals),
            view.findViewById(R.id.btn_modulus),
            view.findViewById(R.id.btn_square_root),
        )
        setButtonListeners()
        setConstraintLayoutListeners()
        buttonsClearAll
        return view
    }

    // Set click listeners for buttons
    private fun setButtonListeners() {
        buttons.forEach { button ->
            button.setOnClickListener {
                val mainActivity = activity as MainActivity
                when (button.id) {
                    R.id.btn_1 -> mainActivity.handleDigitClick(1)
                    R.id.btn_2 -> mainActivity.handleDigitClick(2)
                    R.id.btn_3 -> mainActivity.handleDigitClick(3)
                    R.id.btn_4 -> mainActivity.handleDigitClick(4)
                    R.id.btn_5 -> mainActivity.handleDigitClick(5)
                    R.id.btn_6 -> mainActivity.handleDigitClick(6)
                    R.id.btn_7 -> mainActivity.handleDigitClick(7)
                    R.id.btn_8 -> mainActivity.handleDigitClick(8)
                    R.id.btn_9 -> mainActivity.handleDigitClick(9)
                    R.id.btn_add -> mainActivity.handleOperatorClick("+")
                    R.id.btn_subtract -> mainActivity.handleOperatorClick("-")
                    R.id.btn_multiply -> mainActivity.handleOperatorClick("*")
                    R.id.btn_divide -> mainActivity.handleOperatorClick("/")
                    R.id.btn_equals -> mainActivity.handleEqualsClick()
                    R.id.btn_clear_entry -> clearlastEntry()
                    R.id.btn_modulus -> mainActivity.handleOperatorClick("%")
                    R.id.btn_square_root -> mainActivity.handleOperatorClick("√")

                }
            }
        }
    }

    private fun setConstraintLayoutListeners() {
        val mainActivity = activity as MainActivity
        buttonszero.setOnClickListener {
            // Handle click for buttons zero LinearLayout
            mainActivity.handleDigitClick(0)
        }

        buttonsDecimal.setOnClickListener {
            // Handle click for buttons decimal LinearLayout
            val displayFragment =
                activity?.supportFragmentManager?.findFragmentById(R.id.display_fragment_container) as DisplayFragment
            val currentText = displayFragment.displayText.text.toString()
            // Check if the current text already contains a decimal point
            if (!currentText.contains(".")) {
                displayFragment.appendDigit(".")
            }
        }
        buttonsAddMinus.setOnClickListener {
            val displayFragment =
                activity?.supportFragmentManager?.findFragmentById(R.id.display_fragment_container) as DisplayFragment
            displayFragment.addNegationSign()

        }

        buttonsClearAll.setOnClickListener {
            clearDisplay()
        }
    }

    // Clear the display
    private fun clearDisplay() {
        val mainActivity = activity as MainActivity
        val displayFragment =
            activity?.supportFragmentManager?.findFragmentById(R.id.display_fragment_container) as DisplayFragment
        displayFragment.displayText.text = ""
        mainActivity.handleDigitClick(0)
        mainActivity.opertator=false
    }

    private fun clearlastEntry() {
        val mainActivity = activity as MainActivity
        val displayFragment =
            activity?.supportFragmentManager?.findFragmentById(R.id.display_fragment_container) as DisplayFragment
        val currentText = displayFragment.displayText.text.toString()
        if (currentText.isEmpty()) {
            // If display text is already empty, do nothing
            return
        }

        val lastChar = currentText.last()

        if (isOperator(lastChar)) {
            // If the last character deleted is an operator, set the flag to false
            mainActivity.opertator = false
        }

        if (currentText.length == 1) {
            displayFragment.displayText.text = ""
            mainActivity.handleDigitClick(0)
            return
        }

        // Remove the last character from the display text
        val newText = currentText.dropLast(1)

        // Set the display text to the updated text
        displayFragment.displayText.text = newText
    }

    private fun isOperator(char: Char): Boolean {
        // list of operators
        val operators = listOf('+', '-', '*', '/')
        return operators.contains(char)
    }
}