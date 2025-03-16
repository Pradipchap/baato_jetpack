package com.example.baato_assessment.views.popups


import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.baato_assessment.viewModel.BottomSheetManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomSheet() {
    val sheetContent by BottomSheetManager.sheetContent.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(sheetContent) {
        if (sheetContent != null) {
            scope.launch {
                sheetState.show()
            }
        } else {
            scope.launch {
                sheetState.hide()
            }
        }
    }

    if (sheetContent != null) {
        ModalBottomSheet(
            onDismissRequest = { BottomSheetManager.hideSheet() },
            sheetState = sheetState,
            content = sheetContent!!
        )
    }
}