package app.lawnchair.lawnicons.ui.components.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import app.lawnchair.lawnicons.R
import app.lawnchair.lawnicons.model.IconInfo
import app.lawnchair.lawnicons.repository.preferenceManager
import app.lawnchair.lawnicons.ui.components.core.Card
import app.lawnchair.lawnicons.ui.theme.LawniconsTheme
import app.lawnchair.lawnicons.ui.util.PreviewLawnicons
import app.lawnchair.lawnicons.ui.util.SampleData
import app.lawnchair.lawnicons.ui.util.toPaddingValues
import app.lawnchair.lawnicons.util.appIcon
import java.net.URL
import my.nanihadesuka.compose.InternalLazyVerticalGridScrollbar
import my.nanihadesuka.compose.ScrollbarSelectionMode
import my.nanihadesuka.compose.ScrollbarSettings

data class IconPreviewGridPadding(
    val topPadding: Dp,
    val bottomPadding: Dp,
    val horizontalPadding: Dp,
) {

    companion object {
        val Defaults = IconPreviewGridPadding(
            topPadding = 10.dp,
            bottomPadding = 80.dp,
            horizontalPadding = 8.dp,
        )

        val ExpandedSize = IconPreviewGridPadding(
            topPadding = 72.dp,
            bottomPadding = 0.dp,
            horizontalPadding = 32.dp,
        )
    }
}

@Composable
@ExperimentalFoundationApi
fun IconPreviewGrid(
    iconInfo: List<IconInfo>,
    onSendResult: (IconInfo) -> Unit,
    modifier: Modifier = Modifier,
    containerModifier: Modifier = Modifier
        .applyGridInsets(),
    contentPadding: IconPreviewGridPadding = IconPreviewGridPadding.Defaults,
    isIconPicker: Boolean = false,
    gridState: LazyGridState = rememberLazyGridState(),
    otherContent: (LazyGridScope.() -> Unit) = {},
) {
    val indexOfFirstItem by remember { derivedStateOf { gridState.firstVisibleItemIndex } }
    val letter = iconInfo[indexOfFirstItem].label[0].uppercase()
    var thumbSelected by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth(),
    ) {


        Column(
            modifier = containerModifier
                .padding(bottom = contentPadding.bottomPadding),
        ) {
            Card {

                Column (
                    modifier = Modifier.padding(20.dp),
                ) {
                    Text(
                        text = stringResource(id = R.string.porter_name),
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    HyperlinkText(
                        text = "Telegram Group",
                        hyperlinks = listOf("https://t.me/MotoCustomization"),
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    HyperlinkText(
                        text = "Buy Me a Coffee",
                        hyperlinks = listOf("https://buymeacoffee.com/kalp96757"),
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    HyperlinkText(
                        text = "UPI Link",
                        hyperlinks = listOf("upi://pay?pa=kalp9675-1@okaxis&amp;pn=Kalp%20Shah"),
                    )
                }

            }
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 80.dp),
                contentPadding = WindowInsets.navigationBars.toPaddingValues(
                    additionalStart = contentPadding.horizontalPadding,
                    additionalTop = contentPadding.topPadding,
                    additionalEnd = contentPadding.horizontalPadding,
                ),
                state = gridState,
            ) {
                otherContent()
                items(
                    items = iconInfo,
                    contentType = { "icon_preview" },
                ) { iconInfo ->
                    val scale by animateFloatAsState(
                        if (thumbSelected && iconInfo.label.first().toString() == letter) {
                            1.1f
                        } else {
                            1f
                        },
                        label = "",
                    )
                    IconPreview(
                        modifier = Modifier
                            .scale(scale),
                        iconInfo = iconInfo,
                        isIconPicker = isIconPicker,
                        onSendResult = onSendResult,
                    )
                }
            }
            ScrollbarLayout(
                gridState,
                { thumbSelected = it },
                letter,
                contentPadding.topPadding,
            )
        }
    }
}

private fun Modifier.applyGridInsets() = this
    .widthIn(max = 640.dp)
    .fillMaxWidth()
    .statusBarsPadding()

@Composable
private fun ScrollbarLayout(
    gridState: LazyGridState,
    onSelectedChange: (Boolean) -> Unit,
    currentLetter: String,
    topPadding: Dp = 0.dp,
) {
    Box(
        contentAlignment = Alignment.CenterEnd,
        modifier = Modifier.padding(top = topPadding),
    ) {
        Spacer(
            Modifier
                .fillMaxHeight()
                .width(8.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .clip(CircleShape),
        )
        InternalLazyVerticalGridScrollbar(
            modifier = Modifier.offset(7.dp),
            state = gridState,
            settings = ScrollbarSettings(
                alwaysShowScrollbar = true,
                thumbUnselectedColor = MaterialTheme.colorScheme.primary,
                thumbSelectedColor = MaterialTheme.colorScheme.primary,
                selectionMode = ScrollbarSelectionMode.Thumb,
            ),
            indicatorContent = { _, isThumbSelected ->
                onSelectedChange(isThumbSelected)
                ScrollbarIndicator(currentLetter, isThumbSelected)
            },
        )
    }
}

@Composable
private fun ScrollbarIndicator(
    label: String,
    isThumbSelected: Boolean,
) {
    AnimatedVisibility(
        visible = isThumbSelected,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Box(
            modifier = Modifier
                .padding(end = 16.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.large,
                ),
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = label,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AppBarListItem(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val prefs = preferenceManager(context)
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (!LocalInspectionMode.current) {
                        Image(
                            bitmap = context.appIcon().asImageBitmap(),
                            contentDescription = stringResource(id = R.string.app_name),
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .combinedClickable(
                                    onClick = {},
                                    onLongClick = {
                                        prefs.showDebugMenu.toggle()
                                    },
                                ),
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        stringResource(id = R.string.port_name),
                    )
                }
        },
    )
}
@Composable
fun HyperlinkText(
    modifier: Modifier = Modifier,
    text: String,
    linkText: List<String> = listOf(text),
    hyperlinks: List<String>,
    linkTextColor: Color = MaterialTheme.colorScheme.primary,
    linkTextFontWeight: FontWeight = FontWeight.Normal,
    linkTextDecoration: TextDecoration = TextDecoration.Underline,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontFamily: FontFamily = FontFamily.Monospace
) {
    val uriHandler = LocalUriHandler.current

    val annotatedString = buildAnnotatedString {
        var lastIndex = 0
        linkText.forEachIndexed { index, link ->
            val startIndex = text.indexOf(link, lastIndex)
            val endIndex = startIndex + link.length

            if (startIndex > lastIndex) append(text.substring(lastIndex, startIndex))

            val linkUrL = LinkAnnotation.Url(
                hyperlinks[index], TextLinkStyles(
                    SpanStyle(
                        color = linkTextColor,
                        fontSize = fontSize,
                        fontWeight = linkTextFontWeight,
                        textDecoration = linkTextDecoration,
                        fontFamily = fontFamily
                    )
                )
            ) {
                val url = (it as LinkAnnotation.Url).url
                uriHandler.openUri(url)
            }
            withLink(linkUrL) { append(link) }
            append(" ")
            lastIndex = endIndex + 1
        }
        if (lastIndex < text.length) {
            append(text.substring(lastIndex))
        }
        addStyle(
            style = SpanStyle(
                fontSize = fontSize, fontFamily = fontFamily
            ), start = 0, end = text.length
        )
    }
    Text(text = annotatedString, modifier = modifier)
}
