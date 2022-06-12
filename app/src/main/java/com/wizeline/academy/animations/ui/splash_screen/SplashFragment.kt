package com.wizeline.academy.animations.ui.splash_screen

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnCancel
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.wizeline.academy.animations.databinding.SplashFragmentBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    private var _binding: SplashFragmentBinding? = null
    private val binding get() = _binding!!

    private val bounceAnimator by lazy {
        ObjectAnimator.ofFloat(
            binding.ivWizelineLogo,
            View.TRANSLATION_Y,
            -binding.ivWizelineLogo.height.toFloat(),
            0F
        ).apply {
            duration = 750
            interpolator = BounceInterpolator()
            doOnCancel { restoreOriginalValues() }
        }
    }

    private val rotateAnimator by lazy {
        ObjectAnimator.ofFloat(binding.ivWizelineLogo, View.ROTATION, 0f, 360f).apply {
            repeatCount = 3
            repeatMode = ObjectAnimator.RESTART
            duration = 600
            interpolator = DecelerateInterpolator()
            doOnCancel { restoreOriginalValues() }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SplashFragmentBinding.inflate(inflater, container, false)

        setUpProgrammaticAnimations()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            delay(3250)
            goToHomeScreen()
        }
    }

    private fun goToHomeScreen() {
        val directions = SplashFragmentDirections.toMainFragment()
        findNavController().navigate(directions)
    }

    private fun setUpProgrammaticAnimations() {
        runTogetherAnimation()
    }

    private fun runTogetherAnimation() {
        val buttonAnimation = ObjectAnimator
            .ofFloat(binding.ivWizelineLogo, View.SCALE_X, 0f, 1f)
            .apply {
                duration = 750L
                interpolator = BounceInterpolator()
            }

        AnimatorSet().apply {
            play(bounceAnimator)
                .before(rotateAnimator)
                .with(buttonAnimation)
        }.start()
    }

    private fun restoreOriginalValues() {
        binding.ivWizelineLogo.apply {
            translationX = 0f
            translationY = 0f
            rotation = 0f
            scaleX = 1f
            scaleY = 1f
            alpha = 1f
        }
    }
}