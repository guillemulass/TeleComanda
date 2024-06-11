package com.example.telecomanda.header

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.google.relay.compose.tappable

/**
 * This composable was generated from the UI Package 'header'.
 * Generated code; do not edit directly
 */
@Composable
fun Header(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    TopLevel(
        onClick = onClick,
        modifier = modifier
    ) {
        SeparatorBot(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f))
        TeleComandaa(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f))
        ArrowBack(
            modifier = Modifier.boxAlign(
                alignment = Alignment.Center,
                offset = DpOffset(
                    x = -174.0.dp,
                    y = -1.8891983032226562.dp
                )
            )
        )
    }
}

@Preview(widthDp = 430, heightDp = 60)
@Composable
private fun HeaderPreview() {
    MaterialTheme {
        RelayContainer {
            Header(
                onClick = {},
                modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f)
            )
        }
    }
}

@Composable
fun SeparatorBot(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.header_separator_bot),
        modifier = modifier.padding(
            paddingValues = PaddingValues(
                start = 0.0.dp,
                top = 56.0.dp,
                end = 0.0.dp,
                bottom = 0.0.dp
            )
        ).fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}

@Composable
fun TeleComandaa(modifier: Modifier = Modifier) {
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
                append("Comandaa")
            }
        },
        fontSize = 40.0.sp,
        height = 1.171875.em,
        textAlign = TextAlign.Left,
        fontWeight = FontWeight(600.0.toInt()),
        modifier = modifier.padding(
            paddingValues = PaddingValues(
                start = 91.0.dp,
                top = 4.0.dp,
                end = 69.0.dp,
                bottom = 9.0.dp
            )
        ).fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}

@Composable
fun ArrowBack(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.header_arrow_back),
        modifier = modifier.requiredWidth(32.22184753417969.dp).requiredHeight(40.0.dp)
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
