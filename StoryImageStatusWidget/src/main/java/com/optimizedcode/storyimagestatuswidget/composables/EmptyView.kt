package com.optimizedcode.storyimagestatuswidget.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.optimizedcode.storyimagestatuswidget.R
import com.optimizedcode.storyimagestatuswidget.helpers.calculateDistance

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
internal fun EmptyView(
    modifier: Modifier = Modifier,
    progressSize: Int = 200,
    imageBackgroundColor: Color = Color.LightGray,
    widgetBackground: Color = colorResource(id = R.color.white),
    @DrawableRes placeHolder: Int = R.drawable.ic_placeholder
) {
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