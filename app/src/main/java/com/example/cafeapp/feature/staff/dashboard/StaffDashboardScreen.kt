package com.example.cafeapp.feature.staff.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffDashboardScreen(
    onNewOrderClicked: (String) -> Unit, // Requires the table number
    onActiveOrdersClicked: () -> Unit,
    onTableStatusClicked: () -> Unit,
    onLogoutClicked: () -> Unit
) {
    // 🧠 LOCAL STATE: These variables control the Table Input Dialog
    var showTableDialog by remember { mutableStateOf(false) }
    var tableInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Staff Dashboard",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Triggers the state to show the dialog instead of navigating immediately
        Button(
            onClick = { showTableDialog = true },
            modifier = Modifier.fillMaxWidth().height(56.dp).padding(bottom = 16.dp)
        ) {
            Text("New Order (Select Table)")
        }

        Button(
            onClick = onActiveOrdersClicked,
            modifier = Modifier.fillMaxWidth().height(56.dp).padding(bottom = 16.dp)
        ) {
            Text("Active Orders & Payments")
        }

        Button(
            onClick = onTableStatusClicked,
            modifier = Modifier.fillMaxWidth().height(56.dp).padding(bottom = 32.dp)
        ) {
            Text("Update Table Status")
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        OutlinedButton(
            onClick = onLogoutClicked,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout / Switch Role")
        }
    }

    // 💬 THE DIALOG: Only renders if showTableDialog is true
    if (showTableDialog) {
        AlertDialog(
            onDismissRequest = { showTableDialog = false },
            title = { Text("Enter Table Number") },
            text = {
                OutlinedTextField(
                    value = tableInput,
                    onValueChange = { tableInput = it },
                    label = { Text("Table #") },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (tableInput.isNotBlank()) {
                            showTableDialog = false
                            onNewOrderClicked(tableInput) // Pass the input to the NavHost
                            tableInput = "" // Reset for next time
                        }
                    }
                ) {
                    Text("Start Order")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTableDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}