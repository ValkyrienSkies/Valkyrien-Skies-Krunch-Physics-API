package org.valkyrienskies.physics_api_krunch

import org.valkyrienskies.physics_api.PhysicsWorld
import org.valkyrienskies.physics_api.RigidBody

internal class KrunchPhysicsWorld : PhysicsWorld {
    private val rigidBodies: MutableMap<Int, RigidBody<*>> = HashMap()
    private var nextRigidBodyId = 0
    private val krunchPhysicsWorld = org.valkyrienskies.krunch.PhysicsWorld()

    override fun addRigidBody(rigidBody: RigidBody<*>) {
        rigidBody as KrunchVoxelRigidBody // Only support [KrunchVoxelRigidBody] for now
        rigidBodies[rigidBody.rigidBodyId] = rigidBody
        krunchPhysicsWorld.bodies.add(rigidBody.krunchRigidBody)
    }

    override fun createVoxelRigidBody() = KrunchVoxelRigidBody.createKrunchVoxelRigidBody(nextRigidBodyId++)

    override fun removeRigidBody(rigidBody: RigidBody<*>) {
        TODO("Not yet implemented")
    }

    override fun tick(timeStep: Double) {
        krunchPhysicsWorld.simulate(timeStep)
    }
}
