package com.example.smartdigitalbuspass.utils

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import com.example.smartdigitalbuspass.data.models.BusPass
import com.example.smartdigitalbuspass.data.models.BusRoute
import com.example.smartdigitalbuspass.data.models.User
import java.io.OutputStream

object PassExporter {

    fun downloadPassImage(
        context: Context,
        pass: BusPass,
        route: BusRoute,
        user: User
    ) {
        val width = 800
        val height = 1000
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Background
        canvas.drawColor(Color.WHITE)

        val headerPaint = Paint().apply {
            color = Color.parseColor("#2F4FC9")
            style = Paint.Style.FILL
        }
        canvas.drawRect(0f, 0f, width.toFloat(), 160f, headerPaint)

        // Header Title
        val textPaint = Paint().apply {
            color = Color.WHITE
            textSize = 38f
            isAntiAlias = true
            typeface = Typeface.DEFAULT_BOLD
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("SMART DIGITAL BUS PASS", width / 2f, 100f, textPaint)

        val subTextPaint = Paint().apply {
            color = Color.DKGRAY
            textSize = 28f
            isAntiAlias = true
            typeface = Typeface.DEFAULT_BOLD
        }
        val labelPaint = Paint().apply {
            color = Color.GRAY
            textSize = 22f
            isAntiAlias = true
        }

        var y = 230f
        canvas.drawText("PASS ID:", 60f, y, labelPaint)
        canvas.drawText(pass.id, 240f, y, subTextPaint)

        y += 60f
        canvas.drawText("PASSENGER:", 60f, y, labelPaint)
        canvas.drawText("${user.name} (${user.role.name})", 240f, y, subTextPaint)

        y += 60f
        canvas.drawText("ROUTE:", 60f, y, labelPaint)
        canvas.drawText("${route.fromPlace} to ${route.toPlace}", 240f, y, subTextPaint)

        y += 60f
        canvas.drawText("VALIDITY:", 60f, y, labelPaint)
        canvas.drawText("${pass.startDate} to ${pass.endDate}", 240f, y, subTextPaint)

        y += 60f
        canvas.drawText("STATUS:", 60f, y, labelPaint)
        val statusPaint = Paint(subTextPaint).apply {
            color = if (pass.status.name == "ACTIVE") Color.parseColor("#10B981") else Color.RED
        }
        canvas.drawText(pass.status.name, 240f, y, statusPaint)

        // QR Code
        val qrBitmap = QrCodeGenerator.generateQrBitmap(pass.id, 340)
        canvas.drawBitmap(qrBitmap, (width - 340) / 2f, y + 40f, null)

        // Footer Text
        val footerPaint = Paint().apply {
            color = Color.GRAY
            textSize = 20f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        canvas.drawText("Show this QR to conductor for validation", width / 2f, height - 40f, footerPaint)

        // Save Bitmap to MediaStore
        saveBitmapToGallery(context, bitmap, "BusPass_${pass.id}.png")
    }

    private fun saveBitmapToGallery(context: Context, bitmap: Bitmap, fileName: String) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/SmartDigitalBusPass")
            }
        }

        val resolver = context.contentResolver
        val uri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        if (uri != null) {
            try {
                val outputStream: OutputStream? = resolver.openOutputStream(uri)
                if (outputStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    outputStream.close()
                    Toast.makeText(context, "Pass saved to Gallery successfully!", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error saving pass: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, "Failed to create gallery entry", Toast.LENGTH_SHORT).show()
        }
    }

    fun sharePassDetails(context: Context, pass: BusPass, route: BusRoute, userName: String) {
        val shareText = """
            🚌 SMART DIGITAL BUS PASS
            -------------------------
            Pass ID: ${pass.id}
            Passenger: $userName
            Route: ${route.fromPlace} to ${route.toPlace}
            Valid From: ${pass.startDate}
            Valid Till: ${pass.endDate}
            Status: ${pass.status.name}
            -------------------------
            Verified digital bus pass system.
        """.trimIndent()

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Smart Digital Bus Pass - ${pass.id}")
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        context.startActivity(Intent.createChooser(intent, "Share Bus Pass"))
    }
}
