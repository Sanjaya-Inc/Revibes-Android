/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
 
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME}

#end

import androidx.core.content.edit
import com.carissa.revibes.core.data.local.LocalDataSource

fun interface ${DATA_SOURCE_NAME}DataSourceSetter {
    fun set${DATA_SOURCE_NAME}Value(value: ${VALUE_TYPE})
}

internal class ${DATA_SOURCE_NAME}DataSourceSetterImpl(
    private val localDataSource: LocalDataSource
) : ${DATA_SOURCE_NAME}DataSourceSetter {
    override fun set${DATA_SOURCE_NAME}Value(value: ${VALUE_TYPE}) {
        localDataSource.edit {
            put${VALUE_TYPE}(${DATA_SOURCE_NAME}DataSource.KEY, value)
        }
    }
}
