package com.example.notes.utils.media

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.notes.R

@Composable
fun ScaffoldBottomAppBar() {
    BottomAppBar(
        containerColor = Color(0xFF2A2A2A)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(onClick = {  },
                modifier = Modifier.weight(1f),
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color.White
                )
            ) {
                Icon(painter = painterResource(id = R.drawable.home_02),
                    contentDescription = "Заметки",
                    modifier = Modifier.size(32.dp))
            }

            IconButton(onClick = {  },
                modifier = Modifier.weight(1f),
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color.White
                )
            ) {
                Icon(painter = painterResource(id = R.drawable.favourite),
                    contentDescription = "Избранное",
                    modifier = Modifier.size(32.dp))
            }

            IconButton(onClick = {  },
                modifier = Modifier.weight(1f),
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color.White
                )
            ) {
                Icon(painter = painterResource(id = R.drawable.archive),
                    contentDescription = "Архив",
                    modifier = Modifier.size(32.dp))
            }

            IconButton(onClick = {  },
                modifier = Modifier.weight(1f),
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color.White
                )
            ) {
                Icon(painter = painterResource(id = R.drawable.settings),
                    contentDescription = "Настройки",
                    modifier = Modifier.size(32.dp))
            }
        }
    }
}