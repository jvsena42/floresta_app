package com.github.jvsena42.floresta.presentation.ui.screens.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.jvsena42.floresta.presentation.ui.theme.FlorestaTheme
import com.github.jvsena42.floresta.R
import com.github.jvsena42.floresta.presentation.ui.theme.Danger
import com.github.jvsena42.floresta.presentation.ui.theme.Primary

@Composable
fun TransactionItem(
    title: String,
    date: String,
    amount: String,
    isReceived: Boolean,
    modifier: Modifier = Modifier
) {
    val color = if (isReceived) Primary else Danger

    Column(
        modifier = modifier
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            val icon = if (isReceived) R.drawable.ic_arrow_down else R.drawable.ic_arrow_up
            Icon(
                painterResource(icon),
                contentDescription = null,
                tint = color,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Text(
                title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.weight(1f).padding(end = 32.dp)
            )
            Text(
                amount,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Text(
                stringResource(R.string.sats),
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    FlorestaTheme {
        TransactionItem(
            title = "ASDN646AS8D46ASD8FF8AS84F68AS4DAS5F3",
            date = "01/02/2024 19:32",
            amount = "15265 sats",
            isReceived = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview2() {
    FlorestaTheme {
        TransactionItem(
            title = "ASDN646AS8D46ASD8FF8AS84F68AS4DAS5F3",
            date = "01/02/2024 19:32",
            amount = "15265",
            isReceived = false,
            modifier = Modifier.fillMaxWidth()
        )
    }
}