package com.aaascp.commons

import androidx.annotation.IdRes
import androidx.navigation.NavController

fun NavController.safeNavigate(@IdRes actionId: Int) {
    currentDestination?.getAction(actionId)?.run { navigate(actionId) }
}
