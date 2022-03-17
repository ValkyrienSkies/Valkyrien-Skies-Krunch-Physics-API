package org.valkyrienskies.physics_api_krunch

import org.joml.Vector3d
import org.joml.Vector3i
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.valkyrienskies.physics_api.PhysicsWorldReference
import org.valkyrienskies.physics_api.voxel_updates.*
import org.valkyrienskies.physics_api_krunch.KrunchNativeRigidBodyReference.VOXEL_STATE_UNLOADED

class TestVoxelRigidBody {
    companion object {
        @BeforeAll
        @JvmStatic
        fun loadNativeBinaries() {
            KrunchBootstrap.loadNativeBinaries()
        }
    }

    private fun sendEmptyUpdate(physicsWorldReference: PhysicsWorldReference, rigidBodyId: Int, emptyUpdate: EmptyVoxelShapeUpdate) {
        val voxelShapeUpdates = VoxelRigidBodyShapeUpdates(rigidBodyId, arrayOf(emptyUpdate))
        physicsWorldReference.queueVoxelShapeUpdates(arrayOf(voxelShapeUpdates))
        // Tick the physics world to apply the queued voxel shape updates
        physicsWorldReference.tick(Vector3d(), 1.0, false)
    }

    private fun sendDenseUpdate(physicsWorldReference: PhysicsWorldReference, rigidBodyId: Int, denseUpdate: DenseVoxelShapeUpdate) {
        val voxelShapeUpdates = VoxelRigidBodyShapeUpdates(rigidBodyId, arrayOf(denseUpdate))
        physicsWorldReference.queueVoxelShapeUpdates(arrayOf(voxelShapeUpdates))
        // Tick the physics world to apply the queued voxel shape updates
        physicsWorldReference.tick(Vector3d(), 1.0, false)
    }

    private fun sendSparseUpdate(physicsWorldReference: PhysicsWorldReference, rigidBodyId: Int, sparseUpdate: SparseVoxelShapeUpdate) {
        val voxelShapeUpdates = VoxelRigidBodyShapeUpdates(rigidBodyId, arrayOf(sparseUpdate))
        physicsWorldReference.queueVoxelShapeUpdates(arrayOf(voxelShapeUpdates))
        // Tick the physics world to apply the queued voxel shape updates
        physicsWorldReference.tick(Vector3d(), 1.0, false)
    }

    private fun sendDeleteUpdate(physicsWorldReference: PhysicsWorldReference, rigidBodyId: Int, deleteUpdate: DeleteVoxelShapeUpdate) {
        val voxelShapeUpdates = VoxelRigidBodyShapeUpdates(rigidBodyId, arrayOf(deleteUpdate))
        physicsWorldReference.queueVoxelShapeUpdates(arrayOf(voxelShapeUpdates))
        // Tick the physics world to apply the queued voxel shape updates
        physicsWorldReference.tick(Vector3d(), 1.0, false)
    }

    @Test
    fun testEmptyGetVoxelState() {
        val physicsWorldReference = KrunchBootstrap.createKrunchPhysicsWorld() as KrunchNativePhysicsWorldReference
        try {
            val voxelBodyReference =
                physicsWorldReference.createVoxelRigidBody(0, Vector3i(0, 0, 0), Vector3i(15, 15, 15))

            // We expect [VOXEL_STATE_UNLOADED] because we haven't sent any voxel shape updates and the shape is defined between (0, 0, 0) and (15, 15, 15)
            val voxelState = voxelBodyReference.getVoxelState(0, 0, 0)
            assertEquals(VOXEL_STATE_UNLOADED, voxelState)

            // We expect [KrunchVoxelStates.AIR_STATE] because (16, 0, 0) is outside the defined region between (0, 0, 0) and (15, 15, 15)
            val voxelState2 = voxelBodyReference.getVoxelState(16, 0, 0).toByte()
            assertEquals(KrunchVoxelStates.AIR_STATE, voxelState2)

            // Send the empty update to "load" the terrain the chunk at (0, 0, 0)
            val emptyUpdate = EmptyVoxelShapeUpdate(0, 0, 0, runImmediately = true, overwriteExistingVoxels = true)
            sendEmptyUpdate(physicsWorldReference, voxelBodyReference.rigidBodyId, emptyUpdate)

            // We expect [KrunchVoxelStates.AIR_STATE] because we sent the empty update, which loaded the terrain between (0, 0, 0) and (15, 15, 15)
            val voxelState3 = voxelBodyReference.getVoxelState(0, 0, 0).toByte()
            assertEquals(KrunchVoxelStates.AIR_STATE, voxelState3)
        } finally {
            physicsWorldReference.deletePhysicsWorldResources()
        }
    }

