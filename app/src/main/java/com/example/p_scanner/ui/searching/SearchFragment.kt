package com.example.p_scanner.ui.searching

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.p_scanner.ui.addOrEditItems.AddAndEditItemActivity
import com.example.p_scanner.Utils.observeOnce
import com.example.p_scanner.barcodescanner.BarCodeAnalyzer
import com.example.p_scanner.database.ItemsDatabase
import com.example.p_scanner.databinding.FragmentSearchBinding
import com.example.p_scanner.interfaces.BarCodeInterfaces
import com.example.p_scanner.pojo.Item
import com.example.p_scanner.pojo.ItemInteractions
import com.example.p_scanner.repository.Repository
import com.google.mlkit.vision.barcode.common.Barcode
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class SearchFragment(val owner: LifecycleOwner) : Fragment() {
    private lateinit var camera: Camera
    lateinit var cameraExecutor: ExecutorService
    lateinit var binding: FragmentSearchBinding
    var barCodeAnalyzer: BarCodeAnalyzer? = null
    var animator: ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSearchBinding.inflate(layoutInflater)

        barCodeAnalyzer = BarCodeAnalyzer()
        val repository = Repository(ItemsDatabase.getDatabase(requireContext()).itemDAO())
        barCodeAnalyzer!!.onBarCodeDetection(object : BarCodeInterfaces {
            override fun onBarCodeDetection(barcode: Barcode) {
                repository.getItemById(barcode.rawValue.toString()).observeOnce(owner,object :Observer<Item>{
                    override fun onChanged(item:Item?) {
                        if (item == null)
                        {
                            Toast.makeText(requireContext(),"This item not found" ,Toast.LENGTH_SHORT).show()
                            return
                        }
                        val intent = Intent(context , AddAndEditItemActivity::class.java)
                        intent.putExtra("Item" ,item)
                        intent.putExtra("Interaction" ,ItemInteractions.EDIT)
                        startActivity(intent)
                        return
                    }
                })
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var torchOn = false
        binding.buTorch.setOnClickListener {
            if (!torchOn)
            {
                camera.cameraControl.enableTorch(true)
                torchOn = true
            }
            else
            {
                camera.cameraControl.enableTorch(false)
                torchOn = false
            }
        }
        return binding.root
    }


    override fun onStart() {
        super.onStart()
        cameraExecutor = Executors.newSingleThreadExecutor()
        startCamera()
    }

    override fun onResume() {
        super.onResume()

        cameraExecutor = Executors.newSingleThreadExecutor()

        val viewTreeObserver = binding.scannerLayout.viewTreeObserver

        viewTreeObserver.addOnGlobalLayoutListener(object :ViewTreeObserver.OnGlobalLayoutListener{
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
                    it.setAnalyzer(cameraExecutor,  BarCodeAnalyzer())
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)
                camera.cameraInfo.hasFlashUnit()
            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }
}
