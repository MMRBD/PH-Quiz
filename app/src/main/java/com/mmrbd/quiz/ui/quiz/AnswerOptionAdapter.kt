package com.mmrbd.quiz.ui.quiz

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mmrbd.quiz.R
import com.mmrbd.quiz.databinding.ItemAnswerBinding
import com.mmrbd.quiz.data.model.Question
import java.util.Random

class AnswerOptionAdapter(private val onItemClickListener: (Question, isCorrect: Boolean) -> Unit) :
    RecyclerView.Adapter<AnswerOptionAdapter.AnswerOptionViewHolder>() {

    private lateinit var question: Question

    private var options: List<Map<String, String>> = emptyList()


    inner class AnswerOptionViewHolder(private val binding: ItemAnswerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(option: Map<String, String>) {
            with(binding) {
                tvOption.text = option.map { it.value }[0]
            }
        }
    }

    fun submitData(question: Question) {
        this.question = question


        var tmpOptions: MutableList<Map<String, String>> = mutableListOf()

        if (this.question.answers.A != null) {
            tmpOptions.add(mapOf("A" to this.question.answers.A!!))
        }
        if (this.question.answers.B != null) {
            tmpOptions.add(mapOf("B" to this.question.answers.B!!))
        }
        if (this.question.answers.C != null) {
            tmpOptions.add(mapOf("C" to this.question.answers.C!!))
        }

        if (this.question.answers.D != null) {
            tmpOptions.add(mapOf("D" to this.question.answers.D!!))
        }

        options = tmpOptions.shuffled(random = Random())

        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerOptionViewHolder {
        return AnswerOptionViewHolder(
            ItemAnswerBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: AnswerOptionViewHolder, position: Int) {


        holder.bind(options[position])


        holder.itemView.setOnClickListener {
//            onItemClickListener.invoke()
            if (question.answers.selectedOption == null) {
                question.answers.selectedOption = options[position]
                question.answers.selectedPosition = position
                notifyDataSetChanged()

            }
        }

//        holder.itemView.setOnClickListener {
//
//            question.correctAnswer = options[position]
//
//            notifyDataSetChanged()
//        }

        if (question.answers.selectedOption != null) {
            if (question.answers.selectedOption!!.keys.map { it }[0] == question.correctAnswer && question.answers.selectedPosition == position) {
                holder.itemView.setBackgroundResource(R.drawable.bg_correct_ans)

                onItemClickListener.invoke(question, true)
            }

            if (question.answers.selectedOption!!.keys.map { it }[0] != question.correctAnswer && question.answers.selectedPosition == position) {
                holder.itemView.setBackgroundResource(R.drawable.bg_wrong_ans)

                onItemClickListener.invoke(question, false)
            } else {
                if (options.get(position).get(question.correctAnswer) != null) {
                    holder.itemView.setBackgroundResource(R.drawable.bg_correct_ans)
                }
            }
        } else {
            holder.itemView.setBackgroundResource(R.drawable.bg_normal_option)

        }
//
//        if (question.answers.correctAns.isNullOrEmpty()) {
//
//        } else if (question.answers.correctAns == question.correctAnswer) {
//            holder.itemView.setBackgroundResource(R.drawable.bg_correct_ans)
//        } else if (question.userAnswer != question.correctAnswer) {
//            holder.itemView.setBackgroundResource(R.drawable.bg_wrong_ans)
//        }
    }

    private fun getA(position: Int): String {
        return when (position) {
            0 -> "A"
            1 -> "B"
            2 -> "C"
            else -> "D"
        }

    }

    private fun getB(ans: String): Int {
        return when (ans) {
            "A" -> 0
            "B" -> 1
            "C" -> 2
            else -> 3
        }

    }

    override fun getItemCount(): Int = options.size

}