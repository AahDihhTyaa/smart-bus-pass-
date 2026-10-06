package com.example.smartdigitalbuspass.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object AppIcons {

    private fun createVector(name: String, pathBuilder: ImageVector.Builder.() -> Unit): ImageVector {
        return ImageVector.Builder(
            name = name,
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply(pathBuilder).build()
    }

    private fun buildSimplePath(pathData: androidx.compose.ui.graphics.vector.PathBuilder.() -> Unit): ImageVector.Builder.() -> Unit {
        return {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero,
                pathBuilder = pathData
            )
        }
    }

    val Home: ImageVector by lazy {
        createVector("Home", buildSimplePath {
            moveTo(10f, 20f)
            verticalLineToRelative(-6f)
            horizontalLineToRelative(4f)
            verticalLineToRelative(6f)
            horizontalLineToRelative(5f)
            verticalLineToRelative(-8f)
            horizontalLineToRelative(3f)
            lineTo(12f, 3f)
            lineTo(2f, 12f)
            horizontalLineToRelative(3f)
            verticalLineToRelative(8f)
            close()
        })
    }

    val Person: ImageVector by lazy {
        createVector("Person", buildSimplePath {
            moveTo(12f, 12f)
            curveToRelative(2.21f, 0f, 4f, -1.79f, 4f, -4f)
            curveToRelative(0f, -2.21f, -1.79f, -4f, -4f, -4f)
            curveToRelative(-2.21f, 0f, -4f, 1.79f, -4f, 4f)
            curveToRelative(0f, 2.21f, 1.79f, 4f, 4f, 4f)
            close()
            moveTo(12f, 14f)
            curveToRelative(-2.67f, 0f, -8f, 1.34f, -8f, 4f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(16f)
            verticalLineToRelative(-2f)
            curveToRelative(0f, -2.66f, -5.33f, -4f, -8f, -4f)
            close()
        })
    }

    val Bus: ImageVector by lazy {
        createVector("Bus", buildSimplePath {
            moveTo(4f, 16f)
            curveToRelative(0f, 1.1f, 0.9f, 2f, 2f, 2f)
            verticalLineToRelative(2f)
            curveToRelative(0f, 0.55f, 0.45f, 1f, 1f, 1f)
            horizontalLineToRelative(1f)
            curveToRelative(0.55f, 0f, 1f, -0.45f, 1f, -1f)
            verticalLineToRelative(-2f)
            horizontalLineToRelative(6f)
            verticalLineToRelative(2f)
            curveToRelative(0f, 0.55f, 0.45f, 1f, 1f, 1f)
            horizontalLineToRelative(1f)
            curveToRelative(0.55f, 0f, 1f, -0.45f, 1f, -1f)
            verticalLineToRelative(-2f)
            curveToRelative(1.1f, 0f, 2f, -0.9f, 2f, -2f)
            verticalLineTo(6f)
            curveToRelative(0f, -3.5f, -3.58f, -4f, -8f, -4f)
            curveToRelative(-4.42f, 0f, -8f, 0.5f, -8f, 4f)
            verticalLineToRelative(10f)
            close()
            moveTo(18f, 11f)
            horizontalLineTo(6f)
            verticalLineTo(6f)
            horizontalLineToRelative(12f)
            verticalLineToRelative(5f)
            close()
        })
    }

    val History: ImageVector by lazy {
        createVector("History", buildSimplePath {
            moveTo(13f, 3f)
            curveToRelative(-4.97f, 0f, -9f, 4.03f, -9f, 9f)
            horizontalLineTo(1f)
            lineToRelative(3.89f, 3.89f)
            curveToRelative(0.07f, 0.07f, 0.18f, 0.11f, 0.29f, 0.11f)
            curveToRelative(0.11f, 0f, 0.22f, -0.04f, 0.29f, -0.11f)
            lineTo(9.35f, 12f)
            horizontalLineTo(6.1f)
            curveToRelative(0f, -3.87f, 3.13f, -7f, 7f, -7f)
            curveToRelative(3.87f, 0f, 7f, 3.13f, 7f, 7f)
            curveToRelative(0f, 3.87f, -3.13f, 7f, -7f, 7f)
            curveToRelative(-1.93f, 0f, -3.68f, -0.79f, -4.94f, -2.06f)
            lineToRelative(-1.42f, 1.42f)
            curveTo(8.33f, 19.92f, 10.54f, 21f, 13f, 21f)
            curveToRelative(4.97f, 0f, 9f, -4.03f, 9f, -9f)
            curveToRelative(0f, -4.97f, -4.03f, -9f, -9f, -9f)
            close()
            moveTo(12f, 8f)
            verticalLineToRelative(5f)
            lineToRelative(4.25f, 2.52f)
            lineToRelative(0.77f, -1.28f)
            lineToRelative(-3.52f, -2.09f)
            verticalLineTo(8f)
            horizontalLineTo(12f)
            close()
        })
    }

    val Search: ImageVector by lazy {
        createVector("Search", buildSimplePath {
            moveTo(15.5f, 14f)
            horizontalLineToRelative(-0.79f)
            lineToRelative(-0.28f, -0.27f)
            curveTo(15.41f, 12.59f, 16f, 11.11f, 16f, 9.5f)
            curveTo(16f, 5.91f, 13.09f, 3f, 9.5f, 3f)
            curveTo(5.91f, 3f, 3f, 5.91f, 3f, 9.5f)
            curveTo(3f, 13.09f, 5.91f, 16f, 9.5f, 16f)
            curveToRelative(1.61f, 0f, 3.09f, -0.59f, 4.23f, -1.57f)
            lineToRelative(0.27f, 0.28f)
            verticalLineToRelative(0.79f)
            lineToRelative(5f, 4.99f)
            lineTo(20.49f, 19f)
            lineToRelative(-4.99f, -5f)
            close()
            moveTo(9.5f, 14f)
            curveTo(7.01f, 14f, 5f, 11.99f, 5f, 9.5f)
            curveTo(5f, 7.01f, 7.01f, 5f, 9.5f, 5f)
            curveTo(11.99f, 5f, 14f, 7.01f, 14f, 9.5f)
            curveTo(14f, 11.99f, 11.99f, 14f, 9.5f, 14f)
            close()
        })
    }

    val Clear: ImageVector by lazy {
        createVector("Clear", buildSimplePath {
            moveTo(19f, 6.41f)
            lineTo(17.59f, 5f)
            lineTo(12f, 10.59f)
            lineTo(6.41f, 5f)
            lineTo(5f, 6.41f)
            lineTo(10.59f, 12f)
            lineTo(5f, 17.59f)
            lineTo(6.41f, 19f)
            lineTo(12f, 13.41f)
            lineTo(17.59f, 19f)
            lineTo(19f, 17.59f)
            lineTo(13.41f, 12f)
            close()
        })
    }

    val Close: ImageVector by lazy { Clear }

    val ArrowBack: ImageVector by lazy {
        createVector("ArrowBack", buildSimplePath {
            moveTo(20f, 11f)
            horizontalLineTo(7.83f)
            lineToRelative(5.59f, -5.59f)
            lineTo(12f, 4f)
            lineToRelative(-8f, 8f)
            lineToRelative(8f, 8f)
            lineToRelative(1.41f, -1.41f)
            lineTo(7.83f, 13f)
            horizontalLineTo(20f)
            verticalLineToRelative(-2f)
            close()
        })
    }

    val Check: ImageVector by lazy {
        createVector("Check", buildSimplePath {
            moveTo(9f, 16.17f)
            lineTo(4.83f, 12f)
            lineToRelative(-1.42f, 1.41f)
            lineTo(9f, 19f)
            lineTo(21f, 7f)
            lineToRelative(-1.41f, -1.41f)
            close()
        })
    }

    val CheckCircle: ImageVector by lazy {
        createVector("CheckCircle", buildSimplePath {
            moveTo(12f, 2f)
            curveTo(6.48f, 2f, 2f, 6.48f, 2f, 12f)
            curveToRelative(0f, 5.52f, 4.48f, 10f, 10f, 10f)
            curveToRelative(5.52f, 0f, 10f, -4.48f, 10f, -10f)
            curveTo(22f, 6.48f, 17.52f, 2f, 12f, 2f)
            close()
            moveTo(10f, 17f)
            lineToRelative(-5f, -5f)
            lineToRelative(1.41f, -1.41f)
            lineTo(10f, 14.17f)
            lineToRelative(7.59f, -7.59f)
            lineTo(19f, 8f)
            lineToRelative(-9f, 9f)
            close()
        })
    }

    val Info: ImageVector by lazy {
        createVector("Info", buildSimplePath {
            moveTo(12f, 2f)
            curveTo(6.48f, 2f, 2f, 6.48f, 2f, 12f)
            curveToRelative(0f, 5.52f, 4.48f, 10f, 10f, 10f)
            curveToRelative(5.52f, 0f, 10f, -4.48f, 10f, -10f)
            curveTo(22f, 6.48f, 17.52f, 2f, 12f, 2f)
            close()
            moveTo(13f, 17f)
            horizontalLineToRelative(-2f)
            verticalLineToRelative(-6f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(6f)
            close()
            moveTo(13f, 9f)
            horizontalLineToRelative(-2f)
            verticalLineTo(7f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(2f)
            close()
        })
    }

    val Lock: ImageVector by lazy {
        createVector("Lock", buildSimplePath {
            moveTo(18f, 8f)
            horizontalLineToRelative(-1f)
            verticalLineTo(6f)
            curveToRelative(0f, -2.76f, -2.24f, -5f, -5f, -5f)
            curveTo(9.24f, 1f, 7f, 3.24f, 7f, 6f)
            verticalLineToRelative(2f)
            horizontalLineTo(6f)
            curveToRelative(-1.1f, 0f, -2f, 0.9f, -2f, 2f)
            verticalLineToRelative(10f)
            curveToRelative(0f, 1.1f, 0.9f, 2f, 2f, 2f)
            horizontalLineToRelative(12f)
            curveToRelative(1.1f, 0f, 2f, -0.9f, 2f, -2f)
            verticalLineTo(10f)
            curveToRelative(0f, -1.1f, -0.9f, -2f, -2f, -2f)
            close()
            moveTo(9f, 6f)
            curveToRelative(0f, -1.66f, 1.34f, -3f, 3f, -3f)
            curveToRelative(1.66f, 0f, 3f, 1.34f, 3f, 3f)
            verticalLineToRelative(2f)
            horizontalLineTo(9f)
            verticalLineTo(6f)
            close()
        })
    }

    val Email: ImageVector by lazy {
        createVector("Email", buildSimplePath {
            moveTo(20f, 4f)
            horizontalLineTo(4f)
            curveToRelative(-1.1f, 0f, -1.99f, 0.9f, -1.99f, 2f)
            lineTo(2f, 18f)
            curveToRelative(0f, 1.1f, 0.9f, 2f, 2f, 2f)
            horizontalLineToRelative(16f)
            curveToRelative(1.1f, 0f, 2f, -0.9f, 2f, -2f)
            verticalLineTo(6f)
            curveToRelative(0f, -1.1f, -0.9f, -2f, -2f, -2f)
            close()
            moveTo(20f, 8f)
            lineToRelative(-8f, 5f)
            lineToRelative(-8f, -5f)
            verticalLineTo(6f)
            lineToRelative(8f, 5f)
            lineToRelative(8f, -5f)
            verticalLineToRelative(2f)
            close()
        })
    }

    val Phone: ImageVector by lazy {
        createVector("Phone", buildSimplePath {
            moveTo(6.62f, 10.79f)
            curveToRelative(1.44f, 2.83f, 3.76f, 5.14f, 6.59f, 6.59f)
            lineToRelative(2.2f, -2.2f)
            curveToRelative(0.27f, -0.27f, 0.67f, -0.36f, 1.02f, -0.24f)
            curveToRelative(1.12f, 0.37f, 2.33f, 0.57f, 3.57f, 0.57f)
            curveToRelative(0.55f, 0f, 1f, 0.45f, 1f, 1f)
            verticalLineTo(20f)
            curveToRelative(0f, 0.55f, -0.45f, 1f, -1f, 1f)
            curveTo(10.61f, 21f, 3f, 13.39f, 3f, 4f)
            curveToRelative(0f, -0.55f, 0.45f, -1f, 1f, -1f)
            horizontalLineToRelative(3.5f)
            curveToRelative(0.55f, 0f, 1f, 0.45f, 1f, 1f)
            curveToRelative(0f, 1.25f, 0.2f, 2.45f, 0.57f, 3.57f)
            curveToRelative(0.11f, 0.35f, 0.03f, 0.74f, -0.25f, 1.02f)
            lineToRelative(-2.2f, 2.2f)
            close()
        })
    }

    val Edit: ImageVector by lazy {
        createVector("Edit", buildSimplePath {
            moveTo(3f, 17.25f)
            verticalLineTo(21f)
            horizontalLineToRelative(3.75f)
            lineTo(17.81f, 9.94f)
            lineToRelative(-3.75f, -3.75f)
            lineTo(3f, 17.25f)
            close()
            moveTo(20.71f, 7.04f)
            curveToRelative(0.39f, -0.39f, 0.39f, -1.02f, 0f, -1.41f)
            lineToRelative(-2.34f, -2.34f)
            curveToRelative(-0.39f, -0.39f, -1.02f, -0.39f, -1.41f, 0f)
            lineToRelative(-1.83f, 1.83f)
            lineToRelative(3.75f, 3.75f)
            lineToRelative(1.83f, -1.83f)
            close()
        })
    }

    val Add: ImageVector by lazy {
        createVector("Add", buildSimplePath {
            moveTo(19f, 13f)
            horizontalLineToRelative(-6f)
            verticalLineToRelative(6f)
            horizontalLineToRelative(-2f)
            verticalLineToRelative(-6f)
            horizontalLineTo(5f)
            verticalLineToRelative(-2f)
            horizontalLineToRelative(6f)
            verticalLineTo(5f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(6f)
            horizontalLineToRelative(6f)
            verticalLineToRelative(2f)
            close()
        })
    }

    val Share: ImageVector by lazy {
        createVector("Share", buildSimplePath {
            moveTo(18f, 16.08f)
            curveToRelative(-0.76f, 0f, -1.44f, 0.3f, -1.96f, 0.77f)
            lineTo(8.91f, 12.7f)
            curveToRelative(0.05f, -0.23f, 0.09f, -0.47f, 0.09f, -0.7f)
            curveToRelative(0f, -0.24f, -0.04f, -0.47f, -0.09f, -0.7f)
            lineToRelative(7.05f, -4.11f)
            curveToRelative(0.54f, 0.5f, 1.25f, 0.81f, 2.04f, 0.81f)
            curveToRelative(1.66f, 0f, 3f, -1.34f, 3f, -3f)
            curveToRelative(0f, -1.66f, -1.34f, -3f, -3f, -3f)
            curveToRelative(-1.66f, 0f, -3f, 1.34f, -3f, 3f)
            curveToRelative(0f, 0.24f, 0.04f, 0.47f, 0.09f, 0.7f)
            lineTo(8.04f, 9.81f)
            curveToRelative(-0.54f, -0.5f, -1.25f, -0.81f, -2.04f, -0.81f)
            curveToRelative(-1.66f, 0f, -3f, 1.34f, -3f, 3f)
            curveToRelative(0f, 1.66f, 1.34f, 3f, 3f, 3f)
            curveToRelative(0.79f, 0f, 1.5f, -0.31f, 2.04f, -0.81f)
            lineToRelative(7.12f, 4.16f)
            curveToRelative(-0.05f, 0.21f, -0.08f, 0.43f, -0.08f, 0.65f)
            curveToRelative(0f, 1.61f, 1.31f, 2.92f, 2.92f, 2.92f)
            curveToRelative(1.61f, 0f, 2.92f, -1.31f, 2.92f, -2.92f)
            curveToRelative(0f, -1.61f, -1.31f, -2.92f, -2.92f, -2.92f)
            close()
        })
    }

    val DateRange: ImageVector by lazy {
        createVector("DateRange", buildSimplePath {
            moveTo(9f, 11f)
            horizontalLineTo(7f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(-2f)
            close()
            moveTo(13f, 11f)
            horizontalLineToRelative(-2f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(-2f)
            close()
            moveTo(17f, 11f)
            horizontalLineToRelative(-2f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(-2f)
            close()
            moveTo(19f, 4f)
            horizontalLineToRelative(-1f)
            verticalLineTo(2f)
            horizontalLineToRelative(-2f)
            verticalLineToRelative(2f)
            horizontalLineTo(8f)
            verticalLineTo(2f)
            horizontalLineTo(6f)
            verticalLineToRelative(2f)
            horizontalLineTo(5f)
            curveToRelative(-1.11f, 0f, -1.99f, 0.9f, -1.99f, 2f)
            lineTo(3f, 20f)
            curveToRelative(0f, 1.1f, 0.89f, 2f, 2f, 2f)
            horizontalLineToRelative(14f)
            curveToRelative(1.1f, 0f, 2f, -0.9f, 2f, -2f)
            verticalLineTo(6f)
            curveToRelative(0f, -1.1f, -0.9f, -2f, -2f, -2f)
            close()
            moveTo(19f, 20f)
            horizontalLineTo(5f)
            verticalLineTo(9f)
            horizontalLineToRelative(14f)
            verticalLineToRelative(11f)
            close()
        })
    }

    val List: ImageVector by lazy {
        createVector("List", buildSimplePath {
            moveTo(3f, 13f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(-2f)
            horizontalLineTo(3f)
            verticalLineToRelative(2f)
            close()
            moveTo(3f, 17f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(-2f)
            horizontalLineTo(3f)
            verticalLineToRelative(2f)
            close()
            moveTo(3f, 9f)
            horizontalLineToRelative(2f)
            verticalLineTo(7f)
            horizontalLineTo(3f)
            verticalLineToRelative(2f)
            close()
            moveTo(7f, 13f)
            horizontalLineToRelative(14f)
            verticalLineToRelative(-2f)
            horizontalLineTo(7f)
            verticalLineToRelative(2f)
            close()
            moveTo(7f, 17f)
            horizontalLineToRelative(14f)
            verticalLineToRelative(-2f)
            horizontalLineTo(7f)
            verticalLineToRelative(2f)
            close()
            moveTo(7f, 7f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(14f)
            verticalLineTo(7f)
            horizontalLineTo(7f)
            close()
        })
    }

    val Warning: ImageVector by lazy {
        createVector("Warning", buildSimplePath {
            moveTo(1f, 21f)
            horizontalLineToRelative(22f)
            lineTo(12f, 2f)
            lineTo(1f, 21f)
            close()
            moveTo(13f, 18f)
            horizontalLineToRelative(-2f)
            verticalLineToRelative(-2f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(2f)
            close()
            moveTo(13f, 14f)
            horizontalLineToRelative(-2f)
            verticalLineToRelative(-4f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(4f)
            close()
        })
    }

    val Star: ImageVector by lazy {
        createVector("Star", buildSimplePath {
            moveTo(12f, 17.27f)
            lineTo(18.18f, 21f)
            lineToRelative(-1.64f, -7.03f)
            lineTo(22f, 9.24f)
            lineToRelative(-7.19f, -0.61f)
            lineTo(12f, 2f)
            lineTo(9.19f, 8.63f)
            lineTo(2f, 9.24f)
            lineToRelative(5.46f, 4.73f)
            lineTo(5.82f, 21f)
            close()
        })
    }

    val School: ImageVector by lazy {
        createVector("School", buildSimplePath {
            moveTo(5f, 13.18f)
            verticalLineToRelative(4f)
            lineTo(12f, 21f)
            lineToRelative(7f, -3.82f)
            verticalLineToRelative(-4f)
            lineTo(12f, 17f)
            lineToRelative(-7f, -3.82f)
            close()
            moveTo(12f, 3f)
            lineTo(1f, 9f)
            lineToRelative(11f, 6f)
            lineToRelative(9f, -4.91f)
            verticalLineTo(17f)
            horizontalLineToRelative(2f)
            verticalLineTo(9f)
            lineTo(12f, 3f)
            close()
        })
    }

    val Place: ImageVector by lazy {
        createVector("Place", buildSimplePath {
            moveTo(12f, 2f)
            curveTo(8.13f, 2f, 5f, 5.13f, 5f, 9f)
            curveToRelative(0f, 5.25f, 7f, 13f, 7f, 13f)
            curveToRelative(0f, 0f, 7f, -7.75f, 7f, -13f)
            curveToRelative(0f, -3.87f, -3.13f, -7f, -7f, -7f)
            close()
            moveTo(12f, 11.5f)
            curveToRelative(-1.38f, 0f, -2.5f, -1.12f, -2.5f, -2.5f)
            curveToRelative(0f, -1.38f, 1.12f, -2.5f, 2.5f, -2.5f)
            curveToRelative(1.38f, 0f, 2.5f, 1.12f, 2.5f, 2.5f)
            curveToRelative(0f, 1.38f, -1.12f, 2.5f, -2.5f, 2.5f)
            close()
        })
    }
}
