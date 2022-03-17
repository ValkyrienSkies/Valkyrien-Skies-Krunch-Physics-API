package org.valkyrienskies.physics_api_krunch

import com.badlogic.gdx.utils.SharedLibraryLoader
import org.valkyrienskies.physics_api.PhysicsWorldReference

/**
 * Used to create a [KrunchNativePhysicsWorldReference] without exposing it to dependencies.
 */
object KrunchBootstrap {
    fun createKrunchPhysicsWorld(): PhysicsWorldReference =
        KrunchNativePhysicsWorldReference()

    fun setKrunchSettings(physicsWorldReference: PhysicsWorldReference, settingsWrapper: KrunchPhysicsWorldSettingsc) {
        physicsWorldReference as KrunchNativePhysicsWorldReference
        physicsWorldReference.setSettings(settingsWrapper)
    }

    fun loadNativeBinaries() {
        val nativeLoader = SharedLibraryLoader()
        nativeLoader.load("KrunchJni")
    }
}
