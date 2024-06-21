@file:JsQualifier("chrome.tabs")

package chrome.tabs

import chrome.extensionTypes.ImageDetails
import chrome.others.JsNumber
import chrome.runtime.Port
import kotlin.js.Promise

external val MAX_CAPTURE_VISIBLE_TAB_CALLS_PER_SECOND: JsNumber
external val TAB_ID_NONE: JsNumber

external interface MutedInfo{
    var extensionId: String?
    var muted: Boolean
    var reason:MutedInfoReason?
}

external interface Tab {
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

external interface ZoomSettings {
    var defaultZoomFactor: JsNumber?
    var mode: ZoomSettingsMode?
    var scope: ZoomSettingsScope?
}

external interface CreateProperties {
    var active: Boolean?
    var index: Number?
    var openerTabId: Number?
    var pinned: Boolean?
    var selected: Boolean?
    var url: String?
    var windowId: Number?
}

external interface QueryInfo {
    var active: Boolean?
    var audible: Boolean?
    var autoDiscardable: Boolean?
    var currentWindow: Boolean?
    var discarded: Boolean?
    var groupId: Number?
    var highlighted: Boolean?
    var index: Number?
    var lastFocusedWindow: Boolean?
    var muted: Boolean?
    var pinned: Boolean?
    var status: TabStatus?
    var title: String?
    var url: Array<String>?
    var windowId: Number?
    var windowType: WindowType?
}

external interface UpdateProperties {
    var active: Boolean?
    var autoDiscardable: Boolean?
    var highlighted: Boolean?
    var muted: Boolean?
    var openerTabId: Number?
    var pinned: Boolean?
    var selected: Boolean?
    var url: String?
}

external interface ConnectInfoObject {
    var documentId: String?
    var frameId: JsNumber?
    var name: String?
}

external interface Options {
    var createProperties: CreateProperties
    var groupId: Number
    var tabIds: Array<JsNumber>
}

external fun captureVisibleTab(windowId: JsNumber?, options: ImageDetails?): Promise<String>
external fun connect(tabId: JsNumber, connectInfo: ConnectInfoObject?): Port
external fun create(createProperties: CreateProperties, callback: (Tab) -> Unit): Promise<Tab>
external fun query(queryInfo: QueryInfo, callback: (Array<Tab>) -> Unit): Promise<Array<Tab>>
external fun update(tabId: Number?, updateProperties: UpdateProperties, callback: ((Tab?) -> Unit)?): Promise<Array<Tab>>
external fun group(options: Options, callback: ((groupId: JsNumber) -> Unit)?): Promise<JsNumber>