    @Test
    fun testEmptyGetVoxelState2() {
        val physicsWorldReference = KrunchBootstrap.createKrunchPhysicsWorld() as KrunchNativePhysicsWorldReference
        try {
            val voxelBodyReference =
                physicsWorldReference.createVoxelRigidBody(0, Vector3i(16, 0, -16), Vector3i(31, 15, -1))

            // We expect [VOXEL_STATE_UNLOADED] because we haven't sent any voxel shape updates and the shape is defined between (16, 0, -16) and (31, 15, -1)
            val voxelState = voxelBodyReference.getVoxelState(18, 0, -5)
            assertEquals(VOXEL_STATE_UNLOADED, voxelState)

            // We expect [KrunchVoxelStates.AIR_STATE] because (100, 0, 0) is outside the defined region
            val voxelState2 = voxelBodyReference.getVoxelState(100, 0, 0).toByte()
            assertEquals(KrunchVoxelStates.AIR_STATE, voxelState2)

            // Send the empty update to "load" the terrain the chunk at (0, 0, 0)
            val emptyUpdate = EmptyVoxelShapeUpdate(0, 0, 0, runImmediately = true, overwriteExistingVoxels = true)
            sendEmptyUpdate(physicsWorldReference, voxelBodyReference.rigidBodyId, emptyUpdate)

            // We expect still [VOXEL_STATE_UNLOADED] because we haven't defined the region
            val voxelState3 = voxelBodyReference.getVoxelState(18, 0, -5)
            assertEquals(VOXEL_STATE_UNLOADED, voxelState3)

            // Send the empty update to "load" the terrain the chunk at (0, 0, 0)
            val emptyUpdate2 = EmptyVoxelShapeUpdate(1, 0, -1, runImmediately = true, overwriteExistingVoxels = true)
            sendEmptyUpdate(physicsWorldReference, voxelBodyReference.rigidBodyId, emptyUpdate2)

            // We expect [KrunchVoxelStates.AIR_STATE] because we sent the empty update, which loaded the terrain
            val voxelState4 = voxelBodyReference.getVoxelState(18, 0, -5).toByte()
            assertEquals(KrunchVoxelStates.AIR_STATE, voxelState4)
        } finally {
            physicsWorldReference.deletePhysicsWorldResources()
        }
    }

    @Test
    fun testEmptyUpdateThenDeleteUpdateGetVoxelState() {
        val physicsWorldReference = KrunchBootstrap.createKrunchPhysicsWorld() as KrunchNativePhysicsWorldReference
        try {
            val voxelBodyReference =
                physicsWorldReference.createVoxelRigidBody(0, Vector3i(0, 0, 0), Vector3i(15, 15, 15))

            // We expect [VOXEL_STATE_UNLOADED] because we haven't sent any voxel shape updates and the shape is defined between (0, 0, 0) and (15, 15, 15)
            val voxelState = voxelBodyReference.getVoxelState(0, 0, 0)
            assertEquals(VOXEL_STATE_UNLOADED, voxelState)

            // We expect [KrunchVoxelStates.AIR_STATE] because (16, 0, 0) is outside the defined region between (0, 0, 0) and (15, 15, 15)
            val voxelState2 = voxelBodyReference.getVoxelState(16, 0, 0).toByte()
            assertEquals(KrunchVoxelStates.AIR_STATE, voxelState2)

            // Send the empty update to "load" the terrain the chunk at (0, 0, 0)
            val emptyUpdate = EmptyVoxelShapeUpdate(0, 0, 0, runImmediately = true, overwriteExistingVoxels = true)
            sendEmptyUpdate(physicsWorldReference, voxelBodyReference.rigidBodyId, emptyUpdate)

            // We expect [KrunchVoxelStates.AIR_STATE] because we sent the empty update, which loaded the terrain between (0, 0, 0) and (15, 15, 15)
            val voxelState3 = voxelBodyReference.getVoxelState(0, 0, 0).toByte()
            assertEquals(KrunchVoxelStates.AIR_STATE, voxelState3)

            val deleteUpdate = DeleteVoxelShapeUpdate(0, 0, 0, runImmediately = true)
            sendDeleteUpdate(physicsWorldReference, voxelBodyReference.rigidBodyId, deleteUpdate)

            // We expect [VOXEL_STATE_UNLOADED] because we sent the delete update, which unloaded the terrain between (0, 0, 0) and (15, 15, 15)
            val voxelState4 = voxelBodyReference.getVoxelState(0, 0, 0)
            assertEquals(VOXEL_STATE_UNLOADED, voxelState4)
        } finally {
            physicsWorldReference.deletePhysicsWorldResources()
        }
    }

