package org.valkyrienskies.physics_api_krunch;

import org.jetbrains.annotations.NotNull;
import org.valkyrienskies.physics_api.RigidBodyTransform;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class RigidBodyTransformEncoder {

    public static final int RIGID_BODY_TRANSFORM_BYTES_SIZE = 56; // 7 doubles * 8 bytes per double = 56 bytes

    @NotNull
    public static byte[] encodeRigidBodyTransform(@NotNull RigidBodyTransform rigidBodyTransform) {
        final ByteBuffer outputBuffer = ByteBuffer.allocate(RIGID_BODY_TRANSFORM_BYTES_SIZE);
        outputBuffer.order(ByteOrder.LITTLE_ENDIAN);

        // Put pos
        outputBuffer.putDouble(rigidBodyTransform.getPosition().x());
        outputBuffer.putDouble(rigidBodyTransform.getPosition().y());
        outputBuffer.putDouble(rigidBodyTransform.getPosition().z());

        // Put rot
        outputBuffer.putDouble(rigidBodyTransform.getRotation().x());
        outputBuffer.putDouble(rigidBodyTransform.getRotation().y());
        outputBuffer.putDouble(rigidBodyTransform.getRotation().z());
        outputBuffer.putDouble(rigidBodyTransform.getRotation().w());

        return outputBuffer.array();
    }

    public static RigidBodyTransform decodeRigidBodyTransform(@NotNull byte[] encoded) {
        final ByteBuffer byteBuffer = ByteBuffer.wrap(encoded);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        // Get pos
        final double posX = byteBuffer.getDouble();
        final double posY = byteBuffer.getDouble();
        final double posZ = byteBuffer.getDouble();

        // Get rot
        final double rotX = byteBuffer.getDouble();
        final double rotY = byteBuffer.getDouble();
        final double rotZ = byteBuffer.getDouble();
        final double rotW = byteBuffer.getDouble();

        return RigidBodyTransform.Companion.createRigidBodyTransform(posX, posY, posZ, rotX, rotY, rotZ, rotW);
    }

}
