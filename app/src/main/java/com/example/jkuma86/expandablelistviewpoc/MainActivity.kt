package com.example.jkuma86.expandablelistviewpoc

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.ExpandableListAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var expandaleListAdapter: ExpandableListAdapter
    private var lastExpandedPosition = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        expandaleListAdapter = ExpandableAdapter(this)
        expList.setAdapter(expandaleListAdapter)

        expList.setOnGroupExpandListener { groupPosition ->
            if (lastExpandedPosition != -1
                && groupPosition != lastExpandedPosition
            ) {
                (expandaleListAdapter as ExpandableAdapter).updateArrowImage(groupPosition,false)
                expList.collapseGroup(lastExpandedPosition)

            }
            (expandaleListAdapter as ExpandableAdapter).updateArrowImage(groupPosition,true)
            lastExpandedPosition = groupPosition;
        }

        expList.setOnGroupCollapseListener {
            groupPosition->
            (expandaleListAdapter as ExpandableAdapter).updateArrowImage(groupPosition,false)
        }

    }
}
