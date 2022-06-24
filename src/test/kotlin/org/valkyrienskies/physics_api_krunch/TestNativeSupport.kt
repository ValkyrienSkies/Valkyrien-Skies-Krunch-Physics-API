package org.valkyrienskies.physics_api_krunch

import org.joml.Vector3i
import org.joml.primitives.AABBi
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.valkyrienskies.physics_api.UsingDeletedReferenceException

class TestNativeSupport {
    @Test
    fun testCreateKrunchPhysicsWorld() {
        KrunchBootstrap.loadNativeBinaries()

        val physicsWorldReference = KrunchBootstrap.createKrunchPhysicsWorld() as KrunchNativePhysicsWorldReference
        assertTrue(!physicsWorldReference.hasBeenDeleted())
        physicsWorldReference.deletePhysicsWorldResources()
        assertTrue(physicsWorldReference.hasBeenDeleted())
        assertThrows<UsingDeletedReferenceException> { physicsWorldReference.createVoxelRigidBody(0, Vector3i(Int.MAX_VALUE), Vector3i(Int.MIN_VALUE), AABBi(-128, -128, -128, 127, 127, 127)) }
    }
}
