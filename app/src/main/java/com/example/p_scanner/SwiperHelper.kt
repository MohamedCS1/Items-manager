package com.example.p_scanner

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.p_scanner.Interfaces.MyButtonListener
import java.util.*
import kotlin.collections.ArrayList

abstract class SwiperHelper(val context: Context, val recyclerView: RecyclerView, val buttonWidth:Int) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    var buttonList:ArrayList<MyButton>? = null
    var gestureDetector:GestureDetector? = null
    var swipePosition = -1
    var buttonBuffer:HashMap<Int,ArrayList<MyButton>>? = null
    var removeQueue:Queue<Int>? = null
    var swipeThreshold = 0.5f

    var gestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            for (button in buttonList!!)
            {
                if (button.onClick(e.x ,e.y))
                {
                    break
                }
                return true
            }
            return super.onSingleTapUp(e)
        }
    }

    var onTouListener = object :View.OnTouchListener{
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            if (swipePosition < 0)
            {
                return false
            }
            val point = Point(event!!.rawX.toInt(), event.rawY.toInt())
            val swipeViewHolder = recyclerView?.findViewHolderForAdapterPosition(swipePosition)
            val swipeItem = swipeViewHolder?.itemView
            val rect = Rect()
            swipeItem?.getGlobalVisibleRect(rect)

            if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_MOVE)
            {
                if (rect.top < point.y && rect.bottom > point.y)
                {
                    gestureDetector?.onTouchEvent(event)
                }
                else
                {
                    removeQueue?.add(swipePosition)
                    swipePosition = -1
                    recoverSwipedItem()
                }
            }
            return false
        }
    }

    init {
        gestureDetector = GestureDetector(context ,gestureListener)
        recyclerView.setOnTouchListener(onTouListener)
        buttonList = arrayListOf()
        buttonBuffer = hashMapOf()
        removeQueue = object :LinkedList<Int>(){
            override fun add(element: Int): Boolean {
                if (contains(element))
                {
                    return false
                }
                else
                {
                    return super.add(element)
                }
            }
        }
        attachSwipe()
    }

    fun attachSwipe(){
        val itemTouchHelper = ItemTouchHelper(this)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val pos = viewHolder.adapterPosition
        if (swipePosition != pos)
        {
            removeQueue!!.add(swipePosition)
            swipePosition = pos
        }
        if (buttonBuffer!!.contains(swipePosition))
        {
            buttonList = buttonBuffer!![swipePosition]
        }
        else
        {
            buttonList!!.clear()
            buttonBuffer!!.clear()
            swipeThreshold = 0.5f*buttonList!!.size*buttonWidth
            recoverSwipedItem()
        }
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return swipeThreshold
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return 0.1f*defaultValue
    }

    override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
        return 0.5f*defaultValue
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val pos = viewHolder.adapterPosition
        var translationX = dX
        val itemView = viewHolder.itemView
        if (pos < 0)
        {
            swipePosition = pos
            return
        }
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE)
        {
            if (dX < 0)
            {
                var buffer = arrayListOf<MyButton>()
                if (!buttonBuffer!!.containsKey(pos))
                {
                    instantiateMyButton(viewHolder ,buffer)
                }
                else
                {
                    buffer = buttonBuffer!![pos]!!
                }
                translationX = dX*buffer.size*buttonWidth / itemView.width
                drawButton(c ,itemView ,buffer ,pos ,translationX)
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun drawButton(
        canvas: Canvas,
        itemView: View,
        buffer: java.util.ArrayList<MyButton>,
        pos: Int,
        translationX: Float
    ) {

        var right = itemView.right
        val dButtonWidth = -1*translationX / buffer.size
        for (button in buffer)
        {
            val left = right - dButtonWidth
            button.onDraw(canvas , RectF(left ,itemView.top.toFloat() ,right.toFloat() ,itemView.bottom.toFloat()) ,pos)
            right = left.toInt()
        }
    }

     abstract fun instantiateMyButton(viewHolder: RecyclerView.ViewHolder, buffer: ArrayList<SwiperHelper.MyButton>)

    @Synchronized fun recoverSwipedItem()
    {
        while (!removeQueue!!.isEmpty()){
            val pos = removeQueue!!.poll()
            if (pos > -1)
            {
                recyclerView!!.adapter!!.notifyItemChanged(pos)
            }
        }
    }

    class MyButton(val context: Context, val text:String, val imageResId:Int, val textSize:Int, val color:Int, val listener: MyButtonListener){

        private val clickRegion = RectF()

        fun onClick(x:Float, y:Float): Boolean {
            if (clickRegion.contains(x ,y))
            {
                listener.onClick(pos)
                return true
            }
            return false
        }

        fun onDraw(canvas: Canvas ,rectF: RectF ,pos: Int)
        {
            val paint = Paint()
            paint.color = color
            canvas.drawRect(rectF ,paint)

            paint.color = Color.WHITE
            paint.textSize = textSize.toFloat()

            val rect = Rect()
            val cHeight = rectF.height()
            val cWidth = rectF.width()

            paint.textAlign = Paint.Align.LEFT
            paint.getTextBounds(text ,0 ,text.length ,rect)

            var x = 0.0
            var y = 0.0

            if (imageResId == 0)
            {

                x = (cWidth / 2f - rect.width()/2 - rect.left).toDouble()
                y = (cHeight/2f - rect.height()/2 - rect.bottom).toDouble()
                canvas.drawText(text , (rectF.left+x).toFloat(), (rectF.top+y).toFloat(),paint)
            }
            else
            {
                val drawable = ContextCompat.getDrawable(context ,imageResId)
                val bitmap:Bitmap = drawableToBitmap(drawable!!)
            }
            clickRegion = rectF
            this.pos = pos
        }

        fun drawableToBitmap(drawable: Drawable):Bitmap
        {
            if (drawable is BitmapDrawable)
            {
                return drawable.bitmap
            }

            val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth ,drawable.intrinsicHeight ,Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0 ,0 ,canvas.width ,canvas.height)
            drawable.draw(canvas)
            return bitmap
        }

    }
}