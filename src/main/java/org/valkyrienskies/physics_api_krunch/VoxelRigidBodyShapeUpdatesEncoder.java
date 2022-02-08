package org.valkyrienskies.physics_api_krunch;

import org.jetbrains.annotations.NotNull;
import org.valkyrienskies.physics_api.voxel_updates.DenseVoxelShapeUpdate;
import org.valkyrienskies.physics_api.voxel_updates.EmptyVoxelShapeUpdate;
import org.valkyrienskies.physics_api.voxel_updates.IVoxelShapeUpdate;
import org.valkyrienskies.physics_api.voxel_updates.SparseVoxelShapeUpdate;
import org.valkyrienskies.physics_api.voxel_updates.VoxelRigidBodyShapeUpdates;

import java.nio.ByteBuffer;

public class VoxelRigidBodyShapeUpdatesEncoder {
    private static int getVoxelShapeUpdateSize(@NotNull IVoxelShapeUpdate voxelShapeUpdate) {
        if (voxelShapeUpdate instanceof EmptyVoxelShapeUpdate) {
            return 16; // 16 bytes is the min size
        } else if (voxelShapeUpdate instanceof DenseVoxelShapeUpdate) {
            return 16 + 4096; // 16 bytes for the min size, 4096 bytes for the dense data
        } else {
            // Sparse voxel shape update
            final SparseVoxelShapeUpdate sparseVoxelShapeUpdate = (SparseVoxelShapeUpdate) voxelShapeUpdate;
            return 16 + 4 + sparseVoxelShapeUpdate.getUpdatesPositions().size() * 2; // 16 bytes min size, 4 bytes for update position size, and 2 bytes per updated position
        }
    }

    private static void writeVoxelRigidBodyShapeUpdatesToByteBuf(@NotNull VoxelRigidBodyShapeUpdates update, @NotNull ByteBuffer outputBuffer) {
        outputBuffer.putInt(update.getRigidBodyId());
        outputBuffer.putInt(update.getShapeUpdates().length);
        for (final IVoxelShapeUpdate voxelShapeUpdate : update.getShapeUpdates()) {
            writeIVoxelShapeUpdateByteBuf(voxelShapeUpdate, outputBuffer);
        }
    }

    private static void writeIVoxelShapeUpdateByteBuf(@NotNull IVoxelShapeUpdate update, @NotNull ByteBuffer outputBuffer) {
        outputBuffer.putInt(update.getRegionX()); // 4 bytes for regionX
        outputBuffer.putInt(update.getRegionY()); // 4 bytes for regionY
        outputBuffer.putInt(update.getRegionZ()); // 4 bytes for regionZ
        // fourthInt is | 6 bits data | 2 bits update type |
        if (update instanceof EmptyVoxelShapeUpdate) {
            int fourthInt = 0;
            if (update.getRunImmediately()) fourthInt |= 4;
            if (((EmptyVoxelShapeUpdate) update).getOverwriteExistingVoxels()) fourthInt |= 8;
            outputBuffer.putInt(fourthInt); // 4 bytes
        } else if (update instanceof DenseVoxelShapeUpdate) {
            int fourthInt = 1;
            if (update.getRunImmediately()) fourthInt |= 4;
            outputBuffer.putInt(fourthInt); // 4 bytes
            outputBuffer.put(((DenseVoxelShapeUpdate) update).getVoxelDataRaw()); // 4096 bytes
        } else if (update instanceof SparseVoxelShapeUpdate) {
            int fourthInt = 2;
            if (update.getRunImmediately()) fourthInt |= 4;
            outputBuffer.putInt(fourthInt); // 4 bytes
            final SparseVoxelShapeUpdate sparseVoxelShapeUpdate = (SparseVoxelShapeUpdate) update;
            outputBuffer.putInt(sparseVoxelShapeUpdate.getUpdatesPositions().size());
            for (int i = 0; i < sparseVoxelShapeUpdate.getUpdatesPositions().size(); i++) {
                final short singleUpdatePos = sparseVoxelShapeUpdate.getUpdatesPositions().getShort(i);
                final byte singleUpdateData = sparseVoxelShapeUpdate.getUpdatesTypes().getByte(i);
                // The bottom 12 bits of [singleUpdatePos] hold the update position
                outputBuffer.putShort(singleUpdatePos);
                outputBuffer.put(singleUpdateData);
            }
        } else {
            throw new IllegalArgumentException("Unknown update with class type: " + update.getClass());
        }
    }

