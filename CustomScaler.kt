import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomScaler() {
    val density = LocalDensity.current.density
    val cmToPx = remember { 10 * density }
    val totalWidthPx = remember { 3000 * cmToPx }
    val scrollState = rememberScrollState()
    val textMeasurer = rememberTextMeasurer()
    val totalWidthDp = with(LocalDensity.current) { totalWidthPx.toDp() }
    val currentWeight = remember(scrollState.value) {
        (scrollState.value / density) / 100f
    }
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val screenWidth = maxWidth
        Text(
            text = "${formatWeight(currentWeight)} кг",
            modifier = Modifier.align(alignment = Alignment.Center).offset(y = (-120).dp),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Box(
            modifier = Modifier.height(120.dp).width(4.dp).background(Color.Cyan)
                .align(alignment = Alignment.Center)
        )
        Row(
            modifier = Modifier.background(Color.Transparent).horizontalScroll(scrollState).offset {
                val screenWidthPx = with(density) { screenWidth.toPx() }
                IntOffset((screenWidthPx / 2).toInt(), 0)
            }.align(alignment = Alignment.Center), horizontalArrangement = Arrangement.Center
        ) {
            Canvas(
                modifier = Modifier.height(100.dp).width(totalWidthDp)
            ) {
                val rulerHeight = size.height
                val centerY = rulerHeight / 2
                for (i in 0..30000) {
                    val x = i * density
                    val lineHeight = when {
                        i % 100 == 0 -> rulerHeight * 0.8f
                        i % 50 == 0 -> rulerHeight * 0.6f
                        i % 10 == 0 -> rulerHeight * 0.4f
                        else -> rulerHeight * 0f
                    }
                    drawLine(
                        color = when {
                            i % 100 == 0 -> Color.Black
                            i % 50 == 0 -> Color.Black
                            i % 10 == 0 -> Color.LightGray
                            else -> Color.Transparent
                        },
                        start = Offset(x, centerY - lineHeight / 2),
                        end = Offset(x, centerY + lineHeight / 2),
                        strokeWidth = 2.0f
                    )
                    if (i % 100 == 0) {
                        val text = (i / 100).toString()
                        val textLayoutResult = textMeasurer.measure(
                            text = text, style = TextStyle(
                                fontSize = 16.sp, fontWeight = FontWeight.Thin, color = Color.Black
                            )
                        )
                        val textX = x - textLayoutResult.size.width / 2
                        val textY = centerY + lineHeight / 2 + textLayoutResult.size.height
                        drawText(
                            textLayoutResult = textLayoutResult,
                            topLeft = Offset(textX, textY),
                            color = Color.Black,
                            alpha = 1.0f,
                            shadow = Shadow(Color.Gray, offset = Offset(2f, 2f), blurRadius = 4f)
                        )
                    }
                }
            }
        }
    }
}

fun formatWeight(weight: Float): String {
    val rounded = (weight * 10).toInt() / 10f
    return "$rounded"
}
