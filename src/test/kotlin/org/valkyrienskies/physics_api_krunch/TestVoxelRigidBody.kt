package org.valkyrienskies.physics_api_krunch

import org.joml.Vector3d
import org.joml.Vector3i
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.valkyrienskies.physics_api.voxel_updates.EmptyVoxelShapeUpdate
import org.valkyrienskies.physics_api.voxel_updates.KrunchVoxelStates
import org.valkyrienskies.physics_api.voxel_updates.VoxelRigidBodyShapeUpdates
import org.valkyrienskies.physics_api_krunch.KrunchNativeRigidBodyReference.VOXEL_STATE_UNLOADED

class TestVoxelRigidBody {
    companion object {
        @BeforeAll
        @JvmStatic
        fun loadNativeBinaries() {
            KrunchBootstrap.loadNativeBinaries()
        }
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
            val voxelShapeUpdates = VoxelRigidBodyShapeUpdates(voxelBodyReference.rigidBodyId, arrayOf(emptyUpdate))
            physicsWorldReference.queueVoxelShapeUpdates(arrayOf(voxelShapeUpdates))
            // Tick the physics world to apply the queued voxel shape updates
            physicsWorldReference.tick(Vector3d(), 1.0, false)

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
            val voxelShapeUpdates = VoxelRigidBodyShapeUpdates(voxelBodyReference.rigidBodyId, arrayOf(emptyUpdate))
            physicsWorldReference.queueVoxelShapeUpdates(arrayOf(voxelShapeUpdates))
            // Tick the physics world to apply the queued voxel shape updates
            physicsWorldReference.tick(Vector3d(), 1.0, false)

            // We expect still [VOXEL_STATE_UNLOADED] because we haven't defined the region
            val voxelState3 = voxelBodyReference.getVoxelState(18, 0, -5)
            assertEquals(VOXEL_STATE_UNLOADED, voxelState3)

            // Send the empty update to "load" the terrain the chunk at (0, 0, 0)
            val emptyUpdate2 = EmptyVoxelShapeUpdate(1, 0, -1, runImmediately = true, overwriteExistingVoxels = true)
            val voxelShapeUpdates2 = VoxelRigidBodyShapeUpdates(voxelBodyReference.rigidBodyId, arrayOf(emptyUpdate2))
            physicsWorldReference.queueVoxelShapeUpdates(arrayOf(voxelShapeUpdates2))
            // Tick the physics world to apply the queued voxel shape updates
            physicsWorldReference.tick(Vector3d(), 1.0, false)

            // We expect [KrunchVoxelStates.AIR_STATE] because we sent the empty update, which loaded the terrain
            val voxelState4 = voxelBodyReference.getVoxelState(18, 0, -5).toByte()
            assertEquals(KrunchVoxelStates.AIR_STATE, voxelState4)
        } finally {
            physicsWorldReference.deletePhysicsWorldResources()
        }
    }
}
