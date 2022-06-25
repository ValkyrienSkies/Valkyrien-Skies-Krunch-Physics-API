package org.valkyrienskies.physics_api_krunch

import org.joml.Matrix3d
import org.joml.Vector3d
import org.joml.Vector3dc
import org.joml.Vector3ic
import org.junit.jupiter.api.Assertions
import org.valkyrienskies.physics_api.PhysicsWorldReference
import org.valkyrienskies.physics_api.RigidBodyInertiaData
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

    internal fun setBlock(physicsWorldReference: PhysicsWorldReference, rigidBodyId: Int, pos: Vector3ic, voxelState: Byte) {
        val sparseUpdate = SparseVoxelShapeUpdate(pos.x() shr 4, pos.y() shr 4, pos.z() shr 4, true)
        sparseUpdate.addUpdate(pos.x() and 15, pos.y() and 15, pos.z() and 15, voxelState)
        val voxelShapeUpdates = VoxelRigidBodyShapeUpdates(rigidBodyId, arrayOf(sparseUpdate))
        physicsWorldReference.queueVoxelShapeUpdates(arrayOf(voxelShapeUpdates))
        // Tick the physics world to apply the queued voxel shape updates
        physicsWorldReference.tick(Vector3d(), 1.0, false)
    }

    /**
     * Generate a [RigidBodyInertiaData] with an invMass of 1.0, and an inertia tensor of identity.
     */
    internal fun generateUnitInertiaData(): RigidBodyInertiaData {
        return RigidBodyInertiaData(1.0, Matrix3d())
    }

    /**
     * Asserts that two [Vector3dc] are *nearly* equal, within [epsilon] precision.
     */
    internal fun assertVecNearlyEquals(expected: Vector3dc, actual: Vector3dc, epsilon: Double = 1e-8) {
        Assertions.assertEquals(expected.x(), actual.x(), epsilon)
        Assertions.assertEquals(expected.y(), actual.y(), epsilon)
        Assertions.assertEquals(expected.z(), actual.z(), epsilon)
    }
}