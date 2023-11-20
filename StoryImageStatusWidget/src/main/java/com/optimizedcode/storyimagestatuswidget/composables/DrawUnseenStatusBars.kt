package com.optimizedcode.storyimagestatuswidget.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.dp
import com.optimizedcode.storyimagestatuswidget.data.StatusInfo
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
fun DrawUnseenStatusBars(
    modifier: Modifier,
    progressSize: Int = 200,
    strokeInActiveColor: Color = Color.DarkGray,
    statusList: ArrayList<StatusInfo> = arrayListOf(),
    arcLength: Float
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
            .then(modifier)
    )
}