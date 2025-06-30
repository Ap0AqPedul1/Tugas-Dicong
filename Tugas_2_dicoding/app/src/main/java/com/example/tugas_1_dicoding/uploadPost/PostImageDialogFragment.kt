package com.example.tugas_1_dicoding.uploadPost

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.tugas_1_dicoding.databinding.FragmentPostImageDialogBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream

@Suppress("DEPRECATION")
class PostImageDialogFragment : DialogFragment() {

    private var _binding: FragmentPostImageDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UploadPostViewModel by viewModels()

    private val CAMERA_REQUEST_CODE = 100
    private val GALLERY_REQUEST_CODE = 101


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostImageDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(), // 90% width
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tokenFromActivity = arguments?.getString("key_data") ?: ""
        viewModel.setToken(tokenFromActivity)


        viewModel.imageBitmap.observe(viewLifecycleOwner) { bitmap ->
            binding.imageView.setImageBitmap(bitmap)
        }

        viewModel.uploadResult.observe(viewLifecycleOwner) { success ->
            success?.let{
                clearAllInputs()
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                sendDummy()
                dismiss()
            }
        }

        viewModel.uploadError.observe(viewLifecycleOwner) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                binding.editTextDescription.setText("")
            }
        }

        binding.buttonOpenCamera.setOnClickListener {
            openCamera()
        }


        binding.buttonOpenGallery.setOnClickListener {
            openGallery()
        }


        binding.buttonPost.setOnClickListener {
            viewModel.setDescription(binding.editTextDescription.text.toString())
            val tokenValue = viewModel.token.value ?: ""
            val description  = createDescriptionRequest(viewModel.description.value ?: "")
            viewModel.imageBitmap.value?.let { bitmap ->
                val filePart = prepareFilePartFromBitmap(bitmap)
                viewModel.uploadImage(tokenValue,filePart, description)
            } ?: run {
                Toast.makeText(requireContext(), "Belum ada gambar yang dipilih", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonCancel.setOnClickListener {
            sendDummy()
            dismiss()
        }

        dialog?.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                Log.d("MyDialogFragment", "Tombol Back HP ditekan")
                sendDummy()
                dismiss()
                true
            } else {
                false
            }
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    val bitmap = data?.extras?.get("data") as? Bitmap
                    bitmap?.let {
                        viewModel.setImageBitmap(it)
                    }
                }
                GALLERY_REQUEST_CODE -> {
                    val uri = data?.data
                    uri?.let {
                        // Anda bisa konversi URI ke bitmap agar disimpan di ViewModel
                        val bitmap = uriToBitmap(it)
                        if (bitmap != null) {
                            viewModel.setImageBitmap(bitmap)
                        }
                    }
                }
            }
        }
    }

    // Contoh fungsi helper konversi URI ke Bitmap
    private fun uriToBitmap(uri: Uri): Bitmap? {
        return try {
            val stream = requireContext().contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(stream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun prepareFilePartFromBitmap(bitmap: Bitmap): MultipartBody.Part {
        val maxWidth = 800
        val maxHeight = 800

        // Resize bitmap agar dimensinya tidak terlalu besar
        val resizedBitmap = resizeBitmap(bitmap, maxWidth, maxHeight)

        var compressQuality = 80
        var bos = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bos)
        var bitmapData = bos.toByteArray()

        // Turunkan kualitas kompresi jika ukuran masih > 1MB
        while (bitmapData.size > 1_048_576 && compressQuality > 10) {
            compressQuality -= 10
            bos = ByteArrayOutputStream()
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bos)
            bitmapData = bos.toByteArray()
        }

        Log.d("asd", "Final bitmap size: ${bitmapData.size} bytes with quality $compressQuality")

        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), bitmapData)
        return MultipartBody.Part.createFormData("photo", "image.jpg", requestFile)
    }

    fun resizeBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val ratioBitmap = width.toFloat() / height.toFloat()
        val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()
        var finalWidth = maxWidth
        var finalHeight = maxHeight

        if (ratioMax > ratioBitmap) {
            finalWidth = (maxHeight.toFloat() * ratioBitmap).toInt()
        } else {
            finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt()
        }
        return Bitmap.createScaledBitmap(bitmap, finalWidth, finalHeight, true)
    }


    private fun createDescriptionRequest(text: String): RequestBody {
        return RequestBody.create("text/plain".toMediaTypeOrNull(), text)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun clearAllInputs(){
        viewModel.clearImageBitmap()
        viewModel.setDescription("")
        binding.editTextDescription.setText("")
    }

    private fun sendDummy(){
        val result = Bundle().apply {
            putString("result_key", "Data balikan dari DialogFragment")
        }
        parentFragmentManager.setFragmentResult("requestKey", result)
    }
}
