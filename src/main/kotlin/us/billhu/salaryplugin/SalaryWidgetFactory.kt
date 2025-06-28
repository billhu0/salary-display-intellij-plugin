package us.billhu.salaryplugin

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory

class SalaryWidgetFactory: StatusBarWidgetFactory {
    override fun getId() = "salary-widget"
    override fun getDisplayName() = "Today's Salary"
    override fun createWidget(project: Project) = SalaryWidget(project)
    override fun disposeWidget(widget: StatusBarWidget) {
        (widget as? SalaryWidget)?.dispose()
    }
}
