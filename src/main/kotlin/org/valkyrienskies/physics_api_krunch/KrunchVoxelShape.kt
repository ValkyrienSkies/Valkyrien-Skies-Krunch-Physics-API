package org.valkyrienskies.physics_api_krunch

import org.valkyrienskies.physics_api.VoxelShape

internal class KrunchVoxelShape : VoxelShape {

    private val krunchShape = org.valkyrienskies.krunch.collision.shapes.VoxelShape(emptyList())

    override fun addVoxel(x: Int, y: Int, z: Int) {
        krunchShape.setVoxelType(x, y, z, org.valkyrienskies.krunch.collision.shapes.VoxelShape.VoxelType.FULL)
    }

    override fun removeVoxel(x: Int, y: Int, z: Int) {
        krunchShape.setVoxelType(x, y, z, org.valkyrienskies.krunch.collision.shapes.VoxelShape.VoxelType.EMPTY)
    }

    override fun setVoxelShapeOffset(xOffset: Double, yOffset: Double, zOffset: Double) {
        krunchShape.shapeOffset.set(xOffset, yOffset, zOffset)
    }
}
