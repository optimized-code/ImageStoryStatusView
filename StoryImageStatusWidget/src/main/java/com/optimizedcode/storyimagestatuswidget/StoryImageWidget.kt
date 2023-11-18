package com.optimizedcode.storyimagestatuswidget

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/*
**************************************************************
 * www.optimizedcode.com 
 * Kotlin
 *
 * @author Ehtisham Ahmad
 * @package com.optimizedcode.storyimagestatuswidget
 * @date 04-Nov-2023
 * @copyright 2023 Optimized code (https://www.optimizedcode.io)
 * @license Open source
 ***************************************************************
 */

private const val MIN_GAP = 1f
private const val MAX_GAP = 2f
private const val STROKE_WIDTH_MULTIPLIER = 0.025f
private const val DISTANCE_MULTIPLIER = 0.05f
private const val TOTAL_ANGLE = 360f
private const val START_ANGLE = -90f
private const val TAG = "OC_WIDGET"
private var AUTO_MODE = false

fun prepareImageList(): List<Any> {
    return listOf(
        "https://fastly.picsum.photos/id/0/5000/3333.jpg?hmac=_j6ghY5fCfSD6tvtcV74zXivkJSPIfR9B8w34XeQmvU",
        "https://fastly.picsum.photos/id/14/2500/1667.jpg?hmac=ssQyTcZRRumHXVbQAVlXTx-MGBxm6NHWD3SryQ48G-o",
        "https://fastly.picsum.photos/id/20/3670/2462.jpg?hmac=CmQ0ln-k5ZqkdtLvVO23LjVAEabZQx2wOaT4pyeG10I",
        "https://fastly.picsum.photos/id/22/4434/3729.jpg?hmac=fjZdkSMZJNFgsoDh8Qo5zdA_nSGUAWvKLyyqmEt2xs0",
        "https://fastly.picsum.photos/id/28/4928/3264.jpg?hmac=GnYF-RnBUg44PFfU5pcw_Qs0ReOyStdnZ8MtQWJqTfA",
        "https://fastly.picsum.photos/id/63/5000/2813.jpg?hmac=HvaeSK6WT-G9bYF_CyB2m1ARQirL8UMnygdU9W6PDvM",
        "https://fastly.picsum.photos/id/0/5000/3333.jpg?hmac=_j6ghY5fCfSD6tvtcV74zXivkJSPIfR9B8w34XeQmvU",
        "https://fastly.picsum.photos/id/14/2500/1667.jpg?hmac=ssQyTcZRRumHXVbQAVlXTx-MGBxm6NHWD3SryQ48G-o",
        "https://fastly.picsum.photos/id/20/3670/2462.jpg?hmac=CmQ0ln-k5ZqkdtLvVO23LjVAEabZQx2wOaT4pyeG10I"
    )
}

@Preview(showBackground = true)
@Composable
fun DrawStatusThumbnailWithViewProgress_preview() {
    DrawStatusThumbnailWithViewProgress(
        progressSize = 100,
        strokeActiveColor = Color.Red,
        imagesList = prepareImageList(),
        progressBarColor = Color.LightGray,
        autoModeIntervalInMilliSeconds = 3000
    )
}

data class StatusInfo(
    val startAngle: Float,
    val arcLength: Float,
    val gap: Float
)

private fun calculateGap(imageCount: Int): Float {
    var result = TOTAL_ANGLE / imageCount
    if (result > MAX_GAP) {
        result = MAX_GAP
    } else if (result < MIN_GAP) {
        result = MIN_GAP
    }
    return result
}

private fun calculateStrokeWidth(progressSize: Int): Float {
    val result = progressSize * STROKE_WIDTH_MULTIPLIER
    return if (result < 3) {
        3f
    } else {
        result
    }
}

private fun calculateDistance(progressSize: Int): Float {
    val result = progressSize * DISTANCE_MULTIPLIER
    return if (result < 8) {
        6.5f
    } else {
        result
    }
}

private fun getImage(imagesList: List<Any>, index: Int): Any {
    var result: Any = R.drawable.ic_placeholder
    if (index < 0) {
        return result
    }

    if (index < imagesList.size && (imagesList[index] is Int || imagesList[index] is String)) {
        result = imagesList[index]
    }

    return result
}

internal fun getAllAnglesList(count: Int): ArrayList<StatusInfo> {
    val result = ArrayList<StatusInfo>()
    val gap = calculateGap(count)
    val eachArcLength = (TOTAL_ANGLE - (count * gap)) / count

    var startArcAngle = START_ANGLE
    for (i in 0 until count) {
        result.add(
            StatusInfo(
                startAngle = startArcAngle,
                arcLength = eachArcLength,
                gap = gap
            )
        )
        startArcAngle += (eachArcLength + gap)
    }
    return result
}

private fun getTargetValue(totalImageCount: Int, viewStatusIndex: Int): Float {
    return (1f / totalImageCount) * (viewStatusIndex + 1)
}

