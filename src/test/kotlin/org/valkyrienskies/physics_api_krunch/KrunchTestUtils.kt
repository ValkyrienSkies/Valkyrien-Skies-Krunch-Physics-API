package org.valkyrienskies.physics_api_krunch

import org.joml.Vector3d
import org.valkyrienskies.physics_api.PhysicsWorldReference
import org.valkyrienskies.physics_api.voxel_updates.DeleteVoxelShapeUpdate
import org.valkyrienskies.physics_api.voxel_updates.DenseVoxelShapeUpdate
import org.valkyrienskies.physics_api.voxel_updates.EmptyVoxelShapeUpdate
import org.valkyrienskies.physics_api.voxel_updates.SparseVoxelShapeUpdate
import org.valkyrienskies.physics_api.voxel_updates.VoxelRigidBodyShapeUpdates

internal object KrunchTestUtils {
    internal fun sendEmptyUpdate(physicsWorldReference: PhysicsWorldReference, rigidBodyId: Int, emptyUpdate: EmptyVoxelShapeUpdate) {
        val voxelShapeUpdates = VoxelRigidBodyShapeUpdates(rigidBodyId, arrayOf(emptyUpdate))
        physicsWorldReference.queueVoxelShapeUpdates(arrayOf(voxelShapeUpdates))
        // Tick the physics world to apply the queued voxel shape updates
        physicsWorldReference.tick(Vector3d(), 1.0, false)
    }

    internal fun sendDenseUpdate(physicsWorldReference: PhysicsWorldReference, rigidBodyId: Int, denseUpdate: DenseVoxelShapeUpdate) {
        val voxelShapeUpdates = VoxelRigidBodyShapeUpdates(rigidBodyId, arrayOf(denseUpdate))
        physicsWorldReference.queueVoxelShapeUpdates(arrayOf(voxelShapeUpdates))
        // Tick the physics world to apply the queued voxel shape updates
        physicsWorldReference.tick(Vector3d(), 1.0, false)
    }

    internal fun sendSparseUpdate(physicsWorldReference: PhysicsWorldReference, rigidBodyId: Int, sparseUpdate: SparseVoxelShapeUpdate) {
        val voxelShapeUpdates = VoxelRigidBodyShapeUpdates(rigidBodyId, arrayOf(sparseUpdate))
        physicsWorldReference.queueVoxelShapeUpdates(arrayOf(voxelShapeUpdates))
        // Tick the physics world to apply the queued voxel shape updates
        physicsWorldReference.tick(Vector3d(), 1.0, false)
    }

    internal fun sendDeleteUpdate(physicsWorldReference: PhysicsWorldReference, rigidBodyId: Int, deleteUpdate: DeleteVoxelShapeUpdate) {
        val voxelShapeUpdates = VoxelRigidBodyShapeUpdates(rigidBodyId, arrayOf(deleteUpdate))
        physicsWorldReference.queueVoxelShapeUpdates(arrayOf(voxelShapeUpdates))
        // Tick the physics world to apply the queued voxel shape updates
        physicsWorldReference.tick(Vector3d(), 1.0, false)
    }
}