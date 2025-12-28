package com.example.mobileparallel

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.SurfaceView
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc

class MainActivity : AppCompatActivity(), CameraBridgeViewBase.CvCameraViewListener2 {

    private var mOpenCvCameraView: CameraBridgeViewBase? = null
    private var isScanning = false
    private lateinit var objectDetector: ObjectDetector
    private var bitmapBuffer: Bitmap? = null
    private var isAIProcessing = false

    // AI Tracking & Counting Variables
    private val masterPositionList = mutableListOf<Point>()
    private var currentFrameObjects = mutableListOf<Rect>()

    // NEW: Variable for the dynamic UI Sensitivity
    private var distanceThreshold = 150.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_main)

        // Initialize ML Kit AI
        val options = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
            .enableMultipleObjects()
            .build()
        objectDetector = ObjectDetection.getClient(options)

        // Setup Camera View
        mOpenCvCameraView = findViewById(R.id.camera_surface)
        mOpenCvCameraView?.visibility = SurfaceView.VISIBLE
        mOpenCvCameraView?.setCvCameraViewListener(this)

        // UI Element Binding
        val btnAction = findViewById<Button>(R.id.btn_action)
        val btnDone = findViewById<Button>(R.id.btn_done)
        val resultOverlay = findViewById<LinearLayout>(R.id.result_overlay)
        val finalResultText = findViewById<TextView>(R.id.final_result_text)
        val sliderContainer = findViewById<LinearLayout>(R.id.slider_container)
        val distanceSeekBar = findViewById<SeekBar>(R.id.distance_seekbar)
        val sensitivityLabel = findViewById<TextView>(R.id.sensitivity_label)

        // NEW: SeekBar Logic for Dynamic Sensitivity
        distanceSeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Minimum threshold of 30 to prevent noise
                distanceThreshold = if (progress < 30) 30.0 else progress.toDouble()
                sensitivityLabel?.text = "Object Spacing (Sensitivity): ${distanceThreshold.toInt()}"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // START/STOP Logic
        btnAction?.setOnClickListener {
            if (!isScanning) {
                isScanning = true
                masterPositionList.clear()
                currentFrameObjects.clear()
                btnAction.text = "STOP SCAN"
                btnAction.setBackgroundColor(Color.RED)
                sliderContainer?.visibility = View.VISIBLE
            } else {
                isScanning = false
                val count = masterPositionList.size
                runOnUiThread {
                    finalResultText?.text = "TOTAL DETECTED: $count"
                    resultOverlay?.visibility = View.VISIBLE
                    btnAction.visibility = View.GONE
                    sliderContainer?.visibility = View.GONE
                }
            }
        }

        // NEW SCAN Logic
        btnDone?.setOnClickListener {
            runOnUiThread {
                resultOverlay?.visibility = View.GONE
                btnAction?.visibility = View.VISIBLE
                btnAction?.text = "START SCAN"
                btnAction?.setBackgroundColor(Color.GREEN)
                sliderContainer?.visibility = View.VISIBLE

                masterPositionList.clear()
                currentFrameObjects.clear()
                isScanning = false
            }
        }

        checkPermissions()
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 101)
        } else {
            startCameraWithDelay()
        }
    }

    private fun startCameraWithDelay() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (OpenCVLoader.initDebug()) {
                mOpenCvCameraView?.setCameraPermissionGranted()
                mOpenCvCameraView?.enableView()
            }
        }, 500)
    }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame): Mat {
        val rgba = inputFrame.rgba() ?: return inputFrame.rgba()

        // 1. Draw most recent AI Bounding Boxes
        synchronized(currentFrameObjects) {
            for (rect in currentFrameObjects) {
                Imgproc.rectangle(rgba,
                    Point(rect.x.toDouble(), rect.y.toDouble()),
                    Point((rect.x + rect.width).toDouble(), (rect.y + rect.height).toDouble()),
                    Scalar(0.0, 255.0, 0.0), 5)
            }
        }

        // 2. Process AI Logic
        if (!isAIProcessing) {
            isAIProcessing = true

            if (bitmapBuffer == null || bitmapBuffer!!.width != rgba.width() || bitmapBuffer!!.height != rgba.height()) {
                bitmapBuffer = Bitmap.createBitmap(rgba.width(), rgba.height(), Bitmap.Config.ARGB_8888)
            }

            try {
                Utils.matToBitmap(rgba, bitmapBuffer)
                val image = InputImage.fromBitmap(bitmapBuffer!!, 0)

                objectDetector.process(image)
                    .addOnSuccessListener { detectedObjects ->
                        val newBoxes = mutableListOf<Rect>()

                        for (obj in detectedObjects) {
                            val bounds = obj.boundingBox
                            val centerX = (bounds.left + bounds.right) / 2.0
                            val centerY = (bounds.top + bounds.bottom) / 2.0
                            val currentPos = Point(centerX, centerY)

                            newBoxes.add(Rect(bounds.left, bounds.top, bounds.width(), bounds.height()))

                            if (isScanning) {
                                // SPATIAL GUARD using the dynamic distanceThreshold from the Slider
                                val isAlreadyCounted = masterPositionList.any { counted ->
                                    Math.hypot(counted.x - currentPos.x, counted.y - currentPos.y) < distanceThreshold
                                }

                                if (!isAlreadyCounted) {
                                    masterPositionList.add(currentPos)
                                }
                            }
                        }

                        synchronized(currentFrameObjects) {
                            currentFrameObjects.clear()
                            currentFrameObjects.addAll(newBoxes)
                        }
                        isAIProcessing = false
                    }
                    .addOnFailureListener { isAIProcessing = false }
            } catch (e: Exception) { isAIProcessing = false }
        }

        // 3. Draw On-Screen Display
        if (isScanning) {
            Imgproc.putText(rgba, "STABLE COUNT: ${masterPositionList.size}", Point(50.0, 100.0),
                Imgproc.FONT_HERSHEY_SIMPLEX, 1.8, Scalar(0.0, 255.0, 0.0), 5)
        }

        return rgba
    }

    override fun onCameraViewStarted(w: Int, h: Int) {}
    override fun onCameraViewStopped() {}

    override fun onResume() {
        super.onResume()
        startCameraWithDelay()
    }

    override fun onPause() {
        super.onPause()
        mOpenCvCameraView?.disableView()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCameraWithDelay()
        }
    }
}