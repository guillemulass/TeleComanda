package com.example.telecomanda.botonbig24sp

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerArrangement
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayText
import com.google.relay.compose.tappable

/**
 * This composable was generated from the UI Package 'boton_big24sp'.
 * Generated code; do not edit directly
 */
@Composable
fun BotonBig24sp(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick: () -> Unit = {}
) {
    TopLevel(
        onClick = onClick,
        modifier = modifier
    ) {
        BotonBig24spText(text = text)
    }
}

@Preview(widthDp = 266, heightDp = 52)
@Composable
private fun BotonBig24spPreview() {
    MaterialTheme {
        RelayContainer {
            BotonBig24sp(
                onClick = {},
                text = "Empleado",
                modifier = Modifier.rowWeight(1.0f)
            )
        }
    }
}

@Composable
fun BotonBig24spText(
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
        fontWeight = FontWeight(700.0.toInt()),
        maxLines = -1,
        modifier = modifier.requiredWidth(247.0.dp)
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
        arrangement = RelayContainerArrangement.Row,
        padding = PaddingValues(
            start = 79.0.dp,
            top = 12.0.dp,
            end = 79.0.dp,
            bottom = 12.0.dp
        ),
        itemSpacing = 10.0,
        radius = 16.0,
        content = content,
        modifier = modifier.tappable(onTap = onClick).height(IntrinsicSize.Min).fillMaxWidth(1.0f)
    )
}
