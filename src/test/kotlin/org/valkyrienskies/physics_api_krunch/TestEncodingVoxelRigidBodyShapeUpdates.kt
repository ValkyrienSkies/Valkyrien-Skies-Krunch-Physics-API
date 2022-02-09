package org.valkyrienskies.physics_api_krunch

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test
import org.valkyrienskies.physics_api.voxel_updates.*

class TestEncodingVoxelRigidBodyShapeUpdates {

    @Test
    fun testEncodingAndDecoding() {
        val overallUpdatesArray: Array<VoxelRigidBodyShapeUpdates?> = arrayOfNulls(2)

        run {
            val body0Id = 2
            val body0Updates: Array<IVoxelShapeUpdate?> = arrayOfNulls(3)
            body0Updates[0] = EmptyVoxelShapeUpdate(1, 2, 3, runImmediately = true, true)
            body0Updates[1] = DenseVoxelShapeUpdate(2, 3, 4)
            (body0Updates[1] as DenseVoxelShapeUpdate).setVoxel(4, 5, 2, KrunchVoxelStates.SOLID_STATE)
            body0Updates[2] = SparseVoxelShapeUpdate(5, 6, 7)
            (body0Updates[2] as SparseVoxelShapeUpdate).addUpdate(2, 6, 3, KrunchVoxelStates.SOLID_STATE)
            (body0Updates[2] as SparseVoxelShapeUpdate).addUpdate(3, 3, 15, KrunchVoxelStates.AIR_STATE)
            (body0Updates[2] as SparseVoxelShapeUpdate).addUpdate(0, 0, 0, KrunchVoxelStates.SOLID_STATE)

            @Suppress("UNCHECKED_CAST")
            overallUpdatesArray[0] = VoxelRigidBodyShapeUpdates(body0Id, body0Updates as Array<IVoxelShapeUpdate>)
        }

        run {
            val body1Id = 15
            val body1Updates: Array<IVoxelShapeUpdate?> = arrayOfNulls(1)
            body1Updates[0] = SparseVoxelShapeUpdate(5, 6, 7)
            (body1Updates[0] as SparseVoxelShapeUpdate).addUpdate(4, 6, 3, KrunchVoxelStates.SOLID_STATE)
            (body1Updates[0] as SparseVoxelShapeUpdate).addUpdate(3, 5, 15, KrunchVoxelStates.AIR_STATE)
            (body1Updates[0] as SparseVoxelShapeUpdate).addUpdate(0, 0, 7, KrunchVoxelStates.SOLID_STATE)

            @Suppress("UNCHECKED_CAST")
            overallUpdatesArray[1] = VoxelRigidBodyShapeUpdates(body1Id, body1Updates as Array<IVoxelShapeUpdate>)
        }

        val encodedToBytes = VoxelRigidBodyShapeUpdatesEncoder.encodeVoxelRigidBodyShapeUpdatesArray(overallUpdatesArray)
        val decoded = VoxelRigidBodyShapeUpdatesEncoder.decodeVoxelRigidBodyShapeUpdatesArray(encodedToBytes)

        assertArrayEquals(overallUpdatesArray, decoded)
    }

    @Test
    fun testEncodingAndDecodingEmptyOnly() {
        val overallUpdatesArray: Array<VoxelRigidBodyShapeUpdates?> = arrayOfNulls(2)

        run {
            val body0Id = 2
            val body0Updates: Array<IVoxelShapeUpdate?> = arrayOfNulls(1)
            body0Updates[0] = EmptyVoxelShapeUpdate(1, 2, 3, runImmediately = true, true)

            @Suppress("UNCHECKED_CAST")
            overallUpdatesArray[0] = VoxelRigidBodyShapeUpdates(body0Id, body0Updates as Array<IVoxelShapeUpdate>)
        }

        run {
            val body1Id = 15
            val body1Updates: Array<IVoxelShapeUpdate?> = arrayOfNulls(1)
            body1Updates[0] = EmptyVoxelShapeUpdate(5, 6, 7, runImmediately = false, false)

            @Suppress("UNCHECKED_CAST")
            overallUpdatesArray[1] = VoxelRigidBodyShapeUpdates(body1Id, body1Updates as Array<IVoxelShapeUpdate>)
        }

        val encodedToBytes = VoxelRigidBodyShapeUpdatesEncoder.encodeVoxelRigidBodyShapeUpdatesArray(overallUpdatesArray)
        val decoded = VoxelRigidBodyShapeUpdatesEncoder.decodeVoxelRigidBodyShapeUpdatesArray(encodedToBytes)

        assertArrayEquals(overallUpdatesArray, decoded)
    }

    @Test
    fun testEncodingAndDecodingDenseOnly() {
        val overallUpdatesArray: Array<VoxelRigidBodyShapeUpdates?> = arrayOfNulls(1)

        run {
            val body0Id = 2
            val body0Updates: Array<IVoxelShapeUpdate?> = arrayOfNulls(1)
            body0Updates[0] = DenseVoxelShapeUpdate(2, 3, 4)
            (body0Updates[0] as DenseVoxelShapeUpdate).setVoxel(4, 5, 2, KrunchVoxelStates.SOLID_STATE)

            @Suppress("UNCHECKED_CAST")
            overallUpdatesArray[0] = VoxelRigidBodyShapeUpdates(body0Id, body0Updates as Array<IVoxelShapeUpdate>)
        }

        val encodedToBytes = VoxelRigidBodyShapeUpdatesEncoder.encodeVoxelRigidBodyShapeUpdatesArray(overallUpdatesArray)
        val decoded = VoxelRigidBodyShapeUpdatesEncoder.decodeVoxelRigidBodyShapeUpdatesArray(encodedToBytes)

        assertArrayEquals(overallUpdatesArray, decoded)
    }

    @Test
    fun testEncodingAndDecodingSparseOnly() {
        val overallUpdatesArray: Array<VoxelRigidBodyShapeUpdates?> = arrayOfNulls(1)

        run {
            val body0Id = 2
            val body0Updates: Array<IVoxelShapeUpdate?> = arrayOfNulls(1)
            body0Updates[0] = SparseVoxelShapeUpdate(5, 6, 7)
            (body0Updates[0] as SparseVoxelShapeUpdate).addUpdate(2, 6, 3, KrunchVoxelStates.SOLID_STATE)
            (body0Updates[0] as SparseVoxelShapeUpdate).addUpdate(3, 3, 15, KrunchVoxelStates.AIR_STATE)
            (body0Updates[0] as SparseVoxelShapeUpdate).addUpdate(0, 0, 0, KrunchVoxelStates.SOLID_STATE)

            @Suppress("UNCHECKED_CAST")
            overallUpdatesArray[0] = VoxelRigidBodyShapeUpdates(body0Id, body0Updates as Array<IVoxelShapeUpdate>)
        }

        val encodedToBytes = VoxelRigidBodyShapeUpdatesEncoder.encodeVoxelRigidBodyShapeUpdatesArray(overallUpdatesArray)
        val decoded = VoxelRigidBodyShapeUpdatesEncoder.decodeVoxelRigidBodyShapeUpdatesArray(encodedToBytes)

        assertArrayEquals(overallUpdatesArray, decoded)
    }

}
