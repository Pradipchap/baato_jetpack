package com.example.baato_assessment.viewModel

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object BottomSheetManager {
    private val _sheetContent = MutableStateFlow<(@Composable ColumnScope.() -> Unit)?>(null)
    val sheetContent: StateFlow<(@Composable ColumnScope.() -> Unit)?> = _sheetContent

    fun showSheet(content: @Composable ColumnScope.() -> Unit) {
        _sheetContent.value = content
    }

    fun hideSheet() {
        _sheetContent.value = null
    }
}