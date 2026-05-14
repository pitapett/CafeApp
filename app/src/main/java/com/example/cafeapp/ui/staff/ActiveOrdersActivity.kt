package com.example.cafeapp.ui.staff

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import com.example.cafeapp.utils.Resource
import com.example.cafeapp.viewmodel.ActiveOrdersViewModel
import kotlinx.coroutines.launch

class ActiveOrdersActivity : AppCompatActivity() {

    private val viewModel: ActiveOrdersViewModel by viewModels()
    private lateinit var adapter: ActiveOrdersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_active_orders)

        setupRecyclerView()
        observeViewModel()
        viewModel.fetchActiveOrders()
    }

    private fun setupRecyclerView() {
        adapter = ActiveOrdersAdapter { selectedOrder ->
            showPaymentDialog(selectedOrder.id)
        }

        findViewById<RecyclerView>(R.id.rvActiveOrders).apply {
            layoutManager = LinearLayoutManager(this@ActiveOrdersActivity)
            this.adapter = this@ActiveOrdersActivity.adapter
        }
    }

    private fun showPaymentDialog(orderId: String) {
        val methods = arrayOf("Cash", "QRIS")

        AlertDialog.Builder(this)
            .setTitle("Select Payment Method")
            .setItems(methods) { _, which ->
                val selectedMethod = methods[which]
                viewModel.processPayment(orderId, selectedMethod)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Observe the list of orders
                launch {
                    viewModel.orders.collect { state ->
                        if (state is Resource.Success) {
                            adapter.submitList(state.data)
                        } else if (state is Resource.Error) {
                            Toast.makeText(this@ActiveOrdersActivity, state.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                launch {
                    viewModel.paymentStatus.collect { state ->
                        if (state is Resource.Success) {
                            Toast.makeText(this@ActiveOrdersActivity, state.data, Toast.LENGTH_SHORT).show()
                            viewModel.resetPaymentStatus()
                        } else if (state is Resource.Error) {
                            Toast.makeText(this@ActiveOrdersActivity, state.message, Toast.LENGTH_LONG).show()
                            viewModel.resetPaymentStatus()
                        }
                    }
                }
            }
        }
    }
}