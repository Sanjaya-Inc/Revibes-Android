package com.carissa.revibes.home_admin.presentation.screen

import androidx.compose.ui.text.input.TextFieldValue
import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.home_admin.data.NewsRepository
import org.koin.core.annotation.KoinViewModel

data class ManageNewsScreenUiState(
    val titleInput: TextFieldValue = TextFieldValue(""),
    val contentInput: TextFieldValue = TextFieldValue(""),
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val currentNewsTitle: String? = null,
    val currentNewsContent: String? = null,
    val successMessage: String? = null,
    val errorMessage: String? = null
)

sealed interface ManageNewsScreenUiEvent {
    data object NavigateBack : ManageNewsScreenUiEvent, NavigationEvent
    data class OnTitleChange(val title: TextFieldValue) : ManageNewsScreenUiEvent
    data class OnContentChange(val content: TextFieldValue) : ManageNewsScreenUiEvent
    data object SubmitNews : ManageNewsScreenUiEvent
    data object ClearMessage : ManageNewsScreenUiEvent
    data object LoadCurrentNews : ManageNewsScreenUiEvent
}

@KoinViewModel
class ManageNewsScreenViewModel(
    private val newsRepository: NewsRepository
) : BaseViewModel<ManageNewsScreenUiState, ManageNewsScreenUiEvent>(
    initialState = ManageNewsScreenUiState(isLoading = true),
    onCreate = {
        onEvent(ManageNewsScreenUiEvent.LoadCurrentNews)
    }
) {
    override fun onEvent(event: ManageNewsScreenUiEvent) {
        super.onEvent(event)
        when (event) {
            ManageNewsScreenUiEvent.NavigateBack -> intent {
                postSideEffect(event)
            }
            is ManageNewsScreenUiEvent.OnTitleChange -> intent {
                reduce { state.copy(titleInput = event.title) }
            }
            is ManageNewsScreenUiEvent.OnContentChange -> intent {
                reduce { state.copy(contentInput = event.content) }
            }
            ManageNewsScreenUiEvent.LoadCurrentNews -> loadCurrentNews()
            ManageNewsScreenUiEvent.SubmitNews -> submitNews()
            ManageNewsScreenUiEvent.ClearMessage -> clearMessage()
        }
    }

    private fun loadCurrentNews() {
        intent {
            reduce { state.copy(isLoading = true) }
            runCatching {
                newsRepository.getDailyNews()
            }.onSuccess { news ->
                reduce {
                    state.copy(
                        isLoading = false,
                        currentNewsTitle = news?.title,
                        currentNewsContent = news?.content,
                        titleInput = news?.title?.let { TextFieldValue(it) } ?: state.titleInput,
                        contentInput = news?.content?.let { TextFieldValue(it) } ?: state.contentInput
                    )
                }
            }.onFailure {
                reduce { state.copy(isLoading = false) }
            }
        }
    }

    private fun submitNews() {
        intent {
            val title = state.titleInput.text.trim()
            val content = state.contentInput.text.trim()
            if (title.isBlank() || content.isBlank()) {
                reduce { state.copy(errorMessage = "Title and Content cannot be empty") }
                return@intent
            }

            reduce { state.copy(isSubmitting = true, errorMessage = null, successMessage = null) }
            runCatching {
                newsRepository.createNews(title, content)
            }.onSuccess {
                reduce {
                    state.copy(
                        isSubmitting = false,
                        currentNewsTitle = title,
                        currentNewsContent = content,
                        successMessage = "Daily news published successfully!"
                    )
                }
            }.onFailure { ex ->
                reduce {
                    state.copy(
                        isSubmitting = false,
                        errorMessage = ex.message ?: "Failed to publish daily news"
                    )
                }
            }
        }
    }

    private fun clearMessage() {
        intent {
            reduce { state.copy(successMessage = null, errorMessage = null) }
        }
    }
}
