package com.example.color_draw

import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import com.example.color_draw.ui.theme.PaintData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PaintViewModel : ViewModel() {

    private var _eyePaintState = MutableStateFlow<List<PaintData>?>(null)
    val eyePaintState : StateFlow<List<PaintData>?> = _eyePaintState

    private var _mousePaintState = MutableStateFlow<List<PaintData>?>(null)
    val mousePaintState : StateFlow<List<PaintData>?> = _mousePaintState

    private var _accessoryPaintState =MutableStateFlow<List<PaintData>?>(null)
    val accessoryPaintState : StateFlow<List<PaintData>?> = _accessoryPaintState

    fun updateEyePaint(paintData: List<PaintData>?){
        _eyePaintState.value = paintData
    }

    fun setEyePaint(): List<PaintData>?{
        return _eyePaintState.value
    }

    fun updateMousePaint(paintData: List<PaintData>?) {
        _mousePaintState.value = paintData
    }

    fun setMousePaint(): List<PaintData>?{
        return _mousePaintState.value
    }

    fun updateAccessoryPaint(paintData: List<PaintData>?) {
        _accessoryPaintState.value = paintData
    }

    fun setAccessoryPaint(): List<PaintData>?{
        return _accessoryPaintState.value
    }


}