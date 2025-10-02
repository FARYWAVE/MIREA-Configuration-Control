package ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.sp

val impact = FontFamily(Font(resource = "fonts/impact.ttf"))

object TextStyles {
    val Logo = TextStyle(fontFamily = FontFamily.Monospace)
    val Normal = TextStyle(fontFamily = impact, fontSize = 20.sp)
}