package com.example.telecomanda.menuitemshow

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.telecomanda.R
import com.google.relay.compose.EmptyPainter
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerArrangement
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayImage
import com.google.relay.compose.RelayText

/**
 * This composable was generated from the UI Package 'menu_item_show'.
 * Generated code; do not edit directly
 */
@Composable
fun MenuItemShow(
    modifier: Modifier = Modifier,
    itemImg: Painter = EmptyPainter(),
    itemNameText: String = "",
    itemPriceText: String = "",
    itemIngredientsText: String = ""
) {
    TopLevel(modifier = modifier) {
        Frame7 {
            PapaConCarne(itemNameText = itemNameText)
            Class35(itemPriceText = itemPriceText)
            Ingredientes(itemIngredientsText = itemIngredientsText)
        }
        ItemImg(itemImg = itemImg)
    }
}

@Preview(widthDp = 305, heightDp = 156)
@Composable
private fun MenuItemShowPreview() {
    MaterialTheme {
        RelayContainer {
            MenuItemShow(
                itemImg = painterResource(R.drawable.menu_item_show_item_img),
                itemNameText = "Papa con Carne",
                itemPriceText = "3.5€",
                itemIngredientsText = "Ingredientes",
                modifier = Modifier.rowWeight(1.0f)
            )
        }
    }
}

@Composable
fun PapaConCarne(
    itemNameText: String,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = itemNameText,
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
        modifier = modifier.requiredWidth(142.0.dp).wrapContentHeight(
            align = Alignment.CenterVertically,
            unbounded = true
        )
    )
}

@Composable
fun Class35(
    itemPriceText: String,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = itemPriceText,
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
        modifier = modifier.requiredWidth(142.0.dp).requiredHeight(28.0.dp).wrapContentHeight(
            align = Alignment.CenterVertically,
            unbounded = true
        )
    )
}

@Composable
fun Ingredientes(
    itemIngredientsText: String,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = itemIngredientsText,
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
        modifier = modifier.requiredWidth(135.0.dp).wrapContentHeight(
            align = Alignment.CenterVertically,
            unbounded = true
        )
    )
}

@Composable
fun Frame7(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        clipToParent = false,
        content = content,
        modifier = modifier.width(IntrinsicSize.Min)
    )
}

@Composable
fun ItemImg(
    itemImg: Painter,
    modifier: Modifier = Modifier
) {
    RelayImage(
        image = itemImg,
        radius = 16.0,
        contentScale = ContentScale.Crop,
        modifier = modifier.requiredWidth(155.0.dp).requiredHeight(156.0.dp)
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
        arrangement = RelayContainerArrangement.Row,
        padding = PaddingValues(
            start = 8.0.dp,
            top = 0.0.dp,
            end = 8.0.dp,
            bottom = 0.0.dp
        ),
        itemSpacing = 8.0,
        radius = 16.0,
        content = content,
        modifier = modifier.height(IntrinsicSize.Min).fillMaxWidth(1.0f)
    )
}
