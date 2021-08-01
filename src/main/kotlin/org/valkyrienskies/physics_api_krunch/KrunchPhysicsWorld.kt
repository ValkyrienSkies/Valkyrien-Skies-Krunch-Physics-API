package org.valkyrienskies.physics_api_krunch

import org.valkyrienskies.physics_api.PhysicsWorld
import org.valkyrienskies.physics_api.RigidBody

internal class KrunchPhysicsWorld : PhysicsWorld {
    private val rigidBodies: MutableMap<Int, RigidBody<*>> = HashMap()
    private var nextRigidBodyId = 0

    override fun addRigidBody(rigidBody: RigidBody<*>) {
        rigidBodies[rigidBody.rigidBodyId] = rigidBody
    }

    override fun createVoxelRigidBody() = KrunchVoxelRigidBody.createKrunchVoxelRigidBody(nextRigidBodyId++)

    override fun removeRigidBody(rigidBody: RigidBody<*>) {
        rigidBodies.remove(rigidBody.rigidBodyId)
    }

    override fun tick(timeStep: Double) {
        TODO("Not yet implemented")
    }
}
