package com.example.deepfake_detection

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.example.deepfake_detection.databinding.ActivityImageBinding
import com.example.deepfake_detection.ml.DeepfakeImgDetection
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class ImageActivity : AppCompatActivity() {
    lateinit var bitmap: Bitmap
    private lateinit var binding: ActivityImageBinding
    companion object {
        val IMAGE_REQUEST_CODE = 1_000;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //var labels =application.assets.open("labels.txt").bufferedReader().readLines()
       // var imageProcessor = ImageProcessor.Builder()
           // .add(ResizeOp(128,128, ResizeOp.ResizeMethod.BILINEAR)).build()
        val filename = "labels.txt"
        val inputString = application.assets.open(filename).bufferedReader().use{it.readText()}
        var townlist =inputString.split("\n")

        binding.btnImg.setOnClickListener {
            pickImageFromGallery()
        }
        binding.btnImg2.setOnClickListener {

            var resized: Bitmap = Bitmap.createScaledBitmap(bitmap,128,128,true)
            val model = DeepfakeImgDetection.newInstance(this)

            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 128, 128, 3), DataType.FLOAT32)
            //var tbuffer= TensorImage.fromBitmap(resized)
           // var byteBuffer = tbuffer.buffer
            val tensorImage = TensorImage(DataType.FLOAT32)
            tensorImage.load(resized)
            val byteBuffer = tensorImage.buffer

            inputFeature0.loadBuffer(byteBuffer)

            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer
            var max = getMax(outputFeature0.floatArray)

             binding.txtRes.setText(townlist[max])

            model.close()
        }
    }
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }
    override fun  onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            binding.imgBtn.setImageURI(data?.data)
            var uri = data?.data
            bitmap =MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            
        }
    }

    fun getMax(arr:FloatArray): Int{
        var ind = 0
        var min= 0.0f
        for (i in 0..1)
        {
            if(arr[i]>min)
            {
                ind = i
                min = arr[i]
            }

        }
        return ind
    }
}




