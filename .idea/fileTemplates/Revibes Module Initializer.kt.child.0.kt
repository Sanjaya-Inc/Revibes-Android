/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
 
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME}

#end
import android.content.Context
import androidx.startup.Initializer
import com.carissa.revibes.core.di.KoinInitializer
import com.carissa.revibes.di.AppModule
import org.koin.core.context.loadKoinModules
import org.koin.ksp.generated.module

class ${MODULE_NAME}ModuleInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        loadKoinModules(${MODULE_NAME}Module.module)
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return listOf(KoinInitializer::class.java)
    }
}
