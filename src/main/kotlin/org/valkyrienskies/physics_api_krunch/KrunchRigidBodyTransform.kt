package org.valkyrienskies.physics_api_krunch

import org.joml.Quaterniond
import org.joml.Vector3d
import org.valkyrienskies.physics_api.RigidBodyTransform

internal data class KrunchRigidBodyTransform(override val position: Vector3d, override val rotation: Quaterniond) : RigidBodyTransform {
    companion object {
        fun createEmptyKrunchRigidBodyTransform() = KrunchRigidBodyTransform(Vector3d(), Quaterniond())
    }
}
