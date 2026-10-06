package com.example.smartdigitalbuspass.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.util.Random

object QrCodeGenerator {

    const val GRID_SIZE = 17

    fun isDarkSquare(r: Int, c: Int, seed: Int): Boolean {
        // Top-Left Finder Pattern (5x5)
        if (r in 0..4 && c in 0..4) {
            return (r == 0 || r == 4 || c == 0 || c == 4) || (r == 2 && c == 2)
        }
        // Top-Right Finder Pattern (5x5)
        if (r in 0..4 && c in 12..16) {
            val cc = c - 12
            return (r == 0 || r == 4 || cc == 0 || cc == 4) || (r == 2 && cc == 2)
        }
        // Bottom-Left Finder Pattern (5x5)
        if (r in 12..16 && c in 0..4) {
            val rr = r - 12
            return (rr == 0 || rr == 4 || c == 0 || c == 4) || (rr == 2 && c == 2)
        }
        // Deterministic pseudo-random pattern based on seed + coordinates
        val cellHash = (seed * 31 + r * 17 + c * 13)
        val rand = Random(cellHash.toLong())
        return rand.nextBoolean()
    }

    fun generateQrBitmap(passId: String, sizePx: Int = 400): Bitmap {
        val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(android.graphics.Color.WHITE)

        val paintDark = Paint().apply {
            color = android.graphics.Color.BLACK
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        val padding = sizePx * 0.05f
        val qrAreaSize = sizePx - 2 * padding
        val cellSize = qrAreaSize / GRID_SIZE
        val seed = passId.hashCode()

        for (r in 0 until GRID_SIZE) {
            for (c in 0 until GRID_SIZE) {
                if (isDarkSquare(r, c, seed)) {
                    val left = padding + c * cellSize
                    val top = padding + r * cellSize
                    val right = left + cellSize
                    val bottom = top + cellSize
                    canvas.drawRect(left, top, right, bottom, paintDark)
                }
            }
        }
        return bitmap
    }
}

@Composable
fun DemoQrCodeView(
    passId: String,
    modifier: Modifier = Modifier,
    sizeDp: Dp = 160.dp
) {
    val seed = passId.hashCode()
    Box(
        modifier = modifier
            .size(sizeDp)
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .border(1.dp, Color.LightGray, shape = RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val canvasSize = size.minDimension
            val cellSize = canvasSize / QrCodeGenerator.GRID_SIZE

            for (r in 0 until QrCodeGenerator.GRID_SIZE) {
                for (c in 0 until QrCodeGenerator.GRID_SIZE) {
                    if (QrCodeGenerator.isDarkSquare(r, c, seed)) {
                        drawRect(
                            color = Color.Black,
                            topLeft = Offset(c * cellSize, r * cellSize),
                            size = Size(cellSize, cellSize)
                        )
                    }
                }
            }
        }
    }
}
