package com.example.jkuma86.expandablelistviewpoc

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daimajia.swipe.SwipeLayout
import kotlinx.android.synthetic.main.child_row_item.view.*

class ChildAdapter(recyclerViewPosition: RecyclerViewPosition) : RecyclerView.Adapter<ChildAdapter.MyViewHolder>() {
    private var context: Context? = null
    private var recyclerViewPosition = recyclerViewPosition
    private var swipePosition = -1
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        if (context == null) {
            context = parent.context
        }
        val view = LayoutInflater.from(parent.context).inflate(R.layout.child_row_item, parent, false)
        return view?.let { MyViewHolder(it) }!!
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        if (position % 2 == 0) {
            viewHolder.rlCard.background = context?.let { ContextCompat.getDrawable(it, R.drawable.cardview_bg) }
        } else {
            viewHolder.rlCard.background = context?.let { ContextCompat.getDrawable(it, R.drawable.cardview_bg_red) }
        }
        viewHolder.slLayout.addDrag(SwipeLayout.DragEdge.Left, viewHolder.leftLayout)
        viewHolder.slLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.rightLayout)
        viewHolder.leftLayout.setOnClickListener {
            viewHolder.slLayout.close()
        }
        viewHolder.rightLayout.setOnClickListener {
            viewHolder.slLayout.close()
        }
        viewHolder.slLayout.tag = position
        viewHolder.slLayout.addSwipeListener(object : SwipeLayout.SwipeListener {
            override fun onOpen(layout: SwipeLayout?) {
                val position = viewHolder.slLayout.tag as Int
                if (swipePosition != -1 && swipePosition != position) {
                    val holder = recyclerViewPosition.getView(position)
                    holder?.itemView?.findViewById<SwipeLayout>(R.id.slLayout)?.close()
                }
                swipePosition = position

            }

            override fun onClose(layout: SwipeLayout?) {
            }

            override fun onUpdate(layout: SwipeLayout?, leftOffset: Int, topOffset: Int) {
            }

            override fun onStartOpen(layout: SwipeLayout?) {
            }

            override fun onStartClose(layout: SwipeLayout?) {
            }

            override fun onHandRelease(layout: SwipeLayout?, xvel: Float, yvel: Float) {
            }
        })
    }

    class MyViewHolder(val item: View) : RecyclerView.ViewHolder(item) {

        val rlCard = item.rlCard
        val slLayout = item.slLayout
        val leftLayout = item.bottom_wrapper
        val rightLayout = item.bottom_wrapper_2

        init {
            item.setOnClickListener {
                //slLayout.close()
            }
        }
    }

}