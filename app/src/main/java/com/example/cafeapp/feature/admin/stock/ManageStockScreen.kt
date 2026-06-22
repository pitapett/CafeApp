package com.example.cafeapp.feature.admin.stock

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cafeapp.utils.Resource
import com.example.cafeapp.data.remote.dto.StockResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageStockScreen(
    onBackClicked: () -> Unit,
    onAddStockClicked: () -> Unit,
    onEditItemClicked: (String) -> Unit,
    viewModel: ManageStockViewModel = viewModel() // 🌟 Inject the ViewModel
) {
    // 🌟 Observe the StateFlow from your ViewModel
    val stockState by viewModel.stockList.collectAsState()

    // 🌟 Trigger fetchStock() only once when the screen is first loaded
    LaunchedEffect(Unit) {
        viewModel.fetchStock()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Stock") },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Go Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddStockClicked) {
                Icon(Icons.Default.Add, contentDescription = "Add New Stock")
            }
        }
    ) { paddingValues ->

        // 🌟 Handle the Resource states (Loading, Success, Error)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (stockState) {
                is Resource.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is Resource.Error -> {
                    Text(
                        text = stockState.message ?: "An unknown error occurred",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is Resource.Success -> {
                    val stockItems = stockState.data ?: emptyList()

                    if (stockItems.isEmpty()) {
                        Text("No stock available.", modifier = Modifier.align(Alignment.Center))
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(bottom = 80.dp)
                        ) {
                            items(stockItems) { item ->
                                StockItemCard(
                                    item = item,
                                    onEditClicked = { onEditItemClicked(item.id.toString()) } // Ensure ID is a string
                                )
                            }
                        }
                    }
                }
                is Resource.Idle -> { /* Do nothing, waiting for load */ }
            }
        }
    }
}

// 🧱 LOCAL COMPONENT
@Composable
private fun StockItemCard(
    item: StockResponse, // 🌟 Updated to use your actual DTO from Retrofit
    onEditClicked: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = item.name ?: "Unknown", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                // Assuming your DTO has an `amount` field based on your ViewModel parameters
                Text(
                    text = "Qty: ${item.amount}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(onClick = onEditClicked) {
                Icon(Icons.Default.Edit, contentDescription = "Edit ${item.name}")
            }
        }
    }
}