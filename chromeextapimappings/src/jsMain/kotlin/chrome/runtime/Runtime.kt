@file:JsQualifier("chrome.runtime")

package chrome.runtime

import chrome.events.Event
import chrome.others.Callback
import chrome.others.JsNumber
import chrome.others.MessageCallback
import chrome.tabs.Tab
import kotlin.js.Promise

external interface Port{
    var name: String
    /**
     * https://developer.chrome.com/docs/extensions/reference/runtime/#type-Port
     *
     *  Should use onDisconnect.addListener()
     */
    var onDisconnect: Event<Callback<Port>>

    var onMessage: Event<MessageCallback>

    var sender: MessageSender?
}

external interface MessageSender{
    var documentId: String?
    var documentLifecycle: String?
    var frameId: JsNumber?
    var id: String?
    var nativeApplication: String?
    var origin: String?
    var tab: Tab?
    var tlsChannelId: String?
    var url: String?
}

external interface InstalledDetails {
    var reason: String
    var previousVersion: String? get() = definedExternally; set(value) = definedExternally
    var id: String? get() = definedExternally; set(value) = definedExternally
}

external interface MessageOptions {
    var includeTlsChannelId: Boolean?
}


external interface ExtensionMessageEvent : chrome.events.Event2<(message: Any, sender: MessageSender, sendResponse: (response: Any) -> Unit) -> Boolean>

//external fun sendMessage(message: Any, responseCallback: ((response: Any) -> Unit)? = definedExternally /* null */): Unit = definedExternally
//external fun sendMessage(message: Any, options: MessageOptions, responseCallback: ((response: Any) -> Unit)? = definedExternally /* null */): Unit = definedExternally
//external fun sendMessage(extensionId: String, message: Any, responseCallback: ((response: Any) -> Unit)? = definedExternally /* null */): Unit = definedExternally
//external fun sendMessage(extensionId: String, message: Any, options: MessageOptions, responseCallback: ((response: Any) -> Unit)? = definedExternally /* null */): Unit = definedExternally

external fun sendMessage(extensionId: String?, message: Any, options: MessageOptions?, responseCallback: ((response: Any) -> Unit)?): Promise<Any>
external var onMessage: ExtensionMessageEvent
external var onInstalled: Event<InstalledDetails>