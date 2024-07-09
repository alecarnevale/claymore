package com.alessandro.claymore.demo.multiround.villains

import androidx.lifecycle.ViewModel
import com.alessandro.claymore.demo.multiround.models.Villain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class VillainsViewModel @Inject constructor(
  @VillainsActivityIntent.FirstVillain private val firstVillain: Villain,
  @VillainsActivityIntent.SecondVillain private val secondVillain: Villain,
) : ViewModel() {

  private val initialViewState = VillainsViewState(
    firstVillain = null,
    secondVillain = null,
    releaseVillainsButtonEnabled = true
  )

  private val _viewState = MutableStateFlow(initialViewState)
  val viewState: StateFlow<VillainsViewState> = _viewState

  fun releaseVillains() {
    _viewState.update {
      it.copy(
        firstVillain = firstVillain.name,
        secondVillain = secondVillain.name,
        releaseVillainsButtonEnabled = false,
      )
    }
  }

}