package com.example.p_scanner

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class SwiperHelper(dragDirs: Int, swipeDirs: Int) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    var buttonList:List<MyButton>? = null
    var recyclerView:RecyclerView? = null
    var gestureDetector:GestureDetector? = null
    var swipePosition = -1
    var buttonBuffer:Map<Int,List<MyButton>>? = null
    var removeQueue:Queue<Int>? = null

    var gestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            for (button in buttonList)
            {

            }
            return super.onSingleTapUp(e)
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        TODO("Not yet implemented")
    }
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        TODO("Not yet implemented")
    }

    private class MyButton(val context: Context ,val text:String ,val imageResId:Int ,val textSize:Int ,val color:Int ,val pos:Int ,val clickRegion:RectF ,listener:MyButtonListener){

        fun onClick(x:Float ,y:Float)
        {
            if (clickRegion.contains(x ,y))
            {
                listener.onClick(pos)
            }
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

            val x = 0.0
            val y = 0.0

        }
    }
}