package com.example.baato_assessment.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baato_assessment.api.NearbyPlace
import com.example.baato_assessment.views.popups.MarkerInfoPopup
import kotlinx.coroutines.launch


class PlaceShowManager : ViewModel() {
    fun setSelectedPlace(place: NearbyPlace) {
        viewModelScope.launch {
            BottomSheetManager.showSheet(content = { MarkerInfoPopup(place) })
        }

    }

    fun clearSelectedPlace() {
        viewModelScope.launch {
            BottomSheetManager.hideSheet()
        }
    }
}