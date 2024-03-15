package com.alessandro.claymore.demo.multiround.allies

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class AlliesViewModel @Inject constructor(
  @AlliesActivityIntent.FirstAlly private val firstAlly: String,
  @AlliesActivityIntent.SecondAlly private val secondAlly: String,
) : ViewModel() {

  private val initialViewState = AlliesViewState(
    firstAlly = null,
    secondAlly = null,
    releaseAlliesButtonEnabled = true
  )

  private val _viewState = MutableStateFlow(initialViewState)
  val viewState: StateFlow<AlliesViewState> = _viewState

  fun releaseAllies() {
    _viewState.update {
      it.copy(
        firstAlly = firstAlly,
        secondAlly = secondAlly,
        releaseAlliesButtonEnabled = false,
      )
    }
  }

}