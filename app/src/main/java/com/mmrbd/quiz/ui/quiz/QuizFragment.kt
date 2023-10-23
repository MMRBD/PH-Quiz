package com.mmrbd.quiz.ui.quiz

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.mmrbd.quiz.R
import com.mmrbd.quiz.databinding.FragmentQuizBinding
import com.mmrbd.quiz.utils.network.Result
import com.mmrbd.quiz.data.model.Question
import com.mmrbd.quiz.ui.theme.PHQuizTheme
import com.mmrbd.quiz.utils.AppConstants
import com.mmrbd.quiz.utils.AppLogger
import com.mmrbd.quiz.utils.network.NetworkFailureMessage
import com.mmrbd.quiz.utils.SharePrefUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class QuizFragment : Fragment() {

    private lateinit var binding: FragmentQuizBinding

    private val viewModel: QuizViewModel by viewModels()


    private lateinit var adapter: AnswerOptionAdapter

    private lateinit var questions: List<Question>

    private var counter = 0
    private var totalQuestion = 0

    private var scoreCount = 0


    @Inject
    lateinit var sharePrefUtil: SharePrefUtil

    @Inject
    lateinit var networkFailureMessage: NetworkFailureMessage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        adapter = AnswerOptionAdapter { ques, isCorrect ->
            if (isCorrect) {
                scoreCount += ques.score
                binding.tvScore.text = getString(R.string.score, scoreCount)
            }

            counter += 1
            if (totalQuestion > counter) {
                viewModel.setQuestionX(questions[counter], counter)
            } else {
                if (sharePrefUtil.getValueInt(AppConstants.HIGH_SCORE) < scoreCount)
                    sharePrefUtil.save(AppConstants.HIGH_SCORE, scoreCount)

                showAlertDialog(scoreCount)
            }
        }
        binding.rcvAnswerOption.layoutManager = LinearLayoutManager(requireContext())
        binding.rcvAnswerOption.adapter = adapter

        lifecycleScope.launch {
            viewModel.questionResponseSFlow.collect {
                when (it) {
                    is Result.Loading -> {
                        binding.progressLoading.isVisible = true
                    }

                    is Result.Error -> {
                        binding.progressLoading.isVisible = false
                        AppLogger.log(it.throwable.toString())

                        Toast.makeText(
                            requireContext(),
                            networkFailureMessage.handleFailure(it.throwable),
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                    is Result.Success -> {
                        binding.progressLoading.isVisible = false
                        binding.constraintGroup.isVisible = true

                        questions = it.data!!.questions
                        viewModel.setQuestionX(it.data.questions[0], 0)

                        AppLogger.log("DATA:: ${it.data.questions}")


                        totalQuestion = it.data.questions.size

                        timerProgressCompose()


                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.questionFlow.collect {

                binding.tvQuestionCount.text =
                    getString(R.string.question_count, counter + 1, totalQuestion)
                adapter.submitData(it)

                with(binding) {
                    tvScore.text = getString(R.string.score, scoreCount)
                    tvPoint.text = getString(R.string.point, it.score)
                    tvQuestion.text = HtmlCompat.fromHtml(
                        it.question,
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )

                    imgQuestion.load(it.questionImageUrl) {
                        crossfade(true)
                        placeholder(R.drawable.no_image)
                        error(R.drawable.no_image)
                    }
                }

                timerProgressCompose()
            }
        }


    }

    private fun timerProgressCompose() {
        binding.composeProgress.disposeComposition()
        binding.composeProgress.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )

            setContent {
                PHQuizTheme {
                    LinearProgressTimerIndicator(duration = 10000) {
                        counter += 1
                        if (totalQuestion > counter) {
                            viewModel.setQuestionX(questions[counter], counter, true)
                        } else {
                            if (sharePrefUtil.getValueInt(AppConstants.HIGH_SCORE) < scoreCount)
                                sharePrefUtil.save(AppConstants.HIGH_SCORE, scoreCount)

                            showAlertDialog(scoreCount)
                        }
                    }

                }
            }
        }
    }

    private fun showAlertDialog(score: Int) {
        val builder = AlertDialog.Builder(requireContext())
        with(builder)
        {
            setTitle(getString(R.string.final_score))
            setMessage(getString(R.string.your_score, score))
            setPositiveButton(getString(R.string.ok)) { _, _ ->
                findNavController().popBackStack()
            }
            show()
        }
    }
}