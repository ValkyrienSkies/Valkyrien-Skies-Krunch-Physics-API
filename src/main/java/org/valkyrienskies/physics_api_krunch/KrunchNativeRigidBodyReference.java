package org.valkyrienskies.physics_api_krunch;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3dc;
import org.joml.Vector3i;
import org.joml.Vector3ic;
import org.valkyrienskies.physics_api.PhysicsWorldReference;
import org.valkyrienskies.physics_api.RigidBodyInertiaData;
import org.valkyrienskies.physics_api.RigidBodyReference;
import org.valkyrienskies.physics_api.RigidBodyTransform;
import org.valkyrienskies.physics_api.UsingDeletedReferenceException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a reference to a rigid body in Krunch Native.
 *
 * Note this class doesn't store a direct pointer to the rigid body; instead it stores a pointer to the physics world
 * the rigid body exists in; as well as the unique id belonging to the rigid body it points to.
 */
class KrunchNativeRigidBodyReference implements RigidBodyReference {

    private static final int DEFAULT_CACHED_RIGID_BODY_INDEX_IN_PHYSICS_WORLD = 0;
    // Returned by [getCachedRigidBodyIndex()] iff the rigid body this reference points to has been deleted.
    private static final int DELETED_CACHED_RIGID_BODY_INDEX_IN_PHYSICS_WORLD = -1;

    private final KrunchNativePhysicsWorldReference physicsWorldReference;
    private final int rigidBodyUniqueId;

    // The index of the rigid body in the rigid body vector of the physics world.
    // This index can change because the physics world is allowed to change where rigid bodies are stored.
    private int cachedRigidBodyIndex;

    public static final int VOXEL_STATE_RIGID_BODY_NOT_VOXEL = -1;
    public static final int VOXEL_STATE_UNLOADED = -2;

    protected KrunchNativeRigidBodyReference(final KrunchNativePhysicsWorldReference physicsWorldReference,
                                             final int rigidBodyUniqueId) {
        this.physicsWorldReference = physicsWorldReference;
        this.rigidBodyUniqueId = rigidBodyUniqueId;
        this.cachedRigidBodyIndex = DEFAULT_CACHED_RIGID_BODY_INDEX_IN_PHYSICS_WORLD;
    }

    @Override
    public int getRigidBodyId() {
        return rigidBodyUniqueId;
    }

    @Override
    public double getDynamicFrictionCoefficient() throws UsingDeletedReferenceException {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        return getDynamicFrictionCoefficient(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex);
    }

    @Override
    public void setDynamicFrictionCoefficient(double coefficient) throws UsingDeletedReferenceException {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        setDynamicFrictionCoefficient(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex, coefficient);
    }

    @Override
    public boolean isStatic() throws UsingDeletedReferenceException {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        return getIsStatic(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex);
    }

    @Override
    public void setStatic(boolean isStatic) throws UsingDeletedReferenceException {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        setStatic(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex, isStatic);
    }

    @Override
    public double getRestitutionCoefficient() throws UsingDeletedReferenceException {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        return getRestitutionCoefficient(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex);
    }

    @Override
    public void setRestitutionCoefficient(double coefficient) throws UsingDeletedReferenceException {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        setRestitutionCoefficient(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex, coefficient);
    }

    @Override
    public double getStaticFrictionCoefficient() throws UsingDeletedReferenceException {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        return getStaticFrictionCoefficient(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex);
    }

    @Override
    public void setStaticFrictionCoefficient(double coefficient) throws UsingDeletedReferenceException {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        setStaticFrictionCoefficient(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex, coefficient);
    }

    @NotNull
    @Override
    public Vector3dc getCollisionShapeOffset() throws UsingDeletedReferenceException {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        return getCollisionShapeOffset(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex);
    }

    @Override
    public void setCollisionShapeOffset(@NotNull Vector3dc offset) throws UsingDeletedReferenceException {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        setCollisionShapeOffset(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex, offset.x(), offset.y(), offset.z());
    }

