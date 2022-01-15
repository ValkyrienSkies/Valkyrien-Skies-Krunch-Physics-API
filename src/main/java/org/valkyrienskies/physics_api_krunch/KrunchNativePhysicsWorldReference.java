package org.valkyrienskies.physics_api_krunch;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3dc;
import org.valkyrienskies.physics_api.PhysicsWorldReference;
import org.valkyrienskies.physics_api.voxel_updates.VoxelRigidBodyShapeUpdates;

import java.util.List;

/**
 * This class points to a physics world in Krunch Native.
 */
class KrunchNativePhysicsWorldReference implements PhysicsWorldReference {

    // The null_ptr in C++.
    private static final long DELETED_PHYSICS_WORLD_POINTER = 0;

    private long physicsWorldPointer;
    private boolean hasBeenDeleted;

    public KrunchNativePhysicsWorldReference() throws OutOfMemoryError {
        this.physicsWorldPointer = createKrunchNativePhysicsWorld();
        this.hasBeenDeleted = false;
    }

    @NotNull
    @Override
    public KrunchNativeRigidBodyReference createVoxelRigidBody(int dimension) throws OutOfMemoryError, AlreadyDeletedException {
        ensureResourcesNotDeleted();
        final int rigidBodyUniqueId = createVoxelRigidBody(physicsWorldPointer, dimension);
        return new KrunchNativeRigidBodyReference(this, rigidBodyUniqueId);
    }

    @Override
    public void queueVoxelShapeUpdates(@NotNull List<VoxelRigidBodyShapeUpdates> list) throws AlreadyDeletedException {
        ensureResourcesNotDeleted();
        queueVoxelShapeUpdates(physicsWorldPointer, list);
    }

    @Override
    public void tick(@NotNull Vector3dc gravity, double timeStep, boolean simulatePhysics) throws AlreadyDeletedException {
        ensureResourcesNotDeleted();
        tick(physicsWorldPointer, gravity.x(), gravity.y(), gravity.z(), timeStep, simulatePhysics);
    }

    public void setSettings(@NotNull KrunchPhysicsWorldSettingsc settingsWrapper) throws AlreadyDeletedException {
        ensureResourcesNotDeleted();
        setSettings(physicsWorldPointer, settingsWrapper.getSubSteps(), settingsWrapper.getIterations(),
                    settingsWrapper.getSolverIterationWeight(), settingsWrapper.getCollisionCompliance(),
                    settingsWrapper.getCollisionRestitutionCompliance(), settingsWrapper.getDynamicFrictionCompliance(),
                    settingsWrapper.getSpeculativeContactDistance(), settingsWrapper.getSolverType().getSolverName(),
                    settingsWrapper.getMaxCollisionPoints(), settingsWrapper.getMaxCollisionPointDepth(),
                    settingsWrapper.getMaxDePenetrationSpeed());
    }

    @Override
    public void deletePhysicsWorldResources() {
        if (!hasBeenDeleted) {
            deleteKrunchNativePhysicsWorld(physicsWorldPointer);
            physicsWorldPointer = DELETED_PHYSICS_WORLD_POINTER;
            hasBeenDeleted = true;
        }
    }

    @Override
    public boolean deleteRigidBody(int rigidBodyId) throws AlreadyDeletedException {
        ensureResourcesNotDeleted();
        return deleteRigidBody(physicsWorldPointer, rigidBodyId);
    }

    @Override
    public boolean hasBeenDeleted() {
        return hasBeenDeleted;
    }

    private void ensureResourcesNotDeleted() throws AlreadyDeletedException {
        if (hasBeenDeleted()) throw new AlreadyDeletedException("The underlying physics world has already been deleted!");
    }

    // region Native Functions
    private static native long createKrunchNativePhysicsWorld() throws OutOfMemoryError;

    private static native void deleteKrunchNativePhysicsWorld(long physicsWorldPointer);

    private static native int createVoxelRigidBody(long physicsWorldPointer, int dimension) throws OutOfMemoryError;

    private static native void queueVoxelShapeUpdates(long physicsWorldPointer, @NotNull List<VoxelRigidBodyShapeUpdates> list);

    private static native void tick(long physicsWorldPointer, double gravityX, double gravityY, double gravityZ, double timeStep, boolean simulatePhysics);

    private static native void setSettings(long physicsWorldPointer, int subSteps, int iterations, double solverIterationWeight, double collisionCompliance, double collisionRestitutionCompliance, double dynamicFrictionCompliance, double speculativeContactDistance, String solverType, int maxCollisionPoints, double maxCollisionPointDepth, double maxDePenetrationSpeed);

    private static native boolean deleteRigidBody(long physicsWorldPointer, int rigidBodyId);
    // endregion
}
