package com.graltion.aquaclean.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.graltion.aquaclean.R
import com.graltion.aquaclean.data.PreferencesDataStore
import com.graltion.aquaclean.ui.components.DrawerMenu
import com.graltion.aquaclean.ui.screens.AboutScreen
import com.graltion.aquaclean.ui.screens.CleaningScreen
import com.graltion.aquaclean.ui.screens.CompletionScreen
import com.graltion.aquaclean.ui.screens.LicensesScreen
import com.graltion.aquaclean.ui.screens.MainScreen
import com.graltion.aquaclean.ui.screens.OnboardingScreen
import com.graltion.aquaclean.ui.screens.PrivacyScreen
import com.graltion.aquaclean.ui.screens.SettingsScreen
import com.graltion.aquaclean.ui.screens.SplashScreen
import com.graltion.aquaclean.ui.screens.TermsScreen
import com.graltion.aquaclean.utils.CleaningMode
import com.graltion.aquaclean.utils.DeviceType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AquaCleanApp() {
    val context = LocalContext.current
    val prefs = PreferencesDataStore(context)
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val isOnboardingComplete by prefs.isOnboardingComplete.collectAsState(initial = false)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Splash.route

    val showScaffold = currentRoute !in listOf(
        Screen.Splash.route,
        Screen.Onboarding.route,
        Screen.Cleaning.route
    )

    val screenTitles = mapOf(
        Screen.Main.route to R.string.nav_home,
        Screen.Settings.route to R.string.nav_settings,
        Screen.About.route to R.string.nav_about,
        Screen.Terms.route to R.string.nav_terms,
        Screen.Privacy.route to R.string.nav_privacy,
        Screen.Licenses.route to R.string.nav_licenses,
        Screen.Completion.route to R.string.cleaning_completed,
    )

    if (showScaffold) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    DrawerMenu(
                        currentRoute = currentRoute,
                        onNavigate = { route ->
                            navController.navigate(route) {
                                popUpTo(Screen.Main.route)
                                launchSingleTop = true
                            }
                        },
                        onClose = { scope.launch { drawerState.close() } }
                    )
                }
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = screenTitles[currentRoute]?.let { stringResource(it) } ?: "AquaClean"
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Filled.Menu, contentDescription = stringResource(R.string.menu))
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors()
                    )
                }
            ) { padding ->
                Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                    AppNavHost(navController, prefs, isOnboardingComplete)
                }
            }
        }
    } else {
        AppNavHost(navController, prefs, isOnboardingComplete)
    }
}

@Composable
private fun AppNavHost(
    navController: androidx.navigation.NavHostController,
    prefs: PreferencesDataStore,
    isOnboardingComplete: Boolean
) {
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                isOnboardingComplete = isOnboardingComplete,
                onFinished = { complete ->
                    if (complete) navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    } else navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Onboarding.route) {
            OnboardingScreen(onAccepted = {
                scope.launch { prefs.setOnboardingComplete() }
                navController.navigate(Screen.Main.route) {
                    popUpTo(Screen.Onboarding.route) { inclusive = true }
                }
            })
        }

        composable(Screen.Main.route) {
            MainScreen(onStartCleaning = { mode, duration, device ->
                navController.navigate(Screen.Cleaning.createRoute(mode.name, duration, device.name))
            })
        }

        composable(
            route = Screen.Cleaning.route,
            arguments = listOf(
                navArgument("mode") { type = NavType.StringType },
                navArgument("duration") { type = NavType.IntType },
                navArgument("deviceType") { type = NavType.StringType }
            )
        ) { backStack ->
            val mode = CleaningMode.valueOf(backStack.arguments?.getString("mode") ?: "NORMAL")
            val duration = backStack.arguments?.getInt("duration") ?: 45
            val deviceType = DeviceType.valueOf(backStack.arguments?.getString("deviceType") ?: "PHONE")

            CleaningScreen(
                mode = mode,
                duration = duration,
                deviceType = deviceType,
                onCompleted = {
                    navController.navigate(Screen.Completion.createRoute(mode.name, duration)) {
                        popUpTo(Screen.Main.route)
                    }
                },
                onStop = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.Completion.route,
            arguments = listOf(
                navArgument("mode") { type = NavType.StringType },
                navArgument("duration") { type = NavType.IntType }
            )
        ) { backStack ->
            val mode = CleaningMode.valueOf(backStack.arguments?.getString("mode") ?: "NORMAL")
            val duration = backStack.arguments?.getInt("duration") ?: 45

            CompletionScreen(
                mode = mode,
                duration = duration,
                onRepeat = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                },
                onDone = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Settings.route) { SettingsScreen(prefs) }
        composable(Screen.About.route) { AboutScreen() }
        composable(Screen.Terms.route) { TermsScreen() }
        composable(Screen.Privacy.route) { PrivacyScreen() }
        composable(Screen.Licenses.route) { LicensesScreen() }
    }
}
