package com.example.sadari.view

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseInOutBounce
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.sadari.R
import com.example.sadari.viewmodel.DetectorViewModel
import kotlinx.coroutines.delay
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@Composable
fun DrawSenseScreen(
    navController: NavController? = null,
    viewModel: DetectorViewModel = viewModel()
) {
    val context = LocalContext.current
    val result by viewModel.result.collectAsState()
    val loading by viewModel.loading.collectAsState()
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    val showResultOnly = result != null || loading

    fun prepareImageUri(): Uri {
        val photoFile = File.createTempFile("photo_", ".jpg", context.externalCacheDir)
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", photoFile)
    }

    fun createMultipartFromUri(uri: Uri): MultipartBody.Part {
        val inputStream = context.contentResolver.openInputStream(uri)!!
        val file = File.createTempFile("upload_", ".jpg", context.cacheDir)
        file.outputStream().use { inputStream.copyTo(it) }
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", file.name, requestBody)
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && photoUri != null) {
            val multipart = createMultipartFromUri(photoUri!!)
            viewModel.submitAndPollShape(multipart)
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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0x4DE1DEFD),
                        Color.White,
                        Color(0x99E1DEFD),
                    )
                )
            )
    ) {
        if (!showResultOnly) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth() // Isi seluruh lebar
                        .padding(horizontal = 24.dp) // Padding horizontal untuk setiap item
                ) {
                Spacer(modifier = Modifier.height(48.dp))

                Text(
                    "Hello, John Doe!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    buildAnnotatedString {
                        append("Welcome to ")
                        withStyle(
                            SpanStyle(
                                color = Color(0xFF5B5DED),
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("DrawSense AI")
                        }
                    },
                    fontSize = 18.sp
                )

                Text(
                    text = "An AI-powered Draw A Person Test\nReflection",
                    fontSize = 14.sp,
                    color = Color(0xFF444444),
                    modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(listOf(Color(0xFFE4D9FF), Color(0xFFB9EAFF))),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Draw A Person (DAP) Test ")
                            }
                            append("is a classic projective psychology method used to explore your self-image, emotions, and perceptions of others through simple drawings.")
                        },
                        fontSize = 12.sp,
                        color = Color(0xFF333333)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                color = Color(0xFF7BB7FC),
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("This is not an ")
                        }
                        withStyle(
                            SpanStyle(
                                color = Color(0xFF7A33CC),
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("art test.")
                        }
                    },
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.frame_02),
                        contentDescription = null,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        buildAnnotatedString {
                            append("It’s about how you ")
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("express yourself")
                            }
                            append(", not how well you draw.")
                        },
                        fontSize = 14.sp,
                        color = Color(0xFF333333),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Button(
                        onClick = {
                            when {
                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA
                                ) ==
                                        PackageManager.PERMISSION_GRANTED -> {
                                    val uri = prepareImageUri()
                                    photoUri = uri
                                    cameraLauncher.launch(uri)
                                }

                                else -> {
                                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                            }
                        },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(),
                        modifier = Modifier
                            .height(44.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(Color(0xFFB3D6EA), Color(0xFFD0CBFF))
                                    ),
                                    shape = RoundedCornerShape(50)
                                )
                                .padding(horizontal = 24.dp, vertical = 10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "Next",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    Icons.Default.Camera,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))
                }
            }
        }

        if (showResultOnly) {
            item {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                        .padding(horizontal = 24.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                Color(0xFF7BB7FC),
                                                Color(0xFF30E0B6)
                                            )
                                        ),
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                ) {
                                    append("What Your Drawing")
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "May Reveal . . .",
                            fontSize = 16.sp,
                            color = Color(0xFF333333),
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        if (loading) {
                            var isVisible by remember { mutableStateOf(true) }

                            val alpha by animateFloatAsState(
                                targetValue = if (isVisible) 1f else 0f,
                                animationSpec = tween(durationMillis = 800, easing = LinearEasing)
                            )

                            val alphaImage by animateFloatAsState(
                                targetValue = if (isVisible) 1f else 0f,
                                animationSpec = tween(durationMillis = 1500, easing = EaseInOut)
                            )

                            LaunchedEffect(Unit) {
                                while (true) {
                                    delay(1000)
                                    isVisible = !isVisible
                                }
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                photoUri?.let { uri ->
                                    Image(
                                        painter = rememberAsyncImagePainter(uri),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(280.dp)
                                            .wrapContentWidth(Alignment.CenterHorizontally)
                                            .clip(RoundedCornerShape(16.dp))
                                            .alpha(alphaImage) // Apply fade animation
                                    )
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                Text(
                                    text = "Analyzing the image, please wait...",
                                    style = TextStyle(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium,
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                Color(0xFF7BB7FC), // Blue
                                                Color(0xFF30E0B6)  // Light Green
                                            )
                                        )
                                    ),
                                    modifier = Modifier
                                        .alpha(alpha) // Apply fade animation
                                        .align(Alignment.CenterHorizontally)
                                )

                                Spacer(modifier = Modifier.height(48.dp))
                            }


                        } else {

                            photoUri?.let { uri ->
                                Image(
                                    painter = rememberAsyncImagePainter(uri),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(280.dp)
                                        .wrapContentWidth(Alignment.CenterHorizontally)
                                        .clip(RoundedCornerShape(16.dp))
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            var textToShow by remember { mutableStateOf("") }
                            val resultText = result ?: "Belum ada hasil."
                            val characterDelay = 2L

                            LaunchedEffect(resultText) {
                                textToShow = ""
                                resultText.forEach { char ->
                                    textToShow += char
                                    delay(characterDelay)
                                }
                            }


                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(
                                                Color(0xFFE4D9FF),
                                                Color(0xFFB9EAFF)

                                            )
                                        ),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = textToShow,
                                    fontSize = 14.sp,
                                    color = Color(0xFF333333),
                                    lineHeight = 22.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = Color(0xFFFFA726),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Reminder",
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold,
                                    )
                                }
                            }

                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Spacer(modifier = Modifier.height(14.dp))

                                Text(
                                    text = buildAnnotatedString {
                                        withStyle(
                                            style = SpanStyle(
                                                fontWeight = FontWeight.Bold
                                            )
                                        ) {
                                            append("This is not a clinical diagnosis")
                                        }
                                        append(", but a gentle, reflective interpretation based on Karen Machover’s projective theory. Things like your mood, drawing style, and recent experiences can all influence the outcome. ")

                                        withStyle(
                                            style = SpanStyle(
                                                fontWeight = FontWeight.Bold
                                            )
                                        ) {
                                            append("Give insights into your personality and emotional state.")
                                        }
                                    },
                                    style = TextStyle(
                                        textAlign = TextAlign.Justify,
                                        fontSize = 14.sp,
                                        lineHeight = 22.sp,
                                        letterSpacing = TextUnit.Unspecified,
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 1.dp)
                                )

                                Spacer(modifier = Modifier.height(30.dp))

                                val gradientBrush = Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF7BB7FC), Color(0xFF00E6C0))
                                )

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp)
                                        .background(
                                            brush = Brush.horizontalGradient(
                                                listOf(
                                                    Color.White.copy(alpha = 0.4f),
                                                    Color.White.copy(alpha = 0f)
                                                )
                                            ),
                                            shape = RoundedCornerShape(24.dp)
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = Color.White,
                                            shape = RoundedCornerShape(24.dp)
                                        )
                                        .padding(horizontal = 12.dp, vertical = 16.dp)
                                ) {
                                    Text(
                                        buildAnnotatedString {
                                            append("Book a session with a ")
                                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                                append("licensed therapist")
                                            }
                                            append(" to explore your results in a supportive space.")
                                        },
                                        fontSize = 14.sp,
                                        lineHeight = 22.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }


                                Spacer(modifier = Modifier.height(24.dp))
                            }

                            GradientButton(
                                text = "Talk to a Therapist",
                                onClick = {
                                    viewModel.reset()
                                    photoUri = null
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp, start = 4.dp, end = 4.dp)
                            )

                            GradientButton(
                                text = "Retake the Test",
                                onClick = {
                                    viewModel.reset()
                                    photoUri = null
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp, start = 4.dp, end = 4.dp)
                            )

                            GradientButton(
                                text = "Self-Care Journey",
                                onClick = {
                                    viewModel.reset()
                                    photoUri = null
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp, start = 4.dp, end = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GradientButton(
    text: String,
    gradientColors: List<Color> = listOf(Color(0xFFE8DEFF), Color(0xFFE1FAFF)), // background
    textGradientColors: List<Color> = listOf(Color(0xFF7BB7FC), Color(0xFF00E6C0)), // text
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(gradientColors),
                    shape = RoundedCornerShape(24.dp)
                )
                .fillMaxWidth()
                .height(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = text,
                    style = TextStyle(
                        brush = Brush.horizontalGradient(textGradientColors),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward, // default arrow
                    contentDescription = null,
                    tint = Color(0xFF00E6C0), // sesuaikan dengan akhir gradien jika ingin harmonis
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

