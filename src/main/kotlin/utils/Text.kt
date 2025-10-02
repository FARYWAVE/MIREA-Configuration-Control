package utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import ui.theme.Colors

val argTypes = mapOf (
    '$' to Colors.EnvVar
)

fun highlightCommand(input: String): AnnotatedString {
    val parts = input.split(" ")

    return buildAnnotatedString {
        if (parts.isNotEmpty()) {
            withStyle(SpanStyle(color = Colors.Primary)) {
                append(parts[0])
            }
        }
        for (i in 1 until parts.size) {
            append(" ")
            val part = parts[i]
            if (part.isEmpty()) continue
            withStyle(SpanStyle(argTypes[part[0]]?: Colors.Text)) {
                append(parts[i])
            }
        }
    }
}

fun highlightHistoryCommand(input: String): AnnotatedString {
    val parts = input.replace('>', ' ').split(' ')
    return buildAnnotatedString {
        withStyle(SpanStyle(color = Colors.Text)) {
            append(parts[0] + ">")
        }
        withStyle(SpanStyle(color = Colors.Primary)) {
            append(parts[1])
        }
        for (i in 2 until parts.size) {
            append(" ")
            val part = parts[i]
            if (part.isEmpty()) continue
            withStyle(SpanStyle(argTypes[part[0]]?: Colors.Text)) {
                append(parts[i])
            }
        }
    }
}