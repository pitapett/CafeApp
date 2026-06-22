package com.example.cafeapp.feature.staff.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cafeapp.data.remote.dto.ProcessOrderResponse
import com.example.cafeapp.utils.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveOrdersScreen(
    onBackClicked: () -> Unit,
    onOrderClicked: (String) -> Unit, // Pass the Order ID to view details or process payment
    viewModel: ActiveOrdersViewModel = viewModel() // 🌟 Inject the ViewModel
) {
    // 🌟 Observe the StateFlow from your ViewModel
    val orderState by viewModel.orders.collectAsState()

    // 🌟 Trigger fetchActiveOrders() when the screen is first loaded
    LaunchedEffect(Unit) {
        viewModel.fetchActiveOrders()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Active Orders") },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back to Dashboard")
                    }
                }
            )
        }
    ) { paddingValues ->

        // 🌟 Handle the Resource states (Loading, Success, Error)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (orderState) {
                is Resource.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is Resource.Error -> {
                    Text(
                        text = orderState.message ?: "Failed to load active orders",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is Resource.Success -> {
                    val activeOrders = orderState.data ?: emptyList()

                    if (activeOrders.isEmpty()) {
                        Text("No active orders at the moment.", modifier = Modifier.align(Alignment.Center))
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
                        ) {
                            items(activeOrders) { order ->
                                ActiveOrderCard(
                                    order = order,
                                    // 🌟 Make sure you pass the correct ID property from your DTO
                                    onClick = { onOrderClicked(order.id.toString()) }
                                )
                            }
                        }
                    }
                }
                is Resource.Idle -> { /* Waiting for fetch to start */ }
            }
        }
    }
}

// 🧱 LOCAL COMPONENT
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActiveOrderCard(
    order: ProcessOrderResponse, // 🌟 Updated to use your actual Retrofit DTO
    onClick: () -> Unit
) {
    // Determine badge color based on status (Provide a fallback)
    val statusColor = when (order.status?.uppercase()) {
        "PREPARING", "PENDING" -> Color(0xFFFF9800) // Orange
        "READY" -> Color(0xFF4CAF50)                // Green
        "SERVED" -> Color(0xFF2196F3)               // Blue
        else -> Color.Gray
    }

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side: Icon and Table Number
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Tbl", style = MaterialTheme.typography.labelSmall)
                        // 🌟 Adjust `order.tableNumber` to whatever property your DTO uses
                        Text(order.tableNumber ?: "-", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Middle: Order Details
            Column(modifier = Modifier.weight(1f)) {
                // 🌟 Adjust `order.id` if your DTO calls it `orderId`
                Text(text = "Order #${order.id}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))

                // 🌟 Adjust `order.itemsCount` and `order.total` based on your actual DTO fields
                val itemsText = "${order.items?.size ?: 0} items" // Example if it contains a list of items
                val totalText = "$${String.format("%.2f", order.total ?: 0.0)}"

                Text(text = "$itemsText • $totalText", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))

                // Status Badge
                Surface(
                    color = statusColor.copy(alpha = 0.15f),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = order.status ?: "UNKNOWN",
                        color = statusColor,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Right side: Action Icon
            Icon(
                Icons.Default.ReceiptLong,
                contentDescription = "View Order",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}