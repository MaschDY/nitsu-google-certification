package com.maschdy.nitsu.core.toastSnack

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.maschdy.nitsu.R
import com.maschdy.nitsu.databinding.FragmentToastSnackBinding
import com.maschdy.nitsu.util.sendSnackBar
import com.maschdy.nitsu.util.sendToast

class ToastSnackFragment : Fragment(R.layout.fragment_toast_snack) {

    private lateinit var binding: FragmentToastSnackBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentToastSnackBinding.bind(view)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        setButtons()
    }

    private fun setButtons() = with(binding) {
        messageToast.setOnClickListener { sendToast(R.string.message_toast_content) }
        announcementSnack.setOnClickListener { sendSnackBar(R.string.message_snack_content) }
        messageSnackAction.setOnClickListener {
            sendSnackBar(R.string.message_snack_content, R.string.okay) {
                sendToast(R.string.message_toast_content)
            }
        }
    }
}
