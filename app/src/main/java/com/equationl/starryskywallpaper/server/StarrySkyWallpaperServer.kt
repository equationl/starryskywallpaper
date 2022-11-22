package com.equationl.starryskywallpaper.server

import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import com.equationl.starryskywallpaper.ui.draw.DrawStarrySky
import kotlinx.coroutines.*


class StarrySkyWallpaperServer : WallpaperService() {
    private var coroutineScope = CoroutineScope(Dispatchers.IO)
    private var drawStarrySky = DrawStarrySky()

    override fun onCreateEngine(): Engine = WallpaperEngine()

    inner class WallpaperEngine : WallpaperService.Engine() {

        override fun onSurfaceCreated(holder: SurfaceHolder?) {
            super.onSurfaceCreated(holder)
        }

        override fun onVisibilityChanged(visible: Boolean) {
            if (visible) {
                continueDraw()
            } else {
                stopDraw()
            }
        }

        override fun onDestroy() {
            super.onDestroy()
            stopDraw()
        }

        private fun continueDraw() {
            coroutineScope.launch {
                drawStarrySky.startDraw(this@StarrySkyWallpaperServer, surfaceHolder)
            }
        }

        private fun stopDraw() {
            drawStarrySky.stopDraw()
            coroutineScope.coroutineContext.cancelChildren()
        }
    }
}