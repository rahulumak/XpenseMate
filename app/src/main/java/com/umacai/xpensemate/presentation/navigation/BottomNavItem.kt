package com.umacai.xpensemate.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Savings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    data object Home : BottomNavItem("home", "Home", Icons.Default.Home)
    data object Transactions : BottomNavItem("transactions", "Transactions", Icons.Default.AccountBalanceWallet)
    data object Add : BottomNavItem("add", "", Icons.Default.Add)
    data object Budgets : BottomNavItem("budgets", "Budgets", Icons.Default.Savings)
    data object Account : BottomNavItem("account", "Account", Icons.Default.Person)

    companion object {
        val items = listOf(Home, Transactions, Add, Budgets, Account)
    }
}