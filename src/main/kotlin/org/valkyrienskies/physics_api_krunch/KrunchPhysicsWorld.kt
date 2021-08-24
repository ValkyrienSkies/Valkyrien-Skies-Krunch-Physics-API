package org.valkyrienskies.physics_api_krunch

import org.joml.Vector3dc
import org.valkyrienskies.physics_api.PhysicsWorld
import org.valkyrienskies.physics_api.RigidBody

internal class KrunchPhysicsWorld : PhysicsWorld {
    private val rigidBodies: MutableMap<Int, RigidBody<*>> = HashMap()
    private var nextRigidBodyId = 0
    private val krunchPhysicsWorld = org.valkyrienskies.krunch.PhysicsWorld()
    var subSteps: Int = 40

    override fun addRigidBody(rigidBody: RigidBody<*>) {
        rigidBody as KrunchVoxelRigidBody // Only support [KrunchVoxelRigidBody] for now
        rigidBodies[rigidBody.rigidBodyId] = rigidBody
        krunchPhysicsWorld.bodies.add(rigidBody.krunchRigidBody)
    }

    override fun createVoxelRigidBody() = KrunchVoxelRigidBody.createKrunchVoxelRigidBody(nextRigidBodyId++)

    override fun removeRigidBody(rigidBody: RigidBody<*>) {
        TODO("Not yet implemented")
    }

    override fun tick(gravity: Vector3dc, timeStep: Double) {
        krunchPhysicsWorld.simulate(gravity, subSteps, timeStep)
    }
}
