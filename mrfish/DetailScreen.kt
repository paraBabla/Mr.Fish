package com.example.mrfish

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DetailScreen(title: String) {
    Text(
        text = "Вы выбрали: $title",
        modifier = Modifier.fillMaxSize()
    )
}

@Preview
@Composable
fun DetailPreview() {
    DetailScreen("Тестовая точка")
}