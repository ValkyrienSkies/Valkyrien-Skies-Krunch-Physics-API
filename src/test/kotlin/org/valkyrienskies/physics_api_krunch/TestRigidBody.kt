package org.valkyrienskies.physics_api_krunch

import org.joml.AxisAngle4d
import org.joml.Matrix3d
import org.joml.Quaterniond
import org.joml.Vector3d
import org.joml.Vector3i
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.valkyrienskies.physics_api.RigidBodyInertiaData
import org.valkyrienskies.physics_api.RigidBodyTransform
import kotlin.math.PI

class TestRigidBody {
    companion object {
        @BeforeAll
        @JvmStatic
        fun loadNativeBinaries() {
            KrunchBootstrap.loadNativeBinaries()
        }
    }

    @Test
    fun testDynamicFrictionCoefficient() {
        val physicsWorldReference = KrunchBootstrap.createKrunchPhysicsWorld() as KrunchNativePhysicsWorldReference
        try {
            val voxelBodyReference =
                physicsWorldReference.createVoxelRigidBody(0, Vector3i(0, 0, 0), Vector3i(15, 15, 15))
            val dynamicFrictionCoefficient = 0.8
            voxelBodyReference.dynamicFrictionCoefficient = dynamicFrictionCoefficient
            assertEquals(dynamicFrictionCoefficient, voxelBodyReference.dynamicFrictionCoefficient)
        } finally {
            physicsWorldReference.deletePhysicsWorldResources()
        }
    }

    @Test
    fun testIsStatic() {
        val physicsWorldReference = KrunchBootstrap.createKrunchPhysicsWorld() as KrunchNativePhysicsWorldReference
        try {
            val voxelBodyReference =
                physicsWorldReference.createVoxelRigidBody(0, Vector3i(0, 0, 0), Vector3i(15, 15, 15))
            voxelBodyReference.isStatic = true
            assertEquals(true, voxelBodyReference.isStatic)
            voxelBodyReference.isStatic = false
            assertEquals(false, voxelBodyReference.isStatic)
        } finally {
            physicsWorldReference.deletePhysicsWorldResources()
        }
    }

    @Test
    fun testRestitutionCoefficient() {
        val physicsWorldReference = KrunchBootstrap.createKrunchPhysicsWorld() as KrunchNativePhysicsWorldReference
        try {
            val voxelBodyReference =
                physicsWorldReference.createVoxelRigidBody(0, Vector3i(0, 0, 0), Vector3i(15, 15, 15))
            val restitutionCoefficient = 0.8
            voxelBodyReference.restitutionCoefficient = restitutionCoefficient
            assertEquals(restitutionCoefficient, voxelBodyReference.restitutionCoefficient)
        } finally {
            physicsWorldReference.deletePhysicsWorldResources()
        }
    }

    @Test
    fun testStaticFrictionCoefficient() {
        val physicsWorldReference = KrunchBootstrap.createKrunchPhysicsWorld() as KrunchNativePhysicsWorldReference
        try {
            val voxelBodyReference =
                physicsWorldReference.createVoxelRigidBody(0, Vector3i(0, 0, 0), Vector3i(15, 15, 15))
            val staticFrictionCoefficient = 0.8
            voxelBodyReference.staticFrictionCoefficient = staticFrictionCoefficient
            assertEquals(staticFrictionCoefficient, voxelBodyReference.staticFrictionCoefficient)
        } finally {
            physicsWorldReference.deletePhysicsWorldResources()
        }
    }

    @Test
    fun testCollisionShapeOffset() {
        val physicsWorldReference = KrunchBootstrap.createKrunchPhysicsWorld() as KrunchNativePhysicsWorldReference
        try {
            val voxelBodyReference =
                physicsWorldReference.createVoxelRigidBody(0, Vector3i(0, 0, 0), Vector3i(15, 15, 15))
            val collisionShapeOffset = Vector3d(1.0, 2.0, 3.0)
            voxelBodyReference.collisionShapeOffset = collisionShapeOffset
            assertEquals(collisionShapeOffset, voxelBodyReference.collisionShapeOffset)
        } finally {
            physicsWorldReference.deletePhysicsWorldResources()
        }
    }

