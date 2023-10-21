package com.mmrbd.quiz.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mmrbd.quiz.R
import com.mmrbd.quiz.databinding.FragmentHomeBinding
import com.mmrbd.quiz.utils.AppConstants
import com.mmrbd.quiz.utils.SharePrefUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {


    @Inject
    lateinit var sharePrefUtil: SharePrefUtil

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.tvHighScore.text =
            getString(R.string.high_score, sharePrefUtil.getValueInt(AppConstants.HIGH_SCORE))

        binding.btnStart.setOnClickListener {
            findNavController().navigate(R.id.quizFragment)
        }
    }

}