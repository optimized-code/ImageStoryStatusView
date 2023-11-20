package com.optimizedcode.storyimagestatuswidget

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.optimizedcode.storyimagestatuswidget.composables.DrawArcGaps
import com.optimizedcode.storyimagestatuswidget.composables.DrawUnseenStatusBars
import com.optimizedcode.storyimagestatuswidget.composables.EmptyView
import com.optimizedcode.storyimagestatuswidget.composables.StoryImageStatusProgressBar
import com.optimizedcode.storyimagestatuswidget.helpers.calculateArcGap
import com.optimizedcode.storyimagestatuswidget.helpers.calculateArcLength
import com.optimizedcode.storyimagestatuswidget.helpers.calculateDistance
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
    @DrawableRes placeHolder: Int = R.drawable.ic_placeholder,
    onAutoModeOffCallback: (autoMode: Boolean) -> Unit = {},
    onPausedProgressCallback: (isPaused: Boolean) -> Unit = {},
) {
    val statusCount = imagesList.size
    val statusProgress = remember { Animatable(0f) }
    var mutableAutoMode by remember { mutableStateOf(autoMode) }
    val arcGap = rememberSaveable { calculateArcGap(statusCount) }
    val arcLength = rememberSaveable { calculateArcLength(statusCount, arcGap) }
    var seenStatusIndex by rememberSaveable { mutableIntStateOf(0) }
    var isProgressPaused by rememberSaveable { mutableStateOf(false) }

    if (statusCount > 0) {
        val statusList = getAllAnglesList(statusCount)
        if (mutableAutoMode) {
            LaunchedEffect(isProgressPaused) {
                var targetValue = getTargetValue(statusCount, seenStatusIndex)
                if (isProgressPaused) {
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
                            onAutoModeOffCallback(false)
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
            DrawUnseenStatusBars(
                modifier = Modifier.align(Alignment.Center),
                progressSize = progressSize,
                strokeInActiveColor = strokeInActiveColor,
                statusList = statusList,
                arcLength = arcLength
            )

            StoryImageStatusProgressBar(
                strokeActiveColor = strokeActiveColor,
                progressSize = progressSize,
                modifier = Modifier.align(Alignment.Center),
                animateArc = statusProgress
            )

            DrawArcGaps(
                modifier = Modifier.align(Alignment.Center),
                progressSize = progressSize,
                statusList = statusList,
                arcLength = arcLength,
                arcGap = arcGap,
                widgetBackground = widgetBackground
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
                            isProgressPaused = statusProgress.isRunning
                            onPausedProgressCallback(isProgressPaused)
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
                            isProgressPaused = true
                        }
                        CircularProgressIndicator(
                            color = progressBarColor,
                            modifier = Modifier.size(progressSize.dp)
                        )
                    },
                    onSuccess = {
                        if (mutableAutoMode) {
                            isProgressPaused = false
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
        EmptyView(
            modifier = modifier,
            progressSize = progressSize,
            imageBackgroundColor = imageBackgroundColor,
            widgetBackground = widgetBackground,
            placeHolder = placeHolder
        )
    }
}