package org.valkyrienskies.physics_api_krunch

import org.joml.AxisAngle4d
import org.joml.Matrix3d
import org.joml.Quaterniond
import org.joml.Vector3d
import org.joml.Vector3dc
import org.joml.Vector3i
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.valkyrienskies.physics_api.RigidBodyInertiaData
import org.valkyrienskies.physics_api.RigidBodyTransform
import org.valkyrienskies.physics_api_krunch.KrunchTestUtils.assertVecNearlyEquals
import org.valkyrienskies.physics_api_krunch.KrunchTestUtils.generateUnitInertiaData
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
            val rigidBodyTransform =
                RigidBodyTransform(Vector3d(1.0, 2.0, 3.0), Quaterniond(AxisAngle4d(PI / 3.0, 0.0, 1.0, 0.0)))
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

    @Test
    fun testAddRotDependentTorqueToNextPhysTick() {
        val physicsWorldReference = KrunchBootstrap.createKrunchPhysicsWorld() as KrunchNativePhysicsWorldReference
        try {
            val voxelBodyReference =
                physicsWorldReference.createVoxelRigidBody(0, Vector3i(0, 0, 0), Vector3i(15, 15, 15))
            voxelBodyReference.inertiaData = generateUnitInertiaData()
            // Set fully loaded to allow this body to move
            voxelBodyReference.isVoxelTerrainFullyLoaded = true

            assertEquals(Vector3d(), voxelBodyReference.totalRotDependentTorqueNextPhysTick)
            voxelBodyReference.addRotDependentTorqueToNextPhysTick(Vector3d(1.0, 0.0, 0.0))
            assertEquals(Vector3d(1.0, 0.0, 0.0), voxelBodyReference.totalRotDependentTorqueNextPhysTick)
            voxelBodyReference.addRotDependentTorqueToNextPhysTick(Vector3d(1.0, 1.0, 0.0))
            assertEquals(Vector3d(2.0, 1.0, 0.0), voxelBodyReference.totalRotDependentTorqueNextPhysTick)
            voxelBodyReference.addRotDependentTorqueToNextPhysTick(Vector3d(1.0, 1.0, 1.0))
            assertEquals(Vector3d(3.0, 2.0, 1.0), voxelBodyReference.totalRotDependentTorqueNextPhysTick)
            voxelBodyReference.addRotDependentTorqueToNextPhysTick(Vector3d(0.5, 0.5, 0.5))
            assertEquals(Vector3d(3.5, 2.5, 1.5), voxelBodyReference.totalRotDependentTorqueNextPhysTick)

            // Assert that [voxelBodyReference.totalRotDependentTorqueNextPhysTick] is reset after the physics tick
            physicsWorldReference.tick(Vector3d(), 1.0, true)
            assertEquals(Vector3d(), voxelBodyReference.totalRotDependentTorqueNextPhysTick)
        } finally {
            physicsWorldReference.deletePhysicsWorldResources()
        }
    }

    @Test
    fun testAddInvariantTorqueToNextPhysTick() {
        val physicsWorldReference = KrunchBootstrap.createKrunchPhysicsWorld() as KrunchNativePhysicsWorldReference
        try {
            val voxelBodyReference =
                physicsWorldReference.createVoxelRigidBody(0, Vector3i(0, 0, 0), Vector3i(15, 15, 15))
            voxelBodyReference.inertiaData = generateUnitInertiaData()
            // Set fully loaded to allow this body to move
            voxelBodyReference.isVoxelTerrainFullyLoaded = true

            assertEquals(Vector3d(), voxelBodyReference.totalInvariantTorqueNextPhysTick)
            voxelBodyReference.addInvariantTorqueToNextPhysTick(Vector3d(1.0, 0.0, 0.0))
            assertEquals(Vector3d(1.0, 0.0, 0.0), voxelBodyReference.totalInvariantTorqueNextPhysTick)
            voxelBodyReference.addInvariantTorqueToNextPhysTick(Vector3d(1.0, 1.0, 0.0))
            assertEquals(Vector3d(2.0, 1.0, 0.0), voxelBodyReference.totalInvariantTorqueNextPhysTick)
            voxelBodyReference.addInvariantTorqueToNextPhysTick(Vector3d(1.0, 1.0, 1.0))
            assertEquals(Vector3d(3.0, 2.0, 1.0), voxelBodyReference.totalInvariantTorqueNextPhysTick)
            voxelBodyReference.addInvariantTorqueToNextPhysTick(Vector3d(0.5, 0.5, 0.5))
            assertEquals(Vector3d(3.5, 2.5, 1.5), voxelBodyReference.totalInvariantTorqueNextPhysTick)

            // Assert that [voxelBodyReference.totalInvariantTorqueNextPhysTick] is reset after the physics tick
            physicsWorldReference.tick(Vector3d(), 1.0, true)
            assertEquals(Vector3d(), voxelBodyReference.totalInvariantTorqueNextPhysTick)
        } finally {
            physicsWorldReference.deletePhysicsWorldResources()
        }
    }

    @Test
    fun testAddRotDependentForceToNextPhysTick() {
        val physicsWorldReference = KrunchBootstrap.createKrunchPhysicsWorld() as KrunchNativePhysicsWorldReference
        try {
            val voxelBodyReference =
                physicsWorldReference.createVoxelRigidBody(0, Vector3i(0, 0, 0), Vector3i(15, 15, 15))
            voxelBodyReference.inertiaData = generateUnitInertiaData()
            // Set fully loaded to allow this body to move
            voxelBodyReference.isVoxelTerrainFullyLoaded = true

            assertEquals(Vector3d(), voxelBodyReference.totalRotDependentForceNextPhysTick)
            voxelBodyReference.addRotDependentForceToNextPhysTick(Vector3d(1.0, 0.0, 0.0))
            assertEquals(Vector3d(1.0, 0.0, 0.0), voxelBodyReference.totalRotDependentForceNextPhysTick)
            voxelBodyReference.addRotDependentForceToNextPhysTick(Vector3d(1.0, 1.0, 0.0))
            assertEquals(Vector3d(2.0, 1.0, 0.0), voxelBodyReference.totalRotDependentForceNextPhysTick)
            voxelBodyReference.addRotDependentForceToNextPhysTick(Vector3d(1.0, 1.0, 1.0))
            assertEquals(Vector3d(3.0, 2.0, 1.0), voxelBodyReference.totalRotDependentForceNextPhysTick)
            voxelBodyReference.addRotDependentForceToNextPhysTick(Vector3d(0.5, 0.5, 0.5))
            assertEquals(Vector3d(3.5, 2.5, 1.5), voxelBodyReference.totalRotDependentForceNextPhysTick)

            // Assert that [voxelBodyReference.totalRotDependentForceNextPhysTick] is reset after the physics tick
            physicsWorldReference.tick(Vector3d(), 1.0, true)
            assertEquals(Vector3d(), voxelBodyReference.totalRotDependentForceNextPhysTick)
        } finally {
            physicsWorldReference.deletePhysicsWorldResources()
        }
    }

    @Test
    fun testAddInvariantForceToNextPhysTick() {
        val physicsWorldReference = KrunchBootstrap.createKrunchPhysicsWorld() as KrunchNativePhysicsWorldReference
        try {
            val voxelBodyReference =
                physicsWorldReference.createVoxelRigidBody(0, Vector3i(0, 0, 0), Vector3i(15, 15, 15))
            voxelBodyReference.inertiaData = generateUnitInertiaData()
            // Set fully loaded to allow this body to move
            voxelBodyReference.isVoxelTerrainFullyLoaded = true

            assertEquals(Vector3d(), voxelBodyReference.totalInvariantForceNextPhysTick)
            voxelBodyReference.addInvariantForceToNextPhysTick(Vector3d(1.0, 0.0, 0.0))
            assertEquals(Vector3d(1.0, 0.0, 0.0), voxelBodyReference.totalInvariantForceNextPhysTick)
            voxelBodyReference.addInvariantForceToNextPhysTick(Vector3d(1.0, 1.0, 0.0))
            assertEquals(Vector3d(2.0, 1.0, 0.0), voxelBodyReference.totalInvariantForceNextPhysTick)
            voxelBodyReference.addInvariantForceToNextPhysTick(Vector3d(1.0, 1.0, 1.0))
            assertEquals(Vector3d(3.0, 2.0, 1.0), voxelBodyReference.totalInvariantForceNextPhysTick)
            voxelBodyReference.addInvariantForceToNextPhysTick(Vector3d(0.5, 0.5, 0.5))
            assertEquals(Vector3d(3.5, 2.5, 1.5), voxelBodyReference.totalInvariantForceNextPhysTick)

            // Assert that [voxelBodyReference.totalInvariantForceNextPhysTick] is reset after the physics tick
            physicsWorldReference.tick(Vector3d(), 1.0, true)
            assertEquals(Vector3d(), voxelBodyReference.totalInvariantForceNextPhysTick)
        } finally {
            physicsWorldReference.deletePhysicsWorldResources()
        }
    }

    @Test
    fun testAddInvariantForceAtPosToNextPhysTick() {
        val physicsWorldReference = KrunchBootstrap.createKrunchPhysicsWorld() as KrunchNativePhysicsWorldReference
        try {
            val voxelBodyReference =
                physicsWorldReference.createVoxelRigidBody(0, Vector3i(0, 0, 0), Vector3i(15, 15, 15))
            voxelBodyReference.inertiaData = generateUnitInertiaData()
            // Set fully loaded to allow this body to move
            voxelBodyReference.isVoxelTerrainFullyLoaded = true

            val invariantForces0 = voxelBodyReference.invariantForcesAtPosNextPhysTick
            assertTrue(invariantForces0.isEmpty())

            val addedInvariantForces = ArrayList<Pair<Vector3dc, Vector3dc>>()

            addedInvariantForces.add(Pair(Vector3d(1.0, 0.0, 0.0), Vector3d(1.0, 2.0, 3.0)))
            voxelBodyReference.addInvariantForceAtPosToNextPhysTick(
                addedInvariantForces[0].first,
                addedInvariantForces[0].second
            )
            assertEquals(addedInvariantForces, voxelBodyReference.invariantForcesAtPosNextPhysTick)

            addedInvariantForces.add(Pair(Vector3d(-2.0, 5.0, 10.0), Vector3d(6.0, 2.0, 0.0)))
            voxelBodyReference.addInvariantForceAtPosToNextPhysTick(
                addedInvariantForces[1].first,
                addedInvariantForces[1].second
            )
            assertEquals(addedInvariantForces, voxelBodyReference.invariantForcesAtPosNextPhysTick)

            // Assert that [voxelBodyReference.totalInvariantForceNextPhysTick] is reset after the physics tick
            physicsWorldReference.tick(Vector3d(), 1.0, true)
            val invariantForces5 = voxelBodyReference.invariantForcesAtPosNextPhysTick
            assertTrue(invariantForces5.isEmpty())
        } finally {
            physicsWorldReference.deletePhysicsWorldResources()
        }
    }

    @Test
    fun testInvariantForcesAddedToNextPhysTick() {
        val physicsWorldReference = KrunchBootstrap.createKrunchPhysicsWorld() as KrunchNativePhysicsWorldReference
        try {
            val voxelBodyReference =
                physicsWorldReference.createVoxelRigidBody(0, Vector3i(0, 0, 0), Vector3i(15, 15, 15))
            voxelBodyReference.inertiaData = generateUnitInertiaData()
            // Set fully loaded to allow this body to move
            voxelBodyReference.isVoxelTerrainFullyLoaded = true

            assertEquals(Vector3d(), voxelBodyReference.totalInvariantForceNextPhysTick)
            voxelBodyReference.addInvariantForceToNextPhysTick(Vector3d(1.0, 0.0, 0.0))
            assertEquals(Vector3d(1.0, 0.0, 0.0), voxelBodyReference.totalInvariantForceNextPhysTick)
            voxelBodyReference.addInvariantForceToNextPhysTick(Vector3d(1.0, 1.0, 0.0))
            assertEquals(Vector3d(2.0, 1.0, 0.0), voxelBodyReference.totalInvariantForceNextPhysTick)
            voxelBodyReference.addInvariantForceToNextPhysTick(Vector3d(1.0, 1.0, 1.0))
            assertEquals(Vector3d(3.0, 2.0, 1.0), voxelBodyReference.totalInvariantForceNextPhysTick)
            voxelBodyReference.addInvariantForceToNextPhysTick(Vector3d(0.5, 0.5, 0.5))
            assertEquals(Vector3d(3.5, 2.5, 1.5), voxelBodyReference.totalInvariantForceNextPhysTick)

            // Assert that [voxelBodyReference.totalInvariantForceNextPhysTick] is reset after the physics tick
            physicsWorldReference.tick(Vector3d(), 1.0, true)
            assertEquals(Vector3d(), voxelBodyReference.totalInvariantForceNextPhysTick)

            // Assert that the rigid body was moved by the appropriate amount
            assertVecNearlyEquals(Vector3d(3.5, 2.5, 1.5), voxelBodyReference.velocity)
        } finally {
            physicsWorldReference.deletePhysicsWorldResources()
        }
    }
}
