package com.carissa.revibes.help_center.presentation.model

import androidx.annotation.Keep
import androidx.compose.runtime.Stable
import com.carissa.revibes.core.domain.model.SupportData
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
        fun default(supportData: SupportData): PersistentList<HelpCenterRootData> {
            return persistentListOf(
                HelpCenterRootData(
                    "FAQ",
                    R.drawable.ic_hc_mission,
                    persistentListOf(
                        HelpCenterChildData(
                            false,
                            "How to earn points in Revibes app?",
                            "You can earn points by donating recyclable items at our official drop-off points. " +
                                "Revibes admin officer at each location will verify your donation and help input " +
                                "the points into your Revibes app account."
                        ),
                        HelpCenterChildData(
                            false,
                            "What are the recyclable items that is collected by Revibes?",
                            "The types of recyclable items accepted may vary depending on the event. " +
                                "Please check our latest updates via the Revibes app, official website, or our " +
                                "social media. Currently, we are accepting: wearable clothes (e.g. shirt, " +
                                "t-shirt, pants, etc), used dolls (plushies), milk cartons, and carton " +
                                "(paper packaging: can be carton, boxes, etc).\n\n" +
                                "Note: Make sure the donated items are clean and dry."
                        ),
                        HelpCenterChildData(
                            false,
                            "How long the points will be expired?",
                            "All points collected through the app will expire on December 31, 2025."
                        ),
                        HelpCenterChildData(
                            false,
                            "What can I do with the points I've collected?",
                            "You can redeem your points basic goods that are available in the app. " +
                                "Currently, we have Rice and Sugar available in our apps.\n\n" +
                                "After redeeming, you may choose to take home the items home or donate it to a " +
                                "community in need through our Revibes partner programs."
                        ),
                        HelpCenterChildData(
                            false,
                            "Who should I contact if I have questions or issues with my points?",
                            "For any questions or issues regarding your points, redemption, or donation:\n\n" +
                                "• Email us at ${supportData.supportEmail}. We will respond within 2 business " +
                                "days (48 hours)\n" +
                                "• Call our Admin Center at ${supportData.phoneNumber}. We are available from " +
                                "Monday to Friday from 09.00-17.00 WIB."
                        ),
                        HelpCenterChildData(
                            false,
                            "Are there other ways to support Revibes?",
                            "Of course! You can support us through the following:\n\n" +
                                "• Participating in Revibes events and workshops\n" +
                                "Stay tune on our social media and website for announcements.\n\n" +
                                "• Purchasing Revibes products\n" +
                                "Available on Tokopedia and Shopee. All proceeds will go toward supporting our " +
                                "sustainability and education programs."
                        )
                    )
                )
            )
        }
    }
}
