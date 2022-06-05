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
        // TODO: Basically only want to use the other version for the newest Ryzen and Intel cpus. Theres more flags to
        //       look for than just Ryzen. The _no_avx binaries are about 15% slower in my testing.
        val suffix = "_no_avx"
        val libraryName = "KrunchJni$suffix"
        nativeLoader.load(libraryName)
    }
}
