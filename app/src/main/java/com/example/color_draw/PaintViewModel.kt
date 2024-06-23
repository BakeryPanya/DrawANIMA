package com.example.color_draw

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.color_draw.ui.theme.DrawingPathRoute
import com.example.color_draw.ui.theme.PaintData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.runtime.State

class PaintViewModel : ViewModel() {

    private var _eyePaintState = mutableStateOf<List<PaintData>?>(null)
    val eyePaintState : State<List<PaintData>?> = _eyePaintState

    private var _mousePaintState = mutableStateOf<List<PaintData>?>(null)
    val mousePaintState : State<List<PaintData>?> = _mousePaintState

    private var _accessoryPaintState = mutableStateOf<List<PaintData>?>(null)
    val accessoryPaintState : State<List<PaintData>?> = _accessoryPaintState

    fun updateEyePaint(paintData: List<PaintData>?){
        _eyePaintState.value = paintData
    }

    fun setEyePaint(): List<PaintData>?{
        return _eyePaintState.value
    }

    fun updateMousePaint(paintData: List<PaintData>?) {
        _mousePaintState.value = paintData
    }

    fun updateAccessoryPaint(paintData: List<PaintData>?) {
        _accessoryPaintState.value = paintData
    }

}