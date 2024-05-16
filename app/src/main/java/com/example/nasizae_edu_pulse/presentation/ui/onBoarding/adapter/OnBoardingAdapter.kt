package com.example.edupulse.presentation.ui.onBoarding.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.edupulse.data.model.OnBoardingModel
import com.example.nasizae_edu_pulse.databinding.ItemOnboardingBinding

class OnBoardingAdapter(private val onItemClick: () -> Unit) :
    Adapter<OnBoardingAdapter.OnboardingHolder>() {

    private val list = mutableListOf(
        OnBoardingModel(
            "Планируй свое обучение с удовольствием!",
            "Создавай учебные планы, следи за прогрессом и достигай своих целей с легкостью благодаря нашему удобному приложению.",
            "plan_training.json"
        ),
        OnBoardingModel(
            "Будь в курсе всех заданий и дедлайнов!",
            "Получай уведомления о предстоящих заданиях и дедлайнах, чтобы никогда не пропустить важные сроки и успешно завершить свои учебные проекты.",
            "work_and_dedline.json"
        ),
        OnBoardingModel(
            "Получай персонализированные рекомендации для эффективного обучения!",
            "Наши алгоритмы анализируют твои успехи и предпочтения, чтобы предложить персонализированные рекомендации по курсам, материалам и методам обучения, которые подойдут именно тебе.",
            "personal_recommended.json"
        ),

        )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingHolder {
        return OnboardingHolder(
            ItemOnboardingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: OnboardingHolder, position: Int) {
        holder.bind(list[position])
    }
    inner class OnboardingHolder(private val binding: ItemOnboardingBinding) :
        ViewHolder(binding.root) {
        fun bind(onBoardingModel: OnBoardingModel) {
            binding.tvTitle.text = onBoardingModel.title
            binding.tvDesc.text = onBoardingModel.desc
            binding.lottieView.setAnimation(onBoardingModel.lottie)
            binding.lottieView.playAnimation()
            binding.lottieView.repeatCount = -1
            binding.btnNext.isVisible=adapterPosition==list.lastIndex
            binding.btnNext.setOnClickListener {
                onItemClick()
            }
        }
    }
}