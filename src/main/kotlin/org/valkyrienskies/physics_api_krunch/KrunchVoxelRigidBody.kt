package org.valkyrienskies.physics_api_krunch

import org.joml.Vector3d
import org.valkyrienskies.physics_api.RigidBodyInertiaData
import org.valkyrienskies.physics_api.RigidBodyTransform
import org.valkyrienskies.physics_api.VoxelRigidBody
import org.valkyrienskies.physics_api.VoxelShape

internal class KrunchVoxelRigidBody(
    override val collisionShape: VoxelShape,
    override val inertiaData: RigidBodyInertiaData,
    override val rigidBodyId: Int,
    override val rigidBodyTransform: RigidBodyTransform
) : VoxelRigidBody {
    // private val krunchRigidBody = Body(Pose(rigidBodyTransform.position))

    override fun addMassAt(x: Int, y: Int, z: Int, addedMass: Double) {
        TODO("Not yet implemented")
    }

    companion object {
        fun createKrunchVoxelRigidBody(rigidBodyId: Int): KrunchVoxelRigidBody {
            val collisionShape = KrunchVoxelShape()
            val inertiaData = KrunchRigidBodyInertiaData(100.0, Vector3d(1.0, 1.0, 1.0))
            val rigidBodyTransform = KrunchRigidBodyTransform.createEmptyKrunchRigidBodyTransform()
            return KrunchVoxelRigidBody(collisionShape, inertiaData, rigidBodyId, rigidBodyTransform)
        }
    }
}