    @Test
    fun testDenseGetVoxelState() {
        val physicsWorldReference = KrunchBootstrap.createKrunchPhysicsWorld() as KrunchNativePhysicsWorldReference
        try {
            val voxelBodyReference =
                physicsWorldReference.createVoxelRigidBody(0, Vector3i(0, 0, 0), Vector3i(15, 15, 15))

            // We expect [VOXEL_STATE_UNLOADED] because we haven't sent any voxel shape updates and the shape is defined between (0, 0, 0) and (15, 15, 15)
            val voxelState = voxelBodyReference.getVoxelState(0, 0, 0)
            assertEquals(VOXEL_STATE_UNLOADED, voxelState)

            // Send the dense update to "load" the terrain the chunk at (0, 0, 0)
            val denseUpdate = DenseVoxelShapeUpdate(0, 0, 0, runImmediately = true)
            denseUpdate.setVoxel(4, 5, 6, KrunchVoxelStates.SOLID_STATE)
            denseUpdate.setVoxel(15, 14, 13, KrunchVoxelStates.LAVA_STATE)
            denseUpdate.setVoxel(1, 2, 3, KrunchVoxelStates.WATER_STATE)
            sendDenseUpdate(physicsWorldReference, voxelBodyReference.rigidBodyId, denseUpdate)

            // Verify the dense update was processed correctly
            val voxelState2 = voxelBodyReference.getVoxelState(4, 5, 6).toByte()
            assertEquals(KrunchVoxelStates.SOLID_STATE, voxelState2)

            // Verify the dense update was processed correctly
            val voxelState3 = voxelBodyReference.getVoxelState(15, 14, 13).toByte()
            assertEquals(KrunchVoxelStates.LAVA_STATE, voxelState3)

            // Verify the dense update was processed correctly
            val voxelState4 = voxelBodyReference.getVoxelState(1, 2, 3).toByte()
            assertEquals(KrunchVoxelStates.WATER_STATE, voxelState4)

            // Verify the dense update was processed correctly
            val voxelState5 = voxelBodyReference.getVoxelState(10, 11, 12).toByte()
            assertEquals(KrunchVoxelStates.AIR_STATE, voxelState5)
        } finally {
            physicsWorldReference.deletePhysicsWorldResources()
        }
    }

    @Test
    fun testSparseGetVoxelState() {
        val physicsWorldReference = KrunchBootstrap.createKrunchPhysicsWorld() as KrunchNativePhysicsWorldReference
        try {
            val voxelBodyReference =
                physicsWorldReference.createVoxelRigidBody(0, Vector3i(0, 0, 0), Vector3i(15, 15, 15))

            // We expect [VOXEL_STATE_UNLOADED] because we haven't sent any voxel shape updates and the shape is defined between (0, 0, 0) and (15, 15, 15)
            val voxelState = voxelBodyReference.getVoxelState(0, 0, 0)
            assertEquals(VOXEL_STATE_UNLOADED, voxelState)

            // Send the dense update to "load" the terrain the chunk at (0, 0, 0)
            val sparseUpdate = SparseVoxelShapeUpdate(0, 0, 0, runImmediately = true)
            sparseUpdate.addUpdate(3, 4, 5, KrunchVoxelStates.SOLID_STATE)
            sparseUpdate.addUpdate(14, 13, 12, KrunchVoxelStates.LAVA_STATE)
            sparseUpdate.addUpdate(0, 1, 2, KrunchVoxelStates.WATER_STATE)
            sendSparseUpdate(physicsWorldReference, voxelBodyReference.rigidBodyId, sparseUpdate)

            // Verify the dense update was processed correctly
            val voxelState2 = voxelBodyReference.getVoxelState(3, 4, 5).toByte()
            assertEquals(KrunchVoxelStates.SOLID_STATE, voxelState2)

            // Verify the dense update was processed correctly
            val voxelState3 = voxelBodyReference.getVoxelState(14, 13, 12).toByte()
            assertEquals(KrunchVoxelStates.LAVA_STATE, voxelState3)

            // Verify the dense update was processed correctly
            val voxelState4 = voxelBodyReference.getVoxelState(0, 1, 2).toByte()
            assertEquals(KrunchVoxelStates.WATER_STATE, voxelState4)

            // Verify the dense update was processed correctly
            val voxelState5 = voxelBodyReference.getVoxelState(9, 10, 11).toByte()
            assertEquals(KrunchVoxelStates.AIR_STATE, voxelState5)
        } finally {
            physicsWorldReference.deletePhysicsWorldResources()
        }
    }

