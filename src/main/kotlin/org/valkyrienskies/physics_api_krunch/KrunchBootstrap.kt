package org.valkyrienskies.physics_api_krunch

import org.valkyrienskies.physics_api.PhysicsWorld

/**
 * Used to create a [KrunchPhysicsWorld] without exposing it to dependencies.
 */
object KrunchBootstrap {
    fun createKrunchPhysicsWorld(): PhysicsWorld = KrunchPhysicsWorld()

    fun setKrunchSettings(physicsWorld: PhysicsWorld, settings: KrunchPhysicsWorldSettings) {
        physicsWorld as KrunchPhysicsWorld
        physicsWorld.setSettings(settings)
    }
}
