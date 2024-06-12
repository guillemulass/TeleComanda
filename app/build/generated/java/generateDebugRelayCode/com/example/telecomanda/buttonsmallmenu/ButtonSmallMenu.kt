package com.example.telecomanda.buttonsmallmenu

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
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
    textColor: Color = Color(
        alpha = 0,
        red = 0,
        green = 0,
        blue = 0
    ),
    onClick: () -> Unit = {}
) {
    TopLevel(
        onClick = onClick,
        modifier = modifier
    ) {
        Rectangle2(
            modifier = Modifier.boxAlign(
                alignment = Alignment.Center,
                offset = DpOffset(
                    x = 0.0.dp,
                    y = 0.0.dp
                )
            )
        )
        Primero(
            textColor = textColor,
            text = text,
            modifier = Modifier.boxAlign(
                alignment = Alignment.Center,
                offset = DpOffset(
                    x = 0.0.dp,
                    y = 0.5.dp
                )
            )
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
                textColor = Color(
                    alpha = 255,
                    red = 255,
                    green = 255,
                    blue = 255
                ),
                modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f)
            )
        }
    }
}

@Composable
fun Rectangle2(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.button_small_menu_rectangle_2),
        modifier = modifier.requiredWidth(125.0.dp).requiredHeight(53.0.dp)
    )
}

@Composable
fun Primero(
    textColor: Color,
    text: String,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = text,
        fontSize = 22.0.sp,
        color = textColor,
        height = 1.171875.em,
        textAlign = TextAlign.Left,
        fontWeight = FontWeight(600.0.toInt()),
        modifier = modifier
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
