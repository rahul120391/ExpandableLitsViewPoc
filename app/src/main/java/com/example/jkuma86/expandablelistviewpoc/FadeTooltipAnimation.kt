/*
 *  JOHNSON CONTROLS CONFIDENTIAL
 *  
 *  ©Johnson Controls International plc, All Rights Reserved.
 *
 *  All Rights Reserved.
 *
 *  NOTICE:  All information contained herein is, and remains the property of Johnson Controls International plc and its suppliers,
 *  if any.  The intellectual and technical concepts contained herein are proprietary to Johnson Controls International plc and its 
 *  suppliers and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
 *  from Johnson Controls International plc.
 *
 *
 */
package dev.jci.mwp.customviews.tooltip

import android.animation.Animator
import android.view.View

class FadeTooltipAnimation : TooltipAnimation {

    private var fadeDuration: Long = 200

    constructor() {}

    constructor(fadeDuration: Long) {
        this.fadeDuration = fadeDuration
    }

    override fun animateEnter(view: View, animatorListener: Animator.AnimatorListener) {
        view.alpha = 0f
        view.animate().alpha(1f).setDuration(fadeDuration).setListener(animatorListener)
    }

    override fun animateExit(view: View, animatorListener: Animator.AnimatorListener) {
        view.animate().alpha(0f).setDuration(fadeDuration).setListener(animatorListener)
    }
}