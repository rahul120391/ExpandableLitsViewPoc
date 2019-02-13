package com.example.jkuma86.expandablelistviewpoc

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.*
import android.text.Spannable
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.TextView
import dev.jci.mwp.customviews.tooltip.FadeTooltipAnimation
import dev.jci.mwp.customviews.tooltip.OnViewClickListener
import dev.jci.mwp.customviews.tooltip.TooltipAnimation
import java.util.*

class TooltipView(context: Context, val listener: OnViewClickListener) : FrameLayout(context) {
    private var arrowHeight = 15
        set(arrowHeight) {
            field = arrowHeight
            postInvalidate()
        }
    private var arrowWidth = 15
        set(arrowWidth) {
            field = arrowWidth
            postInvalidate()
        }
    private var arrowSourceMargin = 0
        set(arrowSourceMargin) {
            field = arrowSourceMargin
            postInvalidate()
        }
    private var arrowTargetMargin = 0
        set(arrowTargetMargin) {
            field = arrowTargetMargin
            postInvalidate()
        }
    private var childView: View
    private var color = Color.WHITE
    private var bubblePath: Path? = null
    private var bubblePaint: Paint? = null
    private var borderPaint: Paint? = null
    private var position = Position.BOTTOM
    private var align = ALIGN.CENTER
    private var clickToHide: Boolean = false
    private var autoHide = false
    private var duration: Long = 2000
    private var tooltipAnimation: TooltipAnimation = FadeTooltipAnimation()

    private var corner = 30

    internal var toolTipViewPaddingTop = 20
    internal var toolTipViewPaddingBottom = 30
    internal var toolTipViewPaddingRight = 30
    internal var toolTipViewPaddingLeft = 30

    private var shadowPadding = 10
    private var shadowWidth = 15

    private var viewRect: Rect? = null
    private var distanceWithView = 0
    private var shadowColor = context.getColor(R.color.tool_tip_shadow)

