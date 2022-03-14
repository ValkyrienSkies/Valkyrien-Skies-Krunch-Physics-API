package org.valkyrienskies.physics_api_krunch;

import org.jetbrains.annotations.NotNull;
import org.valkyrienskies.physics_api.RigidBodyInertiaData;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class RigidBodyInertiaDataEncoder {

    public static final int RIGID_BODY_INERTIA_DATA_BYTES_SIZE = 32; // 4 doubles * 8 bytes per double = 32 bytes

    @NotNull
    public static byte[] encodeRigidBodyInertiaData(@NotNull RigidBodyInertiaData rigidBodyInertiaData) {
        final ByteBuffer outputBuffer = ByteBuffer.allocate(RIGID_BODY_INERTIA_DATA_BYTES_SIZE);
        outputBuffer.order(ByteOrder.LITTLE_ENDIAN);

        // Put mass
        outputBuffer.putDouble(rigidBodyInertiaData.getMass());

        // Put MOI
        outputBuffer.putDouble(rigidBodyInertiaData.getMomentOfInertia().x());
        outputBuffer.putDouble(rigidBodyInertiaData.getMomentOfInertia().y());
        outputBuffer.putDouble(rigidBodyInertiaData.getMomentOfInertia().z());

        return outputBuffer.array();
    }

    public static RigidBodyInertiaData decodeRigidBodyInertiaData(@NotNull byte[] encoded) {
        final ByteBuffer byteBuffer = ByteBuffer.wrap(encoded);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        // Get mass
        final double mass = byteBuffer.getDouble();

        // Get MOI
        final double moiX = byteBuffer.getDouble();
        final double moiY = byteBuffer.getDouble();
        final double moiZ = byteBuffer.getDouble();

        return RigidBodyInertiaData.Companion.createRigidBodyInertiaData(mass, moiX, moiY, moiZ);
    }

}
