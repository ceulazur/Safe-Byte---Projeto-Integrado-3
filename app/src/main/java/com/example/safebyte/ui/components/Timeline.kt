package com.example.safebyte.ui.components;

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.safebyte.R
import com.example.safebyte.data.model.TimelineEvent
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

enum class TimelineNodePosition {
    FIRST, MIDDLE, LAST
}

data class CircleParameters(val radius: Dp)
data class LineParameters(val strokeWidth: Dp)

// Safe date formatting function
private fun formatDate(dateStr: String): String {
    return try {
        // Try parsing as ISO date (yyyy-MM-dd)
        val date = LocalDate.parse(dateStr)
        date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
    } catch (e: DateTimeParseException) {
        try {
            // Try parsing as timestamp
            val timestamp = dateStr.toLong()
            val date = LocalDate.ofEpochDay(timestamp / (24 * 60 * 60 * 1000))
            date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
        } catch (e: Exception) {
            // Return original string if all parsing fails
            dateStr
        }
    }
}

@Composable
fun TimelineNode(
    pos: TimelineNodePosition = TimelineNodePosition.MIDDLE,
    circleParameters: CircleParameters,
    lineParameters: LineParameters? = null,
    contentStartOffset: Dp = 16.dp,
    spaceBetweenNodes: Dp = 32.dp,
    content: @Composable BoxScope.(modifier: Modifier) -> Unit,
) {
    val circleColor: Color = MaterialTheme.colorScheme.secondary

    Box(
        modifier = Modifier
            .wrapContentSize()
            .drawBehind {
                val circleRadiusInPx = circleParameters.radius.toPx()
                drawCircle(
                    color = circleColor,
                    radius = circleRadiusInPx,
                    center = Offset(circleRadiusInPx, circleRadiusInPx)
                )

                lineParameters?.let {
                    drawLine(
                        color = circleColor,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondaryTopBar(
    title: String,
    onBackClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color.Black,
            navigationIconContentColor = Color.Black
        )
    )
}

@Composable
fun Timeline(
    timelineEventList: List<TimelineEvent>,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = modifier
    ) {
        items(
            count = timelineEventList.size,
            key = { timelineEventList[it].id!! }
        ) { index ->
            TimelineNode(
                pos = when (index) {
                    0 -> TimelineNodePosition.FIRST
                    timelineEventList.size - 1 -> TimelineNodePosition.LAST
                    else -> TimelineNodePosition.MIDDLE
                },
                circleParameters = CircleParameters(6.dp),
                lineParameters = if (index < timelineEventList.size - 1) LineParameters(3.dp) else null
            ) { modifier ->
                MessageBubble(
                    mediaContent = timelineEventList[index],
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
fun MessageBubble(
    mediaContent: TimelineEvent,
    modifier: Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row {
            Text(
                text = formatDate(mediaContent.date.toString()),
                modifier = Modifier.padding(12.dp),
                fontWeight = FontWeight.Medium
            )
        }

        Row {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                mediaContent.videourl?.let { videoUrl ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(
                                color = Color.Black,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(bottom = 12.dp)
                            .clip(MaterialTheme.shapes.medium)
                    ) {
                        VideoPlayer(
                            modifier = Modifier,
                            videoUrl = videoUrl
                        )
                    }
                }

                mediaContent.activitieslist.forEach { activity ->
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.circle_solid),
                            contentDescription = stringResource(R.string.simbolo_lista),
                            modifier = Modifier.width(8.dp)
                        )

                        Text(
                            text = activity,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }
    }
}