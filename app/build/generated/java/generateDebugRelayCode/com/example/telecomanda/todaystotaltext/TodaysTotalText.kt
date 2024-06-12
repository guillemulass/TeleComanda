package com.example.telecomanda.todaystotaltext

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayText

/**
 * This composable was generated from the UI Package 'todays_total_text'.
 * Generated code; do not edit directly
 */
@Composable
fun TodaysTotalText(
    modifier: Modifier = Modifier,
    moneyText: String = ""
) {
    TopLevel(modifier = modifier) {
        Class12467(
            moneyText = moneyText,
            modifier = Modifier.boxAlign(
                alignment = Alignment.Center,
                offset = DpOffset(
                    x = -0.5.dp,
                    y = 19.0.dp
                )
            )
        )
        TotalIngresadoHoy(
            modifier = Modifier.boxAlign(
                alignment = Alignment.Center,
                offset = DpOffset(
                    x = 0.0.dp,
                    y = -19.0.dp
                )
            )
        )
    }
}

@Preview(widthDp = 290, heightDp = 76)
@Composable
private fun TodaysTotalTextPreview() {
    MaterialTheme {
        RelayContainer {
            TodaysTotalText(
                moneyText = "124.67€",
                modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f)
            )
        }
    }
}

@Composable
fun Class12467(
    moneyText: String,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = moneyText,
        fontSize = 32.0.sp,
        color = Color(
            alpha = 255,
            red = 102,
            green = 186,
            blue = 222
        ),
        height = 1.171875.em,
        fontWeight = FontWeight(600.0.toInt()),
        modifier = modifier
    )
}

@Composable
fun TotalIngresadoHoy(modifier: Modifier = Modifier) {
    RelayText(
        content = "Total ingresado hoy ",
        fontSize = 32.0.sp,
        color = Color(
            alpha = 255,
            red = 255,
            green = 255,
            blue = 255
        ),
        height = 1.171875.em,
        fontWeight = FontWeight(600.0.toInt()),
        modifier = modifier
    )
}

@Composable
fun TopLevel(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        isStructured = false,
        clipToParent = false,
        content = content,
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}
