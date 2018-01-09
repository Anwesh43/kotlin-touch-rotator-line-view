package ui.anwesome.com.touchrotatorview

/**
 * Created by anweshmishra on 09/01/18.
 */
import android.content.*
import android.graphics.*
import android.view.*
class TouchRotatorView(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        return true
    }
    class Animator(var view:TouchRotatorView,var animated:Boolean = false) {
        fun animate(updatecb:()->Unit){
            if(animated) {
                updatecb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun stop() {
            if(animated) {
                animated = false
            }
        }
        fun startAnimating() {
            if(!animated) {
                animated = true
                view.postInvalidate()
            }
        }
    }
    class TouchRotator(var x:Float,var y:Float,var w:Float,var deg:Float = 0f,var dest:Float = 0f) {
        val state = State()
        fun update(stopcb:(Float)->Unit) {
            state.update(stopcb)
        }
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.YELLOW
            paint.strokeWidth = w/30
            paint.strokeCap = Paint.Cap.ROUND
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(deg)
            canvas.drawLine(0f,0f,w,0f,paint)
            canvas.restore()
        }
        fun startUpdating(deg:Float,startcb:()->Unit) {
            state.startUpdating {
                startcb()
                dest = deg
            }
        }
    }
    class State(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update(stopcb:(Float)->Unit) {
            scale += dir*0.1f
            if(Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                stopcb(scale)
            }
        }
        fun startUpdating(startcb:()->Unit) {
            if(dir == 0f) {
                dir = 1-2*scale
                startcb()
            }
        }
    }
    data class TouchRotatorContainer(var w:Float,var h:Float,var deg:Float = 0f) {
        var line = TouchRotatorContainer(w/2,h/2,Math.min(w,h)/3)
        fun updateDeg() {
            if(deg < 360) {
                deg+=5
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.GREEN
            paint.strokeWidth = Math.min(w,h)/30
            paint.strokeCap = Paint.Cap.ROUND
            canvas.drawLine(w/10,4*h/5,w+0.8f*w*(deg.toFloat())/360,4*h/5,paint)
        }
        fun update(stopcb:(Float)->Unit) {
            line.update(stopcb)
        }
        fun startUpdating(startcb:()->Unit) {
            line.startUpdating(startcb)
        }
    }
}