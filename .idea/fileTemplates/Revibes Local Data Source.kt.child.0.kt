/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
 
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME}

#end

import com.carissa.revibes.core.data.main.local.LocalDataSource

fun interface ${DATA_SOURCE_NAME}DataSourceGetter {
    fun get${DATA_SOURCE_NAME}Value(): Result<${VALUE_TYPE}>
}

internal class ${DATA_SOURCE_NAME}DataSourceGetterImpl(
    private val localDataSource: LocalDataSource
) : ${DATA_SOURCE_NAME}DataSourceGetter {
    override fun get${DATA_SOURCE_NAME}Value(): Result<${VALUE_TYPE}> {
        return localDataSource.runCatching {
            get${VALUE_TYPE}(${DATA_SOURCE_NAME}DataSource.KEY, ${DEFAULT_VALUE})
        }
    }
}
