package com.example.ripcurrent.tool.noUse


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun Drawing() {
    var path by remember { mutableStateOf(Path()) }
    var prevX by remember { mutableStateOf(0f) }
    var prevY by remember { mutableStateOf(0f) }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        path.moveTo(it.x, it.y)
                        prevX = it.x
                        prevY = it.y
                        true
                    },
                    onDragEnd = {
                        path.reset()
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val newX = prevX + dragAmount.x
                        val newY = prevY + dragAmount.y
                        path.lineTo(newX, newY)
                        prevX = newX
                        prevY = newY
                    }
                )
            }
    ) {
        drawPath(
            path = path,
            color = Color.Blue,
            alpha = 1f
        )
    }
}