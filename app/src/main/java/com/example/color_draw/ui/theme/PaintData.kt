package com.example.color_draw.ui.theme

import android.os.Parcelable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import java.io.Serializable

data class PaintData(
    val path: DrawingPathRoute? = null,
    var color: Color = Color.Black,
    var size: Float = 0f
)

sealed class DrawingPathRoute: Serializable{
    abstract val x: Float
    abstract val y: Float

    data class MoveTo(override val x: Float, override val y: Float) : DrawingPathRoute()
    data class LineTo(override val x: Float = 0f, override val y: Float = 0f) : DrawingPathRoute()
}

//val DrawingPathRouteSaver = Saver<DrawingPathRoute, List<Any>>(
//    save = { route ->
//        when (route) {
//            is DrawingPathRoute.MoveTo -> listOf("MoveTo", route.x, route.y)
//            is DrawingPathRoute.LineTo -> listOf("LineTo", route.x, route.y)
//        }
//    },
//    restore = { list ->
//        when (list[0] as String) {
//            "MoveTo" -> DrawingPathRoute.MoveTo(list[1] as Float, list[2] as Float)
//            "LineTo" -> DrawingPathRoute.LineTo(list[1] as Float, list[2] as Float)
//            else -> throw IllegalArgumentException("Unknown DrawingPathRoute type")
//        }
//    }
//)
//
//val PaintDataSaver = Saver<PaintData, List<Any>>(
//    save = { paintData ->
//        listOf(paintData.path?.let { DrawingPathRouteSaver.save(it) } ?: emptyList(),
//            paintData.color.toArgb(),
//            paintData.size
//        )
//    },
//    restore = { list ->
//        PaintData(
//            path = if (list[0] is List<*> && (list[0] as List<*>).isNotEmpty()) {
//                DrawingPathRouteSaver.restore(list[0] as List<Any>)
//            } else {
//                null
//            },
//            color = Color(list[1] as Int),
//            size = list[2] as Float
//        )
//    }
//)
