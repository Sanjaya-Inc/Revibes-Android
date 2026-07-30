package com.carissa.revibes.home_admin.presentation.screen

import androidx.compose.ui.text.input.TextFieldValue
import com.carissa.revibes.core.data.utils.ApiException
import com.carissa.revibes.core.presentation.navigation.NavigationEventBus
import com.carissa.revibes.home_admin.data.NewsRepository
import com.carissa.revibes.home_admin.data.model.NewsData
import io.mockk.coEvery
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
class ManageNewsScreenViewModelTest {

    private val newsRepository = mockk<NewsRepository>()
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
    fun `loadCurrentNews updates state with news data on success`() = runTest {
        val newsData = NewsData(id = "news123", title = "Inflasi", content = "Deskripsi inflasi")
        coEvery { newsRepository.getDailyNews() } returns newsData

        val viewModel = ManageNewsScreenViewModel(newsRepository)
        viewModel.test(this) {
            runOnCreate()
            expectState {
                copy(
                    isLoading = false,
                    currentNewsId = "news123",
                    currentNewsTitle = "Inflasi",
                    currentNewsContent = "Deskripsi inflasi",
                    titleInput = TextFieldValue("Inflasi"),
                    contentInput = TextFieldValue("Deskripsi inflasi")
                )
            }
        }
    }

    @Test
    fun `submitNews with blank fields sets validation error message`() = runTest {
        coEvery { newsRepository.getDailyNews() } returns null

        val viewModel = ManageNewsScreenViewModel(newsRepository)
        viewModel.test(this) {
            runOnCreate()
            expectState { copy(isLoading = false) }

            containerHost.onEvent(ManageNewsScreenUiEvent.SubmitNews)
            expectState { copy(isLoading = false, errorMessage = "Title and Content cannot be empty") }
        }
    }

    @Test
    fun `submitNews when 404 occurs handles error gracefully without raw Ktor error`() = runTest {
        coEvery { newsRepository.getDailyNews() } returns null
        coEvery { newsRepository.createNews(any(), any()) } throws ApiException(
            statusCode = 404,
            errorResponse = null,
            cause = RuntimeException("Client request(POST https://v1-via54wl6oa-et.a.run.app/news) invalid response 404 Not Found")
        )

        val viewModel = ManageNewsScreenViewModel(newsRepository)
        viewModel.test(this) {
            runOnCreate()
            expectState { copy(isLoading = false) }

            containerHost.onEvent(ManageNewsScreenUiEvent.OnTitleChange(TextFieldValue("Inflasi")))
            expectState { copy(isLoading = false, titleInput = TextFieldValue("Inflasi")) }

            containerHost.onEvent(ManageNewsScreenUiEvent.OnContentChange(TextFieldValue("Konten inflasi")))
            expectState {
                copy(
                    isLoading = false,
                    titleInput = TextFieldValue("Inflasi"),
                    contentInput = TextFieldValue("Konten inflasi")
                )
            }

            containerHost.onEvent(ManageNewsScreenUiEvent.SubmitNews)
            expectState {
                copy(
                    isLoading = false,
                    titleInput = TextFieldValue("Inflasi"),
                    contentInput = TextFieldValue("Konten inflasi"),
                    isSubmitting = true,
                    errorMessage = null,
                    successMessage = null
                )
            }
            expectState {
                copy(
                    isLoading = false,
                    titleInput = TextFieldValue("Inflasi"),
                    contentInput = TextFieldValue("Konten inflasi"),
                    isSubmitting = false,
                    errorMessage = "Requested endpoint not found (404)"
                )
            }
        }
    }

    @Test
    fun `submitNews in create mode succeeds and updates state cleanly`() = runTest {
        coEvery { newsRepository.getDailyNews() } returns null
        coEvery { newsRepository.createNews("Inflasi", "Konten inflasi") } returns Unit

        val viewModel = ManageNewsScreenViewModel(newsRepository)
        viewModel.test(this) {
            runOnCreate()
            expectState { copy(isLoading = false) }

            containerHost.onEvent(ManageNewsScreenUiEvent.OnTitleChange(TextFieldValue("Inflasi")))
            expectState { copy(isLoading = false, titleInput = TextFieldValue("Inflasi")) }

            containerHost.onEvent(ManageNewsScreenUiEvent.OnContentChange(TextFieldValue("Konten inflasi")))
            expectState {
                copy(
                    isLoading = false,
                    titleInput = TextFieldValue("Inflasi"),
                    contentInput = TextFieldValue("Konten inflasi")
                )
            }

            containerHost.onEvent(ManageNewsScreenUiEvent.SubmitNews)
            expectState {
                copy(
                    isLoading = false,
                    titleInput = TextFieldValue("Inflasi"),
                    contentInput = TextFieldValue("Konten inflasi"),
                    isSubmitting = true,
                    errorMessage = null,
                    successMessage = null
                )
            }
            expectState {
                copy(
                    isLoading = false,
                    titleInput = TextFieldValue("Inflasi"),
                    contentInput = TextFieldValue("Konten inflasi"),
                    isSubmitting = false,
                    isEditing = false,
                    currentNewsTitle = "Inflasi",
                    currentNewsContent = "Konten inflasi",
                    successMessage = "Daily news published successfully!"
                )
            }
        }
    }

