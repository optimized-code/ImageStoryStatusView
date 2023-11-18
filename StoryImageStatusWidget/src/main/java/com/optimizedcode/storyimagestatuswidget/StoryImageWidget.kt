package com.optimizedcode.storyimagestatuswidget

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.optimizedcode.storyimagestatuswidget.helpers.START_ANGLE
import com.optimizedcode.storyimagestatuswidget.helpers.TOTAL_ANGLE
import com.optimizedcode.storyimagestatuswidget.helpers.calculateArcLength
import com.optimizedcode.storyimagestatuswidget.helpers.calculateDistance
import com.optimizedcode.storyimagestatuswidget.helpers.calculateGap
import com.optimizedcode.storyimagestatuswidget.helpers.calculateStrokeWidth
import com.optimizedcode.storyimagestatuswidget.helpers.getAllAnglesList
import com.optimizedcode.storyimagestatuswidget.helpers.getImage
import com.optimizedcode.storyimagestatuswidget.helpers.getTargetValue
import com.optimizedcode.storyimagestatuswidget.helpers.uptoPrecision

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

fun prepareSmallImageList(): List<Any> {
    return listOf(
        "https://fastly.picsum.photos/id/0/5000/3333.jpg?hmac=_j6ghY5fCfSD6tvtcV74zXivkJSPIfR9B8w34XeQmvU",
        "https://fastly.picsum.photos/id/14/2500/1667.jpg?hmac=ssQyTcZRRumHXVbQAVlXTx-MGBxm6NHWD3SryQ48G-o",
        "https://fastly.picsum.photos/id/20/3670/2462.jpg?hmac=CmQ0ln-k5ZqkdtLvVO23LjVAEabZQx2wOaT4pyeG10I",
        "https://fastly.picsum.photos/id/22/4434/3729.jpg?hmac=fjZdkSMZJNFgsoDh8Qo5zdA_nSGUAWvKLyyqmEt2xs0",
        "https://fastly.picsum.photos/id/22/4434/3729.jpg?hmac=fjZdkSMZJNFgsoDh8Qo5zdA_nSGUAWvKLyyqmEt2xs0"
    )
}

@Preview(showBackground = true)
@Composable
fun StoryImageWidgetPreview() {
    DrawStatusThumbnailWithViewProgress(
        progressSize = 150,
        strokeActiveColor = Color.Red,
        imagesList = prepareSmallImageList(),
        strokeInActiveColor = Color.LightGray,
        progressBarColor = Color.Red,
        widgetBackground = MaterialTheme.colorScheme.surface
    )
}

@Composable
fun ViewStatusProgressBar(
    modifier: Modifier = Modifier,
    strokeActiveColor: Color = Color.Red,
    progressSize: Int = 200,
    animateArc: Animatable<Float, AnimationVector1D> = Animatable(0f)
) {
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
    progressSize: Int = 200,
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
    val statusCount = imagesList.size
    val statusProgress = remember { Animatable(0f) }
    var mutableAutoMode by remember { mutableStateOf(autoMode) }
    val gap = rememberSaveable { calculateGap(statusCount) }
    val arcLength = rememberSaveable { calculateArcLength(statusCount, gap) }
    var seenStatusIndex by rememberSaveable { mutableIntStateOf(0) }
    var isAnimationPaused by rememberSaveable { mutableStateOf(false) }

    if (statusCount > 0) {
        val statusList = getAllAnglesList(statusCount)

        if (mutableAutoMode) {
            LaunchedEffect(isAnimationPaused) {
                var targetValue = getTargetValue(statusCount, seenStatusIndex)
                if (isAnimationPaused) {
                    statusProgress.stop()
                } else {
                    statusProgress.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(
                            durationMillis = (autoModeIntervalInMilliSeconds * (statusCount - (seenStatusIndex + 1))).toInt(),
                            easing = LinearEasing
                        )
                    ) {
                        val precisedValue = value.uptoPrecision(3)
                        if (precisedValue == 1f) {
                            mutableAutoMode = false
                            seenStatusIndex = 0
                        } else {
                            if (precisedValue == targetValue) {
                                seenStatusIndex++
                                targetValue = getTargetValue(
                                    statusCount,
                                    seenStatusIndex
                                )
                            }
                        }
                    }
                }
            }
        } else {
            LaunchedEffect(seenStatusIndex) {
                val targetValue = getTargetValue(
                    statusCount,
                    seenStatusIndex
                )
                statusProgress.animateTo(
                    targetValue = targetValue,
                    animationSpec = tween(
                        durationMillis = 150,
                        easing = LinearEasing
                    )
                )
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
                            statusList.forEach { statusInfo ->
                                drawArc(
                                    color = strokeInActiveColor,
                                    startAngle = statusInfo.startAngle,
                                    useCenter = false,
                                    sweepAngle = arcLength,
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
                strokeActiveColor = strokeActiveColor,
                progressSize = progressSize,
                modifier = Modifier.align(Alignment.Center),
                animateArc = statusProgress
            )
            Spacer(
                modifier = Modifier
                    .drawWithContent {
                        drawIntoCanvas {
                            statusList.forEach { statusInfo ->
                                drawArc(
                                    color = widgetBackground,
                                    startAngle = statusInfo.startAngle + arcLength,
                                    useCenter = false,
                                    sweepAngle = gap,
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
                        if (mutableAutoMode) {
                            isAnimationPaused = statusProgress.isRunning
                        } else {
                            if (seenStatusIndex < statusCount - 1) {
                                seenStatusIndex++
                            } else {
                                seenStatusIndex = 0
                            }
                        }
                    }
            ) {
                SubcomposeAsyncImage(
                    model = getImage(imagesList, seenStatusIndex),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    loading = {
                        if (mutableAutoMode) {
                            isAnimationPaused = true
                        }
                        CircularProgressIndicator(
                            color = progressBarColor,
                            modifier = Modifier.size(progressSize.dp)
                        )
                    },
                    onSuccess = {
                        if (mutableAutoMode) {
                            isAnimationPaused = false
                        }
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