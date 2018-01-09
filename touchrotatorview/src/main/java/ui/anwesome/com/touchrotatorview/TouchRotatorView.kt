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
        fun update(stopcb:(Float)->Unit) {

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
            dest = deg
        }
    }
    class State(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update(stopcb:(Float)->Unit) {
            scale += dir*0.1f
            if(Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
            }
        }
        fun startUpdating(startcb:()->Unit) {
            if(dir == 0f) {
                dir = 1-2*scale
                startcb()
            }
        }
    }
}