package org.valkyrienskies.physics_api_krunch

import org.joml.AxisAngle4d
import org.joml.Quaterniond
import org.joml.Vector3d
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.valkyrienskies.physics_api.RigidBodyTransform
import kotlin.math.PI

class TestEncodingRigidBodyTransform {

    @Test
    fun testEncodingAndDecoding() {
        val transform = RigidBodyTransform(Vector3d(10.0, 11.0, 12.0), Quaterniond(AxisAngle4d(PI / 3.0, 1.0, 0.0, 0.0)))
        val encodedToBytes = RigidBodyTransformEncoder.encodeRigidBodyTransform(transform)
        val decoded = RigidBodyTransformEncoder.decodeRigidBodyTransform(encodedToBytes)
        assertEquals(transform, decoded)
    }

    @Test
    fun testEncodingAndDecoding2() {
        val transform = RigidBodyTransform(Vector3d(-10.0, 2000.0, 1.0), Quaterniond(AxisAngle4d(-PI / 2.0, 2.0, 1.0, 3.0)))
        val encodedToBytes = RigidBodyTransformEncoder.encodeRigidBodyTransform(transform)
        val decoded = RigidBodyTransformEncoder.decodeRigidBodyTransform(encodedToBytes)
        assertEquals(transform, decoded)
    }

}
