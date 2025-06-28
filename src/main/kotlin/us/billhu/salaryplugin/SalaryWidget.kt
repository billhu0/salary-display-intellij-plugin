package us.billhu.salaryplugin

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.CustomStatusBarWidget
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidget.TextPresentation
import com.intellij.util.Consumer
import java.awt.event.MouseEvent
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.Timer

class SalaryWidget(private val project: Project) : CustomStatusBarWidget, TextPresentation {
    private val label = JLabel()
    private var statusBar: StatusBar? = null

    override fun install(statusBar: StatusBar) {
        this.statusBar = statusBar
    }

    private var myTimer = Timer(1_000) {
        updateValue2()
    }

    private val settings: SalarySettings.State = SalarySettings.getInstance().state

    init {
        myTimer.initialDelay = 10000
        myTimer.start()
    }

    private fun updateValue2() {

        // Check weekday
        val now = LocalDateTime.now()

        // MONDAY=1…SUNDAY=7 so .ordinal gives 0=MON…6=SUN
        // convert so 0=Sun,1=Mon…6=Sat
        val dayOfWeek = now.dayOfWeek.ordinal.let { if (it == 6) 0 else it + 1 }
        if (dayOfWeek !in 1..5) {
            label.text = "今天不上班"
            statusBar!!.updateWidget(ID())
            return
        }

        val startTimeToday = now.toLocalDate().atTime(LocalTime.parse(settings.workStartTime))
        val endTimeToday = now.toLocalDate().atTime(LocalTime.parse(settings.workEndTime))
        val workedSecondsToday: Long = when {
            now.isBefore(startTimeToday) -> 0L
            now.isAfter(endTimeToday) -> Duration.between(startTimeToday, endTimeToday).toSeconds()
            else -> Duration.between(startTimeToday, now).toSeconds()
        }
        val totalWorkingSecondsToday = Duration.between(startTimeToday, endTimeToday).toSeconds()
        val earnedToday = (workedSecondsToday.toDouble() / totalWorkingSecondsToday.toDouble()) * settings.dailyIncome
        label.text = "${settings.prefix}${"%.${settings.decimalDigits}f".format(earnedToday)}${settings.suffix}"
        if (now.isBefore(startTimeToday)) label.text += " 还未开始上班"
        if (now.isAfter(endTimeToday)) label.text += " 已下班"
        statusBar!!.updateWidget(ID())
    }

    override fun ID(): String = "salary-widget"
    override fun getPresentation(): StatusBarWidget.WidgetPresentation = this

    override fun dispose() { myTimer.stop() }

    // TextPresentation:
    override fun getText(): String = label.text
    override fun getAlignment(): Float = JComponent.LEFT_ALIGNMENT
    override fun getTooltipText(): String = "Today's earnings so far"
    override fun getClickConsumer(): Consumer<MouseEvent>? = null

    // Not used for text widgets
    override fun getComponent(): JComponent = label
}
