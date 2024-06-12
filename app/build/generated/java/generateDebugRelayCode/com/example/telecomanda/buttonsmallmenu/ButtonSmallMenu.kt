package com.example.telecomanda.buttonsmallmenu

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.telecomanda.R
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayText
import com.google.relay.compose.RelayVector
import com.google.relay.compose.tappable

/**
 * This composable was generated from the UI Package 'button_small_menu'.
 * Generated code; do not edit directly
 */
@Composable
fun ButtonSmallMenu(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick: () -> Unit = {}
) {
    TopLevel(
        onClick = onClick,
        modifier = modifier
    ) {
        Rectangle2(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f))
        Primero(
            text = text,
            modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f)
        )
    }
}

@Preview(widthDp = 125, heightDp = 53)
@Composable
private fun ButtonSmallMenuPreview() {
    MaterialTheme {
        RelayContainer {
            ButtonSmallMenu(
                onClick = {},
                text = "Primero",
                modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f)
            )
        }
    }
}

@Composable
fun Rectangle2(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.button_small_menu_rectangle_2),
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}

@Composable
fun Primero(
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
        maxLines = -1,
        modifier = modifier.padding(
            paddingValues = PaddingValues(
                start = 17.0.dp,
                top = 13.0.dp,
                end = 17.0.dp,
                bottom = 12.0.dp
            )
        ).fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}

@Composable
fun TopLevel(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        isStructured = false,
        clipToParent = false,
        content = content,
        modifier = modifier.tappable(onTap = onClick).fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}
