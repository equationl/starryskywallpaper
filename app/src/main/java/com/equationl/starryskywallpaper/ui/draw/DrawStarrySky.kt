package com.equationl.starryskywallpaper.ui.draw

import android.graphics.*
import android.view.SurfaceHolder
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.delay
import kotlin.math.*
import kotlin.random.Random


private const val meteorScaleTime = 500L  // 生成流星的间隔时间
private const val meteorTime = 300  // 流星持续飞行时间


class DrawStarrySky {
    private var isRunning = false

    fun stopDraw() {
        isRunning = false
    }

    suspend fun startDraw(
        holder: SurfaceHolder,
        randomSeed: Long = 1L
    ) {

        isRunning = true

        // 初始化参数
        val random = Random(randomSeed)
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
        drawFixedContent(Canvas(bitmap), random)

        while (isRunning) {

            // 绘制动态流星
            val safeDistanceStandard = canvasWidth / 10
            val safeDistanceVertical = canvasHeight / 10
            val startX = random.nextInt(safeDistanceStandard, canvasWidth - safeDistanceStandard)
            val startY = random.nextInt(safeDistanceVertical, canvasHeight - safeDistanceVertical)

            for (time in 0..meteorTime) {
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
                        paint
                    )
                }
            }

            delay(meteorScaleTime)
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
                currentLength = meteorLength
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