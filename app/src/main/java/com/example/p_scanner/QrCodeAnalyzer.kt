package com.example.p_scanner

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.currentCoroutineContext
import java.lang.Exception
import kotlin.coroutines.coroutineContext

class QrCodeAnalyzer(val context:Context): ImageAnalysis.Analyzer {
    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(image: ImageProxy) {
        val img= image.image

        if (img != null)
        {
            val inputImage = InputImage.fromMediaImage(img ,image.imageInfo.rotationDegrees)

            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()

            val scanner = BarcodeScanning.getClient(options)
            scanner.process(inputImage)
                .addOnSuccessListener(object :OnSuccessListener<List<Barcode>>{
                    override fun onSuccess(barcodes: List<Barcode>?) {
                        for (barcode in barcodes!!)
                        {
                            Toast.makeText(context ,barcode.toString() ,Toast.LENGTH_LONG).show()
                        }
                    }
                })
                .addOnFailureListener(object :OnFailureListener{
                    override fun onFailure(p0: Exception) {
                        TODO("Not yet implemented")
                    }
                })
        }
        image.close()
    }
}