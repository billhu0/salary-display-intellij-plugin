package us.billhu.salaryplugin

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(
    name = "SalarySettings",
    storages = [Storage("SdkSettingsPlugin.xml")]
)
class SalarySettings() : PersistentStateComponent<SalarySettings.State> {

    data class State(
        @JvmField var workStartTime: String = "08:00",
        @JvmField var workEndTime: String = "18:00",
        @JvmField var prefix: String = "$",
        @JvmField var suffix: String = ".",
        @JvmField var decimalDigits: Int = 2,
        @JvmField var dailyIncome: Double = 300.0,
    )

    private var myState: State = State()

    companion object {
        @JvmStatic
        fun getInstance(): SalarySettings =
            ApplicationManager.getApplication().getService(SalarySettings::class.java)
    }

    override fun getState(): State {
        return myState
    }

    override fun loadState(state: State) { myState = state }
}
