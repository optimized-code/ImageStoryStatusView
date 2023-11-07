[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![](https://jitpack.io/v/optimized-code/ImageStoryStatusView.svg)](https://jitpack.io/#optimized-code/ImageStoryStatusView) [![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=19)

# ImageStoryStatusView

A fast circular image thumbnail widget in jetpack compose, perfect for showing the whatsapp like status with "seen" and "not seen" indications.

## Screen shots

![showcase_video](https://github.com/optimized-code/ImageStoryStatusView/assets/143836196/6facb8b5-b8df-48bc-9b3e-b7e5e56209ec)


## Getting Started

Gradle
------
Add below dependancy to the app-level build.gradle file.

```
dependencies {
    ...
    implementation ("com.github.optimized-code:ImageStoryStatusView:1.0.1")
}
```

Usage
-----
To use the widget, just add the below code anywhere in the compose project and adjust its parameters accordingly. 

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

And **follow me** on github (https://github.com/optimized-code) for my other useful content 👍

LICENCE
-----

ImageStoryStatusView by [Ehtisham Ahmad Qureshi](http://www.optimizedcode.io/) is licensed under a [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).

Copyright 2023 Optimized-code
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
      http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
