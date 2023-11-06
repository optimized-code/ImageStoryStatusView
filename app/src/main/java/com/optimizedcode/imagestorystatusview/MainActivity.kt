package com.optimizedcode.imagestorystatusview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import com.optimizedcode.imagestorystatusview.ui.theme.ImageStoryStatusViewTheme
import com.optimizedcode.storyimagestatuswidget.DrawStatusThumbnailWithViewProgress

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageStoryStatusViewTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DrawStatusThumbnailWithViewProgress(
                        strokeWidth = 5,
                        progressSize = 200,
                        strokeActiveColor = colorResource(id = R.color.purple_500),
                        imagesList = prepareImageList(),
                        distanceOffset = 10,
                        progressBarColor = colorResource(id = R.color.purple_200)
                    )
                }
            }
        }
    }
}

fun prepareImageList(): List<Any>{
    return listOf(
        "https://fastly.picsum.photos/id/0/5000/3333.jpg?hmac=_j6ghY5fCfSD6tvtcV74zXivkJSPIfR9B8w34XeQmvU",
        "https://fastly.picsum.photos/id/14/2500/1667.jpg?hmac=ssQyTcZRRumHXVbQAVlXTx-MGBxm6NHWD3SryQ48G-o",
        "https://fastly.picsum.photos/id/20/3670/2462.jpg?hmac=CmQ0ln-k5ZqkdtLvVO23LjVAEabZQx2wOaT4pyeG10I",
        "https://fastly.picsum.photos/id/22/4434/3729.jpg?hmac=fjZdkSMZJNFgsoDh8Qo5zdA_nSGUAWvKLyyqmEt2xs0",
        "https://fastly.picsum.photos/id/28/4928/3264.jpg?hmac=GnYF-RnBUg44PFfU5pcw_Qs0ReOyStdnZ8MtQWJqTfA",
        "https://fastly.picsum.photos/id/63/5000/2813.jpg?hmac=HvaeSK6WT-G9bYF_CyB2m1ARQirL8UMnygdU9W6PDvM",
        R.drawable.yoga_pose_22,
        R.drawable.yoga_pose_55
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ImageStoryStatusViewTheme {
        DrawStatusThumbnailWithViewProgress(
            strokeWidth = 5,
            progressSize = 200,
            strokeActiveColor = colorResource(id = R.color.purple_500),
            imagesList = listOf(),
            distanceOffset = 10,
            progressBarColor = colorResource(id = R.color.purple_200)
        )
    }
}