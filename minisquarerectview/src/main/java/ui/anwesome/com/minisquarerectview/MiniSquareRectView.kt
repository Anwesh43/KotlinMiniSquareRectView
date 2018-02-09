package ui.anwesome.com.minisquarerectview

/**
 * Created by anweshmishra on 09/02/18.
 */
import android.app.Activity
import android.content.*
import android.view.*
import android.graphics.*
class MiniSquareRectView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = Renderer(this)
    override fun onDraw(canvas:Canvas) {
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }
    data class Animator(var view:View, var animated:Boolean = false) {
        fun animate(updatecb:()->Unit) {
            if(animated) {
                updatecb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch (ex:Exception) {

                }
            }
        }
        fun start() {
            if(!animated) {
                animated = true
                view.postInvalidate()
            }
        }
        fun stop() {
            if(animated) {
                animated = false
            }
        }
    }
    data class State(var j:Int = 0,var dir:Float = 0f,var prevScale:Float = 0f, var jDir:Int = 1) {
        val scales:Array<Float> = arrayOf(0f,0f,0f)
        fun update(stopcb:(Float)->Unit) {
            scales[j] += 0.1f*dir
            if(Math.abs(scales[j] - prevScale) > 1) {
                scales[j] = prevScale + dir
                j += jDir
                if(j == scales.size || j == -1) {
                    jDir *= -1
                    j += jDir
                    prevScale = scales[j]
                    dir = 0f
                    stopcb(prevScale)
                }
            }
        }
        fun startUpdating(startcb:()->Unit) {
            if(dir == 0f) {
                dir = 1 - 2*prevScale
                startcb()
            }
        }
    }
    data class MiniSquareRect(var x:Float, var y:Float, var size:Float) {
        val state = State()
        fun draw(canvas:Canvas,paint:Paint) {
            val size_square = size*state.scales[0]
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(90*state.scales[2])
            canvas.save()
            canvas.translate(-size_square/2,-size_square/2)
            for(i in 0..8) {
                val x_rect = (i%3)*(size_square/2)
                val y_rect = (i/3)*(size_square/2)
                canvas.save()
                canvas.translate(x_rect,y_rect)
                paint.color = Color.parseColor("#9E9E9E")
                canvas.drawRoundRect(RectF(-size/10,-size/10,size/10,size/10),size/12,size/12,paint)
                paint.color = Color.parseColor("#EF6C00")
                canvas.save()
                canvas.scale(state.scales[1],state.scales[1])
                canvas.drawRoundRect(RectF(-size/10,-size/10,size/10,size/10),size/12,size/12,paint)
                canvas.restore()
                canvas.restore()
            }
            canvas.restore()
            canvas.restore()
        }
        fun update(stopcb:(Float)->Unit) {
            state.update(stopcb)
        }
        fun startUpdating(startcb:()->Unit) {
            state.startUpdating(startcb)
        }
    }
    data class Renderer(var view:MiniSquareRectView, var time:Int = 0) {
        var miniSquareRect:MiniSquareRect?=null
        val animator = Animator(view)
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                miniSquareRect = MiniSquareRect(w/2,h/2,2*Math.min(w,h)/3)
            }
            canvas.drawColor(Color.parseColor("#212121"))
            miniSquareRect?.draw(canvas,paint)
            time++
            animator.animate {
                miniSquareRect?.update {
                    animator.stop()
                }
            }
        }
        fun handleTap() {
            miniSquareRect?.startUpdating {
                animator.start()
            }
        }
    }
    companion object {
        fun create(activity:Activity):MiniSquareRectView {
            val view = MiniSquareRectView(activity)
            activity.setContentView(view)
            return view
        }
    }
}