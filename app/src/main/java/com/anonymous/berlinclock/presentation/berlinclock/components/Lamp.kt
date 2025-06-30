package com.anonymous.berlinclock.presentation.berlinclock.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun Lamp(
    tag: String,
    size: Int = 60
) {
    Column(modifier = Modifier.padding(2.dp)) {
        Box(
            modifier = Modifier
                .testTag(tag)
                .padding(horizontal = 0.dp, vertical = 5.dp)
                .fillMaxWidth()
                .size(size.dp)
                .border(2.dp, Color.DarkGray, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
        )
    }
}