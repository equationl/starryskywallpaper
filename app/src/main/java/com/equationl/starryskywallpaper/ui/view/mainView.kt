package com.equationl.starryskywallpaper.ui.view

import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.equationl.starryskywallpaper.server.StarrySkyWallpaperServer

@Composable
fun MainScreen() {
    val context = LocalContext.current

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            onClickSetWallPaper(context)
        }) {
            Text(text = "设置")
        }
    }
}

private fun onClickSetWallPaper(context: Context) {
    val intent = Intent()
    intent.action = WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER
    intent.putExtra(
        WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
        ComponentName(context.packageName, StarrySkyWallpaperServer::class.java.name)
    )
    context.startActivity(intent)
}