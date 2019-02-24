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
import com.example.components.architecture.nvice.util.getInt
import com.example.components.architecture.nvice.util.isValidCitizenId
import com.google.android.gms.tasks.Task
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.text.FirebaseVisionText
import io.fotoapparat.preview.Frame
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject


class CameraViewModel @Inject constructor() : ViewModel() {

    private lateinit var task: Task<FirebaseVisionText>

    private val textElements = MutableLiveData<List<FirebaseVisionText.Element>>()
    private val isCitizenIdDetected = MutableLiveData<Boolean>()
    private val citizenId = MutableLiveData<String>()
    private var deviceRotation = 0
    private val metadata = FirebaseVisionImageMetadata.Builder()
            .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
    private val detector = FirebaseVision.getInstance().onDeviceTextRecognizer!!

    companion object {
        private val ORIENTATIONS = SparseIntArray()

        init {
            ORIENTATIONS.append(Surface.ROTATION_0, 90)
            ORIENTATIONS.append(Surface.ROTATION_90, 0)
            ORIENTATIONS.append(Surface.ROTATION_180, 270)
            ORIENTATIONS.append(Surface.ROTATION_270, 180)
        }
    }

    fun getTextElements() = textElements

    fun getCitizenIdStatus() = isCitizenIdDetected

    fun getCitizenId() = citizenId

    fun processTextRecognition(frame: Frame, cameraId: String?, cameraManager: CameraManager?) {

        val image = FirebaseVisionImage.fromByteArray(
                frame.image,
                metadata.setWidth(frame.size.width)
                        .setHeight(frame.size.height)
                        .setRotation(getRotationCompensation(cameraId, deviceRotation, cameraManager))
                        .build()
        )

        task = detector.processImage(image)
                .addOnSuccessListener {
                    if (it.textBlocks.size > 0) {
                        for (block in it.textBlocks) {
                            for (line in block.lines) {
                                for (element in line.elements) {
                                    Timber.i(element.text)
                                    isCitizenIdDetected.postValue(
                                            detectCitizenId(element.text)
                                    )
                                }
                                textElements.postValue(line.elements)
                            }
                        }
                    }
                }
                .addOnCanceledListener {
                    Timber.i("FAILURE")
                }
    }

    fun updateDeviceRotation(rotation: Int?) {
        deviceRotation = rotation ?: 0
    }

    private fun detectCitizenId(text: String?): Boolean {
        if (text.isValidCitizenId()){
            citizenId.postValue(text)
            detector.close()
            return true
        }
        return false
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