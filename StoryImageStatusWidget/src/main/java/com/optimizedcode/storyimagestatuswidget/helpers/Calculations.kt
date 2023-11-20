package com.optimizedcode.storyimagestatuswidget.helpers

import com.optimizedcode.storyimagestatuswidget.R
import com.optimizedcode.storyimagestatuswidget.data.StatusInfo

/*
**************************************************************
 * www.optimizedcode.io 
 * Kotlin
 *
 * @author ehtisham
 * @package com.optimizedcode.storyimagestatuswidget.helpers
 * @date 18-Nov-2023
 * @copyright 2023 Optimized code (https://www.optimizedcode.io)
 * @license Open source
 ***************************************************************
 */

internal fun calculateArcGap(imageCount: Int): Float {
    var result = TOTAL_ANGLE / imageCount
    if (result > MAX_GAP) {
        result = MAX_GAP
    } else if (result < MIN_GAP) {
        result = MIN_GAP
    }
    return result
}

internal fun calculateStrokeWidth(progressSize: Int): Float {
    val result = progressSize * STROKE_WIDTH_MULTIPLIER
    return if (result < 3) {
        3f
    } else {
        result
    }
}

internal fun calculateDistance(progressSize: Int): Float {
    val result = progressSize * DISTANCE_MULTIPLIER
    return if (result < 8) {
        6.5f
    } else {
        result
    }
}

internal fun calculateArcLength(imageCount: Int, gap: Float): Float {
    return (TOTAL_ANGLE - (imageCount * gap)) / imageCount
}

internal fun getAllAnglesList(count: Int): ArrayList<StatusInfo> {
    val result = ArrayList<StatusInfo>()
    val gap = calculateArcGap(count)
    val eachArcLength = (TOTAL_ANGLE - (count * gap)) / count

    var startArcAngle = START_ANGLE
    for (i in 0 until count) {
        result.add(
            StatusInfo(startAngle = startArcAngle)
        )
        startArcAngle += (eachArcLength + gap)
    }
    return result
}

internal fun getTargetValue(totalImageCount: Int, viewStatusIndex: Int): Float {
    return ((1f / totalImageCount) * (viewStatusIndex + 1)).uptoPrecision(3)
}

internal fun getImage(imagesList: List<Any>, index: Int): Any {
    var result: Any = R.drawable.ic_placeholder
    if (index < 0) {
        return result
    }

    if (index < imagesList.size && (imagesList[index] is Int || imagesList[index] is String)) {
        result = imagesList[index]
    }

    return result
}