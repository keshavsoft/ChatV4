package com.example.compose.jetchat.core.navigation

import androidx.annotation.StringRes
import com.example.compose.jetchat.R

sealed class DrawerDestination(
    val key: String,
    @StringRes val labelRes: Int,
    val navId: Int? = null   // ✅ ADD THIS
) {
        data object ChatWsV1 : DrawerDestination("ChatWsV1", R.string.menu_ChatWsV1)
    data object ChatWsV2 : DrawerDestination("ChatWsV2", R.string.menu_ChatWsV2)

    // 🔥 Add this block
    companion object {
        fun fromKey(key: String): DrawerDestination = when (key) {
            ChatWsV1.key -> ChatWsV1
            ChatWsV2.key -> ChatWsV2

            else -> ChatWsV2
        }
    }
}
