package org.valkyrienskies.physics_api_krunch

import org.joml.Quaterniondc
import org.joml.Vector3d
import org.joml.Vector3dc
import org.valkyrienskies.krunch.Body
import org.valkyrienskies.krunch.Pose
import org.valkyrienskies.physics_api.RigidBodyTransform
import org.valkyrienskies.physics_api.VoxelRigidBody
import org.valkyrienskies.physics_api.VoxelShape

internal class KrunchVoxelRigidBody(
    override val collisionShape: VoxelShape,
    override val inertiaData: KrunchRigidBodyInertiaData,
    override val rigidBodyId: Int,
    rigidBodyTransform: KrunchRigidBodyTransform
) : VoxelRigidBody {
    internal val krunchRigidBody = Body(Pose(rigidBodyTransform.position, rigidBodyTransform.rotation))
    override val rigidBodyTransform: RigidBodyTransform =
        KrunchRigidBodyTransform(krunchRigidBody.pose.p, krunchRigidBody.pose.q)

    init {
        krunchRigidBody.isStatic = false
        krunchRigidBody.dynamicFrictionCoefficient = .5
        krunchRigidBody.staticFrictionCoefficient = 1.0
        krunchRigidBody.coefficientOfRestitution = .5
        krunchRigidBody.shape = (collisionShape as KrunchVoxelShape).krunchShape
    }

    override var isStatic: Boolean
        get() = krunchRigidBody.isStatic
        set(value) { krunchRigidBody.isStatic = value }
    override var dynamicFrictionCoefficient: Double
        get() = krunchRigidBody.dynamicFrictionCoefficient
        set(value) { krunchRigidBody.dynamicFrictionCoefficient = value }
    override var staticFrictionCoefficient: Double
        get() = krunchRigidBody.staticFrictionCoefficient
        set(value) { krunchRigidBody.staticFrictionCoefficient = value }
    override var restitutionCoefficient: Double
        get() = krunchRigidBody.coefficientOfRestitution
        set(value) { krunchRigidBody.coefficientOfRestitution = value }

    override fun setRigidBodyTransform(position: Vector3dc, rotation: Quaterniondc) {
        krunchRigidBody.pose.p.set(position)
        krunchRigidBody.pose.q.set(rotation)
        krunchRigidBody.prevPose.p.set(position)
        krunchRigidBody.prevPose.q.set(rotation)
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
