package com.example.telecomanda.tableiconbutton

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.google.relay.compose.RelayImage
import com.google.relay.compose.RelayText
import com.google.relay.compose.tappable

/**
 * This composable was generated from the UI Package 'table_icon_button'.
 * Generated code; do not edit directly
 */
@Composable
fun TableIconButton(
    modifier: Modifier = Modifier,
    tableNumber: String = "",
    onClick: () -> Unit = {}
) {
    TopLevel(
        onClick = onClick,
        modifier = modifier
    ) {
        Class1(tableNumber = tableNumber)
        Table()
    }
}

@Preview(widthDp = 100, heightDp = 129)
@Composable
private fun TableIconButtonPreview() {
    MaterialTheme {
        TableIconButton(
            onClick = {},
            tableNumber = "1"
        )
    }
}

@Composable
fun Class1(
    tableNumber: String,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = tableNumber,
        fontSize = 50.0.sp,
        color = Color(
            alpha = 255,
            red = 102,
            green = 186,
            blue = 222
        ),
        height = 1.171875.em,
        textAlign = TextAlign.Left,
        fontWeight = FontWeight(600.0.toInt()),
        modifier = modifier
    )
}

@Composable
fun Table(modifier: Modifier = Modifier) {
    RelayImage(
        image = painterResource(R.drawable.table_icon_button_table),
        contentScale = ContentScale.Fit,
        modifier = modifier.requiredWidth(100.0.dp).requiredHeight(100.0.dp)
    )
}

@Composable
fun TopLevel(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        itemSpacing = -30.0,
        clipToParent = false,
        content = content,
        modifier = modifier.tappable(onTap = onClick).width(IntrinsicSize.Min)
    )
}
