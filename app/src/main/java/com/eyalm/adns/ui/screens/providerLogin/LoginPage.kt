package com.eyalm.adns.ui.screens.providerLogin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eyalm.adns.data.models.DnsProvider
import com.eyalm.adns.ui.components.OnboardingTemplate
import com.eyalm.adns.ui.components.StandardBottomBar

@Composable
fun Login(provider: DnsProvider,
          onNextClick: (email: String, password: String) -> Unit,
          onBackClick: () -> Unit
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    OnboardingTemplate(
        onBackClick = onBackClick,
        bottomBarContent = {
            StandardBottomBar(
                message = "",
                buttonText = "Next",
                enabled = true,
                onNextClick = { onNextClick(email, password) }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Login")
                Text("To your ${provider.name} account")

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Username or email") },
                    modifier = androidx.compose.ui.Modifier.fillMaxWidth()
                )

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = androidx.compose.ui.Modifier.fillMaxWidth()
                )
            }

        }
    )

}