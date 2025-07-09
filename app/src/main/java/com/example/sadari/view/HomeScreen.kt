package com.example.sadari.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sadari.R

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0x4DE1DEFD), // Warna pertama
                        Color.White,
                        Color(0x99E1DEFD),
                    )
                )
            )
    ) {
        HeaderSection(userName = "John Doe")
        GradientSearchBar(
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 16.dp)
        )
        FeatureIconSection(navController)
        MeditationCard(navController)
        TherapistAvailabilitySection(navController)

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun HeaderSection(userName: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, bottom = 24.dp, top = 38.dp, end = 24.dp)
    ) {

        GradientText("Hello, $userName!")

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "How are you feeling today?",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.Gray,
                fontSize = 14.sp
            )
        )
    }
}

@Composable
fun GradientSearchBar(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit = {}
) {
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFFD0CBFF),
            Color(0xFFB3D6EA)
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(brush = gradientBrush, shape = RoundedCornerShape(40.dp))
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    color = Color.White
                ),
                modifier = Modifier.weight(1f),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = "Search our service here",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}


@Composable
fun FeatureIconSection(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        FeatureCard("DeepSense AI", "frame_01") {
            navController.navigate("drawsense")
        }
        FeatureCard("DrawSense AI","frame_02") {
            navController.navigate("drawsense")
        }
        FeatureCard("Online Therapist", "frame_03") {
            navController.navigate("drawsense")
        }
    }
}

@Composable
fun FeatureCard(label: String, image: String, onClick: () -> Unit = {}) {
    val context = LocalContext.current
    val imageRes = remember(image) {
        context.resources.getIdentifier(image, "drawable", context.packageName)
    }

    val interactionSource = remember { MutableInteractionSource() }
    val rippleColor = Color(0xFFFFFFFF)

    Column(
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(color = rippleColor),
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = label,
            modifier = Modifier
                .size(80.dp)
                .shadow(1.dp, shape = RoundedCornerShape(24.dp), clip = false)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFFEFEFE))
                .padding(4.dp)
        )
        Text(
            label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}


@Composable
fun MeditationCard(navController: NavController) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), // ✅ Add shadow
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp, bottom = 24.dp, top = 16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent) // Gradient inside
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFFD0CBFF), Color(0xFFB3D6EA)),
                        start = Offset(0f, 0f),
                        end = Offset.Infinite
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.frame_04),
                    contentDescription = null,
                    modifier = Modifier.size(86.dp),
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Need a brain break?",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                    Text(
                        "Relax your mind and reconnect with yourself.",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = { navController.navigate("detector") },
                            shape = RoundedCornerShape(40.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                        ) {
                            Text("Meditate Now", fontSize = 12.sp, color = Color(0xFF444444))
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun TherapistAvailabilitySection(navController: NavController) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text("Therapists Availability", fontWeight = FontWeight.Bold, fontSize = 16.sp)

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            "For those who need immediate help",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TherapistCard(
                name = "Alysha Raya H.",
                title = "Therapist",
                time1 = "Today, 08:00 WIB",
                imageRes = R.drawable.frame_06,
                navController = navController,
                modifier = Modifier.weight(1f)
            )
            TherapistCard(
                name = "Junaedi H.",
                title = "Therapist",
                time1 = "Today, 08:00 WIB",
                imageRes = R.drawable.frame_05,
                navController = navController,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun TherapistCard(
    name: String,
    title: String,
    time1: String,
    imageRes: Int,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .height(300.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal=14.dp, vertical = 18.dp)
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = name,
                modifier = Modifier
                    .size(80.dp),
            )
            Text(
                name,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(title, fontSize = 12.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Chip("Anxiety")
                Chip("Psychology")
            }

            Text(
                "Next Available Time",
                fontSize = 10.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )

            Button(
                onClick = { navController.navigate("detector") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                shape = RoundedCornerShape(40.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEEF6FF))
            ) {
                Text(time1, fontSize = 11.sp, color = Color(0xFF1976D2))
            }
        }
    }
}

@Composable
fun Chip(label: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFFEEF6FF), shape = RoundedCornerShape(50))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(label, fontSize = 10.sp, color = Color(0xFF1976D2))
    }
}

@Composable
fun GradientText(text: String) {
    val gradientColors = listOf(
        Color(0xFF7BB7FC),
        Color(0xFF4F0099)
    )

    val brush = Brush.linearGradient(
        colors = gradientColors,
        start = Offset(0f, 0f),
        end = Offset(300f, 0f) // Adjust panjang gradien horizontal
    )

    Text(
        text = text,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        style = TextStyle(
            brush = brush
        )
    )
}

