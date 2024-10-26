package com.github.jvsena42.floresta.presentation.ui.screens.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.jvsena42.floresta.R
import com.github.jvsena42.floresta.presentation.ui.theme.Danger
import com.github.jvsena42.floresta.presentation.ui.theme.FlorestaTheme
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
                modifier = Modifier.padding(end = 8.dp)
            )

            Text(
                title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 32.dp)
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
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                date,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 32.dp)
            )

            val textStatus =
                if (isReceived) stringResource(R.string.received) else stringResource(R.string.send)

            Card(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .width(116.dp)
                    .height(28.dp),
                colors = CardDefaults.cardColors(containerColor = color),
                shape = CircleShape.copy(CornerSize(6.dp))
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        textStatus,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
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
            amount = "15265",
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