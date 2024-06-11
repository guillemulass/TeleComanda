package com.example.telecomanda.tablenumbertext

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayText

/**
 * This composable was generated from the UI Package 'table_number_text'.
 * Generated code; do not edit directly
 */
@Composable
fun TableNumberText(
    modifier: Modifier = Modifier,
    tableQuantityText: String = ""
) {
    TopLevel(modifier = modifier) {
        NumeroDeMesas()
        Class27(tableQuantityText = tableQuantityText)
    }
}

@Preview(widthDp = 262, heightDp = 84)
@Composable
private fun TableNumberTextPreview() {
    MaterialTheme {
        TableNumberText(tableQuantityText = "27")
    }
}

@Composable
fun NumeroDeMesas(modifier: Modifier = Modifier) {
    RelayText(
        content = "Numero de Mesas",
        fontSize = 32.0.sp,
        color = Color(
            alpha = 255,
            red = 255,
            green = 255,
            blue = 255
        ),
        height = 1.171875.em,
        fontWeight = FontWeight(600.0.toInt()),
        modifier = modifier.wrapContentHeight(
            align = Alignment.CenterVertically,
            unbounded = true
        )
    )
}

@Composable
fun Class27(
    tableQuantityText: String,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = tableQuantityText,
        fontSize = 32.0.sp,
        color = Color(
            alpha = 255,
            red = 102,
            green = 186,
            blue = 222
        ),
        height = 1.171875.em,
        fontWeight = FontWeight(600.0.toInt()),
        modifier = modifier.wrapContentHeight(
            align = Alignment.CenterVertically,
            unbounded = true
        )
    )
}

@Composable
fun TopLevel(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        itemSpacing = 8.0,
        clipToParent = false,
        content = content,
        modifier = modifier.width(IntrinsicSize.Min)
    )
}
