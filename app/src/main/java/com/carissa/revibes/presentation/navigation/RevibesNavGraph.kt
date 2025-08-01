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
import com.ramcosta.composedestinations.generated.homeadmin.navgraphs.HomeAdminNavGraph
import com.ramcosta.composedestinations.generated.manageusers.navgraphs.ManageUsersNavGraph
import com.ramcosta.composedestinations.generated.managetransaction.navgraphs.ManageTransactionNavGraph
import com.ramcosta.composedestinations.generated.managevoucher.navgraphs.ManageVoucherNavGraph
import com.ramcosta.composedestinations.generated.onboarding.navgraphs.OnboardingNavGraph
import com.ramcosta.composedestinations.generated.pickup.navgraphs.PickUpNavGraph
import com.ramcosta.composedestinations.generated.point.navgraphs.PointNavGraph
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
    @ExternalNavGraph<HomeAdminNavGraph>
    @ExternalNavGraph<ManageUsersNavGraph>
    @ExternalNavGraph<ManageTransactionNavGraph>
    @ExternalNavGraph<ManageVoucherNavGraph>
    @ExternalNavGraph<PickUpNavGraph>
    @ExternalNavGraph<PointNavGraph>
    companion object Includes
}
