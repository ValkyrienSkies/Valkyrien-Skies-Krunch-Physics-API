package org.valkyrienskies.physics_api_krunch

/**
 * Test linking when packaged in a jar
 */
fun main() {
    KrunchBootstrap.loadNativeBinaries()
    val physicsWorld = KrunchNativePhysicsWorldReference()
}
