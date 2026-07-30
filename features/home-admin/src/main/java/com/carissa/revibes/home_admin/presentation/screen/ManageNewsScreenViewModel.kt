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
    val isDeleting: Boolean = false,
    val isEditing: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val currentNewsId: String? = null,
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
    data object OnEditClick : ManageNewsScreenUiEvent
    data object OnCancelEdit : ManageNewsScreenUiEvent
    data object OnDeleteClick : ManageNewsScreenUiEvent
    data object OnDismissDeleteDialog : ManageNewsScreenUiEvent
    data object OnConfirmDelete : ManageNewsScreenUiEvent
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
            ManageNewsScreenUiEvent.OnEditClick -> startEditing()
            ManageNewsScreenUiEvent.OnCancelEdit -> cancelEditing()
            ManageNewsScreenUiEvent.OnDeleteClick -> intent { reduce { state.copy(showDeleteDialog = true) } }
            ManageNewsScreenUiEvent.OnDismissDeleteDialog -> intent { reduce { state.copy(showDeleteDialog = false) } }
            ManageNewsScreenUiEvent.OnConfirmDelete -> deleteNews()
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
                        currentNewsId = news?.id,
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

    private fun startEditing() {
        intent {
            val title = state.currentNewsTitle.orEmpty()
            val content = state.currentNewsContent.orEmpty()
            reduce {
                state.copy(
                    isEditing = true,
                    titleInput = TextFieldValue(title),
                    contentInput = TextFieldValue(content)
                )
            }
        }
    }

    private fun cancelEditing() {
        intent {
            reduce {
                state.copy(
                    isEditing = false,
                    titleInput = TextFieldValue(""),
                    contentInput = TextFieldValue("")
                )
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
            
            val isEditingMode = state.isEditing && !state.currentNewsId.isNullOrEmpty()
            runCatching {
                if (isEditingMode) {
                    newsRepository.updateNews(state.currentNewsId!!, title, content)
                } else {
                    newsRepository.createNews(title, content)
                }
            }.onSuccess {
                reduce {
                    state.copy(
                        isSubmitting = false,
                        isEditing = false,
                        currentNewsTitle = title,
                        currentNewsContent = content,
                        successMessage = if (isEditingMode) "Daily news updated successfully!" else "Daily news published successfully!"
                    )
                }
            }.onFailure { ex ->
                reduce {
                    state.copy(
                        isSubmitting = false,
                        errorMessage = ex.message ?: "Failed to save daily news"
                    )
                }
            }
        }
    }

    private fun deleteNews() {
        intent {
            val newsId = state.currentNewsId
            if (newsId.isNullOrBlank()) {
                reduce { state.copy(showDeleteDialog = false, errorMessage = "No active news to delete") }
                return@intent
            }

            reduce { state.copy(isDeleting = true, showDeleteDialog = false, errorMessage = null, successMessage = null) }
            runCatching {
                newsRepository.deleteNews(newsId)
            }.onSuccess {
                reduce {
                    state.copy(
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
            }.onFailure { ex ->
                reduce {
                    state.copy(
                        isDeleting = false,
                        errorMessage = ex.message ?: "Failed to delete daily news"
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
