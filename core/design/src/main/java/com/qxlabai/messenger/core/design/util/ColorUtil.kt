package com.qxlabai.messenger.core.designsystem.util

import androidx.compose.ui.graphics.Color
import com.qxlabai.messenger.core.designsystem.theme.Amber400
import com.qxlabai.messenger.core.designsystem.theme.Blue400
import com.qxlabai.messenger.core.designsystem.theme.DeepOrange400
import com.qxlabai.messenger.core.designsystem.theme.DeepPurple400
import com.qxlabai.messenger.core.designsystem.theme.Green400
import com.qxlabai.messenger.core.designsystem.theme.Indigo400
import com.qxlabai.messenger.core.designsystem.theme.Lime400
import com.qxlabai.messenger.core.designsystem.theme.Pink400
import com.qxlabai.messenger.core.designsystem.theme.Red400
import com.qxlabai.messenger.core.designsystem.theme.Teal400

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
