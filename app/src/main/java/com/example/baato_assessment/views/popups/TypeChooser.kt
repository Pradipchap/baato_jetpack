package com.example.baato_assessment.views.popups

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.baato_assessment.api.Category
import com.example.baato_assessment.viewModel.MapManager
import com.example.baato_assessment.viewModel.MapManager.fetchPlacesInCurrentViewport

@Composable
fun CategorySelector(
    categories: List<Category>,
    selectedCategory: String?,
    onCategorySelected: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        categories.forEach { category ->
            val isSelected = selectedCategory == category.value
            ElevatedButton(
                onClick = { onCategorySelected(category.value) },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray,
                    contentColor = if (isSelected) Color.White else Color.Black
                ),
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Text(text = category.label)
            }
        }
    }
}

@Composable
fun CategorySelectorPreview() {
    val categories = listOf(
        Category("Hospitals", "hospital"),
        Category("Food", "eat"),
        Category("Hotels", "hotel"),
        Category("Grocery", "grocery"),
        Category("Entertainment", "entertainment"),
        Category("Tourism", "tourism")
    )
    var selectedCategory by remember { mutableStateOf<String?>(null) }


    LaunchedEffect(selectedCategory) {
        if (selectedCategory != null && MapManager.map != null) {
            MapManager?.map?.addOnCameraIdleListener {

                if (selectedCategory != null) {
                    fetchPlacesInCurrentViewport(selectedCategory ?: "")
                }
            }
        }
    }

    fun onCategorySelected(category: String) {
        if (selectedCategory == category) {
            selectedCategory = null
            MapManager.clearMapFeatures()
        } else {
            selectedCategory = category
            fetchPlacesInCurrentViewport(category)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .zIndex(1f)
            .padding(top = 16.dp)
    ) {
        CategorySelector(
            categories = categories,
            selectedCategory = selectedCategory,
            onCategorySelected = { onCategorySelected(it) }
        )
    }
}