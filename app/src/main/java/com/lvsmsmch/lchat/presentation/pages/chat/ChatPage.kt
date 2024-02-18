package com.lvsmsmch.lchat.presentation.pages.chat

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lvsmsmch.lchat.R
import com.lvsmsmch.lchat.presentation.theme.Colors
import kotlinx.coroutines.launch
import com.lvsmsmch.lchat.presentation.pages.chat.message_components.MessageFromChat
import com.lvsmsmch.lchat.presentation.pages.chat.message_components.MessageFromChatFailed
import com.lvsmsmch.lchat.presentation.pages.chat.message_components.MessageFromChatLoading
import com.lvsmsmch.lchat.presentation.pages.chat.message_components.MessageFromChatTyping
import com.lvsmsmch.lchat.presentation.pages.chat.message_components.MessageFromUser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatPage(
    state: ChatState,
    onSendMessage: (text: String) -> Unit,
    onSelected: (ChatItem) -> Unit,
    onUnselected: (ChatItem) -> Unit,
    onSelectAll: () -> Unit,
    onUnselectAll: () -> Unit,
    onDelete: () -> Unit,
    onReload: (ChatItem) -> Unit,
) {
    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.Chat.Bg)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                SelectingBar(
                    isSelecting = state.isSelecting,
                    selectedCount = state.selectedCount,
                    onUnselectAll = onUnselectAll,
                    onSelectAll = onSelectAll,
                    onDelete = onDelete,
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                )


                MessageList(
                    chatItems = state.chatItems,
                    characterName = state.characterName,
                    onClick = { chatItem ->
                        if (state.isSelecting) {
                            if (chatItem.isSelected) {
                                onUnselected(chatItem)
                            } else {
                                onSelected(chatItem)
                            }
                            return@MessageList
                        }

                        if (chatItem is ChatItem.CharacterFailed) {
                            onReload(chatItem)
                        }
                    },
                    onLongClick = { chatItem ->
                        if (state.isSelecting.not()) {
                            onSelected(chatItem)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(bottom = 6.dp)
                )

                InputBox(
                    onMessageSent = { text -> onSendMessage(text) },
                    sendButtonEnabled = state.sendButtonEnabled,
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                )
            }
        }
    }

    BackHandler(enabled = state.isSelecting) {
        onUnselectAll()
    }
}


@Composable
private fun SelectingBar(
    isSelecting: Boolean,
    selectedCount: Int,
    onUnselectAll: () -> Unit,
    onSelectAll: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isSelecting.not()) {
        return
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(color = Colors.Chat.BgSelectingBar)
            .padding(vertical = 18.dp)
    ) {

        Button(
            modifier = Modifier
                .padding(horizontal = 12.dp),
            onClick = {
                onUnselectAll()
            }
        ) {
            Text(text = "Cancel")
        }

        Text(text = "$selectedCount selected")

        Spacer(
            modifier = Modifier
                .weight(1f)
        )

        Button(
            modifier = Modifier
                .padding(horizontal = 12.dp),
            onClick = {
                onDelete()
                onUnselectAll()
            }
        ) {
            Text(text = "Delete")
        }
    }
}

@Composable
private fun MessageList(
    chatItems: List<ChatItem>,
    characterName: String,
    onClick: (ChatItem) -> Unit,
    onLongClick: (ChatItem) -> Unit,
    modifier: Modifier,
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(reverseLayout = true, state = listState, modifier = modifier) {
        items(
            items = chatItems,
            key = { chatItem -> chatItem.id }
        ) { chatItem ->
            when (chatItem) {
                is ChatItem.UserMessage -> MessageFromUser(
                    chatItem = chatItem,
                    isSelected = chatItem.isSelected,
                    onClick = { onClick(chatItem) },
                    onLongClick = { onLongClick(chatItem) },
                )

                is ChatItem.CharacterMessage -> MessageFromChat(
                    characterName = characterName,
                    chatItem = chatItem,
                    isSelected = chatItem.isSelected,
                    onClick = { onClick(chatItem) },
                    onLongClick = { onLongClick(chatItem) },
                )

                is ChatItem.CharacterTyping -> MessageFromChatTyping(
                    characterName = characterName,
                    chatItem = chatItem
                )

                is ChatItem.CharacterLoading -> MessageFromChatLoading(
                    characterName = characterName
                )

                is ChatItem.CharacterFailed -> MessageFromChatFailed(
                    characterName = characterName,
                    isSelected = chatItem.isSelected,
                    onClick = { onClick(chatItem) },
                    onLongClick = { onLongClick(chatItem) },
                )
            }
        }
    }

    LaunchedEffect(
        key1 = chatItems.size,                       // scroll when new message arrived
        key2 = chatItems.lastOrNull()?.id ?: 0,      // scroll when last message id was updated
    ) {
        coroutineScope.launch {
            listState.scrollToItem(index = 0)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InputBox(
    onMessageSent: (String) -> Unit,
    sendButtonEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {

        Spacer(modifier = Modifier.height(6.dp))

        Divider(
            color = Colors.Chat.InputBoxSeparator,
            thickness = 1.dp,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 12.dp, vertical = 16.dp)
            ) {

                Spacer(modifier = Modifier.width(4.dp))

                var text by remember { mutableStateOf("") }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()
                        .clip(shape = RoundedCornerShape(18.dp))
                        .background(Colors.Chat.InputBoxBg)
                ) {
                    val maxChar = 300

                    TextField(
                        value = text,
                        onValueChange = { if (it.length <= maxChar) text = it },
                        maxLines = 2,
                        textStyle = LocalTextStyle.current.copy(
                            color = Colors.Chat.InputBoxText
                        ),
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.enter_text_here),
                                color = Colors.Chat.InputBoxHint
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Colors.Transparent,
                            cursorColor = Colors.Chat.InputBoxText, // Set cursor color
                            focusedIndicatorColor = Color.Transparent, // Remove focus indicator
                            unfocusedIndicatorColor = Color.Transparent // Remove unfocused indicator
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(Colors.Transparent)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    enabled = sendButtonEnabled,
                    onClick = {
                        onMessageSent(text)
                        text = ""
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Colors.Chat.BtnSendBg,
                        contentColor = Colors.Chat.BtnSendText,
                        disabledContainerColor = Colors.Chat.BtnSendBgDisabled,
                        disabledContentColor = Colors.Chat.BtnSendText,
                    ),
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(50.dp)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.send),
                        fontSize = 13.sp,
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                            .background(Colors.Transparent)
                    )
                }
            }
        }
    }
}