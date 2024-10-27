package com.github.jvsena42.floresta.presentation.ui.screens.main

import androidx.annotation.DrawableRes
import com.github.jvsena42.floresta.R



enum class Destinations(
    val route: String,
    val label: String,
    @DrawableRes val icon: Int
) {
    HOME(route = "Home", label = "Home", R.drawable.ic_home),
    NODE(route = "Node", label = "", R.drawable.ic_node),
    RECEIVE(route = "Receive", label = "Receive", R.drawable.ic_arrow_down)
}