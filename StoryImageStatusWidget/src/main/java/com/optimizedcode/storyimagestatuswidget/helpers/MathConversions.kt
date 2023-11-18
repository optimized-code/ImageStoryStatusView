package com.optimizedcode.storyimagestatuswidget.helpers

import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.roundToInt

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

fun Float.uptoPrecision(precision: Int) =
    this.toDouble().toPrecision(precision).toFloat()

fun Double.toPrecision(precision: Int) =
    if (precision < 1) {
        "${this.roundToInt()}"
    } else {
        val p = 10.0.pow(precision)
        val v = (abs(this) * p).roundToInt()
        val i = floor(v / p)
        var f = "${floor(v - (i * p)).toInt()}"
        while (f.length < precision) f = "0$f"
        val s = if (this < 0) "-" else ""
        "$s${i.toInt()}.$f"
    }
 