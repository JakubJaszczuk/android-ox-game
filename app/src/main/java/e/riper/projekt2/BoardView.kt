package e.riper.projekt2

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import kotlin.math.max
import kotlin.math.min

class BoardView : View {

    var board = Board()
    lateinit var interactionManager: InteractionManager
    private var scaleFactor = 0.9f
    private var transX = 0.0f
    private var transY = 0.0f
    private var cellWidth = min(width, height) / min(board.board.width, board.board.height)
    private var halfCell = cellWidth / 2

    private var painter = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.rgb(140, 140, 140)
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }
    private var circlePainter = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.rgb(200, 60, 60)
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }
    private var crossPainter = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.rgb(60, 60, 200)
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas) {
        cellWidth = min(width, height) / min(board.board.width, board.board.height)
        halfCell = cellWidth / 2
        // Save
        canvas.save()
        // Transform
        canvas.scale(scaleFactor, scaleFactor, width / 2.0f, height / 2.0f)
        canvas.translate(transX, transY)

        // Draw
        val h = board.board.height
        for (i in 0..board.board.width) {
            canvas.drawLine((cellWidth * i).toFloat(), 0.0f, (cellWidth * i).toFloat(), (cellWidth * h).toFloat(), painter)
        }
        val w = board.board.width
        for (i in 0..board.board.height) {
            canvas.drawLine(0.0f, (cellWidth * i).toFloat(), (cellWidth * w).toFloat(), (cellWidth * i).toFloat(), painter)
        }
        for (i in 0 until board.board.width) {
            for (j in 0 until board.board.height) {
                if (board.board.get(i, j) == Cell.WHITE) {
                    canvas.drawCircle((i * cellWidth + halfCell).toFloat(), (j * cellWidth + halfCell).toFloat(), halfCell.toFloat(), circlePainter)
                }
                else if (board.board.get(i, j) == Cell.BLACK) {
                    val x = (i * cellWidth + halfCell).toFloat()
                    val y = (j * cellWidth + halfCell).toFloat()
                    canvas.drawLine(x - halfCell, y + halfCell, x + halfCell, y - halfCell, crossPainter)
                    canvas.drawLine(x - halfCell, y - halfCell, x + halfCell, y + halfCell, crossPainter)
                }
            }
        }
        // Restore
        canvas.restore()
    }

    inner class TransListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            transX -= distanceX
            transY -= distanceY
            return true
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            val x = (((e.x - width/2.0f) / scaleFactor) + width/2.0f - transX) / cellWidth
            val y = (((e.y - height/2.0f) / scaleFactor) + height/2.0f - transY) / cellWidth
            interactionManager.makeMove(x.toInt(), y.toInt())
            return true
        }
    }

    inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            scaleFactor = max(0.1f, min(scaleFactor, 5.0f))
            return true
        }
    }

    private val gestureDetector: GestureDetector = GestureDetector(context, TransListener())
    private val scaleGestureDetector: ScaleGestureDetector = ScaleGestureDetector(context, ScaleListener())

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var retVal: Boolean = scaleGestureDetector.onTouchEvent(event)
        retVal = gestureDetector.onTouchEvent(event) || retVal
        invalidate()
        return retVal || super.onTouchEvent(event)
    }
}
