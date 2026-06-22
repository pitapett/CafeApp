package com.example.cafeapp.feature.staff.tablestatus

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cafeapp.data.remote.dto.TableResponse
import com.example.cafeapp.utils.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffTableStatusScreen(
    onBackClicked: () -> Unit,
    viewModel: StaffTableViewModel = viewModel() // 🌟 Inject ViewModel
) {
    // 🌟 Observe the StateFlow
    val tableState by viewModel.tables.collectAsState()

    // 🌟 Fetch tables when screen loads
    LaunchedEffect(Unit) {
        viewModel.fetchTables()
    }

    // 🧠 LOCAL STATE: Uses your actual TableResponse DTO now
    var selectedTable by remember { mutableStateOf<TableResponse?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Update Table Status") },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back to Dashboard")
                    }
                }
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (tableState) {
                is Resource.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is Resource.Error -> {
                    Text(
                        text = tableState.message ?: "Failed to load tables",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is Resource.Success -> {
                    val tables = tableState.data ?: emptyList()

                    if (tables.isEmpty()) {
                        Text("No tables found.", modifier = Modifier.align(Alignment.Center))
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
                        ) {
                            items(tables) { table ->
                                StaffTableCard(
                                    table = table,
                                    onClick = { selectedTable = table }
                                )
                            }
                        }
                    }
                }
                is Resource.Idle -> {}
            }
        }
    }

    // 💬 THE DIALOG: Hooked up to the ViewModel
    selectedTable?.let { table ->
        UpdateStatusDialog(
            tableNumber = table.tableNumber?.toString() ?: "Unknown",
            currentStatus = table.status ?: "AVAILABLE",
            onDismiss = { selectedTable = null },
            onStatusSelected = { newStatus ->
                // 🌟 Call the ViewModel function!
                viewModel.updateTableStatus(table.id.toString(), newStatus)
                selectedTable = null // Close dialog after selection
            }
        )
    }
}

// 🧱 LOCAL COMPONENTS

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StaffTableCard(
    table: TableResponse, // 🌟 Updated to use your DTO
    onClick: () -> Unit
) {
    val statusColor = when (table.status?.uppercase()) {
        "AVAILABLE" -> Color(0xFF4CAF50) // Green
        "OCCUPIED" -> Color(0xFFF44336)  // Red
        "CLEANING" -> Color(0xFFFF9800)  // Orange
        "RESERVED" -> Color(0xFF2196F3)  // Blue
        else -> Color.Gray
    }

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Table ${table.tableNumber}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                color = statusColor.copy(alpha = 0.2f),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = table.status ?: "UNKNOWN",
                    color = statusColor,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun UpdateStatusDialog(
    tableNumber: String,
    currentStatus: String,
    onDismiss: () -> Unit,
    onStatusSelected: (String) -> Unit
) {
    val statuses = listOf("AVAILABLE", "OCCUPIED", "CLEANING", "RESERVED")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Update Table $tableNumber") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Select new status:")
                Spacer(modifier = Modifier.height(8.dp))
                statuses.forEach { status ->
                    TextButton(
                        onClick = { onStatusSelected(status) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = status,
                            // Highlight the current status
                            fontWeight = if (status.equals(currentStatus, ignoreCase = true)) FontWeight.Bold else FontWeight.Normal,
                            color = if (status.equals(currentStatus, ignoreCase = true)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}