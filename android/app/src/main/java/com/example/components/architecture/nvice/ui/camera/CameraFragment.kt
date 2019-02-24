package com.example.components.architecture.nvice.ui.camera

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context.CAMERA_SERVICE
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.view.*
import com.example.components.architecture.nvice.BaseFragment
import com.example.components.architecture.nvice.R
import io.fotoapparat.Fotoapparat
import io.fotoapparat.configuration.CameraConfiguration

import kotlinx.android.synthetic.main.fragment_camera.*
import timber.log.Timber
import javax.inject.Inject
import android.hardware.camera2.CameraCharacteristics
import android.os.Handler
import com.example.components.architecture.nvice.util.TaskUtil
import com.example.components.architecture.nvice.util.mlkit.GraphicOverlay
import com.example.components.architecture.nvice.util.mlkit.TextGraphic
import com.google.firebase.ml.vision.text.FirebaseVisionText


class CameraFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: CameraViewModel
    private lateinit var fotoapparat: Fotoapparat
    private lateinit var orientationEventListener: OrientationEventListener

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

    private fun initView() {
        initCamera()
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
                            TaskUtil.run (400) {
                                viewModel.processTextRecognition(frame, cameraId, manager)
                            }
                        }
                )
        )
    }

    private fun initObservers() {
        viewModel.getTextElements().observe(this, Observer { textElements ->
            textElements?.let {
                if (it.isNotEmpty()) {
                    goOverlay.clear()
                    goOverlay.add(getGraphicsFromElements(it))
                }
            }
        })
    }

    private fun initEvent() {
        orientationEventListener = object :OrientationEventListener(context){
            override fun onOrientationChanged(orientation: Int) {
                viewModel.updateDeviceRotation(orientation)
            }
        }
        orientationEventListener.enable()
    }

    private fun getGraphicsFromElements(elements: List<FirebaseVisionText.Element>): List<GraphicOverlay.Graphic> {
        graphics.clear()
        for (element in elements) {
            graphics.add(TextGraphic(goOverlay, element))
        }
        return graphics
    }

    override fun onStart() {
        super.onStart()
        fotoapparat.start()
    }

    override fun onStop() {
        super.onStop()
        fotoapparat.stop()
        orientationEventListener.enable()
    }
}