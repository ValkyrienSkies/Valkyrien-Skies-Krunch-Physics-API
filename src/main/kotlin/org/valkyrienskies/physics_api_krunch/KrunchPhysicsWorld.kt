package org.valkyrienskies.physics_api_krunch

import org.joml.Vector3dc
import org.valkyrienskies.physics_api.PhysicsWorld
import org.valkyrienskies.physics_api.RigidBody
import org.valkyrienskies.physics_api.voxel_updates.VoxelRigidBodyShapeUpdates
import org.valkyrienskies.physics_api.voxel_updates.VoxelShapeUpdateIterator
import java.util.concurrent.ConcurrentLinkedQueue

internal class KrunchPhysicsWorld : PhysicsWorld {
    private val rigidBodies: MutableMap<Int, RigidBody<*>> = HashMap()
    private var nextRigidBodyId = 0
    private val krunchPhysicsWorld = org.valkyrienskies.krunch.PhysicsWorld()

    private val updatesQueue = ConcurrentLinkedQueue<List<VoxelRigidBodyShapeUpdates>>()

    override fun addRigidBody(rigidBody: RigidBody<*>) {
        rigidBody as KrunchVoxelRigidBody // Only support [KrunchVoxelRigidBody] for now
        rigidBodies[rigidBody.rigidBodyId] = rigidBody
        krunchPhysicsWorld.bodies.add(rigidBody.krunchRigidBody)
    }

    override fun createVoxelRigidBody() = KrunchVoxelRigidBody.createKrunchVoxelRigidBody(nextRigidBodyId++)

    override fun queueVoxelShapeUpdates(updates: List<VoxelRigidBodyShapeUpdates>) {
        updatesQueue.add(updates)
    }

    override fun removeRigidBody(rigidBody: RigidBody<*>) {
        TODO("Not yet implemented")
    }

    override fun tick(gravity: Vector3dc, timeStep: Double) {
        // For now, just run all the updates on the tick
        // In the future we want to run these in the background.
        while (updatesQueue.isNotEmpty()) {
            val updates = updatesQueue.remove()
            updates.forEach { update ->
                val rigidBody = update.voxelRigidBody
                val shapeUpdates = update.shapeUpdates
                shapeUpdates.forEach { shapeUpdate ->
                    val chunkBasePosX = shapeUpdate.regionX shl 4
                    val chunkBasePosY = shapeUpdate.regionY shl 4
                    val chunkBasePosZ = shapeUpdate.regionZ shl 4
                    VoxelShapeUpdateIterator.forEachVoxel(shapeUpdate) { x: Int, y: Int, z: Int, setVoxel: Boolean ->
                        val posX = x + chunkBasePosX
                        val posY = y + chunkBasePosY
                        val posZ = z + chunkBasePosZ
                        if (setVoxel) {
                            rigidBody.collisionShape.addVoxel(posX, posY, posZ)
                        } else {
                            rigidBody.collisionShape.removeVoxel(posX, posY, posZ)
                        }
                    }
                }
            }
        }

        krunchPhysicsWorld.simulate(gravity, timeStep)
    }

    internal fun setSettings(settings: KrunchPhysicsWorldSettings) {
        krunchPhysicsWorld.settings = settings
    }
}
