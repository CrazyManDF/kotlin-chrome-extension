@file:JsQualifier("chrome.scripting")

package chrome.scripting

import chrome.others.JsNumber
import chrome.others.VoidCallback

external interface InjectionTarget {
    var allFrames: Boolean
    var documentIds: Array<String>
    var frameIds: Array<JsNumber>
    var tabId: Number?
}

external interface CSSInjection {
    var css: String?
    var files: Array<String>?
    var origin: StyleOrigin?
    var target: InjectionTarget?
}

external interface InjectDetails {
    var allFrames: Boolean? get() = definedExternally; set(value) = definedExternally
    var code: String? get() = definedExternally; set(value) = definedExternally
    var runAt: String? get() = definedExternally; set(value) = definedExternally
    var file: String? get() = definedExternally; set(value) = definedExternally
    var frameId: Number? get() = definedExternally; set(value) = definedExternally
    var matchAboutBlank: Boolean? get() = definedExternally; set(value) = definedExternally
    var cssOrigin: String? get() = definedExternally; set(value) = definedExternally
}

external fun insertCSS(injection: CSSInjection, callback: VoidCallback?)
external fun removeCSS(injection: CSSInjection, callback: VoidCallback?)