package com.carissa.revibes.home_admin.presentation.screen

import androidx.compose.ui.text.input.TextFieldValue
import com.carissa.revibes.core.presentation.navigation.NavigationEventBus
import com.carissa.revibes.home_admin.data.AppSettingRepository
import com.carissa.revibes.home_admin.data.model.AppSettingDailyRewardData
import com.carissa.revibes.home_admin.data.model.AppSettingData
import com.carissa.revibes.home_admin.data.model.AppSettingPointData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.orbitmvi.orbit.test.test

@OptIn(ExperimentalCoroutinesApi::class)
class ManageDailyCheckInScreenViewModelTest {

    private val appSettingRepository = mockk<AppSettingRepository>()
    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        startKoin {
            modules(
                module {
                    single<NavigationEventBus> { mockk(relaxed = true) }
                }
            )
        }
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
        Dispatchers.resetMain()
    }

    @Test
    fun `loadSettings fills banner text and point amount from app setting`() = runTest {
        coEvery { appSettingRepository.getAppSetting() } returns AppSettingData(
            point = AppSettingPointData(organic = 5, nonOrganic = 5, b3 = 5),
            dailyReward = AppSettingDailyRewardData(
                days = 7,
                initialPoint = 3,
                multiplier = 0,
                bannerText = "Check in daily to earn 3 points"
            )
        )

        val viewModel = ManageDailyCheckInScreenViewModel(appSettingRepository)
        viewModel.test(this) {
            runOnCreate()
            expectState {
                copy(
                    isLoading = false,
                    hasLoaded = true,
                    bannerTextInput = TextFieldValue("Check in daily to earn 3 points"),
                    pointAmountInput = TextFieldValue("3"),
                    dailyReward = AppSettingDailyRewardData(
                        days = 7,
                        initialPoint = 3,
                        multiplier = 0,
                        bannerText = "Check in daily to earn 3 points"
                    )
                )
            }
        }
    }

    @Test
    fun `saveSettings updates banner text and point amount`() = runTest {
        val loaded = AppSettingData(
            point = AppSettingPointData(organic = 5, nonOrganic = 5, b3 = 5),
            dailyReward = AppSettingDailyRewardData(
                days = 7,
                initialPoint = 1,
                multiplier = 0,
                bannerText = "Old banner"
            )
        )
        coEvery { appSettingRepository.getAppSetting() } returns loaded
        coEvery { appSettingRepository.updateAppSetting(any()) } returns Unit

        val viewModel = ManageDailyCheckInScreenViewModel(appSettingRepository)
        viewModel.test(this) {
            runOnCreate()
            expectState {
                copy(
                    isLoading = false,
                    hasLoaded = true,
                    bannerTextInput = TextFieldValue("Old banner"),
                    pointAmountInput = TextFieldValue("1"),
                    dailyReward = loaded.dailyReward
                )
            }

            containerHost.onEvent(
                ManageDailyCheckInScreenUiEvent.OnBannerTextChange(TextFieldValue("New banner text"))
            )
            expectState {
                copy(
                    isLoading = false,
                    hasLoaded = true,
                    bannerTextInput = TextFieldValue("New banner text"),
                    pointAmountInput = TextFieldValue("1"),
                    dailyReward = loaded.dailyReward
                )
            }

            containerHost.onEvent(
                ManageDailyCheckInScreenUiEvent.OnPointAmountChange(TextFieldValue("5"))
            )
            expectState {
                copy(
                    isLoading = false,
                    hasLoaded = true,
                    bannerTextInput = TextFieldValue("New banner text"),
                    pointAmountInput = TextFieldValue("5"),
                    dailyReward = loaded.dailyReward
                )
            }

            containerHost.onEvent(ManageDailyCheckInScreenUiEvent.SaveSettings)
            expectState {
                copy(
                    isLoading = false,
                    hasLoaded = true,
                    bannerTextInput = TextFieldValue("New banner text"),
                    pointAmountInput = TextFieldValue("5"),
                    isSubmitting = true,
                    errorMessage = null,
                    successMessage = null,
                    dailyReward = loaded.dailyReward
                )
            }
            expectState {
                copy(
                    isLoading = false,
                    hasLoaded = true,
                    bannerTextInput = TextFieldValue("New banner text"),
                    pointAmountInput = TextFieldValue("5"),
                    isSubmitting = false,
                    successMessage = "Daily check-in settings saved",
                    dailyReward = loaded.dailyReward
                )
            }
        }
        coVerify {
            appSettingRepository.updateAppSetting(
                AppSettingData(
                    point = AppSettingPointData(organic = 5, nonOrganic = 5, b3 = 5),
                    dailyReward = AppSettingDailyRewardData(
                        days = 7,
                        initialPoint = 5,
                        multiplier = 0,
                        bannerText = "New banner text"
                    )
                )
            )
        }
    }

    @Test
    fun `saveSettings rejects empty point amount`() = runTest {
        val loaded = AppSettingData(
            point = AppSettingPointData(organic = 5, nonOrganic = 5, b3 = 5),
            dailyReward = AppSettingDailyRewardData(
                days = 7,
                initialPoint = 1,
                multiplier = 0,
                bannerText = "Banner text"
            )
        )
        coEvery { appSettingRepository.getAppSetting() } returns loaded

        val viewModel = ManageDailyCheckInScreenViewModel(appSettingRepository)
        viewModel.test(this) {
            runOnCreate()
            expectState {
                copy(
                    isLoading = false,
                    hasLoaded = true,
                    bannerTextInput = TextFieldValue("Banner text"),
                    pointAmountInput = TextFieldValue("1"),
                    dailyReward = loaded.dailyReward
                )
            }

            containerHost.onEvent(
                ManageDailyCheckInScreenUiEvent.OnPointAmountChange(TextFieldValue(""))
            )
            expectState {
                copy(
                    isLoading = false,
                    hasLoaded = true,
                    bannerTextInput = TextFieldValue("Banner text"),
                    pointAmountInput = TextFieldValue(""),
                    dailyReward = loaded.dailyReward
                )
            }

            containerHost.onEvent(ManageDailyCheckInScreenUiEvent.SaveSettings)
            expectState {
                copy(
                    isLoading = false,
                    hasLoaded = true,
                    bannerTextInput = TextFieldValue("Banner text"),
                    pointAmountInput = TextFieldValue(""),
                    errorMessage = "Point amount is required",
                    dailyReward = loaded.dailyReward
                )
            }
        }

        coVerify(exactly = 0) { appSettingRepository.updateAppSetting(any()) }
    }
}
