@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.compose.jetchat.feature.chatws.v2

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.jetchat.FunctionalityNotAvailablePopup
import com.example.compose.jetchat.R
import com.example.compose.jetchat.components.JetchatAppBar
import com.example.compose.jetchat.conversation.UserInput
import com.example.compose.jetchat.feature.chatws.common.*
import com.example.compose.jetchat.feature.chatws.forMessages.Messages
import com.example.compose.jetchat.feature.chatws.forMessages.WsMessageType
import com.example.compose.jetchat.theme.JetchatTheme
import kotlinx.coroutines.launch
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun ChatWsV2Content(
    uiState: ChatWsUiState,
    viewModel: ChatViewModel,
    modifier: Modifier = Modifier,
    onNavIconPressed: () -> Unit = { }
) {
    val authorMe = stringResource(R.string.author_me)
    val timeNow = stringResource(R.string.now)

    val scrollState = rememberLazyListState()
    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topBarState)
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            ChannelNameBar(
                channelName = uiState.channelName,
                channelMembers = uiState.channelMembers,
                onNavIconPressed = onNavIconPressed,
                scrollBehavior = scrollBehavior
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            Messages(
                modifier = Modifier.weight(1f),
                messages = uiState.messages,
                me = authorMe,
                scrollState = scrollState,
                onAuthorClick = {}
            )

            UserInput(
                onMessageSent = { text ->
                    viewModel.send(text)
                    uiState.addMessage(ChatMessage(authorMe, text, timeNow))
                },
                resetScroll = {
                    scope.launch { scrollState.scrollToItem(0) }
                }
            )
        }
    }
}

@Composable
fun ChatWsV2Screen(onNavIconPressed: () -> Unit) {

    val viewModel: ChatViewModel = viewModel()
    val timeNow = stringResource(R.string.now)

    val uiState = remember {
        ChatWsUiState("#chat-ws-v2", 42, emptyList())
    }

    LaunchedEffect(Unit) {
        viewModel.messages.collect { raw ->

            val isJson = raw.trim().startsWith("{")

            if (!isJson) {
                uiState.addMessage(
                    ChatMessage(
                        author = "Server",
                        content = raw,
                        timestamp = timeNow,
                        messageType = WsMessageType.SYSTEM
                    )
                )
            } else {
                val json = runCatching { JSONObject(raw) }.getOrNull()

                val type = when (json?.optString("Type")) {
                    "IsStudent" -> WsMessageType.IS_STUDENT
                    "Phone" -> WsMessageType.PHONE
                    else -> WsMessageType.UNKNOWN
                }

                val text = when (type) {
                    WsMessageType.IS_STUDENT ->
                        "Student Connected\n${json?.optString("webSocketId")}"
                    WsMessageType.PHONE ->
                        json?.optString("number") ?: raw
                    else -> raw
                }

                uiState.addMessage(
                    ChatMessage(
                        author = "Server",
                        content = text,
                        timestamp = timeNow,
                        messageType = type
                    )
                )
            }
        }
    }

    ChatWsV2Content(
        uiState = uiState,
        viewModel = viewModel,
        onNavIconPressed = onNavIconPressed
    )
}

@Composable
fun ChannelNameBar(
    channelName: String,
    channelMembers: Int,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onNavIconPressed: () -> Unit = { }
) {
    var popup by remember { mutableStateOf(false) }
    if (popup) FunctionalityNotAvailablePopup { popup = false }

    JetchatAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        onNavIconPressed = onNavIconPressed,
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(channelName)
                Text(stringResource(R.string.members, channelMembers))
            }
        },
        actions = {
            Icon(Icons.Outlined.Search, null,
                Modifier.clickable { popup = true }.padding(16.dp))
            Icon(Icons.Outlined.Info, null,
                Modifier.clickable { popup = true }.padding(16.dp))
        }
    )
}

@Preview
@Composable
fun PreviewV2() {
    JetchatTheme {
        ChannelNameBar("Chat Ws V2", 42)
    }
}
