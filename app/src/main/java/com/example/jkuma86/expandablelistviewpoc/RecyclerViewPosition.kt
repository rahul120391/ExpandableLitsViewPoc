package com.example.jkuma86.expandablelistviewpoc

import android.support.v7.widget.RecyclerView

interface RecyclerViewPosition {
    fun getView(position:Int): RecyclerView.ViewHolder?
}