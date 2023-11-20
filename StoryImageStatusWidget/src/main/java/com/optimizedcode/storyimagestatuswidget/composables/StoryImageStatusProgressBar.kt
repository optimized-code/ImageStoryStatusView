package com.optimizedcode.storyimagestatuswidget.composables

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.dp
import com.optimizedcode.storyimagestatuswidget.helpers.START_ANGLE
import com.optimizedcode.storyimagestatuswidget.helpers.TOTAL_ANGLE
import com.optimizedcode.storyimagestatuswidget.helpers.calculateStrokeWidth

/*
**************************************************************
 * www.optimizedcode.io 
 * Kotlin
 *
 * @author ehtisham
 * @package com.optimizedcode.storyimagestatuswidget.composables
 * @date 20-Nov-2023
 * @copyright 2023 Optimized code (https://www.optimizedcode.io)
 * @license Open source
 ***************************************************************
 */

@Composable
fun StoryImageStatusProgressBar(
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