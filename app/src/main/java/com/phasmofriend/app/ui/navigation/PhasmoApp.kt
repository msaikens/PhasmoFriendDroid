package com.phasmofriend.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.phasmofriend.app.R
import com.phasmofriend.app.ui.behavior.BehaviorScreen
import com.phasmofriend.app.ui.evidence.EvidenceScreen
import com.phasmofriend.app.ui.ghostdetail.GhostDetailScreen
import com.phasmofriend.app.ui.results.ResultsScreen
import com.phasmofriend.app.ui.shared.InvestigationViewModel

@Composable
fun PhasmoApp(viewModel: InvestigationViewModel = viewModel()) {
    val navController = rememberNavController()
    var showClearAllDialog by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            val backStackEntry by navController.currentBackStackEntryAsState()
            val destination = backStackEntry?.destination

            NavigationBar {
                NavigationBarItem(
                    selected = destination?.hierarchyHasRoute<Route.Evidence>() == true,
                    onClick = { navController.navigateSingleTopTo(Route.Evidence) },
                    icon = { Icon(painterResource(R.drawable.ic_evidence), contentDescription = null) },
                    label = { Text(stringResource(R.string.title_evidence)) }
                )
                NavigationBarItem(
                    selected = destination?.hierarchyHasRoute<Route.Behavior>() == true,
                    onClick = { navController.navigateSingleTopTo(Route.Behavior) },
                    icon = { Icon(painterResource(R.drawable.ic_behavior), contentDescription = null) },
                    label = { Text(stringResource(R.string.title_behavior)) }
                )
                NavigationBarItem(
                    selected = destination?.hierarchyHasRoute<Route.Results>() == true,
                    onClick = { navController.navigateSingleTopTo(Route.Results) },
                    icon = { Icon(painterResource(R.drawable.ic_results), contentDescription = null) },
                    label = { Text(stringResource(R.string.title_results)) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { showClearAllDialog = true },
                    icon = { Icon(painterResource(R.drawable.ic_clear_all), contentDescription = null) },
                    label = { Text(stringResource(R.string.title_clear_all)) }
                )
            }
        }
    ) { contentPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.Evidence,
            modifier = Modifier.padding(contentPadding)
        ) {
            composable<Route.Evidence> {
                EvidenceScreen(viewModel = viewModel)
            }
            composable<Route.Behavior> {
                BehaviorScreen(viewModel = viewModel)
            }
            composable<Route.Results> {
                ResultsScreen(
                    viewModel = viewModel,
                    onGhostClick = { ghostId -> navController.navigate(Route.GhostDetail(ghostId)) }
                )
            }
            composable<Route.GhostDetail> { backStackEntry ->
                val route: Route.GhostDetail = backStackEntry.toRoute()
                GhostDetailScreen(
                    ghostId = route.ghostId,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }

    if (showClearAllDialog) {
        ClearAllDialog(
            onConfirm = {
                viewModel.clearAll()
                showClearAllDialog = false
            },
            onDismiss = { showClearAllDialog = false }
        )
    }
}

private inline fun <reified T : Route> NavDestination.hierarchyHasRoute(): Boolean =
    hierarchy.any { it.hasRoute<T>() }

private fun NavController.navigateSingleTopTo(route: Route) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
