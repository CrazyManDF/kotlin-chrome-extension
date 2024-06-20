package chrome.others

import chrome.action.BadgeTextDetails
import chrome.browserAction.TabDetails
import chrome.omnibox.DefaultSuggestResult
import chrome.omnibox.SuggestResult
import chrome.scripting.CSSInjection
import chrome.scripting.InjectionTarget
import chrome.tabs.CreateProperties

enum class OnInputEnteredDisposition {
    currentTab,
    newForegroundTab,
    newBackgroundTab
}


inline fun BadgeTextDetails(block: BadgeTextDetails.() -> Unit) =
    (js("{}") as BadgeTextDetails).apply(block)

fun DefaultSuggestResult(block: DefaultSuggestResult.() -> Unit): DefaultSuggestResult =
    (js("{}") as DefaultSuggestResult).apply(block)

fun SuggestResult(block: SuggestResult.() -> Unit): SuggestResult =
    (js("{}") as SuggestResult).apply(block)

fun CreateProperties(block: CreateProperties.() -> Unit): CreateProperties =
    (js("{}") as CreateProperties).apply(block)

fun TabDetails(block: TabDetails.() -> Unit): TabDetails =
    (js("{}") as TabDetails).apply(block)

fun CSSInjection(block: CSSInjection.() -> Unit): CSSInjection =
    (js("{}") as CSSInjection).apply(block)

fun InjectionTarget(block: InjectionTarget.() -> Unit): InjectionTarget =
    (js("{}") as InjectionTarget).apply(block)