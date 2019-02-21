package com.example.components.architecture.nvice.ui.camera

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.SparseIntArray
import android.view.Surface
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.text.FirebaseVisionText
import io.fotoapparat.preview.Frame
import timber.log.Timber
import javax.inject.Inject


class CameraViewModel @Inject constructor() : ViewModel() {

    val textElements = MutableLiveData<List<FirebaseVisionText.Element>>()

    companion object {
        private val ORIENTATIONS = SparseIntArray()

        init {
            ORIENTATIONS.append(Surface.ROTATION_0, 90)
            ORIENTATIONS.append(Surface.ROTATION_90, 0)
            ORIENTATIONS.append(Surface.ROTATION_180, 270)
            ORIENTATIONS.append(Surface.ROTATION_270, 180)
        }
    }

    private val metadata = FirebaseVisionImageMetadata.Builder()
            .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)

    private val detector = FirebaseVision.getInstance().onDeviceTextRecognizer

    fun processTextRecognition(frame: Frame, cameraId: String?, deviceRotation: Int?, cameraManager: CameraManager?) {

        val image = FirebaseVisionImage.fromByteArray(
                frame.image,
                metadata.setWidth(frame.size.width)
                        .setHeight(frame.size.height)
                        .setRotation (getRotationCompensation(cameraId, deviceRotation, cameraManager))
                        .build()
        )

        detector.processImage(image)
                .addOnSuccessListener {
                    if (it.textBlocks.size > 0) {
                        for (block in it.textBlocks) {
                            for (line in block.lines) {
                                textElements.postValue(line.elements)
                            }
                        }
                    }
                }
                .addOnCanceledListener {
                    Timber.i("FAILURE")
                }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Throws(CameraAccessException::class)
    private fun getRotationCompensation(cameraId: String?, deviceRotation: Int?, cameraManager: CameraManager?): Int {

        var rotationCompensation = deviceRotation?.let { ORIENTATIONS.get(it) } ?: 0

        val sensorOrientation = cameraManager
                ?.getCameraCharacteristics(cameraId ?: "")
                ?.get(CameraCharacteristics.SENSOR_ORIENTATION)!!
        rotationCompensation = (rotationCompensation + sensorOrientation + 270) % 360

        val result: Int
        when (rotationCompensation) {
            0 -> result = FirebaseVisionImageMetadata.ROTATION_0
            90 -> result = FirebaseVisionImageMetadata.ROTATION_90
            180 -> result = FirebaseVisionImageMetadata.ROTATION_180
            270 -> result = FirebaseVisionImageMetadata.ROTATION_270
            else -> {
                result = FirebaseVisionImageMetadata.ROTATION_0
                Timber.e("Bad rotation value: $rotationCompensation")
            }
        }
        return result
    }
}