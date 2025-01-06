package com.example.safebyte.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

sealed class IconType {
    data class ResourceIcon(val resourceId: Int) : IconType()
    data class VectorIcon(val imageVector: ImageVector) : IconType()
}

@Composable
fun SBTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    placeholder: String = "",
    icon: IconType = IconType.ResourceIcon(0),
    onTextChange: (String) -> Unit = {},
    onFocusChange: (Boolean) -> Unit = {}
) {
    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = value,
            onValueChange = { onTextChange(it) },
            singleLine = true,
            modifier = Modifier
                .weight(1f)
                .onFocusChanged {
                    onFocusChange(it.isFocused)
                },
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
                innerTextField()
            }
        )

        Spacer(modifier = Modifier.width(8.dp))

        when (icon) {
            is IconType.VectorIcon -> Icon(
                imageVector = icon.imageVector,
                contentDescription = "Input Icon",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )

            is IconType.ResourceIcon -> Icon(
                painter = painterResource(id = icon.resourceId),
                contentDescription = "Input Icon",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview
@Composable
fun SBTextFieldPreview() {
    SBTextField(
        placeholder = "Field",
        icon = IconType.VectorIcon(Icons.Default.Add),
        modifier = Modifier,
        onTextChange = {}
    )
}