package com.qxlabai.messenger.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.qxlabai.messenger.models.Message

@Composable
fun SingleMassage(message: Message, index: Int) {
    val isOwnMessage = index % 2 == 0
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        contentAlignment = if (isOwnMessage) Alignment.CenterEnd else Alignment.CenterStart

    ) {
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxWidth(0.80F)
                .drawBehind {
                    val cornerRadius = 10.dp.toPx()
                    val triangleHeight = 10.dp.toPx()
                    val triangleWidth = 15.dp.toPx()
                    val trianglePath = Path().apply {
                        if (isOwnMessage) {
                            moveTo(size.width, size.height - cornerRadius)
                            lineTo(size.width, size.height + triangleHeight)
                            lineTo(size.width - triangleWidth, size.height - cornerRadius)
                            close()
                        } else {
                            moveTo(0f, size.height - cornerRadius)
                            lineTo(0f, size.height + triangleHeight)
                            lineTo(triangleWidth, size.height - cornerRadius)
                            close()
                        }
                    }
                    drawPath(
                        path = trianglePath,
                        color =  Color.DarkGray
                    )
                }
                .background(
                    color =  Color.DarkGray,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(8.dp)
        ) {

            Text(
                text = message.message,
                color = Color.White
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}