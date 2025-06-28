package us.billhu.salaryplugin

import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;

import javax.swing.*

class SalarySettingsComponent {
    private var myMainPanel: JPanel

    private val myWorkStartTimeText = JBTextField()
    private val myWorkEndTimeText = JBTextField()
    private val myDecimalDigits = JBTextField()
    private val myDailySalary = JBTextField()
    private val myPrefix = JBTextField()
    private val mySuffix = JBTextField()

    constructor() {
        myMainPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent("Work start time (HH:MM):", myWorkStartTimeText, 1, false)
            .addLabeledComponent("Work end time (HH:MM):", myWorkEndTimeText, 1, false)
            .addLabeledComponent("Decimal digits (0-5):", myDecimalDigits, 1, false)
            .addLabeledComponent("Daily salary:", myDailySalary, 1, false)
            .addLabeledComponent("Prefix:", myPrefix, 1, false)
            .addLabeledComponent("Suffix:", mySuffix, 1, false)

            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    fun getPanel(): JPanel {
        return myMainPanel
    }

    fun getPreferredFocusedComponent(): JBTextField {
        return myDailySalary
    }

    fun getWorkStartTime(): String = myWorkStartTimeText.text
    fun setWorkStartTime(newText: String) {
        myWorkStartTimeText.text = newText
    }

    fun getWorkEndTime(): String = myWorkEndTimeText.text
    fun setWorkEndTime(newText: String) {
        myWorkEndTimeText.text = newText
    }

    fun getDecimalDigits(): String = myDecimalDigits.text
    fun setDecimalDigits(newValue: Int) {
        myDecimalDigits.text = newValue.toString()
    }

    fun getDailySalary(): String = myDailySalary.text
    fun setDailySalary(newValue: Double) {
        myDailySalary.text = newValue.toString()
    }

    fun getPrefix(): String = myPrefix.text
    fun setPrefix(newText: String) {
        myPrefix.text = newText
    }

    fun getSuffix(): String = mySuffix.text
    fun setSuffix(newText: String) {
        mySuffix.text = newText
    }
}
