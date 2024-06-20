package com.example.color_draw

import androidx.lifecycle.ViewModel
import com.example.color_draw.ui.theme.PaintData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PaintViewModel : ViewModel() {

    private var _eyePaintState = MutableStateFlow(PaintData())
    val facePaintState = _eyePaintState.asStateFlow()

    private var _mousePaintState = MutableStateFlow(PaintData())
    val mousePaintState = _mousePaintState.asStateFlow()

    private var _accessoryPaintState = MutableStateFlow(PaintData())
    val accessoryPaintState = _accessoryPaintState.asStateFlow()

    fun updateEyePaint(paintData: PaintData) {
        _eyePaintState.value = paintData
    }

    fun updateMousePaint(paintData: PaintData) {
        _mousePaintState.value = paintData
    }

    fun updateAccessoryPaint(paintData: PaintData) {
        _accessoryPaintState.value = paintData
    }



}