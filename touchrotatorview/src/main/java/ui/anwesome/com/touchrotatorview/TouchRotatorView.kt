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
            line.draw(canvas,paint)
        }
        fun update(stopcb:(Float)->Unit) {
            line.update(stopcb)
        }
        fun startUpdating(startcb:()->Unit) {
            line.startUpdating(startcb)
        }
    }
    data class TouchRotatorRenderer(var view:TouchRotatorView,var time:Int = 0) {
        var container:TouchRotatorContainer?=null
        var animator = Animator(view)
        var updateDegFn:()->Unit = {
            container?.updateDeg()
        }
        var updateLineFn:()->Unit = {
            container?.update{
                animator.stop()
            }
        }
        var curr:()->Unit  = updateDegFn
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                container = TouchRotatorContainer(w,h)
            }
            container?.draw(canvas,paint)
            time++
            animator.animate(curr)
        }
        fun handleTap(event:MotionEvent) {
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    curr = updateDegFn
                    animator.startAnimating()
                }
                MotionEvent.ACTION_UP -> {
                    curr = updateLineFn
                    animator.stop()
                    animator.startAnimating()
                }
            }
        }
    }
}