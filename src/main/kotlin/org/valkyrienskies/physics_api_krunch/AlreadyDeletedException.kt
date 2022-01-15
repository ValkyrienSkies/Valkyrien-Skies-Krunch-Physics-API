package org.valkyrienskies.physics_api_krunch

/**
 * This exception is thrown when the program tries using a reference that points to deleted data.
 *
 * See[KrunchNativePhysicsWorldReference] or [KrunchNativeRigidBodyReference].
 */
class AlreadyDeletedException(message: String) : RuntimeException(message)
