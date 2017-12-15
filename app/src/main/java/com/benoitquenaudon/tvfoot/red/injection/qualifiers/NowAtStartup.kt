package com.benoitquenaudon.tvfoot.red.injection.qualifiers

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

/**
 * Used for team search. Just basing every search's "StartAt" to when the app process is created.
 * Should fine some better way though
 */
@Qualifier
@Retention(RUNTIME) annotation class NowAtStartup
