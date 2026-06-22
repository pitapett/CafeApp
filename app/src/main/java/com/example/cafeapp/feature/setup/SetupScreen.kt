package com.example.cafeapp.feature.setup

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SetupScreen(
    onRoleSelected: (String) -> Unit
) {
    // 1. Mengambil Context di dalam Compose
    val context = LocalContext.current

    // 2. Fungsi untuk menyimpan data ke SharedPreferences dan memicu navigasi
    fun saveRoleAndNavigate(role: String) {
        val sharedPreferences = context.getSharedPreferences("CafeAppPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("USER_ROLE", role).apply()

        // Panggil fungsi lambda untuk memberi tahu NavHost agar pindah layar
        onRoleSelected(role)
    }

    // 3. UI Layout (Menggantikan activity_setup.xml)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Selamat Datang di CafeApp",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Silakan pilih peran Anda untuk melanjutkan",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Tombol Staff
        Button(
            onClick = { saveRoleAndNavigate("STAFF") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Masuk sebagai STAFF")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tombol Admin
        Button(
            onClick = { saveRoleAndNavigate("ADMIN") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Masuk sebagai ADMIN")
        }
    }
}