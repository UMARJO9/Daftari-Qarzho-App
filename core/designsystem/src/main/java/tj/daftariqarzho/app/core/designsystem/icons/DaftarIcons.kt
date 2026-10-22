package tj.daftariqarzho.app.core.designsystem.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object DaftarIcons {

    val Debtors: ImageVector by lazy {
        daftarIcon("Debtors") {
            stroke {
                circle(9f, 8f, 3.5f)
                moveTo(3f, 20f)
                curveTo(3f, 16.7f, 5.7f, 14.5f, 9f, 14.5f)
                curveTo(12.3f, 14.5f, 15f, 16.7f, 15f, 20f)
                circle(16.5f, 8.5f, 2.5f)
                moveTo(17f, 14.5f)
                curveTo(19.2f, 14.7f, 21f, 16.6f, 21f, 19f)
            }
        }
    }

    val Transactions: ImageVector by lazy {
        daftarIcon("Transactions") {
            stroke {
                moveTo(4f, 8f)
                lineTo(19f, 8f)
                moveTo(15.5f, 4.5f)
                lineTo(19f, 8f)
                lineTo(15.5f, 11.5f)
                moveTo(20f, 16f)
                lineTo(5f, 16f)
                moveTo(8.5f, 12.5f)
                lineTo(5f, 16f)
                lineTo(8.5f, 19.5f)
            }
        }
    }

    val Reports: ImageVector by lazy {
        daftarIcon("Reports") {
            stroke {
                roundRect(4f, 13f, 8f, 20f, 1.5f)
                roundRect(10f, 8f, 14f, 20f, 1.5f)
                roundRect(16f, 4f, 20f, 20f, 1.5f)
            }
        }
    }

    val Settings: ImageVector by lazy {
        daftarIcon("Settings") {
            stroke {
                moveTo(4f, 6f)
                lineTo(7f, 6f)
                moveTo(11f, 6f)
                lineTo(20f, 6f)
                circle(9f, 6f, 2f)
                moveTo(4f, 12f)
                lineTo(13f, 12f)
                moveTo(17f, 12f)
                lineTo(20f, 12f)
                circle(15f, 12f, 2f)
                moveTo(4f, 18f)
                lineTo(6f, 18f)
                moveTo(10f, 18f)
                lineTo(20f, 18f)
                circle(8f, 18f, 2f)
            }
        }
    }

    val Back: ImageVector by lazy {
        daftarIcon("Back", autoMirror = true) {
            stroke {
                moveTo(15f, 5f)
                lineTo(8f, 12f)
                lineTo(15f, 19f)
            }
        }
    }

    val Add: ImageVector by lazy {
        daftarIcon("Add") {
            stroke {
                moveTo(12f, 5f)
                lineTo(12f, 19f)
                moveTo(5f, 12f)
                lineTo(19f, 12f)
            }
        }
    }

    val Close: ImageVector by lazy {
        daftarIcon("Close") {
            stroke {
                moveTo(6.5f, 6.5f)
                lineTo(17.5f, 17.5f)
                moveTo(17.5f, 6.5f)
                lineTo(6.5f, 17.5f)
            }
        }
    }

    val Search: ImageVector by lazy {
        daftarIcon("Search") {
            stroke {
                circle(11f, 11f, 6f)
                moveTo(15.5f, 15.5f)
                lineTo(19.5f, 19.5f)
            }
        }
    }

    val Edit: ImageVector by lazy {
        daftarIcon("Edit") {
            stroke {
                moveTo(4f, 20f)
                lineTo(4.8f, 16.2f)
                lineTo(15.3f, 5.7f)
                arcTo(2.12f, 2.12f, 0f, false, true, 18.3f, 8.7f)
                lineTo(7.8f, 19.2f)
                close()
                moveTo(13.5f, 7.5f)
                lineTo(16.5f, 10.5f)
            }
        }
    }

    val Delete: ImageVector by lazy {
        daftarIcon("Delete") {
            stroke {
                moveTo(4f, 7f)
                lineTo(20f, 7f)
                moveTo(9f, 7f)
                lineTo(9f, 5.5f)
                curveTo(9f, 4.7f, 9.7f, 4f, 10.5f, 4f)
                lineTo(13.5f, 4f)
                curveTo(14.3f, 4f, 15f, 4.7f, 15f, 5.5f)
                lineTo(15f, 7f)
                moveTo(6f, 7f)
                lineTo(6.8f, 18.2f)
                curveTo(6.9f, 19.2f, 7.7f, 20f, 8.8f, 20f)
                lineTo(15.2f, 20f)
                curveTo(16.3f, 20f, 17.1f, 19.2f, 17.2f, 18.2f)
                lineTo(18f, 7f)
                moveTo(10f, 11f)
                lineTo(10f, 16f)
                moveTo(14f, 11f)
                lineTo(14f, 16f)
            }
        }
    }

    val Calendar: ImageVector by lazy {
        daftarIcon("Calendar") {
            stroke {
                roundRect(4f, 5.5f, 20f, 20f, 3f)
                moveTo(4f, 10f)
                lineTo(20f, 10f)
                moveTo(8.5f, 3.5f)
                lineTo(8.5f, 7f)
                moveTo(15.5f, 3.5f)
                lineTo(15.5f, 7f)
            }
        }
    }
}

private const val IconSize = 24f
private const val IconStrokeWidth = 1.8f

private fun daftarIcon(
    name: String,
    autoMirror: Boolean = false,
    block: ImageVector.Builder.() -> Unit,
): ImageVector = ImageVector.Builder(
    name = name,
    defaultWidth = IconSize.dp,
    defaultHeight = IconSize.dp,
    viewportWidth = IconSize,
    viewportHeight = IconSize,
    autoMirror = autoMirror,
).apply(block).build()

private fun ImageVector.Builder.stroke(pathBuilder: PathBuilder.() -> Unit) {
    path(
        fill = null,
        stroke = SolidColor(Color.Black),
        strokeLineWidth = IconStrokeWidth,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round,
        pathBuilder = pathBuilder,
    )
}

private fun PathBuilder.circle(centerX: Float, centerY: Float, radius: Float) {
    moveTo(centerX + radius, centerY)
    arcToRelative(radius, radius, 0f, true, true, -2 * radius, 0f)
    arcToRelative(radius, radius, 0f, true, true, 2 * radius, 0f)
    close()
}

private fun PathBuilder.roundRect(left: Float, top: Float, right: Float, bottom: Float, radius: Float) {
    moveTo(left + radius, top)
    lineTo(right - radius, top)
    arcTo(radius, radius, 0f, false, true, right, top + radius)
    lineTo(right, bottom - radius)
    arcTo(radius, radius, 0f, false, true, right - radius, bottom)
    lineTo(left + radius, bottom)
    arcTo(radius, radius, 0f, false, true, left, bottom - radius)
    lineTo(left, top + radius)
    arcTo(radius, radius, 0f, false, true, left + radius, top)
    close()
}
