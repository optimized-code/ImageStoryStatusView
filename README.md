# ImageStoryStatusView

A fast circular image thumbnail widget in jetpack compose, perfect for showing the whatsapp like status with "seen" and "not seen" indications.

## Screen shots

![ezgif com-video-to-gif](https://github.com/optimized-code/ImageStoryStatusView/assets/143836196/83a32883-a3ca-4d8c-bce7-0ba330fec34e)

## Getting Started

Gradle
------
```
dependencies {
    ...
    implementation ("com.github.optimized-code:ImageStoryStatusView:1.0.1")
}
```

Usage
-----
```kotlin
DrawStatusThumbnailWithViewProgress(
            strokeWidth = 5,
            progressSize = 200,
            strokeActiveColor = colorResource(id = R.color.purple_500),
            imagesList = listOf(
                "https://fastly.picsum.photos/id/0/5000/3333.jpg?hmac=_j6ghY5fCfSD6tvtcV74zXivkJSPIfR9B8w34XeQmvU",
                "https://fastly.picsum.photos/id/14/2500/1667.jpg?hmac=ssQyTcZRRumHXVbQAVlXTx-MGBxm6NHWD3SryQ48G-o",
                "https://fastly.picsum.photos/id/20/3670/2462.jpg?hmac=CmQ0ln-k5ZqkdtLvVO23LjVAEabZQx2wOaT4pyeG10I",
                "https://fastly.picsum.photos/id/22/4434/3729.jpg?hmac=fjZdkSMZJNFgsoDh8Qo5zdA_nSGUAWvKLyyqmEt2xs0",
                "https://fastly.picsum.photos/id/28/4928/3264.jpg?hmac=GnYF-RnBUg44PFfU5pcw_Qs0ReOyStdnZ8MtQWJqTfA",
                "https://fastly.picsum.photos/id/63/5000/2813.jpg?hmac=HvaeSK6WT-G9bYF_CyB2m1ARQirL8UMnygdU9W6PDvM",
                R.drawable.yoga_pose_22,
                R.drawable.yoga_pose_55
            ),
            distanceOffset = 10,
            progressBarColor = colorResource(id = R.color.purple_200)
        )
```

FEATURES
-----

- The library is using Coil for image caching and image loading from remote server
- Library fully supports the images from Remote server and Local drawables
- Upto 100 images can be added when the widget size is 200 dp

LIMITATIONS
-----

- Currently the library is in development phase, and I will expand it with time. Please feel free to contribute if you want.

GET IN TOUCH ❤️
-----

Find this library useful? 

Get in touch with me on LinkedIn (www.linkedin.com/in/ehtisham-ahmad-qureshi)

Whatsapp: +966-59-760-2404

email: ehtisham.ahmad89@gmail.com

And [**follow me**]on github (https://github.com/optimized-code) for my other useful content 👍

LICENCE
-----

ImageStoryStatusView by [Ehtisham Ahmad Qureshi](http://www.optimizedcode.com/) is licensed under a [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).
