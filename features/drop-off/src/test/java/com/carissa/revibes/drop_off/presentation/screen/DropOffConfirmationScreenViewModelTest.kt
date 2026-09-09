package com.carissa.revibes.drop_off.presentation.screen

import android.util.Log
import com.carissa.revibes.core.data.model.ErrorResponse
import com.carissa.revibes.core.data.utils.ApiException
import com.carissa.revibes.core.domain.utils.GeneralErrorMapper
import com.carissa.revibes.core.presentation.navigation.NavigationEventBus
import com.carissa.revibes.drop_off.data.DropOffRepository
import com.carissa.revibes.drop_off.data.SubmitOrderItemData
import com.carissa.revibes.drop_off.domain.model.StoreData
import com.carissa.revibes.drop_off.presentation.handler.DropOffExceptionHandler
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.orbitmvi.orbit.test.test

@OptIn(ExperimentalCoroutinesApi::class)
class DropOffConfirmationScreenViewModelTest {

    private val dropOffRepository = mockk<DropOffRepository>()
    private val exceptionHandler = DropOffExceptionHandler(GeneralErrorMapper())
    private val navigationEventBus = mockk<NavigationEventBus>(relaxed = true)
    private val testDispatcher = UnconfinedTestDispatcher()
    private val screenArguments = DropOffConfirmationScreenArguments(
        orderId = "order-1",
        type = "drop-off",
        name = "Jane",
        store = StoreData(
            id = "store1",
            name = "Green Store",
            country = "ID",
            address = "Jl. Merdeka",
            postalCode = "12345",
            latitude = -6.2,
            longitude = 106.8,
            distance = 1.2,
            status = "success"
        ),
        items = listOf(
            DropOffItem(
                id = "item-1",
                name = "Leaves",
                type = "organic",
                weight = "< 1 kg" to 1,
                photos = listOf("https://example.com/photo.jpg")
            )
        )
    )

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0
        startKoin {
            modules(
                module {
                    single<NavigationEventBus> { navigationEventBus }
                }
            )
        }
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(Log::class)
        stopKoin()
        Dispatchers.resetMain()
    }

    @Test
    fun `estimate point failure clears calculating points`() = runTest {
        coEvery { dropOffRepository.estimatePoint(any()) } throws ApiException(
            statusCode = 400,
            errorResponse = ErrorResponse(message = "Unable to estimate points")
        )

        val viewModel = DropOffConfirmationScreenViewModel(dropOffRepository, exceptionHandler)
        viewModel.test(this) {
            containerHost.onEvent(DropOffConfirmationScreenUiEvent.InitializeScreen(screenArguments))
            expectState { copy(arguments = screenArguments) }
            expectState { copy(arguments = screenArguments, isEstimatingPoints = true) }
            expectState {
                copy(
                    arguments = screenArguments,
                    isEstimatingPoints = false,
                    isLoading = false
                )
            }
            expectSideEffect(
                DropOffConfirmationScreenUiEvent.OnMakeOrderFailed("Unable to estimate points")
            )
        }
    }

    @Test
    fun `submit failure stops button loading and stays on confirm`() = runTest {
        coEvery { dropOffRepository.estimatePoint(any()) } returns (emptyMap<String, Int>() to 0)
        coEvery {
            dropOffRepository.submitOrder(any(), any(), any(), any(), any(), any())
        } throws ApiException(
            statusCode = 500,
            errorResponse = ErrorResponse(message = "Submit failed")
        )

        val viewModel = DropOffConfirmationScreenViewModel(dropOffRepository, exceptionHandler)
        viewModel.test(this) {
            containerHost.onEvent(DropOffConfirmationScreenUiEvent.InitializeScreen(screenArguments))
            expectState { copy(arguments = screenArguments) }
            expectState { copy(arguments = screenArguments, isEstimatingPoints = true) }
            expectState {
                copy(
                    arguments = screenArguments,
                    isEstimatingPoints = false,
                    itemPoints = emptyMap(),
                    totalPoints = 0
                )
            }

            containerHost.onEvent(DropOffConfirmationScreenUiEvent.MakeOrder(screenArguments))
            expectState { copy(isLoading = true) }
            expectState { copy(isLoading = false, isEstimatingPoints = false) }
            expectSideEffect(
                DropOffConfirmationScreenUiEvent.OnMakeOrderFailed("Submit failed")
            )
        }

        verify(exactly = 0) {
            navigationEventBus.post(DropOffConfirmationScreenUiEvent.NavigateToHome)
        }
    }

    @Test
    fun `submit includes pcs unit from drop-off items`() = runTest {
        val itemsSlot = slot<List<SubmitOrderItemData>>()
        coEvery {
            dropOffRepository.submitOrder(any(), any(), any(), any(), any(), capture(itemsSlot))
        } just Runs

        val arguments = screenArguments.copy(
            items = listOf(
                DropOffItem(
                    id = "item-1",
                    name = "Bottles",
                    type = "non-organic",
                    weight = "5 pcs" to 5,
                    photos = listOf("https://example.com/photo.jpg"),
                    unit = UNIT_PCS
                )
            )
        )
        val viewModel = DropOffConfirmationScreenViewModel(dropOffRepository, exceptionHandler)
        viewModel.test(this) {
            containerHost.onEvent(DropOffConfirmationScreenUiEvent.MakeOrder(arguments))
            expectState { copy(isLoading = true) }
            expectState { copy(isLoading = false) }
        }

        assertEquals("pcs", itemsSlot.captured.single().unit)
        assertEquals(5, itemsSlot.captured.single().weight)
    }
}
