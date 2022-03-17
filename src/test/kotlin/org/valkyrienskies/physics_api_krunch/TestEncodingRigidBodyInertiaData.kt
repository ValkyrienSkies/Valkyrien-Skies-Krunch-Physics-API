package org.valkyrienskies.physics_api_krunch

import org.joml.Vector3d
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.valkyrienskies.physics_api.RigidBodyInertiaData

class TestEncodingRigidBodyInertiaData {

    @Test
    fun testEncodingAndDecoding() {
        val inertiaData = RigidBodyInertiaData(20.0, Vector3d(2.0, 3.0, 4.0))
        val encodedToBytes = RigidBodyInertiaDataEncoder.encodeRigidBodyInertiaData(inertiaData)
        val decoded = RigidBodyInertiaDataEncoder.decodeRigidBodyInertiaData(encodedToBytes)
        assertEquals(inertiaData, decoded)
    }

    @Test
    fun testEncodingAndDecoding2() {
        val inertiaData = RigidBodyInertiaData(500.0, Vector3d(10.0, 2.0, 5.0))
        val encodedToBytes = RigidBodyInertiaDataEncoder.encodeRigidBodyInertiaData(inertiaData)
        val decoded = RigidBodyInertiaDataEncoder.decodeRigidBodyInertiaData(encodedToBytes)
        assertEquals(inertiaData, decoded)
    }

}
