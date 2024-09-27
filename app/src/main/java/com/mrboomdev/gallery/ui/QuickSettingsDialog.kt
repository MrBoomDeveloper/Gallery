package com.mrboomdev.gallery.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mrboomdev.gallery.databinding.QuickSettingsDialogBinding

class QuickSettingsDialog : BottomSheetDialogFragment() {
    private lateinit var binding: QuickSettingsDialogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = QuickSettingsDialogBinding.inflate(inflater, container, false)
        return binding.root
    }
}