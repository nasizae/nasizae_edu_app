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
            "Добро пожаловать!",
            "Мы рады приветствовать вас в нашем приложении, которое откроет для вас мир Android-разработки! Наши курсы и упражнения помогут вам легко и быстро освоить программирование на Java и другие важные аспекты разработки. Готовы начать? Погружаемся в мир технологий вместе!",
            "welcome.json"
        ),
        OnBoardingModel(
            "Изучение Андроид разработки!",
            "Здесь вы найдёте всё, что нужно для освоения Android-разработки: от изучения языка программирования до создания полноценных приложений.",
            "read_book.json"
        ),
        OnBoardingModel(
            "Game Interface",
            "Чтобы сделать обучение более интересным, мы создали приложение в игровом стиле, где вы сможете проходить уровни, осваивая новые знания, достигать поставленных целей и повышать свой уровень.",
            "game.json"
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
            binding.btnNext.isVisible = adapterPosition == list.lastIndex
            binding.btnNext.setOnClickListener {
                onItemClick()
            }
        }
    }
}