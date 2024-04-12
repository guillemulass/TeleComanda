package com.example.telecomanda.screens.addToMenu

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddToMenuViewModel : ViewModel() {

    private val _isDish = MutableLiveData<Boolean>()
    val isDish : LiveData<Boolean> = _isDish

    private val _isDrink = MutableLiveData<Boolean>()
    val isDrink : LiveData<Boolean> = _isDrink

    private val _dishOrDrinkString = MutableLiveData<String>()
    val dishOrDrinkString : LiveData<String> = _dishOrDrinkString

    init{
        _isDish.value = true
        _isDrink.value = false
        _dishOrDrinkString.value = "Plato"
    }

    fun drinkOrDishAlternator(){
        if (_isDish.value!!){
            _isDish.value = false
            _isDrink.value = true
            _dishOrDrinkString.value = "Bebida"
        } else{
            _isDish.value = true
            _isDrink.value = false
            _dishOrDrinkString.value = "Plato"
        }
    }

}