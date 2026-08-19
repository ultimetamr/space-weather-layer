package com.spatialapps.spaceweather.input

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/**
 * Shared Space does not expose raw hand-joint tracking. This boundary keeps the
 * palm-up command replaceable when PICO exposes a Shared Space-safe signal.
 */
interface HandInput {
    val palmUpEvents: Flow<Unit>
    val isAvailable: Boolean
}

class SharedSpaceHandInput : HandInput {
    override val palmUpEvents: Flow<Unit> = emptyFlow()
    override val isAvailable: Boolean = false
}
