package com.example.telecomanda.buttonorderitem

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayText
import com.google.relay.compose.tappable

/**
 * This composable was generated from the UI Package 'button_order_item'.
 * Generated code; do not edit directly
 */
@Composable
fun ButtonOrderItem(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick: () -> Unit = {}
) {
    TopLevel(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(text = text)
    }
}

@Preview(widthDp = 230, heightDp = 42)
@Composable
private fun ButtonOrderItemPreview() {
    MaterialTheme {
        RelayContainer {
            ButtonOrderItem(
                onClick = {},
                text = "Plato | 3€ ",
                modifier = Modifier.rowWeight(1.0f)
            )
        }
    }
}

@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = text,
        fontSize = 24.0.sp,
        color = Color(
            alpha = 255,
            red = 255,
            green = 255,
            blue = 255
        ),
        height = 1.171875.em,
        textAlign = TextAlign.Left,
        fontWeight = FontWeight(600.0.toInt()),
        modifier = modifier.wrapContentHeight(
            align = Alignment.CenterVertically,
            unbounded = true
        )
    )
}

@Composable
fun TopLevel(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = Color(
            alpha = 255,
            red = 102,
            green = 186,
            blue = 222
        ),
        padding = PaddingValues(
            start = 13.0.dp,
            top = 7.0.dp,
            end = 13.0.dp,
            bottom = 7.0.dp
        ),
        itemSpacing = 10.0,
        clipToParent = false,
        radius = 16.0,
        content = content,
        modifier = modifier.tappable(onTap = onClick).fillMaxWidth(1.0f)
    )
}