    @NotNull
    @Override
    public RigidBodyInertiaData getInertiaData() throws UsingDeletedReferenceException {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        final byte[] encoded = new byte[RigidBodyInertiaDataEncoder.RIGID_BODY_INERTIA_DATA_BYTES_SIZE];
        getInertiaData(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex, encoded);
        return RigidBodyInertiaDataEncoder.decodeRigidBodyInertiaData(encoded);
    }

    @Override
    public void setInertiaData(@NotNull RigidBodyInertiaData rigidBodyInertiaData) throws UsingDeletedReferenceException {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        final byte[] encoded = RigidBodyInertiaDataEncoder.encodeRigidBodyInertiaData(rigidBodyInertiaData);
        setInertiaData(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex, encoded);
    }

    @NotNull
    @Override
    public RigidBodyTransform getRigidBodyTransform() throws UsingDeletedReferenceException {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        final byte[] encoded = new byte[RigidBodyTransformEncoder.RIGID_BODY_TRANSFORM_BYTES_SIZE];
        getRigidBodyTransform(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex, encoded);
        return RigidBodyTransformEncoder.decodeRigidBodyTransform(encoded);
    }

    @Override
    public void setRigidBodyTransform(@NotNull RigidBodyTransform rigidBodyTransform) throws UsingDeletedReferenceException {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        final byte[] encoded = RigidBodyTransformEncoder.encodeRigidBodyTransform(rigidBodyTransform);
        setRigidBodyTransform(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex, encoded);
    }

    @Override
    public double getCollisionShapeScaling() throws UsingDeletedReferenceException {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        return getCollisionShapeScaling(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex);
    }

    @Override
    public void setCollisionShapeScaling(double scaling) throws UsingDeletedReferenceException {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        setCollisionShapeScaling(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex, scaling);
    }

    private void updateCachedIndexAndEnsureReferenceNotDeleted() throws UsingDeletedReferenceException {
        if (hasBeenDeleted()) throw new UsingDeletedReferenceException("The underlying rigid body has been deleted!");
    }

    @Override
    public boolean hasBeenDeleted() {
        updateCachedIndex();
        return getCachedHasBeenDeleted();
    }

    private void updateCachedIndex() {
        if (getCachedHasBeenDeleted()) return;

        // Check if the physics world has been deleted
        if (physicsWorldReference.hasBeenDeleted()) {
            // If the world has been deleted, then mark this rigid body reference as deleted.
            cachedRigidBodyIndex = DELETED_CACHED_RIGID_BODY_INDEX_IN_PHYSICS_WORLD;
        } else {
            // Update the cached rigid body index
            cachedRigidBodyIndex = getCachedRigidBodyIndex(physicsWorldReference.getPhysicsWorldPointer(), cachedRigidBodyIndex, rigidBodyUniqueId);
        }
    }

    /**
     * Check if {@link KrunchNativeRigidBodyReference#cachedRigidBodyIndex} is the deleted index {@link KrunchNativeRigidBodyReference#DELETED_CACHED_RIGID_BODY_INDEX_IN_PHYSICS_WORLD}.
     */
    private boolean getCachedHasBeenDeleted() {
        return cachedRigidBodyIndex == DELETED_CACHED_RIGID_BODY_INDEX_IN_PHYSICS_WORLD;
    }

    @NotNull
    @Override
    public PhysicsWorldReference getPhysicsWorldReference() {
        return physicsWorldReference;
    }

    @Override
    public int getInitialDimension() throws UsingDeletedReferenceException {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        return getInitialDimension(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex);
    }

    @NotNull
    @Override
    public Vector3dc getVelocity() throws UsingDeletedReferenceException {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        return getVelocity(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex);
    }

    @Override
    public void setVelocity(@NotNull Vector3dc velocity) throws UsingDeletedReferenceException {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        setVelocity(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex, velocity.x(), velocity.y(), velocity.z());
    }

