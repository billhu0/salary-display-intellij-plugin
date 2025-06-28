package us.billhu.salaryplugin

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ConfigurationException
import org.jetbrains.annotations.Nls
import javax.swing.*
import java.util.Objects
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class SalaryConfigurable : Configurable {
    private var mySettingsComponent: SalarySettingsComponent? = null

    @Nls(capitalization = Nls.Capitalization.Title)
    override fun getDisplayName(): String = "SDK: Application Settings Example"

    override fun getPreferredFocusedComponent(): JComponent? {
        return mySettingsComponent!!.getPreferredFocusedComponent()
    }

    override fun createComponent(): JComponent? {
        mySettingsComponent = SalarySettingsComponent()
        return mySettingsComponent!!.getPanel()
    }

    override fun isModified(): Boolean {
        val state: SalarySettings.State =
            Objects.requireNonNull(SalarySettings.getInstance().state)
        return mySettingsComponent!!.getWorkStartTime() != state.workStartTime ||
                mySettingsComponent!!.getWorkEndTime() != state.workEndTime ||
                mySettingsComponent!!.getDecimalDigits() != state.decimalDigits.toString() ||
                mySettingsComponent!!.getDailySalary() != state.dailyIncome.toString() ||
                mySettingsComponent!!.getPrefix() != state.prefix ||
                mySettingsComponent!!.getSuffix() != state.suffix
    }

    fun isValidHHMM(input: String): Boolean =
        try {
            LocalTime.parse(input, DateTimeFormatter.ofPattern("HH:mm"))
            true
        } catch (_: DateTimeParseException) {
            false
        }

    fun convertToValidDecimalDigits(input: String): Int {
        val n = input.toIntOrNull() ?: throw ConfigurationException("Decimal digits ($input) isn't a number")
        if (n !in 0..5) throw ConfigurationException("Decimal digits ($input) isn't between 0 and 5")
        return n
    }

    fun convertToValidDouble(input: String): Double {
        val n = input.toDoubleOrNull() ?: throw ConfigurationException("Daily income ($input) isn't a double")
        return n
    }

    override fun apply() {
        val state: SalarySettings.State =
            Objects.requireNonNull(SalarySettings.getInstance().state)

        // validate
        if (!isValidHHMM(mySettingsComponent!!.getWorkStartTime())) {
            throw ConfigurationException("Work start time (${mySettingsComponent!!.getWorkStartTime()}) doesn't match HH:MM format")
        }
        if (!isValidHHMM(mySettingsComponent!!.getWorkEndTime())) {
            throw ConfigurationException("Work end time (${mySettingsComponent!!.getWorkEndTime()}) doesn't match HH:MM format")
        }

        if (LocalTime.parse(mySettingsComponent!!.getWorkStartTime(), DateTimeFormatter.ofPattern("HH:mm")) >
            LocalTime.parse(mySettingsComponent!!.getWorkEndTime(), DateTimeFormatter.ofPattern("HH:mm"))) {
            throw ConfigurationException("Work start time must be earlier than work end time")
        }

        // apply
        state.apply {
            workStartTime = mySettingsComponent!!.getWorkStartTime()
            workEndTime = mySettingsComponent!!.getWorkEndTime()
            decimalDigits = convertToValidDecimalDigits(mySettingsComponent!!.getDecimalDigits())
            dailyIncome = convertToValidDouble(mySettingsComponent!!.getDailySalary())
            prefix = mySettingsComponent!!.getPrefix()
            suffix = mySettingsComponent!!.getSuffix()
        }
    }

    override fun reset() {
        val state: SalarySettings.State =
            Objects.requireNonNull(SalarySettings.getInstance().state)

        mySettingsComponent!!.apply {
            setWorkStartTime(state.workStartTime)
            setWorkEndTime(state.workEndTime)
            setDecimalDigits(state.decimalDigits)
            setDailySalary(state.dailyIncome)
            setPrefix(state.prefix)
            setSuffix(state.suffix)
        }
    }

    override fun disposeUIResources() {
        mySettingsComponent = null
    }
}
