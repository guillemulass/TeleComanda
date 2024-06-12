package com.example.telecomanda.smallbutton

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
 * This composable was generated from the UI Package 'small_button'.
 * Generated code; do not edit directly
 */
@Composable
fun SmallButton(
    modifier: Modifier = Modifier,
    text: String = "",
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
        Plato(
            text = text,
            modifier = Modifier.boxAlign(
                alignment = Alignment.Center,
                offset = DpOffset(
                    x = 0.5.dp,
                    y = 0.1785717010498047.dp
                )
            )
        )
    }
}

@Preview(widthDp = 173, heightDp = 42)
@Composable
private fun SmallButtonPreview() {
    MaterialTheme {
        RelayContainer {
            SmallButton(
                onClick = {},
                text = "Plato",
                modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f)
            )
        }
    }
}

@Composable
fun Rectangle2(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.small_button_rectangle_2),
        modifier = modifier.requiredWidth(173.0.dp).requiredHeight(41.64285659790039.dp)
    )
}

@Composable
fun Plato(
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
        fontWeight = FontWeight(600.0.toInt()),
        maxLines = -1,
        modifier = modifier.requiredWidth(118.0.dp).wrapContentHeight(
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
        isStructured = false,
        clipToParent = false,
        content = content,
        modifier = modifier.tappable(onTap = onClick).fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}
