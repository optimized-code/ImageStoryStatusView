package com.optimizedcode.storyimagestatuswidget

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage

/*
**************************************************************
 * www.optimizedcode.com 
 * Kotlin
 *
 * @author Ehtisham Ahmad
 * @package com.optimizedcode.storyimagestatuswidget
 * @date 04-Nov-2023
 * @copyright 2023 Optimized code (https://www.optimizedcode.com)
 * @license Open source
 ***************************************************************
 */

internal fun getAllAnglesList(count: Int): ArrayList<Pair<Float, Float>> {
    var calGap = 100f / count
    if (calGap > 3f) {
        calGap = 3f
    }
    val result = ArrayList<Pair<Float, Float>>()
    var startAngle = -90f
    val eachAngle = (360f - (count * calGap)) / count
    for (i in 0 until count) {
        result.add(Pair(startAngle, eachAngle))
        startAngle += (eachAngle + calGap)
    }
    return result
}

private fun getImage(imagesList: List<Any>, index: Int): Any {
    return if (imagesList[index] is Int || imagesList[index] is String) {
        imagesList[index]
    } else {
        R.drawable.ic_placeholder
    }
}

@Composable
fun DrawStatusThumbnailWithViewProgress(
    strokeWidth: Int = 5,
    progressSize: Int = 240,
    strokeActiveColor: Color = Color.Red,
    strokeInActiveColor: Color = Color.LightGray,
    imagesList: List<Any> = listOf(),
    distanceOffset: Int = 10,
    progressBarColor: Color = Color.Red,
    @DrawableRes placeHolder:Int = R.drawable.ic_placeholder
) {
    val imageCount = imagesList.size
    val viewedStatuses: MutableList<Pair<Float, Float>> = remember { mutableStateListOf() }
    var viewStatusIndex by remember {
        mutableIntStateOf(0)
    }

    if (imageCount > 0) {
        val nonViewedStatuses = getAllAnglesList(imageCount)
        viewedStatuses.add(nonViewedStatuses[0])
        Box(
            modifier = Modifier.size((progressSize + distanceOffset).dp)
        ) {
            Spacer(
                modifier = Modifier
                    .drawWithContent {
                        drawIntoCanvas {
                            nonViewedStatuses.forEach { pair ->
                                drawArc(
                                    color = strokeInActiveColor,
                                    startAngle = pair.first,
                                    useCenter = false,
                                    sweepAngle = pair.second,
                                    style = Stroke(strokeWidth.dp.toPx(), cap = StrokeCap.Butt)
                                )
                            }
                        }
                    }
                    .size(progressSize.dp)
                    .align(Alignment.Center)
            )
            Spacer(
                modifier = Modifier
                    .drawWithContent {
                        drawIntoCanvas {
                            viewedStatuses.forEach { pair ->
                                drawArc(
                                    color = strokeActiveColor,
                                    startAngle = pair.first,
                                    useCenter = false,
                                    sweepAngle = pair.second,
                                    style = Stroke(strokeWidth.dp.toPx(), cap = StrokeCap.Butt)
                                )
                            }
                        }
                    }
                    .size(progressSize.dp)
                    .align(Alignment.Center)
            )
            Surface(
                color = Color.Transparent,
                modifier = Modifier
                    .background(Color.Transparent)
                    .align(Alignment.Center)
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) {
                        val viewedStatusesSize = viewedStatuses.size
                        viewStatusIndex = if (viewedStatusesSize < nonViewedStatuses.size) {
                            viewedStatuses.add(nonViewedStatuses[viewedStatusesSize])
                            viewedStatuses.size - 1
                        } else {
                            viewedStatuses.clear()
                            viewedStatuses.add(nonViewedStatuses[0])
                            0
                        }
                    }
            ) {
                SubcomposeAsyncImage(
                    model = getImage(imagesList, viewStatusIndex),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    loading = {
                        CircularProgressIndicator(
                            color = progressBarColor,
                            modifier = Modifier.size(progressSize.dp)
                        )
                    },
                    modifier = Modifier
                        .size((progressSize - distanceOffset).dp)
                        .clip(CircleShape)
                        .background(strokeInActiveColor)
                )
            }
        }
    } else {
        Box(
            modifier = Modifier
                .size(
                    (progressSize + distanceOffset).dp
                )
                .clip(CircleShape)
                .background(Color.LightGray)
        ) {
            Image(
                painter = painterResource(id = placeHolder),
                contentScale = ContentScale.Inside,
                contentDescription = null,
                modifier = Modifier
                    .size((progressSize * 0.3).dp)
                    .align(Alignment.Center)
            )
        }
    }
}