package com.github.jvsena42.floresta.presentation.ui.screens.receive

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.jvsena42.floresta.presentation.ui.theme.FlorestaTheme
import kotlinx.coroutines.launch

@Composable
fun ScreenReceive(
    viewModel: ReceiveViewModel = koinViewModel()
) {
    val uiState: ReceiveUIState by viewModel.uiState.collectAsState()
    ScreenReceive(uiState)
}

@Composable
private fun ScreenReceive(uiState: ReceiveUIState) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(visible = uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(200.dp)
                        .padding(32.dp)
                )
            }

            Text(
                uiState.address,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .clickable {
                        clipboardManager.setText(AnnotatedString(uiState.address))
                        scope.launch {
                            snackBarHostState
                                .showSnackbar(
                                    message = "Text copied to clipboard",
                                    duration = SnackbarDuration.Short
                                )
                        }
                    }
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    FlorestaTheme {
        ScreenReceive(uiState = ReceiveUIState(address = "bc1qfdsafkpowfenfsdlknv"))
    }
}