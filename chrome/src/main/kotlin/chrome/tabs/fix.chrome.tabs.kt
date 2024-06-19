@file:JsQualifier("chrome.action")
package chrome.tabs

import chrome.others.JsNumber

external interface Tab2 {
    var active: Boolean
    var audible: Boolean?
    var autoDiscardable: Boolean
    var discarded: Boolean
    var favIconUrl: String?
    var groupId: JsNumber
    var height: JsNumber?
    var highlighted: Boolean
    var id: JsNumber?
    var incognito: Boolean
    var index: JsNumber
    var mutedInfo: MutedInfo?
    var openerTabId: JsNumber?
    var pendingUrl: String?
    var pinned: Boolean
    var sessionId: String?
    var status: TabStatus?
    var title: String?
    var url: String?
    var width: JsNumber?
    var windowId: JsNumber
}
