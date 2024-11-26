package com.mango.task.ui.screens.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mango.task.R
import com.mango.task.ui.screens.chat.component.ChatMessageItem
import com.mango.task.ui.screens.chat.component.MessageInputBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    nestedNavController: NavController,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    state.chatName,
                    fontWeight = FontWeight.Bold
                )
            }, navigationIcon = {
                IconButton(onClick = { nestedNavController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            })
        },
        bottomBar = {
            MessageInputBar { message ->
                viewModel.handleIntent(ChatIntent.SendMessage(message))
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                state.messages.isEmpty() -> EmptyChatContent(modifier = Modifier.align(Alignment.Center))
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp),
                        reverseLayout = true
                    ) {
                        items(state.messages.reversed()) { message ->
                            ChatMessageItem(message = message)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyChatContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(300.dp),
            painter = painterResource(R.drawable.ic_splash), contentDescription = "No message",
        )
        Text(
            fontWeight = FontWeight.Normal,
            text = stringResource(R.string.chat_list_no_messages_text),
            color = MaterialTheme.colorScheme.onTertiary,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}
