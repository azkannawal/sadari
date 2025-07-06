package com.example.sadari.view

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.sadari.viewmodel.DetectorViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@Composable
fun DetectorScreen(viewModel: DetectorViewModel = viewModel()) {
    val context = LocalContext.current
    val result by viewModel.result.collectAsState()
    val loading by viewModel.loading.collectAsState()

    var photoUri by remember { mutableStateOf<Uri?>(null) }

    fun prepareImageUri(): Uri {
        val photoFile = File.createTempFile("photo_", ".jpg", context.externalCacheDir)
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            photoFile
        )
    }

    fun createMultipartFromUri(uri: Uri): MultipartBody.Part {
        val inputStream = context.contentResolver.openInputStream(uri)!!
        val file = File.createTempFile("upload_", ".jpg", context.cacheDir)
        file.outputStream().use { outputStream -> inputStream.copyTo(outputStream) }

        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", file.name, requestBody)
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && photoUri != null) {
            val multipart = createMultipartFromUri(photoUri!!)
            viewModel.submitAndPollShape(multipart) // ⬅️ GANTI INI
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            val uri = prepareImageUri()
            photoUri = uri
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Izin kamera ditolak", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = {
            when {
                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                        PackageManager.PERMISSION_GRANTED -> {
                    val uri = prepareImageUri()
                    photoUri = uri
                    cameraLauncher.launch(uri)
                }
                else -> {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }
        }) {
            Text("Ambil Gambar")
        }

        Spacer(Modifier.height(16.dp))

        photoUri?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        when {
            loading -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Sedang menganalisis gambar, mohon tunggu...")
                }
            }
            result != null -> {
                Text("Interpretasi Psikologis:")
                Text(result!!, modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}