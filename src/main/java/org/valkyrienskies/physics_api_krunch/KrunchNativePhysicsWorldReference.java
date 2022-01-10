package org.valkyrienskies.physics_api_krunch;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3dc;
import org.valkyrienskies.physics_api.PhysicsWorldReference;
import org.valkyrienskies.physics_api.RigidBodyReference;
import org.valkyrienskies.physics_api.voxel_updates.VoxelRigidBodyShapeUpdates;

import java.util.List;

/**
 * This class points to physics world in Krunch Native.
 */
class KrunchNativePhysicsWorldReference implements PhysicsWorldReference {

    private long physicsWorldPointer;

    public KrunchNativePhysicsWorldReference() {
        createKrunchNativePhysicsWorld();
    }

    /**
     * Create the physics world in Krunch C++
     */
    private native void createKrunchNativePhysicsWorld();

    @NotNull
    @Override
    public native RigidBodyReference createVoxelRigidBody(int dimension);

    @Override
    public native void queueVoxelShapeUpdates(@NotNull List<VoxelRigidBodyShapeUpdates> list);

    @Override
    public native void tick(@NotNull Vector3dc vector3dc, double v, boolean b);

    public native void setSettings(@NotNull KrunchPhysicsWorldSettingsc settingsWrapper);

    @Override
    public native void deletePhysicsWorldResources();

    @Override
    public native boolean deleteRigidBody(int rigidBodyId);

    @Override
    public native boolean hasBeenDeleted();
}
