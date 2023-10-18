package com.qxlabai.messenger.ui.theme

import androidx.compose.ui.graphics.Color

object ColorUtil {
    private val thumbColors = listOf(
        md_theme_light_secondaryContainer,
        md_theme_dark_inversePrimary,
        md_theme_dark_onErrorContainer
    )

    fun getThumbColor(letter: Char): Color {
        return thumbColors[Character.getNumericValue(letter) % thumbColors.size]
    }
}