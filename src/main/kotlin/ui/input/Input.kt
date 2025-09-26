import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ui.input.PathItem
import ui.responses.Text
import ui.theme.Colors
import ui.theme.TextStyles

@Composable
fun Input(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(Colors.Tertiary)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        androidx.compose.material.Text(":", color = Colors.Text, style = TextStyles.Normal)
        LazyRow(Modifier.wrapContentSize()) {
            items(MainViewModel.currentDirectory.value) { directory ->
                PathItem(directory)
            }
        }
        androidx.compose.material.Text(">", color = Colors.Text, style = TextStyles.Normal)
        val scrollState = rememberScrollState()
        BasicTextField(
            value = MainViewModel.inputText.value,
            onValueChange = { MainViewModel.inputText.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Colors.Transparent)
                .horizontalScroll(scrollState),
            singleLine = true,
            textStyle = TextStyles.Normal.copy(color = Colors.Primary),
            cursorBrush = SolidColor(Colors.Primary),
            decorationBox = { innerTextField ->
                innerTextField()
            }
        )
    }
}
