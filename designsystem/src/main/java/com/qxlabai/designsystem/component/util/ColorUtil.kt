package com.qxlabai.designsystem.component.util

import androidx.compose.ui.graphics.Color
import com.qxlabai.designsystem.component.theme.Amber400
import com.qxlabai.designsystem.component.theme.Blue400
import com.qxlabai.designsystem.component.theme.DeepOrange400
import com.qxlabai.designsystem.component.theme.DeepPurple400
import com.qxlabai.designsystem.component.theme.Green400
import com.qxlabai.designsystem.component.theme.Indigo400
import com.qxlabai.designsystem.component.theme.Lime400
import com.qxlabai.designsystem.component.theme.Pink400
import com.qxlabai.designsystem.component.theme.Red400
import com.qxlabai.designsystem.component.theme.Teal400

object ColorUtil {
    private val thumbColors = listOf(
        Red400,
        Pink400,
        DeepPurple400,
        Indigo400,
        Blue400,
        Teal400,
        Green400,
        Lime400,
        Amber400,
        DeepOrange400
    )

    fun getThumbColor(letter: Char): Color {
        return thumbColors[Character.getNumericValue(letter) % thumbColors.size]
    }
}