    @NotNull
    @Override
    public Vector3dc getOmega() throws UsingDeletedReferenceException {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        return getOmega(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex);
    }

    @Override
    public void setOmega(@NotNull Vector3dc omega) throws UsingDeletedReferenceException {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        setOmega(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex, omega.x(), omega.y(), omega.z());
    }

    /**
     * Gets the voxel state of the rigid body at the given block position. This should be used for testing purposes only.
     *
     * @return VOXEL_STATE_RIGID_BODY_NOT_VOXEL if this rigid body is not a voxel rigid body, VOXEL_STATE_UNLOADED if the voxel is unloaded
     */
    protected int getVoxelState(int posX, int posY, int posZ) throws UsingDeletedReferenceException {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        return getVoxelState(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex, posX, posY, posZ);
    }

    protected List<Vector3ic> getSetVoxels() throws UsingDeletedReferenceException {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        final int voxelsSize = getSetVoxelsSize(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex);
        final int[] setVoxels = new int[voxelsSize * 3];
        getSetVoxels(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex, setVoxels);
        final List<Vector3ic> toReturn = new ArrayList<>(voxelsSize);
        for (int i = 0; i < voxelsSize; i++) {
            toReturn.add(new Vector3i(setVoxels[i], setVoxels[i + 1], setVoxels[i + 2]));
        }
        return toReturn;
    }

    // region Native Functions
    /**
     * @param physicsWorldPointer The pointer to the physics world this rigid body exists in
     * @param rigidBodyUniqueId The id of the rigid body this reference points to
     * @param cachedIndex A guess of the cachedIndex. If it is correct then we can skip a map lookup, so we might as
     *                    well pass it in
     * @return The index where the rigid body this reference points to is stored.
     *         If this returns {@link KrunchNativeRigidBodyReference#DELETED_CACHED_RIGID_BODY_INDEX_IN_PHYSICS_WORLD}
     *         then that implies the rigid body has been deleted.
     */
    private static native int getCachedRigidBodyIndex(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex);

    private static native double getDynamicFrictionCoefficient(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex);

    private static native void setDynamicFrictionCoefficient(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex, double dynamicFrictionCoefficient);

    private static native boolean getIsStatic(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex);

    private static native void setStatic(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex, boolean isStatic);

    private static native double getRestitutionCoefficient(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex);

    private static native void setRestitutionCoefficient(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex, double restitutionCoefficient);

    private static native double getStaticFrictionCoefficient(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex);

    private static native void setStaticFrictionCoefficient(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex, double staticFrictionCoefficient);

    private static native Vector3dc getCollisionShapeOffset(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex);

    private static native void setCollisionShapeOffset(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex, double offsetX, double offsetY, double offsetZ);

    private static native void getInertiaData(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex, @NotNull byte[] output);

    private static native void setInertiaData(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex, @NotNull byte[] data);

    private static native void getRigidBodyTransform(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex, @NotNull byte[] output);

    private static native void setRigidBodyTransform(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex, @NotNull byte[] data);

    private static native double getCollisionShapeScaling(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex);

    private static native void setCollisionShapeScaling(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex, double scaling);

    private static native int getInitialDimension(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex);

    private static native Vector3dc getVelocity(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex);

    private static native void setVelocity(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex, double velX, double velY, double velZ);

    private static native Vector3dc getOmega(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex);

    private static native void setOmega(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex, double omegaX, double omegaY, double omegaZ);

    /**
     * This should only be used for testing
     *
     * @return VOXEL_STATE_RIGID_BODY_NOT_VOXEL if this rigid body is not a voxel rigid body, VOXEL_STATE_UNLOADED if the voxel is unloaded
     */
    private static native int getVoxelState(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex, int posX, int posY, int posZ);

    /**
     * This should only be used for testing
     */
    private static native int getSetVoxelsSize(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex);

    /**
     * This should only be used for testing
     */
    private static native void getSetVoxels(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex, @NotNull int[] data);
    // endregion
}
