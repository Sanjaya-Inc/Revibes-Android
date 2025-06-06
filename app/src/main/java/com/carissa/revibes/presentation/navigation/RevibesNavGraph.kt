package com.carissa.revibes.presentation.navigation

import com.carissa.revibes.core.presentation.navigation.RevibesHostNavigationStyle
import com.ramcosta.composedestinations.annotation.NavHostGraph

@NavHostGraph(defaultTransitions = RevibesHostNavigationStyle::class)
annotation class RevibesNavGraph {

    companion object Includes
}
