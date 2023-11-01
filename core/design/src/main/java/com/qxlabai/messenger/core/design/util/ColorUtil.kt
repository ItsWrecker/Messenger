package com.qxlabai.messenger.core.design.util

import androidx.compose.ui.graphics.Color
import com.qxlabai.messenger.core.design.theme.Amber400
import com.qxlabai.messenger.core.design.theme.Blue400
import com.qxlabai.messenger.core.design.theme.DeepOrange400
import com.qxlabai.messenger.core.design.theme.DeepPurple400
import com.qxlabai.messenger.core.design.theme.Green400
import com.qxlabai.messenger.core.design.theme.Indigo400
import com.qxlabai.messenger.core.design.theme.Lime400
import com.qxlabai.messenger.core.design.theme.Pink400
import com.qxlabai.messenger.core.design.theme.Red400
import com.qxlabai.messenger.core.design.theme.Teal400

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
