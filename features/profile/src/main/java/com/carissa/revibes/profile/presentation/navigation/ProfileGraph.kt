/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.profile.presentation.navigation

import com.carissa.revibes.core.presentation.navigation.RevibesHostNavigationStyle
import com.ramcosta.composedestinations.annotation.ExternalModuleGraph
import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.parameters.CodeGenVisibility

@NavGraph<ExternalModuleGraph>(
    defaultTransitions = RevibesHostNavigationStyle::class,
    visibility = CodeGenVisibility.PUBLIC,
)
internal annotation class ProfileGraph