@Composable
fun ViewStatusProgressBar(
    modifier: Modifier = Modifier,
    viewedStatuses: MutableList<StatusInfo>,
    viewStatusIndex: Int,
    strokeActiveColor: Color = Color.Red,
    progressSize: Int = 240,
    autoModeIntervalInMilliSeconds: Long,
    isReset: Boolean = false
) {

    val targetValue = getTargetValue(
        viewedStatuses.size,
        viewStatusIndex
    )

    val animateArc = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(viewStatusIndex) {
        animateArc.animateTo(
            targetValue = targetValue,
            animationSpec = tween(
                durationMillis = autoModeIntervalInMilliSeconds.toInt(),
                easing = LinearEasing
            )
        ) {
            coroutineScope.launch {
                if (value == 1f && isReset) {
                    animateArc.snapTo(0f)
                }
            }
        }
    }

    Spacer(
        modifier = modifier
            .drawWithCache {
                onDrawWithContent {
                    drawIntoCanvas {
                        drawArc(
                            color = strokeActiveColor,
                            startAngle = START_ANGLE,
                            useCenter = false,
                            sweepAngle = TOTAL_ANGLE * animateArc.value,
                            style = Stroke(
                                (calculateStrokeWidth(progressSize)).dp.toPx(),
                                cap = StrokeCap.Butt
                            )
                        )
                    }
                }
            }
            .size(progressSize.dp)
    )
}

@Composable
fun DrawStatusThumbnailWithViewProgress(
    modifier: Modifier = Modifier,
    progressSize: Int = 240,
    strokeActiveColor: Color = Color.Red,
    strokeInActiveColor: Color = Color.DarkGray,
    imageBackgroundColor: Color = Color.LightGray,
    imagesList: List<Any> = listOf(),
    progressBarColor: Color = Color.Red,
    widgetBackground: Color = colorResource(id = R.color.white),
    autoMode: Boolean = true,
    autoModeIntervalInMilliSeconds: Long = 5000,
    @DrawableRes placeHolder: Int = R.drawable.ic_placeholder
) {
    val imageCount = imagesList.size
    var viewStatusIndex by rememberSaveable { mutableIntStateOf(0) }
    var autoModeToggle = rememberSaveable { autoMode }
    //var isProgressRunning by rememberSaveable { mutableStateOf(true) }
    var isProgressRunning = rememberSaveable { true }

    if (imageCount > 0) {
        val nonViewedStatuses = getAllAnglesList(imageCount)

        if (autoModeToggle) {
            val coroutineScope = rememberCoroutineScope()
            LaunchedEffect(key1 = imageCount) {
                coroutineScope.launch {
                    while (viewStatusIndex < imageCount - 1) {
                        delay(autoModeIntervalInMilliSeconds)
                        viewStatusIndex++
                        isProgressRunning = true
                    }
                    autoModeToggle = false
                }
            }
        }

        Box(
            modifier = Modifier
                .size((progressSize + calculateDistance(progressSize)).dp)
                .background(widgetBackground)
                .then(modifier)
        ) {
            Spacer(
                modifier = Modifier
                    .drawWithContent {
                        drawIntoCanvas {
                            nonViewedStatuses.forEach { statusInfo ->
                                drawArc(
                                    color = strokeInActiveColor,
                                    startAngle = statusInfo.startAngle,
                                    useCenter = false,
                                    sweepAngle = statusInfo.arcLength,
                                    style = Stroke(
                                        (calculateStrokeWidth(progressSize)).dp.toPx(),
                                        cap = StrokeCap.Butt
                                    )
                                )
                            }
                        }
                    }
                    .size(progressSize.dp)
                    .align(Alignment.Center)
            )
            ViewStatusProgressBar(
                viewedStatuses = nonViewedStatuses,
                viewStatusIndex = viewStatusIndex,
                strokeActiveColor = strokeActiveColor,
                progressSize = progressSize,
                modifier = Modifier.align(Alignment.Center),
                autoModeIntervalInMilliSeconds = if (autoModeToggle) autoModeIntervalInMilliSeconds else 300
            )
            Spacer(
                modifier = Modifier
                    .drawWithContent {
                        drawIntoCanvas {
                            nonViewedStatuses.forEach { statusInfo ->
                                drawArc(
                                    color = widgetBackground,
                                    startAngle = statusInfo.startAngle + statusInfo.arcLength,
                                    useCenter = false,
                                    sweepAngle = statusInfo.gap,
                                    style = Stroke(
                                        (calculateStrokeWidth(progressSize)).dp.toPx(),
                                        cap = StrokeCap.Butt
                                    )
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
                        if (autoModeToggle.not()) {
                            if (viewStatusIndex < imageCount - 1) {
                                viewStatusIndex++
                                isProgressRunning = true
                            } else {
                                viewStatusIndex = 0
                                //autoModeToggle = false
                            }
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
                    onSuccess = {
                        isProgressRunning = false
                    },
                    modifier = Modifier
                        .size((progressSize - calculateDistance(progressSize)).dp)
                        .clip(CircleShape)
                        .background(imageBackgroundColor)
                )
            }
        }
    } else {
        Box(
            modifier = Modifier
                .size((progressSize + calculateDistance(progressSize)).dp)
                .clip(CircleShape)
                .then(modifier)
                .background(widgetBackground)
        ) {
            Image(
                painter = painterResource(id = placeHolder),
                contentScale = ContentScale.Inside,
                contentDescription = null,
                modifier = Modifier
                    .size((progressSize * 0.3).dp)
                    .align(Alignment.Center)
                    .background(imageBackgroundColor)
            )
        }
    }
}