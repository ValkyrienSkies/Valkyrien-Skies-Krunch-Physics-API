package org.valkyrienskies.physics_api_krunch

import org.joml.Vector3d
import org.valkyrienskies.krunch.Body
import org.valkyrienskies.krunch.Pose
import org.valkyrienskies.physics_api.VoxelRigidBody
import org.valkyrienskies.physics_api.VoxelShape

internal class KrunchVoxelRigidBody(
    override val collisionShape: VoxelShape,
    override val inertiaData: KrunchRigidBodyInertiaData,
    override val rigidBodyId: Int,
    override val rigidBodyTransform: KrunchRigidBodyTransform
) : VoxelRigidBody {
    internal val krunchRigidBody = Body(Pose(rigidBodyTransform.position, rigidBodyTransform.rotation))

    override var isStatic: Boolean
        get() = krunchRigidBody.isStatic
        set(value) { krunchRigidBody.isStatic = value }

    override fun addMassAt(x: Int, y: Int, z: Int, addedMass: Double) {
        inertiaData.mass += addedMass
        // TODO: Change moment of inertia tensor
    }

    companion object {
        fun createKrunchVoxelRigidBody(rigidBodyId: Int): KrunchVoxelRigidBody {
            val collisionShape = KrunchVoxelShape()
            val inertiaData = KrunchRigidBodyInertiaData(0.0, Vector3d(1.0, 1.0, 1.0))
            val rigidBodyTransform = KrunchRigidBodyTransform.createEmptyKrunchRigidBodyTransform()
            return KrunchVoxelRigidBody(collisionShape, inertiaData, rigidBodyId, rigidBodyTransform)
        }
    }
}
