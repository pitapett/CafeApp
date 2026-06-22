package com.example.cafeapp.navigation
import kotlinx.serialization.Serializable

class AppRoutes {
    @Serializable object SetupRoute
    @Serializable object AdminDashboardRoute
    @Serializable object ManageStockRoute
    @Serializable object ManageTablesRoute
    @Serializable object StaffDashboardRoute
    @Serializable object ActiveOrdersRoute
    @Serializable object StaffTableStatusRoute

    // Screens that require arguments (Replaces intent.putExtra)
    @Serializable data class StaffMenuRoute(val tableNumber: String)
    @Serializable data class CartDetailRoute(val tableNumber: String)
}