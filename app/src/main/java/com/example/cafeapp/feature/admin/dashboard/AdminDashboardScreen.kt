package com.example.cafeapp.feature.admin.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AdminDashboardScreen(
    onManageStockClicked: () -> Unit,
    onManageTablesClicked: () -> Unit,
    onLogoutClicked: () -> Unit // Good practice to allow them to go back to Setup
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Placeholder for a Top App Bar / Header
        Text(
            text = "Admin Dashboard",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Navigation Buttons
        Button(
            onClick = onManageStockClicked,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(bottom = 16.dp)
        ) {
            Text("Manage Stock")
        }

        Button(
            onClick = onManageTablesClicked,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(bottom = 32.dp)
        ) {
            Text("Manage Tables")
        }

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        OutlinedButton(
            onClick = onLogoutClicked,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout / Switch Role")
        }
    }
}