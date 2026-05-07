package com.example.cafeapp.ui.staff

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import com.example.cafeapp.utils.Resource
import com.example.cafeapp.viewmodel.StaffTableViewModel
import kotlinx.coroutines.launch

class StaffTableStatusActivity : AppCompatActivity() {

    private val viewModel: StaffTableViewModel by viewModels()
    private lateinit var adapter: StaffTableAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff_table_status)

        setupRecyclerView()
        observeViewModel()

        viewModel.fetchTables()
    }

    private fun setupRecyclerView() {
        adapter = StaffTableAdapter { id, isAvailable ->
            viewModel.toggleTableStatus(id, isAvailable)
        }

        findViewById<RecyclerView>(R.id.rvStaffTables).apply {
            layoutManager = LinearLayoutManager(this@StaffTableStatusActivity)
            this.adapter = this@StaffTableStatusActivity.adapter
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tables.collect { state ->
                    if (state is Resource.Success) {
                        adapter.submitList(state.data)
                    } else if (state is Resource.Error) {
                        Toast.makeText(this@StaffTableStatusActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}