    init {
        setWillNotDraw(false)

        this.childView = TextView(context)
        (childView as TextView).setTextColor(Color.WHITE)
        addView(childView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        childView.setPadding(0, 0, 0, 0)

        bubblePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        bubblePaint?.color = color
        bubblePaint?.style = Paint.Style.FILL

        borderPaint = null

        setLayerType(View.LAYER_TYPE_SOFTWARE, bubblePaint)

        setWithShadow(true)

    }

    fun setCustomView(customView: View) {
        this.removeView(childView)
        this.childView = customView
        addView(childView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun setColor(color: Int) {
        this.color = color
        bubblePaint?.color = color
        postInvalidate()
    }

    fun setShadowColor(color: Int) {
        this.shadowColor = color
        postInvalidate()
    }

    fun setPaint(paint: Paint) {
        bubblePaint = paint
        setLayerType(View.LAYER_TYPE_SOFTWARE, paint)
        postInvalidate()
    }

    fun setPosition(position: Position) {
        this.position = position
        when (position) {
            Position.TOP -> setPadding(toolTipViewPaddingLeft, toolTipViewPaddingTop, toolTipViewPaddingRight, toolTipViewPaddingBottom + this.arrowHeight)
            Position.BOTTOM -> setPadding(toolTipViewPaddingLeft, toolTipViewPaddingTop + this.arrowHeight, toolTipViewPaddingRight, toolTipViewPaddingBottom)
            Position.LEFT -> setPadding(toolTipViewPaddingLeft, toolTipViewPaddingTop, toolTipViewPaddingRight + this.arrowHeight, toolTipViewPaddingBottom)
            Position.RIGHT -> setPadding(toolTipViewPaddingLeft + this.arrowHeight, toolTipViewPaddingTop, toolTipViewPaddingRight, toolTipViewPaddingBottom)
        }
        postInvalidate()
    }

    fun setAlign(align: ALIGN) {
        this.align = align
        postInvalidate()
    }

    fun setText(text: String) {
        if (childView is TextView) {
            (this.childView as TextView).text = text
        }
        postInvalidate()
    }

    fun setText(text: Spannable) {
        if (childView is TextView) {
            (this.childView as TextView).text = text
        }
        postInvalidate()
    }

    fun setText(text: Int) {
        if (childView is TextView) {
            (this.childView as TextView).setText(text)
        }
        postInvalidate()
    }

    fun setTextColor(textColor: Int) {
        if (childView is TextView) {
            (this.childView as TextView).setTextColor(textColor)
        }
        postInvalidate()
    }

    fun setTextTypeFace(textTypeFace: Typeface) {
        if (childView is TextView) {
            (this.childView as TextView).typeface = textTypeFace
        }
        postInvalidate()
    }

    fun setTextSize(unit: Int, size: Float) {
        if (childView is TextView) {
            (this.childView as TextView).setTextSize(unit, size)
        }
        postInvalidate()
    }

    fun setTextGravity(textGravity: Int) {
        if (childView is TextView) {
            (this.childView as TextView).gravity = textGravity
        }
        postInvalidate()
    }

    fun setClickToHide(clickToHide: Boolean) {
        this.clickToHide = clickToHide
    }


    fun setCorner(corner: Int) {
        this.corner = corner
    }

    override fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(width, height, oldw, oldh)

        bubblePath = getBubblePath(RectF(shadowPadding.toFloat(), shadowPadding.toFloat(), (width - shadowPadding * 2).toFloat(), (height - shadowPadding * 2).toFloat()), corner.toFloat(), corner.toFloat(), corner.toFloat(), corner.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (bubblePath != null) {
            canvas.drawPath(bubblePath, bubblePaint)
        }
    }

    fun setTooltipAnimation(tooltipAnimation: TooltipAnimation) {
        this.tooltipAnimation = tooltipAnimation
    }

    private fun startEnterAnimation() {
        tooltipAnimation.animateEnter(this, object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
            }
        })
    }

    private fun startExitAnimation(animatorListener: Animator.AnimatorListener) {
        tooltipAnimation.animateExit(this, object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                animatorListener.onAnimationEnd(animation)
            }
        })
    }

    private fun handleRemoveOfToolTip() {
        if (clickToHide) {
            setOnClickListener {
                remove()
            }
        }

        if (autoHide) {
            postDelayed({ remove() }, duration)
        }
    }

    internal fun remove() {
        startExitAnimation(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                removeNow()
            }
        })
    }

    fun setDuration(duration: Long) {
        this.duration = duration
    }

    fun setAutoHide(autoHide: Boolean) {
        this.autoHide = autoHide
    }

    private fun setupPosition(rect: Rect) {

        val x: Int
        val y: Int

        if (position == Position.LEFT || position == Position.RIGHT) {
            if (position == Position.LEFT) {
                x = rect.left - width - distanceWithView
            } else {
                x = rect.right + distanceWithView
            }
            y = rect.top + getAlignOffset(height, rect.height())
        } else {
            if (position == Position.BOTTOM) {
                y = rect.bottom + distanceWithView
            } else { // top
                y = rect.top - height - distanceWithView
            }
            x = rect.left + getAlignOffset(width, rect.width())
        }

        translationX = x.toFloat()
        translationY = y.toFloat()
    }

    private fun getAlignOffset(viewHeight: Int, rectHeight: Int): Int {
        return when (align) {
            ALIGN.END -> rectHeight - viewHeight
            ALIGN.CENTER -> (rectHeight - viewHeight) / 2
            else -> {
                0
            }
        }
    }

    private fun getBubblePath(myRect: RectF, topLeftDia: Float, topRightDia: Float, bottomRightDia: Float, bottomLeftDia: Float): Path {
        var topLeftDiameter = topLeftDia
        var topRightDiameter = topRightDia
        var bottomRightDiameter = bottomRightDia
        var bottomLeftDiameter = bottomLeftDia
        val path = Path()

        if (viewRect == null)
            return path

        topLeftDiameter = if (topLeftDiameter < 0) 0f else topLeftDiameter
        topRightDiameter = if (topRightDiameter < 0) 0f else topRightDiameter
        bottomLeftDiameter = if (bottomLeftDiameter < 0) 0f else bottomLeftDiameter
        bottomRightDiameter = if (bottomRightDiameter < 0) 0f else bottomRightDiameter

        val spacingLeft = (if (this.position == Position.RIGHT) this.arrowHeight else 0).toFloat()
        val spacingTop = (if (this.position == Position.BOTTOM) this.arrowHeight else 0).toFloat()
        val spacingRight = (if (this.position == Position.LEFT) this.arrowHeight else 0).toFloat()
        val spacingBottom = (if (this.position == Position.TOP) this.arrowHeight else 0).toFloat()

        val left = spacingLeft + myRect.left
        val top = spacingTop + myRect.top
        val right = myRect.right - spacingRight
        val bottom = myRect.bottom - spacingBottom
        val centerX = viewRect?.centerX() as Int - x

        val arrowSourceX = if (Arrays.asList(Position.TOP, Position.BOTTOM).contains(this.position))
            centerX + this.arrowSourceMargin
        else
            centerX
        val arrowTargetX = if (Arrays.asList(Position.TOP, Position.BOTTOM).contains(this.position))
            centerX + this.arrowTargetMargin
        else
            centerX
        val arrowSourceY = if (Arrays.asList(Position.RIGHT, Position.LEFT).contains(this.position))
            bottom / 2f - this.arrowSourceMargin
        else
            bottom / 2f
        val arrowTargetY = if (Arrays.asList(Position.RIGHT, Position.LEFT).contains(this.position))
            bottom / 2f - this.arrowTargetMargin
        else
            bottom / 2f

        path.moveTo(left + topLeftDiameter / 2f, top)
        //LEFT, TOP

        if (position == Position.BOTTOM) {
            path.lineTo(arrowSourceX - this.arrowWidth, top)
            path.lineTo(arrowTargetX, myRect.top)
            path.lineTo(arrowSourceX + this.arrowWidth, top)
        }
        path.lineTo(right - topRightDiameter / 2f, top)

        path.quadTo(right, top, right, top + topRightDiameter / 2)
        //RIGHT, TOP

        if (position == Position.LEFT) {
            path.lineTo(right, arrowSourceY - this.arrowWidth)
            path.lineTo(myRect.right, arrowTargetY)
            path.lineTo(right, arrowSourceY + this.arrowWidth)
        }
        path.lineTo(right, bottom - bottomRightDiameter / 2)

        path.quadTo(right, bottom, right - bottomRightDiameter / 2, bottom)
        //RIGHT, BOTTOM

        if (position == Position.TOP) {
            path.lineTo(arrowSourceX + this.arrowWidth, bottom)
            path.lineTo(arrowTargetX, myRect.bottom)
            path.lineTo(arrowSourceX - this.arrowWidth, bottom)
        }
        path.lineTo(left + bottomLeftDiameter / 2, bottom)

        path.quadTo(left, bottom, left, bottom - bottomLeftDiameter / 2)
        //LEFT, BOTTOM

        if (position == Position.RIGHT) {
            path.lineTo(left, arrowSourceY + this.arrowWidth)
            path.lineTo(myRect.left, arrowTargetY)
            path.lineTo(left, arrowSourceY - this.arrowWidth)
        }
        path.lineTo(left, top + topLeftDiameter / 2)

        path.quadTo(left, top, left + topLeftDiameter / 2, top)

        path.close()

        return path
    }

    private fun isSizeAdjusted(rect: Rect, screenWidth: Int): Boolean {
        var adjusted = false
        val layoutParams = layoutParams
        if (position == Position.LEFT && width > rect.left) {
            layoutParams.width = rect.left - MARGIN_SCREEN_BORDER_TOOLTIP - distanceWithView
            adjusted = true
        } else if (position == Position.RIGHT && rect.right + width > screenWidth) {
            layoutParams.width = screenWidth - rect.right - MARGIN_SCREEN_BORDER_TOOLTIP - distanceWithView
            adjusted = true
        } else if (position == Position.TOP || position == Position.BOTTOM) {
            var adjustedLeft = rect.left
            var adjustedRight = rect.right
            layoutParams.width = screenWidth - MARGIN_SCREEN_BORDER_TOOLTIP * 4
            if (rect.centerX() + width / 2f > screenWidth) {
                val diff = rect.centerX() + width / 2f - screenWidth

                adjustedLeft -= diff.toInt()
                adjustedRight -= diff.toInt()

                setAlign(ALIGN.CENTER)
                adjusted = true
            } else if (rect.centerX() - width / 2f < 0) {
                val diff = -(rect.centerX() - width / 2f)

                adjustedLeft += diff.toInt()
                adjustedRight += diff.toInt()

                setAlign(ALIGN.CENTER)
                adjusted = true
            }

            if (adjustedLeft < 0) {
                adjustedLeft = 0
            }

            if (adjustedRight > screenWidth) {
                adjustedRight = screenWidth
            }

            rect.left = adjustedLeft
            rect.right = adjustedRight
        }

        setLayoutParams(layoutParams)
        postInvalidate()
        return adjusted
    }

    private fun onSetup(myRect: Rect) {
        setupPosition(myRect)

        bubblePath = getBubblePath(RectF(shadowPadding.toFloat(), shadowPadding.toFloat(), width - shadowPadding * 2f, height - shadowPadding * 2f), corner.toFloat(), corner.toFloat(), corner.toFloat(), corner.toFloat())
        startEnterAnimation()

        handleRemoveOfToolTip()
    }

    fun setup(viewRect: Rect, screenWidth: Int) {
        this.viewRect = Rect(viewRect)
        val myRect = Rect(viewRect)

        val changed = isSizeAdjusted(myRect, screenWidth)
        if (!changed) {
            onSetup(myRect)
        } else {
            viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    onSetup(myRect)
                    viewTreeObserver.removeOnPreDrawListener(this)
                    return false
                }
            })
        }
    }

    fun removeNow() {
        if (parent != null) {
            val parent = parent as ViewGroup
            parent.removeView(this@TooltipView)
            listener.dismiss()
        }
    }

    fun setWithShadow(withShadow: Boolean) {
        if (withShadow) {
            bubblePaint?.setShadowLayer(shadowWidth.toFloat(), 0f, 0f, shadowColor)
        } else {
            bubblePaint?.setShadowLayer(0f, 0f, 0f, Color.TRANSPARENT)
        }
    }

    fun setDistanceWithView(distanceWithView: Int) {
        this.distanceWithView = distanceWithView
    }

    companion object {
        private const val MARGIN_SCREEN_BORDER_TOOLTIP = 30
    }

    enum class Position {
        LEFT,
        RIGHT,
        TOP,
        BOTTOM
    }

    enum class ALIGN {
        START,
        CENTER,
        END
    }
}