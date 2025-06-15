package com.carissa.revibes.presentation.navigation

import com.carissa.revibes.core.presentation.navigation.RevibesHostNavigationStyle
import com.ramcosta.composedestinations.annotation.ExternalNavGraph
import com.ramcosta.composedestinations.annotation.NavHostGraph
import com.ramcosta.composedestinations.generated.auth.navgraphs.AuthNavGraph
import com.ramcosta.composedestinations.generated.home.navgraphs.HomeNavGraph
import com.ramcosta.composedestinations.generated.onboarding.navgraphs.OnboardingNavGraph
import com.ramcosta.composedestinations.generated.profile.navgraphs.ProfileNavGraph
import com.ramcosta.composedestinations.generated.transactionhistory.navgraphs.TransactionHistoryNavGraph

@NavHostGraph(defaultTransitions = RevibesHostNavigationStyle::class)
annotation class RevibesNavGraph {

    @ExternalNavGraph<AuthNavGraph>
    @ExternalNavGraph<OnboardingNavGraph>
    @ExternalNavGraph<HomeNavGraph>
    @ExternalNavGraph<ProfileNavGraph>
    @ExternalNavGraph<TransactionHistoryNavGraph>
    companion object Includes
}
