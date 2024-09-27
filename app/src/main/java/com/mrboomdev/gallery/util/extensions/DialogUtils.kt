package com.mrboomdev.gallery.util.extensions

import android.app.Dialog
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.elevation.SurfaceColors
import com.google.android.material.sidesheet.SideSheetDialog

/**
 * A hacky method to fix the height, width of the dialog and color of the navigation bar.
 * @author MrBoomDev
 */
fun DialogFragment.fixAndShow(fragmentManager: FragmentManager, tag: String? = null) {
    showNow(fragmentManager, tag)
    dialog!!.fix()
}

/**
 * A hacky method to fix the height, width of the dialog and color of the navigation bar.
 * @author MrBoomDev
 */
fun Dialog.fixAndShow() {
    show()
    fix()
}

/**
 * A hacky method to fix the height, width of the dialog and color of the navigation bar.
 * @author MrBoomDev
 */
fun Dialog.fix() {
    if(this is BottomSheetDialog) {
        behavior.peekHeight = 1000
        window?.navigationBarColor = SurfaceColors.SURFACE_1.getColor(context)
    }

    if(this is SideSheetDialog) {
        window?.let {
            val sheet = it.findViewById<View>(R.id.m3_side_sheet)
            sheet.useLayoutParams<ViewGroup.LayoutParams> { params -> params.width = context.dpPx(400f) }
            it.navigationBarColor = SurfaceColors.SURFACE_1.getColor(context)
        }
    } else {
        /* If we'll try to do this shit with the SideSheetDialog, it will get centered,
			   so we use different approaches for different dialog types.*/

        if(context.resources.configuration.screenWidthDp > 400) {
            window?.setLayout(context.dpPx(400f), MATCH_PARENT)
        }
    }
}