@file:Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")

package chrome.others

import chrome.action.BadgeTextDetails
import chrome.alarms.AlarmCreateInfo
import chrome.browserAction.TabDetails
import chrome.omnibox.DefaultSuggestResult
import chrome.omnibox.SuggestResult
import chrome.scripting.CSSInjection
import chrome.scripting.InjectionTarget
import chrome.tabs.CreateProperties
import chrome.tabs.Options
import chrome.tabs.QueryInfo
import chrome.tabs.UpdateProperties
import chrome.windows.UpdateInfo


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

fun AlarmCreateInfo(block: AlarmCreateInfo.() -> Unit): AlarmCreateInfo =
    (js("{}") as AlarmCreateInfo).apply(block)

fun QueryInfo(block: QueryInfo.() -> Unit): QueryInfo =
    (js("{}") as QueryInfo).apply(block)

fun UpdateProperties(block: UpdateProperties.() -> Unit): UpdateProperties =
    (js("{}") as UpdateProperties).apply(block)

fun UpdateInfo(block: UpdateInfo.() -> Unit): UpdateInfo =
    (js("{}") as UpdateInfo).apply(block)

fun Options(block: Options.() -> Unit): Options =
    (js("{}") as Options).apply(block)
