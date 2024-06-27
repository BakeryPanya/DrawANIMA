package com.example.color_draw.ui.theme

import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.example.color_draw.R

@Composable
fun PreviewAnimation(setPaint:(List<PaintData>?)->Unit,legacyTracks:List<PaintData>? = null,nextButton:()->Unit = {},previousButton:()->Unit = {}) {
    // 描画の履歴の記録のため
    val tracks = rememberSaveable { mutableStateOf<List<PaintData>?>(null) }
    var penSize by remember { mutableFloatStateOf(4f) }
    var penColor by remember { mutableStateOf(Color.Black) }
    var showPenSizeSlider by remember { mutableStateOf(false) }
    var showColorPicker by remember { mutableStateOf(false) }
    var flag by remember { mutableStateOf(false) }
    var time by remember { mutableStateOf(0f) }
    val legacyTrack = rememberSaveable{ mutableStateOf<List<PaintData>?>(legacyTracks) }


    Scaffold(
        bottomBar = {
            Column {
                if(showColorPicker) {
                    ColorPicker(penColor = penColor) { color -> penColor = color }
                }

                if(showPenSizeSlider){
                    Row {
                        Slider(
                            value = penSize,
                            valueRange = 0f..100f,
                            onValueChange = {
                                penSize = it
                            },
                            modifier = Modifier.fillMaxWidth(0.8f)
                        )
                        Text(text = String.format("%.2f", penSize), modifier = Modifier.align(
                            CenterVertically
                        ))
                    }
                }
                BottomAppBar(
                    actions = {
                        IconButton(onClick = { time += 5f}) {
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
                            setPaint(tracks.value)
                            nextButton()
                        }) {
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = "次に移動",
                            )
                        }
                        IconButton(onClick = {
                            previousButton()
                        }) {
                            Icon(
                                Icons.Filled.Refresh,
                                contentDescription = "前に戻る",
                            )
                        }
                    },
//                    floatingActionButton = {
//                        FloatingActionButton(
//                            onClick = {
//                                setPaint(tracks.value)
//                            },
//                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
//                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
//                        ) {
//                            Icon(Icons.Filled.ExitToApp, "保存")
//                        }
//                    }
                )
            }
        }
    ) {
        PreviewCanvas(tracks = tracks, legacyTracks = legacyTrack.value, penSize = penSize, penColor = penColor, canvasHeight = it,images = ImageBitmap.imageResource(id = R.drawable.body1),time = time)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PreviewCanvas(tracks: MutableState<List<PaintData>?>, legacyTracks: List<PaintData>?, penSize: Float, penColor: Color, canvasHeight: PaddingValues, images: ImageBitmap,time:Float) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
//            .pointerInteropFilter { motionEvent: MotionEvent ->
//                when (motionEvent.action) {
//                    // 描き始めの処理
//                    MotionEvent.ACTION_DOWN -> {
//                        tracks.value = ArrayList<PaintData>().apply {
//                            tracks.value?.let { addAll(it) }
//                            add(
//                                PaintData(
//                                    path = DrawingPathRoute.MoveTo(motionEvent.x, motionEvent.y),
//                                    color = penColor,
//                                    size = penSize
//                                )
//                            )
//                        }
//                    }
//                    // 書いてる途中の処理
//                    MotionEvent.ACTION_MOVE -> {
//                        tracks.value = ArrayList<PaintData>().apply {
//                            tracks.value?.let { addAll(it) }
//                            add(
//                                PaintData(
//                                    path = DrawingPathRoute.LineTo(motionEvent.x, motionEvent.y),
//                                    color = penColor,
//                                    size = penSize
//                                )
//                            )
//                        }
//                    }
//
//                    else -> false
//                }
//                true
//            }
    ) {

        var currentPath = Path()
        var currentSize = penSize
        val x = size.width
        val y = size.height

        inset(
            top = 0f,
            bottom = 0f,
            left = time,
            right = 0f
        ){

            drawImage(
                image = images,
                topLeft = Offset(x / 2 - images.width / 2 , y / 2 - images.height / 2),
            )


            tracks.let {
                tracks.value?.forEach { PaintData ->
                    when (PaintData.path) {

                        is DrawingPathRoute.MoveTo -> {
                            currentPath = Path().apply { moveTo(PaintData.path.x, PaintData.path.y) }//最初の線を引くポイントを指定する。
                            currentSize = PaintData.size
                            PaintData.color =  penColor
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

            legacyTracks.let {
                legacyTracks?.forEach { PaintData ->
                    when (PaintData.path) {
                        is DrawingPathRoute.MoveTo -> {
                            val dimmedColor = penColor.copy(alpha = 0.5f) // アルファ値を 0.5 に設定
                            currentPath = Path().apply { moveTo(PaintData.path.x, PaintData.path.y) }//最初の線を引くポイントを指定する。
                            currentSize = PaintData.size
                            PaintData.color = dimmedColor
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
}