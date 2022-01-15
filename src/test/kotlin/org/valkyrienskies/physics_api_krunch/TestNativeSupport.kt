package org.valkyrienskies.physics_api_krunch

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TestNativeSupport {
    @Test
    fun testCreateKrunchPhysicsWorld() {
        KrunchBootstrap.loadNativeBinaries()

        val physicsWorldReference = KrunchBootstrap.createKrunchPhysicsWorld() as KrunchNativePhysicsWorldReference
        assertTrue(!physicsWorldReference.hasBeenDeleted())
        physicsWorldReference.deletePhysicsWorldResources()
        assertTrue(physicsWorldReference.hasBeenDeleted())
        assertThrows<AlreadyDeletedException> { physicsWorldReference.createVoxelRigidBody(0) }
    }
}
