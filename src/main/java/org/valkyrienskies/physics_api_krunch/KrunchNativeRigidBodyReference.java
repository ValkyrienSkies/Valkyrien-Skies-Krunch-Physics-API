package org.valkyrienskies.physics_api_krunch;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3dc;
import org.valkyrienskies.physics_api.PhysicsWorldReference;
import org.valkyrienskies.physics_api.RigidBodyInertiaData;
import org.valkyrienskies.physics_api.RigidBodyReference;
import org.valkyrienskies.physics_api.RigidBodyTransform;

/**
 * This class points to a rigid body in Krunch native.
 *
 * It doesn't store a reference to the rigid body; instead it stores a reference to the physics world the rigid body
 * exists in; as well as the unique id belonging to the rigid body it points to.
 */
class KrunchNativeRigidBodyReference implements RigidBodyReference {

    private final PhysicsWorldReference physicsWorldReference;
    private long physicsWorldPointer;
    private int rigidBodyUniqueId;

    // The index of the rigid body in the rigid body vector of the physics world.
    // This index can change because the physics world is allowed to change where rigid bodies are stored.
    private int rigidBodyIndexInPhysicsWorld;

    protected KrunchNativeRigidBodyReference(final PhysicsWorldReference physicsWorldReference,
                                             final long physicsWorldPointer,
                                             final int rigidBodyUniqueId,
                                             final int rigidBodyIndexInPhysicsWorld) {
        this.physicsWorldReference = physicsWorldReference;
        this.physicsWorldPointer = physicsWorldPointer;
        this.rigidBodyUniqueId = rigidBodyUniqueId;
        this.rigidBodyIndexInPhysicsWorld = rigidBodyIndexInPhysicsWorld;
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
    public native int getDimension();

    @Override
    public native void setDimension(int i);

    @Override
    public boolean isReferenceValid() {
        return false;
    }

    @NotNull
    @Override
    public PhysicsWorldReference getPhysicsWorldReference() {
        return physicsWorldReference;
    }
}
