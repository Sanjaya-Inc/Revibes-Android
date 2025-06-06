/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
 
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME}

#end
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan(
    "${PACKAGE_NAME}.data",
    "${PACKAGE_NAME}.domain",
    "${PACKAGE_NAME}.presentation",
)
object ${MODULE_NAME}Module
