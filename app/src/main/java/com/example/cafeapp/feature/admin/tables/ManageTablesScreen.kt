package com.example.cafeapp.feature.admin.tables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
fun ManageTablesScreen(
    onBackClicked: () -> Unit,
    onAddTableClicked: () -> Unit,
    onTableClicked: (String) -> Unit,
    viewModel: ManageTablesViewModel = viewModel() // 🌟 Inject the ViewModel
) {
    // 🌟 Observe the tables state
    val tableState by viewModel.tables.collectAsState()

    // 🌟 Trigger fetchTables() only once when the screen opens
    LaunchedEffect(Unit) {
        viewModel.fetchTables()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Tables") },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Go Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTableClicked) {
                Icon(Icons.Default.Add, contentDescription = "Add New Table")
            }
        }
    ) { paddingValues ->

        // 🌟 Handle Loading, Error, and Success states
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
                        Text("No tables configured.", modifier = Modifier.align(Alignment.Center))
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(bottom = 80.dp, top = 16.dp)
                        ) {
                            items(tables) { table ->
                                TableGridCard(
                                    table = table,
                                    onClick = { onTableClicked(table.id.toString()) } // Ensure ID is string
                                )
                            }
                        }
                    }
                }
                is Resource.Idle -> { }
            }
        }
    }
}

// 🧱 LOCAL COMPONENT
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TableGridCard(
    table: TableResponse, // 🌟 Updated to your actual Retrofit DTO
    onClick: () -> Unit
) {
    // Determine color based on status (Provide a fallback if status is null)
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
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Area: ${table.area ?: "Main"}", // Adjusted to match your TableRequest DTO fields
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Status Badge
            Surface(
                color = statusColor.copy(alpha = 0.2f),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = table.status ?: "UNKNOWN",
                    color = statusColor,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}