package com.carissa.revibes.home_admin.presentation.screen

import androidx.compose.ui.text.input.TextFieldValue
import com.carissa.revibes.core.presentation.navigation.NavigationEventBus
import com.carissa.revibes.home_admin.data.AppSettingRepository
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
class ManageDropOffConversionScreenViewModelTest {

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
    fun `loadSettings fills point fields from app setting`() = runTest {
        coEvery { appSettingRepository.getAppSetting() } returns AppSettingData(
            point = AppSettingPointData(organic = 3, nonOrganic = 5, b3 = 8)
        )

        val viewModel = ManageDropOffConversionScreenViewModel(appSettingRepository)
        viewModel.test(this) {
            runOnCreate()
            expectState {
                copy(
                    isLoading = false,
                    hasLoaded = true,
                    organicInput = TextFieldValue("3"),
                    nonOrganicInput = TextFieldValue("5"),
                    b3Input = TextFieldValue("8")
                )
            }
        }
    }

    @Test
    fun `saveSettings writes typed points and keeps daily reward`() = runTest {
        val loaded = AppSettingData(
            point = AppSettingPointData(organic = 5, nonOrganic = 5, b3 = 5)
        )
        coEvery { appSettingRepository.getAppSetting() } returns loaded
        coEvery { appSettingRepository.updateAppSetting(any()) } returns Unit

        val viewModel = ManageDropOffConversionScreenViewModel(appSettingRepository)
        viewModel.test(this) {
            runOnCreate()
            expectState {
                copy(
                    isLoading = false,
                    hasLoaded = true,
                    organicInput = TextFieldValue("5"),
                    nonOrganicInput = TextFieldValue("5"),
                    b3Input = TextFieldValue("5")
                )
            }

            containerHost.onEvent(
                ManageDropOffConversionScreenUiEvent.OnNonOrganicChange(TextFieldValue("12"))
            )
            expectState {
                copy(
                    isLoading = false,
                    hasLoaded = true,
                    organicInput = TextFieldValue("5"),
                    nonOrganicInput = TextFieldValue("12"),
                    b3Input = TextFieldValue("5")
                )
            }

            containerHost.onEvent(ManageDropOffConversionScreenUiEvent.SaveSettings)
            expectState {
                copy(
                    isLoading = false,
                    hasLoaded = true,
                    organicInput = TextFieldValue("5"),
                    nonOrganicInput = TextFieldValue("12"),
                    b3Input = TextFieldValue("5"),
                    isSubmitting = true,
                    errorMessage = null,
                    successMessage = null
                )
            }
            expectState {
                copy(
                    isLoading = false,
                    hasLoaded = true,
                    organicInput = TextFieldValue("5"),
                    nonOrganicInput = TextFieldValue("12"),
                    b3Input = TextFieldValue("5"),
                    isSubmitting = false,
                    successMessage = "Drop-off points saved"
                )
            }
        }

        coVerify {
            appSettingRepository.updateAppSetting(
                loaded.copy(point = AppSettingPointData(organic = 5, nonOrganic = 12, b3 = 5))
            )
        }
    }

    @Test
    fun `saveSettings with blank field keeps previous values`() = runTest {
        coEvery { appSettingRepository.getAppSetting() } returns AppSettingData()

        val viewModel = ManageDropOffConversionScreenViewModel(appSettingRepository)
        viewModel.test(this) {
            runOnCreate()
            expectState {
                copy(
                    isLoading = false,
                    hasLoaded = true,
                    organicInput = TextFieldValue("5"),
                    nonOrganicInput = TextFieldValue("5"),
                    b3Input = TextFieldValue("5")
                )
            }

            containerHost.onEvent(
                ManageDropOffConversionScreenUiEvent.OnOrganicChange(TextFieldValue(""))
            )
            expectState {
                copy(
                    isLoading = false,
                    hasLoaded = true,
                    organicInput = TextFieldValue(""),
                    nonOrganicInput = TextFieldValue("5"),
                    b3Input = TextFieldValue("5")
                )
            }

            containerHost.onEvent(ManageDropOffConversionScreenUiEvent.SaveSettings)
            expectState {
                copy(
                    isLoading = false,
                    hasLoaded = true,
                    organicInput = TextFieldValue(""),
                    nonOrganicInput = TextFieldValue("5"),
                    b3Input = TextFieldValue("5"),
                    errorMessage = "Enter a point value for every waste type"
                )
            }
        }

        coVerify(exactly = 0) { appSettingRepository.updateAppSetting(any()) }
    }
}
