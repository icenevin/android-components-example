package com.example.components.architecture.nvice.ui.camera

import android.Manifest
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.Context.CAMERA_SERVICE
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.view.*
import com.example.components.architecture.nvice.BaseFragment
import com.example.components.architecture.nvice.R
import io.fotoapparat.Fotoapparat
import io.fotoapparat.configuration.CameraConfiguration

import kotlinx.android.synthetic.main.fragment_camera.*
import javax.inject.Inject
import android.hardware.camera2.CameraCharacteristics
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.components.architecture.nvice.util.TaskUtils
import com.example.components.architecture.nvice.util.mlkit.GraphicOverlay
import com.example.components.architecture.nvice.util.mlkit.TextGraphic
import com.google.firebase.ml.vision.text.FirebaseVisionText


class CameraFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val PERMISSIONS_REQUEST_CAMERA = 1001

    private lateinit var viewModel: CameraViewModel
    private var fotoapparat: Fotoapparat? = null
    private var orientationEventListener: OrientationEventListener? = null

    val graphics = mutableListOf<GraphicOverlay.Graphic>()

    companion object {
        fun getInstance() = CameraFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(CameraViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        initObservers()
        initEvent()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        fotoapparat?.stop()
        orientationEventListener?.enable()
    }

    private fun initView() {
        checkCameraPermission()
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), PERMISSIONS_REQUEST_CAMERA)
        } else {
            initCamera()
        }
    }

    private fun initCamera() {
        val manager = context?.getSystemService(CAMERA_SERVICE) as CameraManager
        viewModel.updateDeviceRotation(activity?.windowManager?.defaultDisplay?.rotation)
        var cameraId: String? = null

        for (id in manager.cameraIdList) {
            val characteristics = manager.getCameraCharacteristics(id)
            val cameraDirection = characteristics.get(CameraCharacteristics.LENS_FACING)
            if (cameraDirection != null &&
                    cameraDirection == CameraCharacteristics.LENS_FACING_FRONT) {
                continue
            }
            cameraId = id
        }

        fotoapparat = Fotoapparat(
                context = context!!,
                view = cvCamera,
                cameraConfiguration = CameraConfiguration(
                        frameProcessor = { frame ->
                            TaskUtils.run(400) {
                                viewModel.processTextRecognition(frame, cameraId, manager)
                            }
                        }
                )
        )

        fotoapparat?.start()
    }

    private fun initObservers() {
        viewModel.getTextElements().observe(viewLifecycleOwner, Observer { textElements ->
            textElements?.let {
                if (it.isNotEmpty()) {
                    goOverlay.clear()
                    goOverlay.add(getGraphicsFromElements(it))
                }
            }
        })

        viewModel.getCitizenId().observe(viewLifecycleOwner, Observer { id ->
            Toast.makeText(context, id.toString(), Toast.LENGTH_LONG).show()
        })
    }

    private fun initEvent() {
        orientationEventListener = object : OrientationEventListener(context) {
            override fun onOrientationChanged(orientation: Int) {
                viewModel.updateDeviceRotation(orientation)
            }
        }
        orientationEventListener?.enable()
    }

    private fun getGraphicsFromElements(elements: List<FirebaseVisionText.Element>): List<GraphicOverlay.Graphic> {
        graphics.clear()
        for (element in elements) {
            graphics.add(TextGraphic(goOverlay, element))
        }
        return graphics
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_CAMERA -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    initCamera()
                } else {
                    activity?.finish()
                }
                return
            }
            else -> {
            }
        }
    }
}