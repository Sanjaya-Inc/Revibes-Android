package com.carissa.revibes.help_center.presentation.model

import androidx.annotation.Keep
import androidx.compose.runtime.Stable
import com.carissa.revibes.help_center.R
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Keep
@Stable
data class HelpCenterRootData(
    val title: String = "",
    val icon: Int = -1,
    val children: PersistentList<HelpCenterChildData> = persistentListOf()
) {
    companion object {
        fun default(): PersistentList<HelpCenterRootData> {
            return persistentListOf(
                HelpCenterRootData(
                    "Water Daily",
                    R.drawable.ic_water_daily,
                    persistentListOf(
                        HelpCenterChildData(
                            false,
                            "How do I do a daily check-in for watering plants?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "What happens if I forget to check-in?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "How long does it take for a tree to grow and bear fruit?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "How many points can I earn each time I check-in?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "Can I water more than once a day to speed up tree growth?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "What happens if I miss a day in the watering process?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "Can I see my tree's progress in real-time?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "How do I know how many days are left until my trees bear fruit?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "Do I get extra points when my trees bear fruit?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "Can I water my plants without an internet connection?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "Is there a reminder notification to check in daily?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "Is there a time limit within a day to check in for watering?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "Do points earned from watering plants have a validity period?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "What to do if points don't appear after checking in?",
                            "Need to be filled"
                        )
                    )
                ),
                HelpCenterRootData(
                    "Mission",
                    R.drawable.ic_hc_mission,
                    persistentListOf(
                        HelpCenterChildData(
                            false,
                            "How do I start a mission in this app?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "What are the different types of missions available?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(false, "How do I earn points from missions?", "Need to be filled"),
                        HelpCenterChildData(
                            false,
                            "Is there a time limit for completing missions?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "Can I repeat an already completed mission?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(false, "How do I track my mission progress?", "Need to be filled"),
                        HelpCenterChildData(
                            false,
                            "Do points earned have a validity period?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "What should I do if I can't complete a mission?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "How do I report bugs or problems that occur during a mission?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "How do I see a list of completed missions?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "How do I find out the details of each mission?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "Are there any special rewards if I complete a certain number of missions?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "How do I contact support if I have a problem with a mission?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "How do I cancel a mission in progress?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "Can missions be done offline or online?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "What to do if points don't appear after completing a mission?",
                            "Need to be filled"
                        )
                    )
                ),
                HelpCenterRootData(
                    "Drop Off",
                    R.drawable.ic_hc_drop_off,
                    persistentListOf(
                        HelpCenterChildData(false, "How to drop off items in this app?", "Need to be filled"),
                        HelpCenterChildData(
                            false,
                            "What information should be filled in when dropping off?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "What items can be dropped off for recycle, non-recycle, or reduce?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "How do I choose a suitable drop off location?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "Is there a fee for dropping off items through this app?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "How do I know that my items have been received by the app team?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "When will I receive my points after dropping off?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "How do I check the points assessment/status for items that have been dropped off?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "How long does it take to assess the items I drop off?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "How can I find out how many points I have earned from dropping off?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "Is there a limit to the number of items I can drop off in a single shipment?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "Can I schedule a drop off for a specific time?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "Can I edit the drop off information after sending the request?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "What if I enter the wrong address or item details during drop off?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "Will I be notified once the team has assessed my drop off?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "Are there any guidelines for packing items before dropping off?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "Can I cancel a drop off after submitting a request?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "How do I contact the app team if there is an issue with the drop off?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "What to do if the points don’t arrive in my account after drop off assessment?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "Is there any way to view drop off history and points earned?",
                            "Need to be filled"
                        )
                    )
                ),
                HelpCenterRootData(
                    "Pick Up",
                    R.drawable.ic_hc_pick_up,
                    persistentListOf(
                        HelpCenterChildData(
                            false,
                            "How do I submit a pick-up request in this app?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "What items can be picked up for recycle, non-recycle or reduce?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "How do I choose a suitable pick up location?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "Is there a fee for the pick-up service through this app?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "When will the app team come to pick up my proposed items?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "How will I know that my item has been picked up by the app team?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "When will I receive my points after the item is picked up?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "How do I check the assessment status for the items that have been picked up?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "How long does it take to score an item after it has been picked up?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "Is there a limit to the number of items I can request for pick up in a single shipment?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "Can I schedule a specific time for pick up?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "What should I do if I enter the wrong address or item details when requesting a pick up?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "Can I edit the pick up information after submitting the request?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "Will I be notified once the team has assessed my item for pick up?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "Are there any guidelines for preparing items before being picked up by the app team?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "What if my item is not picked up at the scheduled time?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "Can I cancel the pick up request after submitting the request?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "What to do if the points are not credited to the account after the pick up assessment?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "How do I contact the app team if there is a problem with the pick up service?",
                            "Need to be filled"
                        )
                    )
                ),
                HelpCenterRootData(
                    "My Transaction History",
                    R.drawable.ic_hc_transaction_history,
                    persistentListOf(
                        HelpCenterChildData(
                            false,
                            "How do I access my transaction records in this app?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "What is included in the transaction record?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "How do I check the drop off status in the transaction record?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "How do I check the pick up status on the transaction record?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "Can I see the details of the incoming coins from each transaction?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "How do I know when the coins from drop off or pick up are in my account?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "Does the transaction record include canceled transactions?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "Can I filter transaction records by type (drop off, pick up, coin entry, etc.)?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "Can I export or download my transaction log?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "How do I correct errors in transaction records?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "What should I do if a transaction does not appear in the log?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "Is there a time limit for saving transaction records in the app?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "Can I see the details of points received from each transaction?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "Does the transaction log show the complete time of each activity?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "How can I check the transactions that are being processed (e.g. item valuation)?",
                            "Need to be filled"
                        ),
                        HelpCenterChildData(
                            false,
                            "Can I view older transactions or are only the most recent transactions displayed?",
                            "Need to be filled"
                        )
                    )
                )
            )
        }
    }
}
