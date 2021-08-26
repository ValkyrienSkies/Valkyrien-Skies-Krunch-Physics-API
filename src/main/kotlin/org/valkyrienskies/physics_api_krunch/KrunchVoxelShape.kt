package org.valkyrienskies.physics_api_krunch

import org.joml.Vector3d
import org.joml.Vector3dc
import org.valkyrienskies.krunch.collision.shapes.TSDFVoxelShape
import org.valkyrienskies.physics_api.VoxelShape

internal class KrunchVoxelShape : VoxelShape {
    internal val krunchShape = TSDFVoxelShape()

    override fun addVoxel(x: Int, y: Int, z: Int) {
        krunchShape.layeredTSDF.setVoxel(x, y, z, true)
    }

    override fun getVoxelOffset(): Vector3dc = Vector3d(krunchShape.voxelOffset)

    override fun removeVoxel(x: Int, y: Int, z: Int) {
        krunchShape.layeredTSDF.setVoxel(x, y, z, false)
    }

    override fun setScaling(scaling: Double) {
        krunchShape.scalingFactor = scaling
    }

    override fun setVoxelOffset(xOffset: Double, yOffset: Double, zOffset: Double) {
        krunchShape.voxelOffset.set(xOffset, yOffset, zOffset)
    }
}
