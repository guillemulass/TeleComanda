package com.example.telecomanda.deleteitemcard

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.google.relay.compose.RelayImage
import com.google.relay.compose.RelayText
import com.google.relay.compose.tappable

/**
 * This composable was generated from the UI Package 'delete_item_card'.
 * Generated code; do not edit directly
 */
@Composable
fun DeleteItemCard(
    modifier: Modifier = Modifier,
    itemName: String = "",
    itemPrice: String = "",
    onClickDelete: () -> Unit = {}
) {
    TopLevel(modifier = modifier) {
        Delete(
            onClickDelete = onClickDelete,
            modifier = Modifier.boxAlign(
                alignment = Alignment.Center,
                offset = DpOffset(
                    x = 0.0.dp,
                    y = 53.0.dp
                )
            )
        )
        Nombre(
            itemName = itemName,
            modifier = Modifier.boxAlign(
                alignment = Alignment.Center,
                offset = DpOffset(
                    x = 0.0.dp,
                    y = -70.5.dp
                )
            )
        )
        Precio(
            itemPrice = itemPrice,
            modifier = Modifier.boxAlign(
                alignment = Alignment.Center,
                offset = DpOffset(
                    x = 0.0.dp,
                    y = -17.5.dp
                )
            )
        )
    }
}

@Preview(widthDp = 167, heightDp = 254)
@Composable
private fun DeleteItemCardPreview() {
    MaterialTheme {
        RelayContainer {
            DeleteItemCard(
                onClickDelete = {},
                itemName = "Nombre",
                itemPrice = "Precio",
                modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f)
            )
        }
    }
}

@Composable
fun Delete(
    onClickDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    RelayImage(
        image = painterResource(R.drawable.delete_item_card_delete),
        contentScale = ContentScale.Fit,
        modifier = modifier.tappable(onTap = onClickDelete).requiredWidth(63.0.dp).requiredHeight(63.0.dp)
    )
}

@Composable
fun Nombre(
    itemName: String,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = itemName,
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
        modifier = modifier
    )
}

@Composable
fun Precio(
    itemPrice: String,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = itemPrice,
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
        modifier = modifier
    )
}

@Composable
fun TopLevel(
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
        isStructured = false,
        clipToParent = false,
        radius = 16.0,
        content = content,
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}
