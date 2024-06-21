@file:JsQualifier("chrome.windows")
package chrome.windows

import chrome.others.Callback
import chrome.tabs.Tab
import chrome.tabs.WindowType
import kotlin.js.Promise

external interface QueryOptions {
    var populate: Boolean
    var windowTypes: Array<WindowType>
}

external interface UpdateInfo {
    var drawAttention: Boolean
    var focused: Boolean
    var height: Number
    var left: Number
    var state: WindowState
    var top: Number
    var width: Number
}

external interface Window {
    var alwaysOnTop: Boolean
    var focused: Boolean
    var height: Number
    var id: Number
    var incognito: Boolean
    var left: Number
    var sessionId: String
    var state: WindowState
    var tabs: Array<Tab>
    var top: Number
    var type: WindowType
    var width: Number
}

//external fun getCurrent(queryOptions: QueryOptions?, callback: Callback<Window>?)
external fun update(windowId: Number, updateInfo: UpdateInfo?, callback: Callback<Window>?): Promise<Window>