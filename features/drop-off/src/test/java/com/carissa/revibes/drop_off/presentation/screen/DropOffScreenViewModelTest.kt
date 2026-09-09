package com.carissa.revibes.drop_off.presentation.screen

import android.util.Log
import com.carissa.revibes.core.data.main.remote.config.ConfigRepository
import com.carissa.revibes.core.data.utils.ApiException
import com.carissa.revibes.core.domain.utils.GeneralErrorMapper
import com.carissa.revibes.core.presentation.navigation.NavigationEventBus
import com.carissa.revibes.drop_off.data.DropOffRepository
import com.carissa.revibes.drop_off.domain.model.StoreData
import com.carissa.revibes.drop_off.presentation.handler.DropOffExceptionHandler
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.collections.immutable.toImmutableList
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
class DropOffScreenViewModelTest {

    private val dropOffRepository = mockk<DropOffRepository>()
    private val configRepository = mockk<ConfigRepository>()
    private val exceptionHandler = DropOffExceptionHandler(GeneralErrorMapper())
    private val testDispatcher = UnconfinedTestDispatcher()
    private val store = StoreData(
        id = "store1",
        name = "Green Store",
        country = "ID",
        address = "Jl. Merdeka",
        postalCode = "12345",
        latitude = -6.2,
        longitude = 106.8,
        distance = 1.2,
        status = "success"
    )

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0
        startKoin {
            modules(
                module {
                    single<NavigationEventBus> { mockk(relaxed = true) }
                }
            )
        }
        every { configRepository.getDropOffFeatureFlagEnabled() } returns true
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(Log::class)
        stopKoin()
        Dispatchers.resetMain()
    }

    @Test
    fun `add item failure keeps form visible and clears loading`() = runTest {
        coEvery { dropOffRepository.getStores(any(), any()) } returns listOf(store)
        coEvery { dropOffRepository.createLogisticOrder() } returns "order-1"
        coEvery { dropOffRepository.createLogisticOrderItem("order-1") } throws ApiException(
            statusCode = 500,
            errorResponse = null
        )

        val viewModel = DropOffScreenViewModel(
            dropOffRepository,
            exceptionHandler,
            configRepository
        )
        viewModel.test(this) {
            runOnCreate()
            expectState { copy(isLoading = true, error = null) }
            expectState {
                copy(
                    isLoading = false,
                    currentOrderId = "order-1",
                    stores = listOf(store).toImmutableList()
                )
            }

            containerHost.onEvent(DropOffScreenUiEvent.AddItemToOrder("order-1"))
            expectState { copy(isAddingItem = true) }
            expectState { copy(isAddingItem = false, isLoading = false) }
            expectSideEffect(
                DropOffScreenUiEvent.OnError("Server error (500). Please try again later.")
            )
        }
    }

    @Test
    fun `initial load failure shows retryable error instead of infinite loader`() = runTest {
        coEvery { dropOffRepository.getStores(any(), any()) } throws ApiException(
            statusCode = 500,
            errorResponse = null
        )
        coEvery { dropOffRepository.createLogisticOrder() } returns "order-1"

        val viewModel = DropOffScreenViewModel(
            dropOffRepository,
            exceptionHandler,
            configRepository
        )
        viewModel.test(this) {
            runOnCreate()
            expectState { copy(isLoading = true, error = null) }
            expectState {
                copy(
                    isLoading = false,
                    error = "Server error (500). Please try again later."
                )
            }
        }
    }
}
