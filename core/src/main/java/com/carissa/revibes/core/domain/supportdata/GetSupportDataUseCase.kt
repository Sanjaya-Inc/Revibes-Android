package com.carissa.revibes.core.domain.supportdata

import com.carissa.revibes.core.data.main.remote.config.ConfigRepository
import com.carissa.revibes.core.domain.model.SupportData
import org.koin.core.annotation.Factory

@Factory
class GetSupportDataUseCase(
    private val configRepository: ConfigRepository
) {
    fun getSupportData(): SupportData {
        return SupportData(
            supportEmail = configRepository.getSupportEmail(),
            phoneNumber = configRepository.getPhoneNumber(),
            whatsappNumber = configRepository.getWhatsappNumber(),
            faxNumber = configRepository.getFaxNumber()
        )
    }
}
