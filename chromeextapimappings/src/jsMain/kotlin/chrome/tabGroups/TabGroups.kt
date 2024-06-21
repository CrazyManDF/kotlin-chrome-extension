@file:JsQualifier("chrome.tabGroups")
package chrome.tabGroups

import chrome.others.Callback
import chrome.others.JsNumber

external interface UpdateProperties {
    var collapsed: Boolean
    var color: Color
    var title: String
}

external interface TabGroup {
    var collapsed: Boolean
    var color: Color
    var id: JsNumber
    var title: String
    var windowId: JsNumber
}

external fun update(groupId: JsNumber, updateProperties: UpdateProperties, callback: Callback<TabGroup?>)

