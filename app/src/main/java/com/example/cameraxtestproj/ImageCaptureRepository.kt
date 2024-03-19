/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.cameraxtestproj;

import android.content.Context
import android.content.Intent
import android.hardware.Camera.ACTION_NEW_PICTURE
import android.media.MediaScannerConnection
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import java.io.File
import java.util.*

/**
 * Manages photo capture filename and location generation. Once a photo is captured and saved to
 * disk, the repository will also notify that the image has been created such that other
 * applications can view it.
 */
class ImageCaptureRepository constructor() {

}
