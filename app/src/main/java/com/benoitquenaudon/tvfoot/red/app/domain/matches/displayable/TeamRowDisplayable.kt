package com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable

import com.benoitquenaudon.tvfoot.red.util.TeamCode

data class TeamRowDisplayable(
    val name: String?,
    val logoPath: String,
    val code: TeamCode?
)