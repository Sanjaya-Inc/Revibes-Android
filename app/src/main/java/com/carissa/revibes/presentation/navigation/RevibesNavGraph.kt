package com.carissa.revibes.presentation.navigation

import com.carissa.revibes.core.presentation.navigation.RevibesHostNavigationStyle
import com.ramcosta.composedestinations.annotation.ExternalNavGraph
import com.ramcosta.composedestinations.annotation.NavHostGraph
import com.ramcosta.composedestinations.generated.adminmenu.navgraphs.AdminMenuNavGraph
import com.ramcosta.composedestinations.generated.auth.navgraphs.AuthNavGraph
import com.ramcosta.composedestinations.generated.dropoff.navgraphs.DropOffNavGraph
import com.ramcosta.composedestinations.generated.exchangepoints.navgraphs.ExchangePointsNavGraph
import com.ramcosta.composedestinations.generated.helpcenter.navgraphs.HelpCenterNavGraph
import com.ramcosta.composedestinations.generated.home.navgraphs.HomeNavGraph
import com.ramcosta.composedestinations.generated.onboarding.navgraphs.OnboardingNavGraph
import com.ramcosta.composedestinations.generated.pickup.navgraphs.PickUpNavGraph
import com.ramcosta.composedestinations.generated.profile.navgraphs.ProfileNavGraph
import com.ramcosta.composedestinations.generated.transactionhistory.navgraphs.TransactionHistoryNavGraph
import com.ramcosta.composedestinations.generated.shop.navgraphs.ShopNavGraph

@NavHostGraph(defaultTransitions = RevibesHostNavigationStyle::class)
annotation class RevibesNavGraph {

    @ExternalNavGraph<AuthNavGraph>
    @ExternalNavGraph<OnboardingNavGraph>
    @ExternalNavGraph<HomeNavGraph>
    @ExternalNavGraph<ProfileNavGraph>
    @ExternalNavGraph<TransactionHistoryNavGraph>
    @ExternalNavGraph<ShopNavGraph>
    @ExternalNavGraph<ExchangePointsNavGraph>
    @ExternalNavGraph<DropOffNavGraph>
    @ExternalNavGraph<HelpCenterNavGraph>
    @ExternalNavGraph<AdminMenuNavGraph>
    @ExternalNavGraph<PickUpNavGraph>
    companion object Includes
}