    @Test
    fun `updateNews in edit mode succeeds and updates state cleanly`() = runTest {
        val newsData = NewsData(id = "news123", title = "Old Title", content = "Old Content")
        coEvery { newsRepository.getDailyNews() } returns newsData
        coEvery { newsRepository.updateNews("news123", "New Title", "New Content") } returns Unit

        val viewModel = ManageNewsScreenViewModel(newsRepository)
        viewModel.test(this) {
            runOnCreate()
            expectState {
                copy(
                    isLoading = false,
                    currentNewsId = "news123",
                    currentNewsTitle = "Old Title",
                    currentNewsContent = "Old Content",
                    titleInput = TextFieldValue("Old Title"),
                    contentInput = TextFieldValue("Old Content")
                )
            }

            containerHost.onEvent(ManageNewsScreenUiEvent.OnEditClick)
            expectState {
                copy(
                    isLoading = false,
                    isEditing = true,
                    currentNewsId = "news123",
                    currentNewsTitle = "Old Title",
                    currentNewsContent = "Old Content",
                    titleInput = TextFieldValue("Old Title"),
                    contentInput = TextFieldValue("Old Content")
                )
            }

            containerHost.onEvent(ManageNewsScreenUiEvent.OnTitleChange(TextFieldValue("New Title")))
            expectState {
                copy(
                    isLoading = false,
                    isEditing = true,
                    currentNewsId = "news123",
                    currentNewsTitle = "Old Title",
                    currentNewsContent = "Old Content",
                    titleInput = TextFieldValue("New Title"),
                    contentInput = TextFieldValue("Old Content")
                )
            }

            containerHost.onEvent(ManageNewsScreenUiEvent.OnContentChange(TextFieldValue("New Content")))
            expectState {
                copy(
                    isLoading = false,
                    isEditing = true,
                    currentNewsId = "news123",
                    currentNewsTitle = "Old Title",
                    currentNewsContent = "Old Content",
                    titleInput = TextFieldValue("New Title"),
                    contentInput = TextFieldValue("New Content")
                )
            }

            containerHost.onEvent(ManageNewsScreenUiEvent.SubmitNews)
            expectState {
                copy(
                    isLoading = false,
                    isEditing = true,
                    currentNewsId = "news123",
                    currentNewsTitle = "Old Title",
                    currentNewsContent = "Old Content",
                    titleInput = TextFieldValue("New Title"),
                    contentInput = TextFieldValue("New Content"),
                    isSubmitting = true,
                    errorMessage = null,
                    successMessage = null
                )
            }
            expectState {
                copy(
                    isLoading = false,
                    isEditing = false,
                    currentNewsId = "news123",
                    currentNewsTitle = "New Title",
                    currentNewsContent = "New Content",
                    titleInput = TextFieldValue("New Title"),
                    contentInput = TextFieldValue("New Content"),
                    isSubmitting = false,
                    successMessage = "Daily news updated successfully!"
                )
            }
        }
    }

    @Test
    fun `deleteNews on confirm deletes active news cleanly`() = runTest {
        val newsData = NewsData(id = "news123", title = "Title to Delete", content = "Content to Delete")
        coEvery { newsRepository.getDailyNews() } returns newsData
        coEvery { newsRepository.deleteNews("news123") } returns Unit

        val viewModel = ManageNewsScreenViewModel(newsRepository)
        viewModel.test(this) {
            runOnCreate()
            expectState {
                copy(
                    isLoading = false,
                    currentNewsId = "news123",
                    currentNewsTitle = "Title to Delete",
                    currentNewsContent = "Content to Delete",
                    titleInput = TextFieldValue("Title to Delete"),
                    contentInput = TextFieldValue("Content to Delete")
                )
            }

            containerHost.onEvent(ManageNewsScreenUiEvent.OnDeleteClick)
            expectState {
                copy(
                    isLoading = false,
                    showDeleteDialog = true,
                    currentNewsId = "news123",
                    currentNewsTitle = "Title to Delete",
                    currentNewsContent = "Content to Delete",
                    titleInput = TextFieldValue("Title to Delete"),
                    contentInput = TextFieldValue("Content to Delete")
                )
            }

            containerHost.onEvent(ManageNewsScreenUiEvent.OnConfirmDelete)
            expectState {
                copy(
                    isLoading = false,
                    showDeleteDialog = false,
                    isDeleting = true,
                    currentNewsId = "news123",
                    currentNewsTitle = "Title to Delete",
                    currentNewsContent = "Content to Delete",
                    titleInput = TextFieldValue("Title to Delete"),
                    contentInput = TextFieldValue("Content to Delete"),
                    errorMessage = null,
                    successMessage = null
                )
            }
            expectState {
                copy(
                    isLoading = false,
                    showDeleteDialog = false,
                    isDeleting = false,
                    isEditing = false,
                    currentNewsId = null,
                    currentNewsTitle = null,
                    currentNewsContent = null,
                    titleInput = TextFieldValue(""),
                    contentInput = TextFieldValue(""),
                    successMessage = "Daily news deleted successfully!"
                )
            }
        }
    }
}
