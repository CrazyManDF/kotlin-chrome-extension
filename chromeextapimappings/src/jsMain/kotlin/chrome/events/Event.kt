package chrome.events

import chrome.others.Callback
import chrome.others.VoidCallback

external interface Event<T> {
    fun <T> addListener(callback: Callback<T>)

    fun addRules(rules: List<Rule>, callback: Callback<List<Rule>>?)
    fun getRules(ruleIdentifiers: List<String>?, callback: Callback<List<Rule>>)
    fun <T> hasListener(callback: Callback<T>): Boolean
    fun hasListeners(): Boolean
    fun <T> removeListener(callback: Callback<T>)
    fun removeRules(ruleIdentifiers: List<String>?, callback: VoidCallback)
}

external interface Event2<T : Function<*>> {
    fun addListener(callback: T)

    fun addRules(rules: List<Rule>, callback: Callback<List<Rule>>?)
    fun getRules(ruleIdentifiers: List<String>?, callback: Callback<List<Rule>>)
    fun hasListener(callback: T): Boolean
    fun hasListeners(): Boolean
    fun removeListener(callback: T)
    fun removeRules(ruleIdentifiers: List<String>?, callback: VoidCallback)
}