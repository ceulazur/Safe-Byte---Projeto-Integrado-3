package com.example.safebyte.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.safebyte.ui.theme.SafeByteTheme

enum class TimelineNodePosition {
    FIRST,
    MIDDLE,
    LAST
}

data class CircleParameters(
    val radius: Dp,
    val backgroundColor: Color
)

data class LineParameters(
    val strokeWidth: Dp
)

@Composable
fun TimelineNode(
    pos: TimelineNodePosition = TimelineNodePosition.MIDDLE,
    circleParameters: CircleParameters,
    lineParameters: LineParameters? = null,
    contentStartOffset: Dp = 16.dp,
    spaceBetweenNodes: Dp = 32.dp,
    content: @Composable BoxScope.(modifier: Modifier) -> Unit
) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .drawBehind {
                val circleRadiusInPx = circleParameters.radius.toPx()
                drawCircle(
                    color = circleParameters.backgroundColor,
                    radius = circleRadiusInPx,
                    center = Offset(circleRadiusInPx, circleRadiusInPx)
                )

                lineParameters?.let {
                    drawLine(
                        color = Color.Black,
                        start = Offset(x = circleRadiusInPx, y = circleRadiusInPx * 2),
                        end = Offset(x = circleRadiusInPx, y = this.size.height),
                        strokeWidth = lineParameters.strokeWidth.toPx()
                    )
                }
            }
    ) {
        content(
            Modifier.padding(
                start = contentStartOffset,
                bottom = if (pos != TimelineNodePosition.LAST) spaceBetweenNodes else 0.dp,
            )
        )
    }
}

object LineParametersDefaults {
    private val defaultsStrokeWidth = 3.dp
}

@Composable
private fun MessageBubble(modifier: Modifier, containerColor: Color) {
    Card(
        modifier = modifier
            .width(200.dp)
            .height(100.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {}
}

@Preview(showBackground = true)
@Composable
fun TimelineNodePreview() {
    SafeByteTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            TimelineNode(
                pos = TimelineNodePosition.FIRST,
                circleParameters = CircleParameters(6.dp, Color.Blue),
                lineParameters = LineParameters(3.dp)
            ) { modifier ->
                MessageBubble(modifier, Color.Blue)
            }

            TimelineNode(
                circleParameters = CircleParameters(6.dp, Color.Cyan),
                lineParameters = LineParameters(3.dp)
            ) { modifier ->
                MessageBubble(modifier, containerColor = Color.Cyan)
            }

            TimelineNode(
                pos = TimelineNodePosition.LAST,
                circleParameters = CircleParameters(6.dp, Color.Red)
            ) { modifier ->
                MessageBubble(modifier, containerColor = Color.Red)
            }
        }
    }
}