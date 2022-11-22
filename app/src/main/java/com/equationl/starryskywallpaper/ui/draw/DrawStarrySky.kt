package com.equationl.starryskywallpaper.ui.draw

import android.content.Context
import android.graphics.*
import android.view.SurfaceHolder
import androidx.compose.ui.geometry.Offset
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import com.alorma.compose.settings.storage.datastore.composeSettingsDataStore
import com.equationl.starryskywallpaper.utills.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.math.*
import kotlin.random.Random

class DrawStarrySky {
    private var isRunning = false

    fun stopDraw() {
        isRunning = false
    }

    suspend fun startDraw(context: Context, holder: SurfaceHolder) {

        isRunning = true

        // 初始化参数
        val isUsingRandom = context.composeSettingsDataStore.data.map { it[booleanPreferencesKey(isUsingRandomKey)] ?: false }.first()
        val starNum = context.composeSettingsDataStore.data.map { it[floatPreferencesKey(starNumKey)] ?: 20f }.first()
        val meteorVelocity = context.composeSettingsDataStore.data.map { it[floatPreferencesKey(meteorVelocityKey)] ?: 10f}.first()
        val meteorLength = context.composeSettingsDataStore.data.map { it[floatPreferencesKey(meteorLengthKey)] ?: 500f}.first()
        val meteorStrokeWidth = context.composeSettingsDataStore.data.map { it[floatPreferencesKey(meteorStrokeWidthKey)] ?: 1f }.first()
        val isUsingMeteorRandomAngle = context.composeSettingsDataStore.data.map { it[booleanPreferencesKey(isUsingMeteorRandomAngleKey)] ?: false }.first()
        val meteorAngle = context.composeSettingsDataStore.data.map { it[floatPreferencesKey(meteorAngleKey)] ?: 45f }.first()
        val isUsingMeteorRandomScaleTime = context.composeSettingsDataStore.data.map { it[booleanPreferencesKey(isUsingMeteorRandomScaleTimeKey)] ?: false }.first()
        val meteorScaleTime = context.composeSettingsDataStore.data.map { it[floatPreferencesKey(meteorScaleTimeKey)] ?: 500f }.first()
        val meteorRunningTime = context.composeSettingsDataStore.data.map { it[floatPreferencesKey(meteorRunningTimeKey)] ?: 300f }.first()

        val random = Random(if (isUsingRandom) System.currentTimeMillis() else 1L)
        val paint = Paint()
        var canvasWidth = 0
        var canvasHeight = 0

        getCanvas(holder) { canvas ->
            canvasWidth = canvas.width
            canvasHeight = canvas.height
        }

        // 背景缓存
        val bitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888)
        // 绘制静态背景
        drawFixedContent(Canvas(bitmap), random, starNum = starNum.toInt())

