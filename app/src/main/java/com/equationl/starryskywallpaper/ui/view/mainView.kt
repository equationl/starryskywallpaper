package com.equationl.starryskywallpaper.ui.view

import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.equationl.starryskywallpaper.R
import com.equationl.starryskywallpaper.server.StarrySkyWallpaperServer
import com.equationl.starryskywallpaper.utills.Permission

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        MainSetting()

        Button(
            onClick = {
                onClickSetWallPaper(
                    context
                )
            },
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(text = stringResource(id = R.string.main_apply_wallpaper))
        }
    }
}

private fun onClickSetWallPaper(context: Context) {
    Permission.clickSetWallPaper(context)
    val intent = Intent()
    intent.action = WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER
    intent.putExtra(
        WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
        ComponentName(context.packageName, StarrySkyWallpaperServer::class.java.name)
    )
    context.startActivity(intent)
}