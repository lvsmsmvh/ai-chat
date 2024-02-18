package com.lvsmsmch.lchat.presentation.pages.chat.message_components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lvsmsmch.lchat.R
import com.lvsmsmch.lchat.presentation.pages.chat.ChatItem
import com.lvsmsmch.lchat.presentation.theme.Colors

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageFromUser(
    chatItem: ChatItem.UserMessage,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.CenterEnd,
        modifier = Modifier
            .fillMaxSize()
            .combinedClickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = onClick,
                onLongClick = onLongClick,
            )
            .background(
                color = if (isSelected) Colors.Chat.BgForSelectedMessage else Colors.Transparent
            )
    ) {

        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .padding(top = 4.dp)
                .padding(bottom = 12.dp)
                .fillMaxSize(0.8f)
        ) {

            Text(
                text = stringResource(id = R.string.you),
                color = Colors.Chat.SenderUserText,
                fontSize = 13.sp,
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .padding(top = 4.dp)
                    .padding(horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .clip(shape = RoundedCornerShape(12.dp, 0.dp, 12.dp, 12.dp))
                    .background(
                        color = if (isSelected) Colors.Chat.MessageUserBgSelected else Colors.Chat.MessageUserBg
                    )
                    .padding(horizontal = 12.dp, vertical = 12.dp)
            ) {
                Text(
                    text = chatItem.messageText,
                    color = Colors.Chat.MessageUserText,
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .defaultMinSize(minWidth = 10.dp)
                )
            }
        }
    }
}