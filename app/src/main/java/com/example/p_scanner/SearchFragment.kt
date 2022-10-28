package com.example.p_scanner

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.p_scanner.BarCodeScanner.BarCodeAnalyzer
import com.example.p_scanner.Interfaces.BarCodeInterfaces
import com.example.p_scanner.databinding.FragmentScanningBinding
import com.example.p_scanner.databinding.FragmentSearchBinding
import com.google.mlkit.vision.barcode.common.Barcode
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class SearchFragment : Fragment() {
    lateinit var cameraExecutor: ExecutorService
    lateinit var binding: FragmentSearchBinding
    var barCodeAnalyzer: BarCodeAnalyzer? = null

    var animator: ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSearchBinding.inflate(layoutInflater)

        barCodeAnalyzer = BarCodeAnalyzer(requireContext())

        barCodeAnalyzer!!.onBarCodeDetection(object : BarCodeInterfaces {
            override fun onBarCodeDetection(barcode: Barcode) {

                val intent = Intent(requireContext() ,AddProductActivity::class.java)
                intent.putExtra("ProductID" ,barcode.rawValue.toString())
                startActivity(intent)
            }
        })

        cameraExecutor = Executors.newSingleThreadExecutor()


        val viewTreeObserver = binding.scannerLayout.viewTreeObserver

        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                binding.scannerLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)

                animator = ObjectAnimator.ofFloat(
                    binding.scannerBar, "translationY",
                    binding.scannerLayout.y-20,
                    (binding.scannerLayout.y +
                            binding.scannerLayout.height))

                animator!!.repeatMode = ValueAnimator.REVERSE
                animator!!.repeatCount = ValueAnimator.INFINITE
                animator!!.interpolator = AccelerateDecelerateInterpolator()
                animator!!.duration = 3000
                animator!!.start()
            }
        })

        startCamera()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        cameraExecutor.shutdown()
    }

    override fun onStart() {
        super.onStart()
        cameraExecutor = Executors.newSingleThreadExecutor()
        startCamera()
    }



    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, BarCodeAnalyzer(
                        requireContext()
                    )
                    )
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalyzer
                )

            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

}