package org.valkyrienskies.physics_api_krunch

import org.joml.Matrix3d
import org.joml.Matrix3dc
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.valkyrienskies.physics_api.RigidBodyInertiaData

class TestEncodingRigidBodyInertiaData {

    @Test
    fun testEncodingAndDecoding() {
        val invMoi: Matrix3dc = Matrix3d(
            5e-1, 7e-2, 6e2,
            2e-2, 3e-1, 0.0,
            4e-3, 0.0, 2.5e-1
        )
        val inertiaData = RigidBodyInertiaData(5e-2, invMoi)
        val encodedToBytes = RigidBodyInertiaDataEncoder.encodeRigidBodyInertiaData(inertiaData)
        val decoded = RigidBodyInertiaDataEncoder.decodeRigidBodyInertiaData(encodedToBytes)
        assertEquals(inertiaData, decoded)
    }

    @Test
    fun testEncodingAndDecoding2() {
        val invMoi: Matrix3dc = Matrix3d(
            1e-1, 0.0, 0.0,
            0.0, 5e-1, 0.0,
            0.0, 0.0, 2e-1
        )
        val inertiaData = RigidBodyInertiaData(2e-3, invMoi)
        val encodedToBytes = RigidBodyInertiaDataEncoder.encodeRigidBodyInertiaData(inertiaData)
        val decoded = RigidBodyInertiaDataEncoder.decodeRigidBodyInertiaData(encodedToBytes)
        assertEquals(inertiaData, decoded)
    }

}