        while (isRunning) {

            // 绘制动态流星
            val safeDistanceStandard = canvasWidth / 10
            val safeDistanceVertical = canvasHeight / 10
            val startX = random.nextInt(safeDistanceStandard, canvasWidth - safeDistanceStandard)
            val startY = random.nextInt(safeDistanceVertical, canvasHeight - safeDistanceVertical)
            val meteorRadian: Double = if (isUsingMeteorRandomAngle) {
                random.nextInt(0, 361) * PI / 180
            } else {
                meteorAngle * PI / 180
            }

            for (time in 0..meteorRunningTime.toInt()) {
                if (!isRunning) break

                getCanvas(holder) { canvas ->
                    // 清除画布
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

                    // 绘制背景
                    paint.reset()
                    canvas.drawBitmap(bitmap, 0f, 0f, paint)

                    // 绘制流星
                    drawMeteor(
                        canvas,
                        time.toFloat(),
                        startX,
                        startY,
                        paint,
                        meteorVelocity = meteorVelocity,
                        meteorLength = meteorLength,
                        meteorStrokeWidth = meteorStrokeWidth,
                        meteorRadian = meteorRadian
                    )
                }
            }

            delay(if (isUsingMeteorRandomScaleTime) random.nextLong(0, 3000) else meteorScaleTime.toLong())
        }
    }

    private fun getCanvas(
        holder: SurfaceHolder,
        drawContent: (canvas: Canvas) -> Unit
    ) {
        var canvas: Canvas? = null

        try {
            canvas = holder.lockCanvas()
            if (canvas != null) {
                drawContent(canvas)
            }
        } finally {
            if (canvas != null) {
                try {
                    holder.unlockCanvasAndPost(canvas)
                } catch (tr: Throwable) {
                    tr.printStackTrace()
                }
            }
        }
    }

    private fun drawFixedContent(
        canvas: Canvas,
        random: Random,
        background: Int = Color.BLACK,
        starNum: Int = 50,
        starColorList: List<Int> = listOf(Color.parseColor("#99CCCCCC"), Color.parseColor("#99AAAAAA"), Color.parseColor("#99777777")),
        starSizeList: List<Float> = listOf(0.8f, 0.9f, 1.2f),
    ) {
        // 初始化参数
        val startInfoList = mutableListOf<StarInfo>()
        val paint = Paint()

        // 添加星星数据
        for (i in 0 until starNum) {
            if (!isRunning) break

            val sizeScale = starSizeList.random(random)

            startInfoList.add(
                StarInfo(
                    Offset(random.nextDouble(canvas.width.toDouble()).toFloat(), random.nextDouble(canvas.height.toDouble()).toFloat()),
                    starColorList.random(random),
                    canvas.width/200 * sizeScale
                )
            )
        }


        // 绘制背景
        paint.reset()
        paint.color = background
        canvas.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), paint)

        // 绘制星星
        for (star in startInfoList) {
            if (!isRunning) break

            paint.reset()
            paint.color = star.color
            canvas.drawCircle(star.offset.x, star.offset.y, star.radius, paint)
        }
    }

    private fun drawMeteor(
        canvas: Canvas,
        currentTime: Float,
        startX: Int,
        startY: Int,
        paint: Paint,
        meteorColor: Int = Color.WHITE,
        meteorVelocity: Float = 10f,
        meteorRadian: Double = 0.7853981633974483,  // 45度
        meteorLength: Float = 500f,
        meteorStrokeWidth: Float = 1f,
    ) {
        val cosAngle = cos(meteorRadian).toFloat()
        val sinAngle = sin(meteorRadian).toFloat()

        // 计算当前起点坐标
        val currentStartX = startX + meteorVelocity * currentTime * cosAngle
        val currentStartY = startY + meteorVelocity * currentTime * sinAngle
        var currentEndX = 0f
        var currentEndY = 0f
        var currentLength = 0f


        // 计算流星坐标并绘制流星
        if (currentStartX <= canvas.width &&
            currentStartY <= canvas.height &&
            currentStartX >= 0 &&
            currentStartY >= 0
        ) { // 只有当流星起点还在绘制范围内时才继续计算以及绘制流星

            // 只有未达到目标长度，且不是无限长度才实时计算当前长度
            if (currentLength != meteorLength && meteorLength > 0) {
                currentLength = sqrt(
                    (currentEndX - currentStartX).pow(2) + (currentEndY - currentStartY).pow(2)
                )
            }

            // 如果是无限长度或长度未达到目标长度，则开始增长长度，具体表现为计算终点坐标时，速度是起点的两倍
            if (meteorLength <= 0 || currentLength < meteorLength) {
                currentEndX = startX + meteorVelocity * currentTime * 2 * cosAngle
                currentEndY = startY + meteorVelocity * currentTime * 2 * sinAngle
            }
            else { // 已达到目标长度，直接用起点坐标加上目标长度即可得到终点坐标
                currentEndX = currentStartX + meteorLength * cosAngle
                currentEndY = currentStartY + meteorLength * sinAngle
            }

            if (currentTime != 0f) {
                // 绘制流星
                paint.reset()
                paint.apply {
                    color = meteorColor
                    strokeWidth = meteorStrokeWidth
                    alpha = (currentTime * 10).toInt().coerceIn(0, 255)
                }
                canvas.drawLine(currentStartX, currentStartY, currentEndX, currentEndY, paint)
            }
        }
    }
}


data class StarInfo(
    val offset: Offset,
    val color: Int,
    val radius: Float
)