package org.valkyrienskies.physics_api_krunch

import org.joml.Matrix3d
import org.joml.Quaterniond
import org.joml.Vector3d
import org.joml.Vector3i
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNull
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
            // Since we want [topBody] to move set isVoxelTerrainFullyLoaded to true
            topBody.isVoxelTerrainFullyLoaded = true

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
            assertNull(e)
        } finally {
            physicsWorldReference.deletePhysicsWorldResources()
        }
    }

    @Test
    fun testBodiesDontMoveWhenCollidingWithUnloadedTerrain() {
        val physicsWorldReference = KrunchBootstrap.createKrunchPhysicsWorld() as KrunchNativePhysicsWorldReference

        try {
            // groundBody is completely undefined
            val groundBody = physicsWorldReference.createVoxelRigidBody(
                0,
                Vector3i(Int.MIN_VALUE, 0, Int.MIN_VALUE),
                Vector3i(Int.MAX_VALUE, 255, Int.MAX_VALUE)
            )
            groundBody.isStatic = true

            val sparseUpdate = SparseVoxelShapeUpdate(0, 0, 0, runImmediately = true)
            sparseUpdate.addUpdate(0, 0, 0, KrunchVoxelStates.SOLID_STATE)

            // [topBody] and [bottomBody] are colliding, but they will not move because the terrain of [groundBody] is
            // undefined
            val topBody = physicsWorldReference.createVoxelRigidBody(0, Vector3i(), Vector3i())
            topBody.rigidBodyTransform = RigidBodyTransform(Vector3d(0.0, 10.0, 0.0), Quaterniond())
            topBody.inertiaData = RigidBodyInertiaData(1.0, Matrix3d().identity())
            sendSparseUpdate(physicsWorldReference, topBody.rigidBodyId, sparseUpdate)
            // Since we want [topBody] to move set isVoxelTerrainFullyLoaded to true
            topBody.isVoxelTerrainFullyLoaded = true

            val bottomBody = physicsWorldReference.createVoxelRigidBody(0, Vector3i(), Vector3i())
            bottomBody.rigidBodyTransform = RigidBodyTransform(Vector3d(0.0, 10.5, 0.0), Quaterniond())
            bottomBody.inertiaData = RigidBodyInertiaData(1.0, Matrix3d().identity())
            sendSparseUpdate(physicsWorldReference, bottomBody.rigidBodyId, sparseUpdate)
            // Since we want [bottomBody] to move set isVoxelTerrainFullyLoaded to true
            bottomBody.isVoxelTerrainFullyLoaded = true

            // Simulate 1 second of physics
            for (i in 0 until 60)
                physicsWorldReference.tick(Vector3d(0.0, -10.0, 0.0), 1.0 / 60.0, true)

            // Expect that topBody and bottomBody haven't moved
            run {
                val topBodyTransform = topBody.rigidBodyTransform
                assertEquals(10.0, topBodyTransform.position.y(), 1e-3)

                val bottomBodyTransform = bottomBody.rigidBodyTransform
                assertEquals(10.5, bottomBodyTransform.position.y(), 1e-3)
            }

            // Then delete the ground body
            physicsWorldReference.deleteRigidBody(groundBody.rigidBodyId)

            // Simulate 1 second of physics
            for (i in 0 until 60)
                physicsWorldReference.tick(Vector3d(0.0, -10.0, 0.0), 1.0 / 60.0, true)

            // Expect that topBody and bottomBody have moved
            run {
                val topBodyTransform = topBody.rigidBodyTransform
                assertNotEquals(10.0, topBodyTransform.position.y(), 1e-3)

                val bottomBodyTransform = bottomBody.rigidBodyTransform
                assertNotEquals(10.5, bottomBodyTransform.position.y(), 1e-3)
            }
        } catch (e: Exception) {
            assertNull(e)
        } finally {
            physicsWorldReference.deletePhysicsWorldResources()
        }
    }
}
