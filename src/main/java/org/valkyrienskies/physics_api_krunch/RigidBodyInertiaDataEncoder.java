package org.valkyrienskies.physics_api_krunch;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3d;
import org.joml.Matrix3dc;
import org.valkyrienskies.physics_api.RigidBodyInertiaData;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class RigidBodyInertiaDataEncoder {

    public static final int RIGID_BODY_INERTIA_DATA_BYTES_SIZE = 80; // 10 doubles * 8 bytes per double = 80 bytes

    @NotNull
    public static byte[] encodeRigidBodyInertiaData(@NotNull RigidBodyInertiaData rigidBodyInertiaData) {
        final ByteBuffer outputBuffer = ByteBuffer.allocate(RIGID_BODY_INERTIA_DATA_BYTES_SIZE);
        outputBuffer.order(ByteOrder.LITTLE_ENDIAN);

        // Put mass
        outputBuffer.putDouble(rigidBodyInertiaData.getInvMass());

        // Put MOI
        outputBuffer.putDouble(rigidBodyInertiaData.getInvMOI().m00());
        outputBuffer.putDouble(rigidBodyInertiaData.getInvMOI().m10());
        outputBuffer.putDouble(rigidBodyInertiaData.getInvMOI().m20());
        outputBuffer.putDouble(rigidBodyInertiaData.getInvMOI().m01());
        outputBuffer.putDouble(rigidBodyInertiaData.getInvMOI().m11());
        outputBuffer.putDouble(rigidBodyInertiaData.getInvMOI().m21());
        outputBuffer.putDouble(rigidBodyInertiaData.getInvMOI().m02());
        outputBuffer.putDouble(rigidBodyInertiaData.getInvMOI().m12());
        outputBuffer.putDouble(rigidBodyInertiaData.getInvMOI().m22());

        return outputBuffer.array();
    }

    public static RigidBodyInertiaData decodeRigidBodyInertiaData(@NotNull byte[] encoded) {
        final ByteBuffer byteBuffer = ByteBuffer.wrap(encoded);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        // Get mass
        final double invMass = byteBuffer.getDouble();

        // Get MOI
        final double invMOI00 = byteBuffer.getDouble();
        final double invMOI10 = byteBuffer.getDouble();
        final double invMOI20 = byteBuffer.getDouble();
        final double invMOI01 = byteBuffer.getDouble();
        final double invMOI11 = byteBuffer.getDouble();
        final double invMOI21 = byteBuffer.getDouble();
        final double invMOI02 = byteBuffer.getDouble();
        final double invMOI12 = byteBuffer.getDouble();
        final double invMOI22 = byteBuffer.getDouble();

        final Matrix3dc invInertia = new Matrix3d(
                invMOI00, invMOI01, invMOI02,
                invMOI10, invMOI11, invMOI12,
                invMOI20, invMOI21, invMOI22
        );

        return new RigidBodyInertiaData(invMass, invInertia);
    }

}
