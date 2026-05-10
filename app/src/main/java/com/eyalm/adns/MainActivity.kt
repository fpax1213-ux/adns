package com.eyalm.adns

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.eyalm.adns.ui.screens.HomeScreen
import com.eyalm.adns.ui.screens.StatsScreen
import com.eyalm.adns.ui.screens.UpdateDialog
import com.eyalm.adns.ui.theme.AdnsTheme
import com.eyalm.adns.viewmodel.MainViewModel


class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    private fun handleShortcutIntent(intent: Intent?) {
        if (intent?.action == "com.eyalm.adns.TOGGLE_ACTION") {
            viewModel.toggleDns()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleShortcutIntent(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val context = this@MainActivity
        installSplashScreen()
        super.onCreate(savedInstanceState)

        if (checkSelfPermission(Manifest.permission.WRITE_SECURE_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
            startActivity(Intent(this, OnboardingActivity::class.java))
            finish()
            return
        }
        enableEdgeToEdge()
        handleShortcutIntent(intent)

        setContent {
            AdnsTheme {
                val isEnabled by viewModel.adBlockingState.collectAsState()
                val runningTime by viewModel.runningTimeFlow.collectAsState()
                val server by viewModel.dnsUrlFlow.collectAsState()

                val showDialog = remember { mutableStateOf(false) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        isEnabled = isEnabled,
                        runningTime = runningTime,
                        onToggle = { viewModel.toggleDns() },
                        modifier = Modifier.padding(innerPadding),
                        server = server,
                        onEditClick = {
                            val intent = Intent(context, SettingsActivity::class.java).apply {
                                putExtra("open_providers", true)
                            }
                            context.startActivity(intent)
                        },
                        checkForUpdate = viewModel::checkForUpdate
                    )
                }

                if (showDialog.value) {
                    DnsDialog(
                        onDismissRequest = { showDialog.value = false },
                        onConfirmation = {
                            viewModel.setDnsUrl(it)
                            showDialog.value = false
                        },
                        currentUrl = server
                    )
                }

            }
        }

    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Greeting(
    isEnabled: Boolean,
    runningTime: String,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    server: String = "dns.adguard-dns.com",
    onEditClick: () -> Unit = {},
    checkForUpdate: ((String?) -> Unit) -> Unit = {}
) {

    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home", "Stats", "Settings")
    val selectedIcons = listOf(Icons.Filled.Home, Icons.Filled.Insights, Icons.Filled.Settings)
    val unselectedIcons =
        listOf(Icons.Outlined.Home, Icons.Outlined.Insights, Icons.Outlined.Settings)


    val latestVersion = remember { mutableStateOf<String?>(null) }

    if (!BuildConfig.IS_FOSS) {
        LaunchedEffect(Unit) {
            checkForUpdate { version ->
                Log.d("update", "Latest version: $version")
                latestVersion.value = version
            }
        }

        latestVersion.value?.let { version ->
            UpdateDialog(
                version = version,
                onClose = { latestVersion.value = null }
            )
        }
    }

    Scaffold(
        bottomBar = {

            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                                contentDescription = item,
                            )
                        },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                    )
                }
            }

        },
        contentWindowInsets = WindowInsets(0)
    ) {
        innerPadding ->

        when (selectedItem) {
            0 -> {
                HomeScreen(
                    isEnabled = isEnabled,
                    runningTime = runningTime,
                    onToggle = onToggle,
                    modifier = modifier,
                    server = server,
                    onEditClick = onEditClick,
                    innerPadding = innerPadding
                )

            }
            1 -> StatsScreen(
                innerPadding
            )
            2 -> {
                Greeting2() // TODO: Move settings screen and logic
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AdnsTheme {
        Greeting(
            isEnabled = true,
            runningTime = "00:05:23",
            onToggle = {}
        )
    }
}