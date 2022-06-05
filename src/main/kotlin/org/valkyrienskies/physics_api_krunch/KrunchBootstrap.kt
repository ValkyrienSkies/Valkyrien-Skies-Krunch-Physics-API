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
        // TODO: Check if the cpu supports AVX. AVX makes Krunch ~10% faster from my testing
        val supportsAvx = false
        val suffix = if (supportsAvx) "_avx" else ""
        val libraryName = "KrunchJni$suffix"
        nativeLoader.load(libraryName)
    }
}
