package org.valkyrienskies.physics_api_krunch

open class KrunchPhysicsWorldSettings(
    override var subSteps: Int = 20,
    override var iterations: Int = 2,
    override var solverIterationWeight: Double = 1.0,
    override var collisionCompliance: Double = 0.0,
    override var collisionRestitutionCompliance: Double = 0.0,
    override var dynamicFrictionCompliance: Double = 0.0,
    override var speculativeContactDistance: Double = 0.05,
    override var solverType: SolverType = SolverType.JACOBI,
    override var maxCollisionPoints: Int = 4,
    override var maxCollisionPointDepth: Double = 1.0,
    override var maxDePenetrationSpeed: Double = 1e4,
    override var maxVoxelShapeCollisionPoints: Int = 64
) : KrunchPhysicsWorldSettingsc
