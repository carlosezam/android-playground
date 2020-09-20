package com.ezam.playground.ui.form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ezam.playground.R
import com.ezam.playground.databinding.FragmentFormBinding

class FormFragment : Fragment() {

    lateinit var binding: FragmentFormBinding

    val vmodel: FormViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.inputDate.style
    }
}