    @Test
    fun testInertiaData() {
        val physicsWorldReference = KrunchBootstrap.createKrunchPhysicsWorld() as KrunchNativePhysicsWorldReference
        try {
            val voxelBodyReference =
                physicsWorldReference.createVoxelRigidBody(0, Vector3i(0, 0, 0), Vector3i(15, 15, 15))
            val invMOI = Matrix3d()
            invMOI.m00 = 1.0
            invMOI.m11 = 5e-1
            invMOI.m22 = 3e-1
            val inertiaData = RigidBodyInertiaData(1e-1, invMOI)
            voxelBodyReference.inertiaData = inertiaData
            assertEquals(inertiaData, voxelBodyReference.inertiaData)
        } finally {
            physicsWorldReference.deletePhysicsWorldResources()
        }
    }

    @Test
    fun testRigidBodyTransform() {
        val physicsWorldReference = KrunchBootstrap.createKrunchPhysicsWorld() as KrunchNativePhysicsWorldReference
        try {
            val voxelBodyReference =
                physicsWorldReference.createVoxelRigidBody(0, Vector3i(0, 0, 0), Vector3i(15, 15, 15))
            val rigidBodyTransform = RigidBodyTransform(Vector3d(1.0, 2.0, 3.0), Quaterniond(AxisAngle4d(PI / 3.0, 0.0, 1.0, 0.0)))
            voxelBodyReference.rigidBodyTransform = rigidBodyTransform
            assertEquals(rigidBodyTransform, voxelBodyReference.rigidBodyTransform)
        } finally {
            physicsWorldReference.deletePhysicsWorldResources()
        }
    }

    @Test
    fun testCollisionShapeScaling() {
        val physicsWorldReference = KrunchBootstrap.createKrunchPhysicsWorld() as KrunchNativePhysicsWorldReference
        try {
            val voxelBodyReference =
                physicsWorldReference.createVoxelRigidBody(0, Vector3i(0, 0, 0), Vector3i(15, 15, 15))
            val collisionShapeScaling = 2.5
            voxelBodyReference.collisionShapeScaling = collisionShapeScaling
            assertEquals(collisionShapeScaling, voxelBodyReference.collisionShapeScaling)
        } finally {
            physicsWorldReference.deletePhysicsWorldResources()
        }
    }

    @Test
    fun testHasBeenDeleted() {
        val physicsWorldReference = KrunchBootstrap.createKrunchPhysicsWorld() as KrunchNativePhysicsWorldReference
        try {
            val voxelBodyReference =
                physicsWorldReference.createVoxelRigidBody(0, Vector3i(0, 0, 0), Vector3i(15, 15, 15))
            assertEquals(false, voxelBodyReference.hasBeenDeleted())
            physicsWorldReference.deleteRigidBody(voxelBodyReference.rigidBodyId)
            assertEquals(true, voxelBodyReference.hasBeenDeleted())
        } finally {
            physicsWorldReference.deletePhysicsWorldResources()
        }
    }

    @Test
    fun testHasBeenDeleted2() {
        val physicsWorldReference = KrunchBootstrap.createKrunchPhysicsWorld() as KrunchNativePhysicsWorldReference
        try {
            val voxelBodyReference =
                physicsWorldReference.createVoxelRigidBody(0, Vector3i(0, 0, 0), Vector3i(15, 15, 15))
            assertEquals(false, voxelBodyReference.hasBeenDeleted())
            physicsWorldReference.deletePhysicsWorldResources()
            assertEquals(true, voxelBodyReference.hasBeenDeleted())
        } finally {
            physicsWorldReference.deletePhysicsWorldResources()
        }
    }

    @Test
    fun testIsVoxelTerrainFullyLoaded() {
        val physicsWorldReference = KrunchBootstrap.createKrunchPhysicsWorld() as KrunchNativePhysicsWorldReference
        try {
            val voxelBodyReference =
                physicsWorldReference.createVoxelRigidBody(0, Vector3i(0, 0, 0), Vector3i(15, 15, 15))
            assertEquals(false, voxelBodyReference.isVoxelTerrainFullyLoaded)
            voxelBodyReference.isVoxelTerrainFullyLoaded = true
            assertEquals(true, voxelBodyReference.isVoxelTerrainFullyLoaded)
            voxelBodyReference.isVoxelTerrainFullyLoaded = false
            assertEquals(false, voxelBodyReference.isVoxelTerrainFullyLoaded)
        } finally {
            physicsWorldReference.deletePhysicsWorldResources()
        }
    }
}
