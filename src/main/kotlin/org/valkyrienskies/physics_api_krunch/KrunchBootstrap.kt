package org.valkyrienskies.physics_api_krunch

import org.valkyrienskies.physics_api.PhysicsWorldReference
import org.valkyrienskies.physics_api_krunch.shared_library_loader.KrunchPhysicsAPISharedLibraryLoader

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
        val nativeLoader = KrunchPhysicsAPISharedLibraryLoader()
        nativeLoader.load("KrunchJni")
    }
}
