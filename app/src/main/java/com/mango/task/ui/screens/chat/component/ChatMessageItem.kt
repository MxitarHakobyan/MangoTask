package com.mango.task.ui.screens.chat.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mango.task.ui.screens.chat.ChatMessage

@Composable
fun ChatMessageItem(message: ChatMessage) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (message.isSentByUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (message.isSentByUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
            ),
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = message.content,
                    color = if (message.isSentByUser) Color.White else MaterialTheme.colorScheme.onTertiary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = message.timestamp,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (message.isSentByUser) Color.White.copy(alpha = 0.7f)
                    else MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.7f)
                )
            }
        }
    }
}