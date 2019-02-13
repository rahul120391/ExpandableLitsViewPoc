package com.example.jkuma86.expandablelistviewpoc

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import dev.jci.mwp.customviews.tooltip.ViewTooltip

class ExpandableAdapter(val context: Context) : BaseExpandableListAdapter(), View.OnClickListener {


    private val context1 = context

    private var imgArrow: ImageView? = null

    private var viewTooltip: ViewTooltip.TooltipView? = null

    private var tipView: View? = null

    private var llAddContacts: LinearLayout? = null

    private var llScheduleEvent: LinearLayout? = null

    private var arrowImageArray = arrayListOf<Boolean>()

    init {
        arrowImageArray.add(false)
        arrowImageArray.add(false)
    }

    override fun getGroup(groupPosition: Int): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View? {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.header_row_item, parent, false)

        }
        imgArrow = view?.findViewById(R.id.imgArrow)
        val imgOverflow = view?.findViewById<ImageView>(R.id.imgOverflow)
        imgOverflow?.tag = groupPosition
        imgOverflow?.setOnClickListener {
            createToolTip(it as ImageView)
        }

        if (arrowImageArray[groupPosition]) {
            imgArrow?.setImageResource(R.drawable.ic_down_arrow)
        } else {
            imgArrow?.setImageResource(R.drawable.ic_expand_arrow)
        }

        return view
    }

    fun updateArrowImage(position: Int, value: Boolean) {
        arrowImageArray[position] = value
        notifyDataSetChanged()
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return 1
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View? {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.child_layout, parent, false)
        }
        val rvView = view?.findViewById<RecyclerView>(R.id.rvChild)
        if (rvView?.layoutManager == null) {
            val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            context?.let { context ->
                ContextCompat.getDrawable(context, R.drawable.divider)?.let { itemDecorator.setDrawable(it) }
            }
            rvView?.layoutManager = LinearLayoutManager(context1)
            rvView?.addItemDecoration(itemDecorator)
        }
        rvView?.adapter = ChildAdapter()
        return view
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getGroupCount(): Int {
        return 2
    }

    private fun generateToolTip(view: ImageView) {
        if (tipView == null) {
            tipView = LayoutInflater.from(context).inflate(R.layout.tooltip_layout, null, false) as View
            llAddContacts = tipView?.findViewById(R.id.llAddContacts) as LinearLayout
            llScheduleEvent = tipView?.findViewById(R.id.llCalendar) as LinearLayout
            llAddContacts?.setOnClickListener(this)
            llScheduleEvent?.setOnClickListener(this)
        }
        viewTooltip?.removeAllViews()
        viewTooltip = tipView?.let {
            ViewTooltip
                .on(view)
                .position(ViewTooltip.Position.BOTTOM)
                .autoHide(false, 10000)
                .distanceWithView(0)
                .color(Color.WHITE)
                .customView(it)
                .onHide(object : ViewTooltip.ListenerHide {
                    override fun onHide(view: View) {
                    }
                })
                .show()
        }
    }

    private fun createToolTip(view: ImageView) {
        viewTooltip?.let {
            if (viewTooltip?.isShown as Boolean) {
                viewTooltip?.closeNow()
            } else {
                generateToolTip(view)
            }
        } ?: kotlin.run {
            generateToolTip(view)
        }
    }


    fun closeToolTip() {
        viewTooltip?.let {
            if (viewTooltip?.isShown as Boolean) {
                viewTooltip?.closeNow()
            }
        }
    }

    override fun onClick(v: View?) {
       when(v?.id){
           R.id.llAddContacts->{
               closeToolTip()
           }
           R.id.llCalendar->{
               closeToolTip()
           }
       }
    }


}