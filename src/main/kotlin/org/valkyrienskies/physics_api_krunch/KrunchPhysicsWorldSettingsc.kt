package org.valkyrienskies.physics_api_krunch

/**
 * An immutable view of [KrunchPhysicsWorldSettings]
 */
interface KrunchPhysicsWorldSettingsc {
    /**
     * The number of sub-steps per physics iteration. Higher values result in better quality simulation.
     */
    val subSteps: Int

    /**
     * The number of iterations the constraint solver runs per sub-step. The ideal value for this depends on the solver
     * being used.
     *
     * For [SolverType.GAUSS_SEIDEL] this should typically be set around 1-2.
     * For [SolverType.JACOBI] this should typically be set around 2-5.
     */
    val iterations: Int

    /**
     * Determines how quickly the solver will converge. Higher values will converge faster with less iterations, but
     * are less stable in complicated situations, and more likely to "blow up" (diverge).
     *
     * Smaller values are more stable but take more iterations, so they are slower.
     *
     * For [SolverType.GAUSS_SEIDEL] this should typically be set around .8 to 1.
     * For [SolverType.JACOBI] this should typically be set around .8 to 1.
     */
    val solverIterationWeight: Double

    /**
     * The collision compliance controls the strength of collision forces, it is the same as spring compliance
     * (See https://en.wikipedia.org/wiki/Spring_(device)).
     *
     * Setting compliance to 0 makes collisions infinitely stiff, which is ideal for rigid body physics.
     *
     * However a compliance of 0 can cause the solver to 'explode' (ie diverge), especially in cases where the solver is
     * over-constrained. If you're seeing explosive behavior in the simulation, then it might be better to set this
     * somewhere between 1e-8 to 1e-4 to prevent the simulation from exploding.
     */
    val collisionCompliance: Double

    /**
     * This constraint controls how well collisions obey Newton's Law of Restitution.
     * (See https://en.wikipedia.org/wiki/Coefficient_of_restitution)
     *
     * Its behavior is similar to [collisionCompliance], in that setting it to 0 will perfectly solve for restitution,
     * however this can cause the solver to 'explode' (diverge).
     *
     * Setting this value somewhere between 1e-8 and 1e-4 will still result in Newton's Law of Restitution mostly being
     * obeyed, without risking the solver diverging.
     */
    val collisionRestitutionCompliance: Double
    val dynamicFrictionCompliance: Double

    /**
     * The distance at which a contact that isn't overlapping is included as a speculative contact.
     */
    val speculativeContactDistance: Double

    /**
     * This changes the solver used to solve the constraints. See [SolverType] for more information.
     */
    val solverType: SolverType

    /**
     * The maximum number of collision points per collision pair used during a sub-step.
     *
     * Traditional physics engines like Bullet typically this to 4, but since we have so many sub-steps its fine to set
     * this to 1.
     *
     * Setting this to 1 also makes the solver more stable, because it has less constraints to solve for each body,
     * which helps to avoid being over-constrained.
     */
    val maxCollisionPoints: Int

    /**
     * The maximum depth of a collision point before we consider it too deep. If a collision point is too deep we ignore
     * it to avoid instability.
     */
    val maxCollisionPointDepth: Double

    /**
     * The max speed a collision constraint will push. This prevents collision constraints from pushing too hard and
     * creating explosions.
     */
    val maxDePenetrationSpeed: Double
}
