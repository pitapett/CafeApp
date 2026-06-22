package com.example.cafeapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cafeapp.navigation.AppRoutes.AdminDashboardRoute
import com.example.cafeapp.navigation.AppRoutes.ManageStockRoute
import com.example.cafeapp.navigation.AppRoutes.ManageTablesRoute
import com.example.cafeapp.navigation.AppRoutes.StaffDashboardRoute
import com.example.cafeapp.navigation.AppRoutes.StaffMenuRoute
import com.example.cafeapp.navigation.AppRoutes.CartDetailRoute
import com.example.cafeapp.navigation.AppRoutes.ActiveOrdersRoute
import com.example.cafeapp.navigation.AppRoutes.StaffTableStatusRoute

@Composable
fun CafeAppNavigation(startRole: String?) {
    val navController = rememberNavController()

    // Menentukan rute awal
    val startDest = when (startRole) {
        "STAFF" -> AppRoutes.StaffDashboardRoute
        "ADMIN" -> AppRoutes.AdminDashboardRoute
        else -> AppRoutes.SetupRoute
    }

    NavHost(navController = navController, startDestination = startDest) {

        // 1. Setup Screen
        composable<AppRoutes.SetupRoute> {
            // SetupScreen() MASIH AKAN MERAH sebelum Anda membuat file SetupScreen.kt
            SetupScreen(
                onRoleSelected = { role ->
                    if (role == "STAFF") {
                        navController.navigate(AppRoutes.StaffDashboardRoute)
                    } else {
                        navController.navigate(AppRoutes.AdminDashboardRoute)
                    }
                }
            )
        }

        // 2. Staff Dashboard (Kosong untuk sementara)
        composable<AppRoutes.StaffDashboardRoute> {

        }

        // 3. Admin Dashboard (Kosong untuk sementara)
        // Inside your NavHost { ... }

        composable<AdminDashboardRoute> {
            AdminDashboardScreen(
                onManageStockClicked = {
                    // Navigate to Manage Stock (assuming you created the route object)
                    navController.navigate(ManageStockRoute)
                },
                onManageTablesClicked = {
                    // Navigate to Manage Tables
                    navController.navigate(ManageTablesRoute)
                },
                onLogoutClicked = {
                    // Clear the backstack and go back to Setup
                    navController.navigate(SetupRoute) {
                        popUpTo(0) { inclusive = true } // Clears navigation history
                    }
                }
            )
        }

        // Inside your NavHost { ... }

        composable<ManageStockRoute> {
            ManageStockScreen(
                onBackClicked = {
                    // Pops this screen off the stack, returning to AdminDashboard
                    navController.popBackStack()
                },
                onAddStockClicked = {
                    // navController.navigate(AddEditStockRoute(itemId = null))
                },
                onEditItemClicked = { itemId ->
                    // navController.navigate(AddEditStockRoute(itemId = itemId))
                }
            )
        }

        // Inside your NavHost { ... }

        composable<ManageTablesRoute> {
            ManageTablesScreen(
                onBackClicked = {
                    navController.popBackStack()
                },
                onAddTableClicked = {
                    // navController.navigate(AddEditTableRoute)
                },
                onTableClicked = { tableId ->
                    // navController.navigate(TableDetailRoute(tableId))
                }
            )
        }

        // Inside your NavHost { ... }

        composable<StaffDashboardRoute> {
            StaffDashboardScreen(
                onNewOrderClicked = { tableNum ->
                    // Navigate to the menu, passing the typed-in table number
                    navController.navigate(StaffMenuRoute(tableNumber = tableNum))
                },
                onActiveOrdersClicked = {
                    navController.navigate(ActiveOrdersRoute)
                },
                onTableStatusClicked = {
                    navController.navigate(StaffTableStatusRoute)
                },
                onLogoutClicked = {
                    // Clear the backstack and go back to Setup
                    navController.navigate(SetupRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Inside your NavHost { ... }

        composable<StaffMenuRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<StaffMenuRoute>()

            StaffMenuScreen(
                tableNumber = route.tableNumber,
                onBackClicked = { navController.popBackStack() },
                onViewCartClicked = {
                    navController.navigate(CartDetailRoute(route.tableNumber))
                }
            )
        }

        // Inside your NavHost { ... }

        composable<CartDetailRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<CartDetailRoute>()

            CartDetailScreen(
                tableNumber = route.tableNumber,
                onBackClicked = { navController.popBackStack() },
                onCheckoutSuccess = {
                    // Once payment is successful, clear the backstack and return to Dashboard!
                    navController.navigate(StaffDashboardRoute) {
                        popUpTo(StaffDashboardRoute) { inclusive = false }
                    }
                }
            )
        }

        // Inside your NavHost { ... }

        composable<ActiveOrdersRoute> {
            ActiveOrdersScreen(
                onBackClicked = { navController.popBackStack() },
                onOrderClicked = { orderId ->
                    // Navigate to an Order Detail or Payment screen
                    // navController.navigate(OrderDetailRoute(orderId))
                }
            )
        }

        // Inside your NavHost { ... }

        composable<StaffTableStatusRoute> {
            StaffTableStatusScreen(
                onBackClicked = { navController.popBackStack() }
            )
        }
    }
}