    @Test
    fun testSparseThenEmptyOverwriteGetVoxelState() {
        val physicsWorldReference = KrunchBootstrap.createKrunchPhysicsWorld() as KrunchNativePhysicsWorldReference
        try {
            val voxelBodyReference =
                physicsWorldReference.createVoxelRigidBody(0, Vector3i(0, 0, 0), Vector3i(15, 15, 15))

            // We expect [VOXEL_STATE_UNLOADED] because we haven't sent any voxel shape updates and the shape is defined between (0, 0, 0) and (15, 15, 15)
            val voxelState = voxelBodyReference.getVoxelState(0, 0, 0)
            assertEquals(VOXEL_STATE_UNLOADED, voxelState)

            // Send the dense update to "load" the terrain the chunk at (0, 0, 0)
            val sparseUpdate = SparseVoxelShapeUpdate(0, 0, 0, runImmediately = true)
            sparseUpdate.addUpdate(3, 4, 5, KrunchVoxelStates.SOLID_STATE)
            sparseUpdate.addUpdate(14, 13, 12, KrunchVoxelStates.LAVA_STATE)
            sparseUpdate.addUpdate(0, 1, 2, KrunchVoxelStates.WATER_STATE)
            sendSparseUpdate(physicsWorldReference, voxelBodyReference.rigidBodyId, sparseUpdate)

            // Verify the dense update was processed correctly
            val voxelState2 = voxelBodyReference.getVoxelState(3, 4, 5).toByte()
            assertEquals(KrunchVoxelStates.SOLID_STATE, voxelState2)

            // Verify the dense update was processed correctly
            val voxelState3 = voxelBodyReference.getVoxelState(14, 13, 12).toByte()
            assertEquals(KrunchVoxelStates.LAVA_STATE, voxelState3)

            // Verify the dense update was processed correctly
            val voxelState4 = voxelBodyReference.getVoxelState(0, 1, 2).toByte()
            assertEquals(KrunchVoxelStates.WATER_STATE, voxelState4)

            // Verify the dense update was processed correctly
            val voxelState5 = voxelBodyReference.getVoxelState(9, 10, 11).toByte()
            assertEquals(KrunchVoxelStates.AIR_STATE, voxelState5)

            // Send an empty update, but with overwriteExistingVoxels=false
            val emptyUpdate = EmptyVoxelShapeUpdate(0, 0, 0, runImmediately = true, overwriteExistingVoxels = false)
            sendEmptyUpdate(physicsWorldReference, voxelBodyReference.rigidBodyId, emptyUpdate)

            // Don't expect any change because [emptyUpdate] has overwriteExistingVoxels=false
            val voxelState6 = voxelBodyReference.getVoxelState(3, 4, 5).toByte()
            assertEquals(KrunchVoxelStates.SOLID_STATE, voxelState6)

            val emptyUpdate2 = EmptyVoxelShapeUpdate(0, 0, 0, runImmediately = true, overwriteExistingVoxels = true)
            sendEmptyUpdate(physicsWorldReference, voxelBodyReference.rigidBodyId, emptyUpdate2)

            // Expect air because [emptyUpdate2] has overwriteExistingVoxels=true
            val voxelState7 = voxelBodyReference.getVoxelState(3, 4, 5).toByte()
            assertEquals(KrunchVoxelStates.AIR_STATE, voxelState7)
        } finally {
            physicsWorldReference.deletePhysicsWorldResources()
        }
    }
}
