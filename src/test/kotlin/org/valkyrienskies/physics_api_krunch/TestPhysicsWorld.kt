package org.valkyrienskies.physics_api_krunch

import org.joml.Matrix3d
import org.joml.Quaterniond
import org.joml.Vector3d
import org.joml.Vector3i
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.valkyrienskies.physics_api.RigidBodyInertiaData
import org.valkyrienskies.physics_api.RigidBodyTransform
import org.valkyrienskies.physics_api.voxel_updates.KrunchVoxelStates
import org.valkyrienskies.physics_api.voxel_updates.SparseVoxelShapeUpdate
import org.valkyrienskies.physics_api_krunch.KrunchTestUtils.sendSparseUpdate

class TestPhysicsWorld {
    companion object {
        @BeforeAll
        @JvmStatic
        fun loadNativeBinaries() {
            KrunchBootstrap.loadNativeBinaries()
        }
    }

    /**
     * Test that bodies don't collide with deleted bodies.
     */
    @Test
    fun testBodiesDontCollideWithDeletedBodies() {
        val physicsWorldReference = KrunchBootstrap.createKrunchPhysicsWorld() as KrunchNativePhysicsWorldReference

        try {
            val sparseUpdate = SparseVoxelShapeUpdate(0, 0, 0, runImmediately = true)
            sparseUpdate.addUpdate(0, 0, 0, KrunchVoxelStates.SOLID_STATE)

            val topBody = physicsWorldReference.createVoxelRigidBody(0, Vector3i(), Vector3i())
            topBody.rigidBodyTransform = RigidBodyTransform(Vector3d(0.0, 1.0, 0.0), Quaterniond())
            topBody.inertiaData = RigidBodyInertiaData(1.0, Matrix3d().identity())
            sendSparseUpdate(physicsWorldReference, topBody.rigidBodyId, sparseUpdate)

            val groundBody = physicsWorldReference.createVoxelRigidBody(0, Vector3i(), Vector3i())
            groundBody.rigidBodyTransform = RigidBodyTransform(Vector3d(), Quaterniond())
            groundBody.isStatic = true
            groundBody.inertiaData = RigidBodyInertiaData(1.0, Matrix3d().identity())
            sendSparseUpdate(physicsWorldReference, groundBody.rigidBodyId, sparseUpdate)

            val groundBody2 = physicsWorldReference.createVoxelRigidBody(0, Vector3i(), Vector3i())
            groundBody2.rigidBodyTransform = RigidBodyTransform(Vector3d(0.0, -1.5, -0.0), Quaterniond())
            groundBody2.isStatic = true
            groundBody2.inertiaData = RigidBodyInertiaData(1.0, Matrix3d().identity())
            sendSparseUpdate(physicsWorldReference, groundBody2.rigidBodyId, sparseUpdate)

            // Simulate 1 second of physics
            for (i in 0 until 60)
                physicsWorldReference.tick(Vector3d(0.0, -10.0, 0.0), 1.0 / 60.0, true)

            // Expect that topBody is resting on groundBody, therefore its y position should be near 1.0
            run {
                val topBodyTransform = topBody.rigidBodyTransform
                assertEquals(1.0, topBodyTransform.position.y(), 1e-3)
            }

            // Then delete groundBody
            physicsWorldReference.deleteRigidBody(groundBody.rigidBodyId)

            // Simulate 1 second of physics
            for (i in 0 until 60)
                physicsWorldReference.tick(Vector3d(0.0, -10.0, 0.0), 1.0 / 60.0, true)

            // Expect that topBody is resting on groundBody2, therefore its y position should be near -0.5
            run {
                val topBodyTransform = topBody.rigidBodyTransform
                assertEquals(-0.5, topBodyTransform.position.y(), 1e-3)
            }
        } catch (e: Exception) {
            assertNotNull(e)
        } finally {
            physicsWorldReference.deletePhysicsWorldResources()
        }
    }
}
