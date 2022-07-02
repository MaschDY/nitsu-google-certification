package com.maschdy.nitsu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.maschdy.nitsu.databinding.FragmentMainBinding
import com.maschdy.nitsu.util.navigateTo

class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        setButtons()
    }

    private fun setButtons() = with(binding) {
        toastAndSnackButton.setOnClickListener {
            navigateTo(MainFragmentDirections.actionMainFragmentToToastSnackbarFragment())
        }
    }
}
