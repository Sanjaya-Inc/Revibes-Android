/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
 
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME}

#end

import com.carissa.revibes.core.data.main.local.LocalDataSource
import org.koin.core.annotation.Single

/**
* Return type ${VALUE_TYPE} with default value ${DEFAULT_VALUE}
**/
interface ${DATA_SOURCE_NAME}DataSource :
    ${DATA_SOURCE_NAME}DataSourceSetter,
    ${DATA_SOURCE_NAME}DataSourceGetter {
    companion object {
        const val KEY = "${KEY}"
    }
}

@Single
internal class ${DATA_SOURCE_NAME}DataSourceImpl(
    private val localDataSource: LocalDataSource
) : ${DATA_SOURCE_NAME}DataSource,
    ${DATA_SOURCE_NAME}DataSourceSetter by ${DATA_SOURCE_NAME}DataSourceSetterImpl(localDataSource),
    ${DATA_SOURCE_NAME}DataSourceGetter by ${DATA_SOURCE_NAME}DataSourceGetterImpl(localDataSource)
