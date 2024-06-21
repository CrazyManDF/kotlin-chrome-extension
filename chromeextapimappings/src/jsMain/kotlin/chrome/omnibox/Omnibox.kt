@file:JsQualifier("chrome.omnibox")
package chrome.omnibox

import chrome.events.Event2
import chrome.others.OnInputEnteredDisposition
import chrome.others.VoidCallback

external interface SuggestResult {
    var content: String
    var deletable: Boolean
    var description: String
}

external interface DefaultSuggestResult {
    var description: String
}

external interface InputChangedEvent : Event2<(text: String, suggest: (suggestResults: Array<SuggestResult>)->Unit)  -> Unit>
external interface InputEntered : Event2<(text: String, disposition: OnInputEnteredDisposition) -> Unit>

external var onInputChanged: InputChangedEvent
external var onInputEntered: InputEntered

external fun setDefaultSuggestion(suggestion: DefaultSuggestResult, callback: VoidCallback)
