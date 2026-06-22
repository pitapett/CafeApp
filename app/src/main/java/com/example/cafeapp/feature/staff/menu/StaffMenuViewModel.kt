package com.example.cafeapp.feature.staff.menu

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cafeapp.data.local.CafeDatabase
import com.example.cafeapp.data.local.entity.MenuEntity
import com.example.cafeapp.data.remote.RetrofitClient
import com.example.cafeapp.data.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StaffMenuViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: OrderRepository

    init {
        val menuDao = CafeDatabase.getDatabase(application).menuDao()
        val draftCartDao = CafeDatabase.getDatabase(application).draftCartDao()
        repository = OrderRepository(
            RetrofitClient.api,
            menuDao,
            draftCartDao
        )
    }

    val menuState = repository.getMenuStream().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // 🌟 UPDATED: Convert liveCart to StateFlow for Compose
    val liveCartState = repository.getLiveCartStream().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList() // Assuming it returns a List<DraftCartEntity>
    )

    fun syncMenuWithServer() {
        viewModelScope.launch {
            repository.syncMenu()
        }
    }

    fun addToCart(menuItem: MenuEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addToCart(menuItem)
        }
    }

    private val _checkoutResult = MutableSharedFlow<Boolean>()
    val checkoutResult = _checkoutResult.asSharedFlow()

    fun checkoutCart(tableNumber: String, staffId: String) {
        viewModelScope.launch {
            val success = repository.processCheckout(tableNumber, staffId)
            _checkoutResult.emit(success)
        }
    }
}