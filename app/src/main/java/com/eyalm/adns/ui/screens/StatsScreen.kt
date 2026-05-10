package com.eyalm.adns.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eyalm.adns.viewmodel.MainViewModel

@Composable
fun StatsScreen(
    paddingValues: PaddingValues
) {

    val viewModel: MainViewModel = viewModel()

    val stats = viewModel.dnsStats

    val error = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(true) {
        if (viewModel.dnsStats == null) {
            try {
                viewModel.getStats()
            } catch (e: Exception) {
                error.value = e.message ?: "Failed to load stats"
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        if (error.value != null) {
            Text("Error: ${error.value}")
        } else if (stats == null) {
            Text("Loading...")
        } else {

            Text(stats.toString())
        }

    }


}