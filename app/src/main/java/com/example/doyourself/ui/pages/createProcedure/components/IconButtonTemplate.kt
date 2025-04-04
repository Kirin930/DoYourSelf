package com.example.doyourself.ui.pages.createProcedure.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.doyourself.ui.previews.ButtonTextProvider
import com.example.doyourself.ui.theme.DoYourSelfTheme

@Preview(showBackground = true, name = "IconButton Dark Theme")
@Composable
fun IconButtonTemplateDarkPreview(
    @PreviewParameter(ButtonTextProvider::class) text: String
) {
    DoYourSelfTheme(darkTheme = true) {
        IconButtonTemplate(
            text = text,
            icon = Icons.Filled.Favorite,
            onClick = {}
        )
    }
}

@Composable
fun IconButtonTemplate(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            disabledContentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,  // Add a description for accessibility if needed
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = text.uppercase(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
