package com.example.telecomanda.mainheader

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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
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

/**
 * This composable was generated from the UI Package 'main_header'.
 * Generated code; do not edit directly
 */
@Composable
fun MainHeader(modifier: Modifier = Modifier) {
    TopLevel(modifier = modifier) {
        MainHeaderSeparatorBot(
            modifier = Modifier.boxAlign(
                alignment = Alignment.Center,
                offset = DpOffset(
                    x = 0.0.dp,
                    y = 28.0.dp
                )
            )
        )
        MainHeaderTeleComanda(
            modifier = Modifier.boxAlign(
                alignment = Alignment.Center,
                offset = DpOffset(
                    x = 0.0.dp,
                    y = -0.5.dp
                )
            )
        )
    }
}

@Preview(widthDp = 430, heightDp = 60)
@Composable
private fun MainHeaderPreview() {
    MaterialTheme {
        RelayContainer {
            MainHeader(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f))
        }
    }
}

@Composable
fun MainHeaderSeparatorBot(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.main_header_main_header_separator_bot),
        modifier = modifier.requiredWidth(430.0.dp).requiredHeight(4.0.dp)
    )
}

@Composable
fun MainHeaderTeleComanda(modifier: Modifier = Modifier) {
    RelayText(
        content = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = Color(
                        alpha = 255,
                        red = 102,
                        green = 186,
                        blue = 222
                    ),
                    fontSize = 40.0.sp
                )
            ) {
                append("Tele")
            }
            withStyle(
                style = SpanStyle(
                    color = Color(
                        alpha = 255,
                        red = 255,
                        green = 255,
                        blue = 255
                    ),
                    fontSize = 40.0.sp
                )
            ) {
                append("Comanda")
            }
        },
        fontSize = 40.0.sp,
        height = 1.171875.em,
        textAlign = TextAlign.Left,
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
