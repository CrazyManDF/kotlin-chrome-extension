@file:JsQualifier("chrome.action")
package chrome.action

import chrome.events.Event
import chrome.events.Event2
import chrome.others.JsNumber
import chrome.tabs.Tab
import kotlin.js.Promise

//external interface BrowserClickedEvent : chrome.events.Event<(tab: chrome.tabs.Tab) -> Unit>
//
//external interface BadgeTextDetails {
//    var text: String
//    var tabId: Number? get() = definedExternally; set(value) = definedExternally
//}
//
//external fun setBadgeText(details: BadgeTextDetails, callback: (() -> Unit)? = definedExternally /* null */): Unit = definedExternally

external interface BadgeTextDetails {
    var tabId: JsNumber?
    var text: String?
}

external interface TabDetails {
    var tabId: JsNumber?
}

external fun getBadgeText(details: TabDetails): Promise<String>
external fun setBadgeText(details: BadgeTextDetails): Promise<Unit>
external var onClicked: Event2<Tab>