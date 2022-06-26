package org.valkyrienskies.physics_api_krunch;

import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.joml.Vector3i;
import org.joml.Vector3ic;
import org.joml.primitives.AABBd;
import org.joml.primitives.AABBi;
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

    @Override
    public boolean isVoxelTerrainFullyLoaded() throws UsingDeletedReferenceException {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        return getIsVoxelTerrainFullyLoaded(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex);
    }

    @Override
    public void setVoxelTerrainFullyLoaded(boolean isVoxelTerrainFullyLoaded) throws UsingDeletedReferenceException {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        setIsVoxelTerrainFullyLoaded(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex, isVoxelTerrainFullyLoaded);
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
            cachedRigidBodyIndex = getCachedRigidBodyIndex(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex);
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

    @Override
    public void addInvariantForceAtPosToNextPhysTick(@NotNull Vector3dc forcePosInLocal, @NotNull Vector3dc invariantForce) {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        addInvariantForceAtPosToNextPhysTick(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex, forcePosInLocal.x(), forcePosInLocal.y(), forcePosInLocal.z(), invariantForce.x(), invariantForce.y(), invariantForce.z());
    }

    @Override
    public void addInvariantForceToNextPhysTick(@NotNull Vector3dc invariantForce) {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        addInvariantForceToNextPhysTick(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex, invariantForce.x(), invariantForce.y(), invariantForce.z());
    }

    @Override
    public void addInvariantTorqueToNextPhysTick(@NotNull Vector3dc invariantTorque) {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        addInvariantTorqueToNextPhysTick(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex, invariantTorque.x(), invariantTorque.y(), invariantTorque.z());
    }

    @Override
    public void addRotDependentForceToNextPhysTick(@NotNull Vector3dc rotDepForce) {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        addRotDependentForceToNextPhysTick(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex, rotDepForce.x(), rotDepForce.y(), rotDepForce.z());
    }

    @Override
    public void addRotDependentTorqueToNextPhysTick(@NotNull Vector3dc rotDepTorque) {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        addRotDependentTorqueToNextPhysTick(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex, rotDepTorque.x(), rotDepTorque.y(), rotDepTorque.z());
    }

    @Override
    public boolean getAABB(@NotNull AABBd outputBB) {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        final double[] output = new double[6];
        boolean success = getAABB(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex, output);
        if (!success) return false;
        outputBB.minX = output[0];
        outputBB.minY = output[1];
        outputBB.minZ = output[2];
        outputBB.maxX = output[3];
        outputBB.maxY = output[4];
        outputBB.maxZ = output[5];
        return true;
    }

    @Override
    public boolean getVoxelShapeAABB(@NotNull AABBi outputBB) {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        final int[] output = new int[6];
        boolean success = getVoxelShapeAABB(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex, output);
        if (!success) return false;
        outputBB.minX = output[0];
        outputBB.minY = output[1];
        outputBB.minZ = output[2];
        outputBB.maxX = output[3];
        outputBB.maxY = output[4];
        outputBB.maxZ = output[5];
        return true;
    }

    protected Vector3dc getTotalInvariantForceNextPhysTick() {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        double[] output = new double[3];
        getTotalInvariantForcesNextPhysTick(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex, output);
        return new Vector3d(output[0], output[1], output[2]);
    }

    protected Vector3dc getTotalInvariantTorqueNextPhysTick() {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        double[] output = new double[3];
        getTotalInvariantTorquesNextPhysTick(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex, output);
        return new Vector3d(output[0], output[1], output[2]);
    }

    protected Vector3dc getTotalRotDependentForceNextPhysTick() {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        double[] output = new double[3];
        getTotalRotDependentForcesNextPhysTick(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex, output);
        return new Vector3d(output[0], output[1], output[2]);
    }

    protected Vector3dc getTotalRotDependentTorqueNextPhysTick() {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        double[] output = new double[3];
        getTotalRotDependentTorquesNextPhysTick(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex, output);
        return new Vector3d(output[0], output[1], output[2]);
    }

    protected List<Pair<Vector3dc, Vector3dc>> getInvariantForcesAtPosNextPhysTick() {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        int invariantForcesCount = getInvariantForcesAtPosNextPhysTickCount(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex);
        double[] output = new double[invariantForcesCount * 6];
        getInvariantForcesAtPosNextPhysTick(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex, output);
        List<Pair<Vector3dc, Vector3dc>> toReturn = new ArrayList<>();

        for (int i = 0; i < invariantForcesCount; i++) {
            int baseIndex = i * 6;
            Vector3dc forcePosInLocal = new Vector3d(output[baseIndex], output[baseIndex + 1], output[baseIndex + 2]);
            Vector3dc invariantForce = new Vector3d(output[baseIndex + 3], output[baseIndex + 4], output[baseIndex + 5]);
            toReturn.add(new Pair<>(forcePosInLocal, invariantForce));
        }

        return toReturn;
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

    protected List<Vector3ic> getSolidSetVoxels() throws UsingDeletedReferenceException {
        updateCachedIndexAndEnsureReferenceNotDeleted();
        final int voxelsSize = getSolidSetVoxelsSize(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex);
        final int[] setVoxels = new int[voxelsSize * 3];
        getSolidSetVoxels(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex, setVoxels);
        final List<Vector3ic> toReturn = new ArrayList<>(voxelsSize);
        for (int i = 0; i < voxelsSize; i++) {
            toReturn.add(new Vector3i(setVoxels[i * 3], setVoxels[(i * 3) + 1], setVoxels[(i * 3) + 2]));
        }
        return toReturn;
    }

    protected boolean isStaticUnsafe() throws IllegalArgumentException {
        return getIsStatic(physicsWorldReference.getPhysicsWorldPointer(), rigidBodyUniqueId, cachedRigidBodyIndex);
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

    private static native boolean getIsVoxelTerrainFullyLoaded(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex);

    private static native void setIsVoxelTerrainFullyLoaded(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex, boolean isVoxelTerrainFullyLoaded);

    private static native void getInertiaData(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex, @NotNull byte[] output);

    private static native void setInertiaData(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex, @NotNull byte[] data);

    private static native void getRigidBodyTransform(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex, @NotNull byte[] output);

    private static native void setRigidBodyTransform(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex, @NotNull byte[] data);

    private static native double getCollisionShapeScaling(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex);

    private static native void setCollisionShapeScaling(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex, double scaling);

    private static native Vector3dc getVelocity(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex);

    private static native void setVelocity(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex, double velX, double velY, double velZ);

    private static native Vector3dc getOmega(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex);

    private static native void setOmega(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex, double omegaX, double omegaY, double omegaZ);

    private static native void addInvariantForceAtPosToNextPhysTick(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex, double forcePosInLocalX, double forcePosInLocalY, double forcePosInLocalZ, double invariantForceX, double invariantForceY, double invariantForceZ);

    private static native void addInvariantForceToNextPhysTick(long physicsWorldPointer, int rigidBodyUniqueId, int cachedRigidBodyIndex, double invariantForceX, double invariantForceY, double invariantForceZ);

    private static native void addInvariantTorqueToNextPhysTick(long physicsWorldPointer, int rigidBodyUniqueId, int cachedRigidBodyIndex, double invariantTorqueX, double invariantTorqueY, double invariantTorqueZ);

    private static native void addRotDependentForceToNextPhysTick(long physicsWorldPointer, int rigidBodyUniqueId, int cachedRigidBodyIndex, double rotDepForceX, double rotDepForceY, double rotDepForceZ);

    private static native void addRotDependentTorqueToNextPhysTick(long physicsWorldPointer, int rigidBodyUniqueId, int cachedRigidBodyIndex, double rotDepTorqueX, double rotDepTorqueY, double rotDepTorqueZ);

    private static native boolean getVoxelShapeAABB(long physicsWorldPointer, int rigidBodyId, int cachedRigidBodyIndex, @NotNull int[] output);

    private static native boolean getAABB(long physicsWorldPointer, int rigidBodyId, int cachedRigidBodyIndex, @NotNull double[] output);

    /**
     * This should only be used for testing
     *
     * @return VOXEL_STATE_RIGID_BODY_NOT_VOXEL if this rigid body is not a voxel rigid body, VOXEL_STATE_UNLOADED if the voxel is unloaded
     */
    private static native int getVoxelState(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex, int posX, int posY, int posZ);

    /**
     * This should only be used for testing
     */
    private static native int getSolidSetVoxelsSize(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex);

    /**
     * This should only be used for testing
     */
    private static native void getSolidSetVoxels(long physicsWorldPointer, int rigidBodyUniqueId, int cachedIndex, @NotNull int[] data);

    private static native void getTotalInvariantForcesNextPhysTick(long physicsWorldPointer, int rigidBodyUniqueId, int cachedRigidBodyIndex, @NotNull double[] data);

    private static native void getTotalInvariantTorquesNextPhysTick(long physicsWorldPointer, int rigidBodyUniqueId, int cachedRigidBodyIndex, @NotNull double[] data);

    private static native void getTotalRotDependentForcesNextPhysTick(long physicsWorldPointer, int rigidBodyUniqueId, int cachedRigidBodyIndex, @NotNull double[] data);

    private static native void getTotalRotDependentTorquesNextPhysTick(long physicsWorldPointer, int rigidBodyUniqueId, int cachedRigidBodyIndex, @NotNull double[] data);

    private static native int getInvariantForcesAtPosNextPhysTickCount(long physicsWorldPointer, int rigidBodyUniqueId, int cachedRigidBodyIndex);

    private static native void getInvariantForcesAtPosNextPhysTick(long physicsWorldPointer, int rigidBodyUniqueId, int cachedRigidBodyIndex, @NotNull double[] data);
    // endregion
}
