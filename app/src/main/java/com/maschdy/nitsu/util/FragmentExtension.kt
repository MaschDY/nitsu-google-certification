package com.maschdy.nitsu.util

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar

fun Fragment.navigateTo(@IdRes destination: NavDirections) =
    findNavController().navigate(destination)

fun Fragment.navigateTo(@IdRes destination: Int, args: Bundle) =
    findNavController().navigate(destination, args)

fun Fragment.sendToast(@IdRes message: Int) =
    Toast.makeText(requireContext(), getText(message), Toast.LENGTH_SHORT).show()

fun Fragment.sendSnackBar(@IdRes message: Int) =
    Snackbar.make(requireContext(), requireView(), getText(message), Snackbar.LENGTH_SHORT).show()

fun Fragment.sendSnackBar(@IdRes message: Int, @IdRes buttonName: Int, action: () -> Unit) =
    Snackbar.make(requireContext(), requireView(), getText(message), Snackbar.LENGTH_INDEFINITE)
        .setAction(buttonName) { action() }.show()