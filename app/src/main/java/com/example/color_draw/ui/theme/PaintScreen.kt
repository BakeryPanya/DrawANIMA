package com.example.color_draw.ui.theme

import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.color_draw.PaintViewModel
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController


@Composable
fun PaintScreen(
    paintviewmodel: PaintViewModel,
){
    DrawingScreen(
//        paintViewmodel = paintviewmodel
    )
}

@Composable
fun Canvas(modifier: Modifier, onDraw: DrawScope.() -> Unit) =
    Spacer(modifier.drawBehind(onDraw))


@Composable
fun DrawingScreen(
//    paintViewmodel: PaintViewModel,
//    eventText: String = "SampleEvent",
//    status: Int = 0 //1だと目、2だと口、3だとアクセサリ,0はどこにも保存しない
    ) {
    // 描画の履歴の記録のため
    val colorTracks = rememberSaveable{ mutableStateOf(PaintData(null, Color.Black, 10f))}
    val _colorTracks = rememberSaveable { mutableStateOf(PaintData(null, Color.Black, 10f))}
    val tracks = rememberSaveable { mutableStateOf<List<PaintData>?>(null) }
    var penSize by remember { mutableFloatStateOf(4f) }
    var penColor by remember { mutableStateOf(Color.Black) }
    var showPenSizeSlider by remember { mutableStateOf(false) }
    var showColorPicker by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            Column {
                if(showColorPicker) {
                    ColorPicker(penColor = colorTracks.value.color) { color -> colorTracks.value.color = color }
                }
                if(showPenSizeSlider){
                    Row {
                        Slider(
                            value = colorTracks.value.size,
                            valueRange = 0f..100f,
                            onValueChange = {
                                penSize = it
                            },
                            modifier = Modifier.fillMaxWidth(0.8f)
                        )
                        Text(text = String.format("%.2f", penSize), modifier = Modifier.align(
                            Alignment.CenterVertically
                        ))
                    }
                }
                BottomAppBar(
                    actions = {
                        IconButton(onClick = { tracks.value = null }) {
                            Icon(Icons.Filled.Delete, contentDescription = "削除")
                        }
                        IconButton(onClick = { showPenSizeSlider = !showPenSizeSlider }) {
                            Icon(
                                Icons.Filled.Edit,
                                contentDescription = "ペンサイズ変更",
                            )
                        }
                        IconButton(onClick = { showColorPicker = !showColorPicker}) {
                            Icon(
                                Icons.Filled.MoreVert,
                                contentDescription = "色の変更",
                            )
                        }
                        IconButton(onClick = {
                            colorTracks.value = _colorTracks.value
                            _colorTracks.value = PaintData(null, Color.Black, 10f)
                        }){
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = "読み込み"
                            )
                        }

                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                _colorTracks.value = colorTracks.value
                                colorTracks.value = PaintData(null, Color.Black, 10f)
                            },
                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                        ) {
                            Icon(Icons.Filled.ExitToApp, "保存")
                        }
                    }
                )
            }
        }
    ) {
        DrawingCanvas(tracks = tracks, penSize = penSize, penColor = penColor, canvasHeight = it)
    }
}


@Suppress("UNUSED_EXPRESSION")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawingCanvas(tracks: MutableState<List<PaintData>?>, penSize: Float, penColor: Color, canvasHeight: PaddingValues) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInteropFilter { motionEvent: MotionEvent ->
                when (motionEvent.action) {
                    // 描き始めの処理
                    MotionEvent.ACTION_DOWN -> {
                        tracks.value = ArrayList<PaintData>().apply {
                            tracks.value?.let { addAll(it) }
                            add(
                                PaintData(
                                    path = DrawingPathRoute.MoveTo(motionEvent.x, motionEvent.y),
                                    color = penColor,
                                    size = penSize
                                )
                            )
                        }
                    }
                    // 書いてる途中の処理
                    MotionEvent.ACTION_MOVE -> {
                        tracks.value = ArrayList<PaintData>().apply {
                            tracks.value?.let { addAll(it) }
                            add(
                                PaintData(
                                    path = DrawingPathRoute.LineTo(motionEvent.x, motionEvent.y),
                                    color = penColor,
                                    size = penSize
                                )
                            )
                        }
                    }

                    else -> false
                }
                true
            }) {

        var currentPath = Path()
        var currentSize = penSize

        tracks.let {
            tracks.value?.forEach { PaintData ->
                when (PaintData.path) {
                    is DrawingPathRoute.MoveTo -> {
                        currentPath = Path().apply { moveTo(PaintData.path.x, PaintData.path.y) }//最初の線を引くポイントを指定する。
                        currentSize = PaintData.size
                        PaintData.color = penColor
                    }
                    is DrawingPathRoute.LineTo -> {
                        drawPath(
                            path = currentPath.apply { lineTo(PaintData.path.x, PaintData.path.y) },
                            color = PaintData.color,
                            style = Stroke(width = currentSize),
                            blendMode = BlendMode.SrcOver
                        )
                    }

                    null -> Log.d("null", "null")
                }
            }
        }
    }
}

@Composable
fun ColorPicker(penColor: Color, onColorChange: (Color) -> Unit) {
    val controller = rememberColorPickerController()
    Column {
        HsvColorPicker(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .padding(20.dp),
            controller = controller,
            initialColor = penColor,
            onColorChanged = { colorEnvelope: ColorEnvelope ->
                onColorChange(colorEnvelope.color)
            }
        )
        Row {
            BrightnessSlider(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(40.dp)
                    .height(20.dp),
                controller = controller,
                initialColor = penColor,
            )
            AlphaTile(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .border(width = 1.dp, color = Color.Black),
                controller = controller
            )
        }
    }
}