    @NotNull
    public static byte[] encodeVoxelRigidBodyShapeUpdatesArray(@NotNull VoxelRigidBodyShapeUpdates[] array) {
        // Step 1: Compute the size in bytes
        int size = 4; // Add 4 bytes for array.length
        for (final VoxelRigidBodyShapeUpdates update : array) {
            int extraSize = 8; // 4 bytes for update.rigidBodyId, and 4 bytes for update.shapeUpdates.length
            for (final IVoxelShapeUpdate voxelShapeUpdate : update.getShapeUpdates()) {
                extraSize += getVoxelShapeUpdateSize(voxelShapeUpdate);
            }
            size += extraSize;
        }

        final ByteBuffer outputBuffer = ByteBuffer.allocate(size);
        outputBuffer.putInt(array.length);
        for (final VoxelRigidBodyShapeUpdates update : array) {
            writeVoxelRigidBodyShapeUpdatesToByteBuf(update, outputBuffer);
        }

        return outputBuffer.array();
    }

    public static VoxelRigidBodyShapeUpdates[] decodeVoxelRigidBodyShapeUpdatesArray(@NotNull byte[] encoded) throws NoSuchFieldException, IllegalAccessException {
        final ByteBuffer byteBuffer = ByteBuffer.wrap(encoded);

        final int arraySize = byteBuffer.getInt();
        final VoxelRigidBodyShapeUpdates[] toReturn = new VoxelRigidBodyShapeUpdates[arraySize];

        for (int i = 0; i < arraySize; i++) {
            final int rigidBodyId = byteBuffer.getInt();
            final int updatesArraySize = byteBuffer.getInt();
            final IVoxelShapeUpdate[] updatesArray = new IVoxelShapeUpdate[updatesArraySize];
            for (int j = 0; j < updatesArraySize; j++) {
                final int regionX = byteBuffer.getInt();
                final int regionY = byteBuffer.getInt();
                final int regionZ = byteBuffer.getInt();
                final int fourthInt = byteBuffer.getInt();
                final boolean updateImmediately = (fourthInt & 4) != 0;
                final int updateType = fourthInt & 3;
                switch (updateType) {
                    case 0: // EmptyVoxelShapeUpdate
                        final boolean overwriteExistingVoxels = (fourthInt & 8) != 0;
                        updatesArray[j] = new EmptyVoxelShapeUpdate(regionX, regionY, regionZ, updateImmediately, overwriteExistingVoxels);
                        break;
                    case 1: // DenseVoxelShapeUpdate
                        // TODO: We could make this faster by not initializing the array, but I don't want to deal with JNI for now
                        final byte[] voxelDataRaw = new byte[4096];
                        copyFromByteBufferToByteArray(byteBuffer, voxelDataRaw, 4096);
                        updatesArray[j] = new DenseVoxelShapeUpdate(regionX, regionY, regionZ, updateImmediately, voxelDataRaw);
                        break;
                    case 2: // SparseVoxelShapeUpdate
                        final SparseVoxelShapeUpdate sparseVoxelShapeUpdate = new SparseVoxelShapeUpdate(regionX, regionY, regionZ, updateImmediately);
                        final int sparseUpdatesSize = byteBuffer.getInt();
                        for (int k = 0; k < sparseUpdatesSize; k++) {
                            final short sparseUpdateAsShort = byteBuffer.getShort();
                            final int posX = (sparseUpdateAsShort & 0xF);
                            final int posY = ((sparseUpdateAsShort >> 8) & 0xF);
                            final int posZ = ((sparseUpdateAsShort >> 4) & 0xF);
                            final byte voxelData = byteBuffer.get();
                            sparseVoxelShapeUpdate.addUpdate(posX, posY, posZ, voxelData);
                        }
                        updatesArray[j] = sparseVoxelShapeUpdate;
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown update encoded type: " + updateType);
                }
            }
            toReturn[i] = new VoxelRigidBodyShapeUpdates(rigidBodyId, updatesArray);
        }

        return toReturn;
    }

    private static void copyFromByteBufferToByteArray(ByteBuffer src, byte[] dest, int bytesToCopy) throws NoSuchFieldException, IllegalAccessException {
        final int oldPosition = src.position();
        final byte[] backingArray = src.array();
        // Copy the bytes
        System.arraycopy(backingArray, oldPosition, dest, 0, bytesToCopy);
        // Increment the position pointer of the [src] buffer
        src.position(oldPosition + bytesToCopy);
    }

}
