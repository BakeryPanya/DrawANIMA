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
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController


@Composable
fun EyeScreen(
    uiState: List<PaintData>?,
    navController: NavHostController,
    load: () -> List<PaintData>?,
    set: (List<PaintData>?) -> Unit,

    ){
    DrawingScreen(
        setPaint = set,
        loadPaint = load,
        legacyTracks = null,
        nextButton = { navController.navigate(Route.MOUSE.name) },
        previousButton = { navController.navigate(Route.HOME.name) }
    )
}

@Composable
fun MouseScreen(
    uiState: List<PaintData>?,
    navController: NavHostController,
    legacyTracks: List<PaintData>?,
    load: () -> List<PaintData>?,
    set: (List<PaintData>?) -> Unit,
){
    DrawingScreen(
        setPaint = set,
        loadPaint = load,
        legacyTracks = uiState,
        nextButton = { navController.navigate(Route.ACCESSORY.name) },
        previousButton = { navController.navigate(Route.EYE.name) }
    )
}

@Composable
fun AccessoryScreen(
    uiState: List<PaintData>?,
    navController: NavHostController,
    legacyTrack1: List<PaintData>?,
    legacyTrack2: List<PaintData>?,
    load: () -> List<PaintData>?,
    set: (List<PaintData>?) -> Unit,
){
    val legacyTracks:List<PaintData> = listOfNotNull(legacyTrack1,legacyTrack2).flatten()

    DrawingScreen(
        setPaint = set,
        loadPaint = load,
        legacyTracks = legacyTracks,
        nextButton = { navController.navigate(Route.PREVIEW.name) },
        previousButton = { navController.navigate(Route.MOUSE.name) }
    )
}
//EYEPAINTSCREENを必ず作る

@Composable
fun Canvas(modifier: Modifier, onDraw: DrawScope.() -> Unit) =
    Spacer(modifier.drawBehind(onDraw))


@Composable
fun DrawingScreen(setPaint:(List<PaintData>?)->Unit , loadPaint:()->List<PaintData>?,legacyTracks:List<PaintData>? = null,nextButton:()->Unit = {},previousButton:()->Unit = {}) {
    // 描画の履歴の記録のため
    val tracks = rememberSaveable { mutableStateOf<List<PaintData>?>(null) }
    var penSize by remember { mutableFloatStateOf(4f) }
    var penColor by remember { mutableStateOf(Color.Black) }
    var showPenSizeSlider by remember { mutableStateOf(false) }
    var showColorPicker by remember { mutableStateOf(false) }

    val legacyTracks = rememberSaveable{ mutableStateOf(legacyTracks) }


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
                        Text(text = String.format("%.2f", penSize), modifier = Modifier.align(CenterVertically))
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
                            tracks.value =  loadPaint()
                        }) {
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = "ペンの変更",
                            )
                        }
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                setPaint(tracks.value)
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

