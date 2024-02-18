package com.lvsmsmch.lchat.presentation.pages.chat.message_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lvsmsmch.lchat.R
import com.lvsmsmch.lchat.presentation.theme.Colors

@Composable
fun MessageFromChatLoading(
    characterName: String,
) {
    Spacer(modifier = Modifier.height(8.dp))

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxSize(0.8f)
        ) {

            Text(
                text = characterName,
                color = Colors.Chat.SenderCharacterText,
                fontSize = 13.sp,
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .clip(shape = RoundedCornerShape(0.dp, 12.dp, 12.dp, 12.dp))
                    .background(Colors.Chat.MessageCharacterBg)
                    .padding(horizontal = 12.dp, vertical = 14.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.typing),
                    color = Colors.Chat.MessageCharacterText,
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
}