package com.example.doyourself.ui.pages.createProcedure.components


import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.doyourself.ui.theme.DoYourSelfTheme
import com.example.doyourself.ui.previews.ButtonTextProvider

@Preview(showBackground = true)
@Composable
fun TextButtonTemplatePreview(
    @PreviewParameter(ButtonTextProvider::class) text: String
) {
    DoYourSelfTheme {
        TextButtonTemplate(text = text, onClick = {})
    }
}

@Composable
fun TextButtonTemplate(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,      // Uses primary color from the theme
            contentColor = MaterialTheme.colorScheme.onPrimary,        // Uses onPrimary color for text
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            disabledContentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
            Text(
                text = text.uppercase(),
                style = MaterialTheme.typography.labelLarge   // Uses the theme typography
            )
        }
    }

