package org.valkyrienskies.physics_api_krunch;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3dc;
import org.valkyrienskies.physics_api.PhysicsWorldReference;
import org.valkyrienskies.physics_api.RigidBodyInertiaData;
import org.valkyrienskies.physics_api.RigidBodyReference;
import org.valkyrienskies.physics_api.RigidBodyTransform;
import org.valkyrienskies.physics_api.UsingDeletedReferenceException;

/**
 * This class is a reference to a rigid body in Krunch Native.
 *
 * Note this class doesn't store a direct pointer to the rigid body; instead it stores a pointer to the physics world
 * the rigid body exists in; as well as the unique id belonging to the rigid body it points to.
 */
class KrunchNativeRigidBodyReference implements RigidBodyReference {

    private static final int DEFAULT_CACHED_RIGID_BODY_INDEX_IN_PHYSICS_WORLD = -1;

    private final PhysicsWorldReference physicsWorldReference;
    private final int rigidBodyUniqueId;

    // The index of the rigid body in the rigid body vector of the physics world.
    // This index can change because the physics world is allowed to change where rigid bodies are stored.
    private int cachedRigidBodyIndexInPhysicsWorld;

    protected KrunchNativeRigidBodyReference(final PhysicsWorldReference physicsWorldReference,
                                             final int rigidBodyUniqueId) {
        this.physicsWorldReference = physicsWorldReference;
        this.rigidBodyUniqueId = rigidBodyUniqueId;
        this.cachedRigidBodyIndexInPhysicsWorld = DEFAULT_CACHED_RIGID_BODY_INDEX_IN_PHYSICS_WORLD;
    }

    @Override
    public int getRigidBodyId() {
        return rigidBodyUniqueId;
    }

    @Override
    public native double getDynamicFrictionCoefficient();

    @Override
    public native void setDynamicFrictionCoefficient(double v);

    @Override
    public native boolean isStatic();

    @Override
    public native void setStatic(boolean b);

    @Override
    public native double getRestitutionCoefficient();

    @Override
    public native void setRestitutionCoefficient(double v);

    @Override
    public native double getStaticFrictionCoefficient();

    @Override
    public native void setStaticFrictionCoefficient(double v);

    @NotNull
    @Override
    public native Vector3dc getCollisionShapeOffset();

    @Override
    public native void setCollisionShapeOffset(@NotNull Vector3dc vector3dc);

    @NotNull
    @Override
    public native RigidBodyInertiaData getInertiaData();

    @Override
    public native void setInertiaData(@NotNull RigidBodyInertiaData rigidBodyInertiaData);

    @NotNull
    @Override
    public native RigidBodyTransform getRigidBodyTransform();

    @Override
    public native void setRigidBodyTransform(@NotNull RigidBodyTransform rigidBodyTransform);

    @Override
    public native double getCollisionShapeScaling();

    @Override
    public native void setCollisionShapeScaling(double v);

    @Override
    public boolean hasBeenDeleted() {
        return false;
    }

    @NotNull
    @Override
    public PhysicsWorldReference getPhysicsWorldReference() {
        return physicsWorldReference;
    }

    @Override
    public native int getInitialDimension() throws UsingDeletedReferenceException;
}
