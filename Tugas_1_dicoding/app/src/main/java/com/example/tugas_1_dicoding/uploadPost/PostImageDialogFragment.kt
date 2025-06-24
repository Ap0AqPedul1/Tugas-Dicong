package com.example.tugas_1_dicoding.uploadPost

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.tugas_1_dicoding.apiService.RetrofitClient
import com.example.tugas_1_dicoding.apiService.UploadFetchCallback
import com.example.tugas_1_dicoding.dataClass.UploadRequest
import com.example.tugas_1_dicoding.dataClass.UploadResponse
import com.example.tugas_1_dicoding.databinding.FragmentPostImageDialogBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PostImageDialogFragment : DialogFragment() {

    private var _binding: FragmentPostImageDialogBinding? = null
    private val binding get() = _binding!!

    private var photoUri: Uri? = null
    private var message: String? = null

    private var data = UploadRequest(
        token = "",
        description = "",
        lat = null,
        lon = null,
        photoFile = File("dummy.jpg")
    )


    // Register Activity Result Launchers at class-level (private val)
    private val pickImageGalleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            photoUri = it
            displayImage(photoUri)
        }
    }

    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoUri?.let {
                displayImage(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostImageDialogBinding.inflate(inflater, container, false)

        return binding.root
    }

    // At this stage Dialog size can be adjusted
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(), // 90% width
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        message = arguments?.getString("key_data")
        data?.token = message.toString()
        Log.d("azhari", data.toString())
        Log.d("azhari", message.toString())


        binding.buttonOpenCamera.setOnClickListener {
            openCamera()
        }

        binding.buttonOpenGallery.setOnClickListener {
            openGallery()
        }

        binding.buttonPost.setOnClickListener {
            data.description = binding.editTextDescription.text.toString()
            uploadImage()
            data.photoFile = resizeImageIfNeeded(data.photoFile)
            fetchUploadStory(object: UploadFetchCallback {
                override fun onUploadFetched(upload: String) {
                    Log.d("azhari", upload)
                    clearAllInputs()
                }

                override fun onError(message: String) {
                    Log.d("azhari", message)
                }

            }

            )
        }

        binding.buttonCancel.setOnClickListener {
            val result = Bundle().apply {
                putString("result_key", "Data balikan dari DialogFragment")
            }
            parentFragmentManager.setFragmentResult("requestKey", result)
            dialog?.dismiss()
        }
    }

    private fun openCamera() {
        val file = createImageFile()
        photoUri = FileProvider.getUriForFile(
            requireContext(),
            "com.example.tugas_1_dicoding.fileprovider", // gunakan authority sesuai manifest
            file
        )
        takePictureLauncher.launch(photoUri)
    }

    private fun openGallery() {
        pickImageGalleryLauncher.launch("image/*")
    }

    private fun displayImage(uri: Uri?) {
        uri ?: return
        Glide.with(this)
            .load(uri)
            .fitCenter()
            .into(binding.imageView)
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir = requireContext().cacheDir
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    private fun uriToFile(uri: Uri): File? {
        val context = requireContext()
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val tempFile = createImageFile()
        tempFile.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }
        return tempFile
    }

    private fun uploadImage() {
        photoUri?.let { uri ->
            val file = uriToFile(uri)
            if (file != null) {
                data.photoFile = file
            }else{
                showToast("File error saat mengambil file")
                return
            }

        } ?: showToast("Gambar belum dipilih")
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchUploadStory(callback: UploadFetchCallback) {

        val photoRequestBody = data.photoFile.asRequestBody("image/png".toMediaTypeOrNull())
        val photoPart = MultipartBody.Part.createFormData("photo", data.photoFile.name, photoRequestBody)
        val descriptionPart = data.description.toRequestBody("text/plain".toMediaTypeOrNull())

        val latPart = data.lat?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())
        val lonPart = data.lon?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())


        RetrofitClient.instance.uploadImage(data.token, photoPart, descriptionPart, latPart, lonPart)
            .enqueue(object : Callback<UploadResponse> {
                override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>) {
                    if (response.isSuccessful) {
                        val storyResponse = response.body()
                        if (storyResponse != null) {
                            callback.onUploadFetched(storyResponse.message)
                        } else {
                            callback.onError("Response body or listStory is null")
                        }
                    } else {
                        callback.onError("Response failed: ${response.code()} - ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                    callback.onError("Network request failed: ${t.message}")
                }
            })
    }

    private fun clearAllInputs(){
        binding.imageView.setImageDrawable(null)
        binding.editTextDescription.setText("")
    }

    private fun resizeImageIfNeeded(originalFile: File): File {
        val maxSizeBytes = 1 * 1024 * 1024 // 1 MB in bytes

        if (originalFile.length() <= maxSizeBytes) {
            return originalFile
        }

        // Get image dimensions without loading full bitmap
        val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(originalFile.absolutePath, options)
        var width = options.outWidth
        var height = options.outHeight

        // Calculate an inSampleSize (scale factor) to reduce dimensions roughly under 1 MB
        var scaleFactor = 1
        val maxPixels = maxSizeBytes / 4 // approximate bytes per pixel (ARGB)

        while ((width / scaleFactor) * (height / scaleFactor) > maxPixels) {
            scaleFactor *= 2
        }

        // Decode bitmap with scale factor (downsample)
        val decodeOptions = BitmapFactory.Options().apply { inSampleSize = scaleFactor }
        val bitmap = BitmapFactory.decodeFile(originalFile.absolutePath, decodeOptions)

        // Compress bitmap to JPEG with quality to reduce file size
        val resizedFile = File(originalFile.parent, "resized_${originalFile.name}")
        FileOutputStream(resizedFile).use { outStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outStream) // 80% quality, adjust as needed
        }

        return resizedFile
    }
}
