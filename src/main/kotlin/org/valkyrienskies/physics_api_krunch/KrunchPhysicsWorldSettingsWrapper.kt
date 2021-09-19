package org.valkyrienskies.physics_api_krunch

import org.valkyrienskies.krunch.KrunchPhysicsWorldSettings
import org.valkyrienskies.krunch.SolverType

/**
 * A wrapper around [KrunchPhysicsWorldSettings]. Used because this package doesn't expose Krunch classes to dependents.
 */
class KrunchPhysicsWorldSettingsWrapper {
    internal val krunchSettings = KrunchPhysicsWorldSettings()

    fun setSubSteps(subSteps: Int) {
        krunchSettings.subSteps = subSteps
    }

    fun getSubSteps(): Int {
        return krunchSettings.subSteps
    }

    fun setCollisionCompliance(collisionCompliance: Double) {
        krunchSettings.collisionCompliance = collisionCompliance
    }

    fun getCollisionCompliance(): Double {
        return krunchSettings.collisionCompliance
    }

    fun setCollisionRestitutionCompliance(collisionRestitutionCompliance: Double) {
        krunchSettings.collisionRestitutionCompliance = collisionRestitutionCompliance
    }

    fun getCollisionRestitutionCompliance(): Double {
        return krunchSettings.collisionRestitutionCompliance
    }

    fun setDynamicFrictionCompliance(dynamicFrictionCompliance: Double) {
        krunchSettings.dynamicFrictionCompliance = dynamicFrictionCompliance
    }

    fun getDynamicFrictionCompliance(): Double {
        return krunchSettings.dynamicFrictionCompliance
    }

    fun setIterations(iterations: Int) {
        krunchSettings.iterations = iterations
    }

    fun getIterations(): Int {
        return krunchSettings.iterations
    }

    fun setSpeculativeContactDistance(speculativeContactDistance: Double) {
        krunchSettings.speculativeContactDistance = speculativeContactDistance
    }

    fun getSpeculativeContactDistance(): Double {
        return krunchSettings.speculativeContactDistance
    }

    fun getSolverType(): String {
        return when (krunchSettings.solverType) {
            SolverType.GAUSS_SEIDEL -> "gauss_seidel"
            SolverType.JACOBI -> "jacobi"
        }
    }

    fun setSolverType(solverType: String) {
        when (solverType) {
            "gauss_seidel" -> krunchSettings.solverType = SolverType.GAUSS_SEIDEL
            "jacobi" -> krunchSettings.solverType = SolverType.JACOBI
            else -> throw IllegalArgumentException("Unknown solver type $solverType")
        }
    }

    fun setMaxCollisionPoints(maxCollisionPoints: Int) {
        krunchSettings.maxCollisionPoints = maxCollisionPoints
    }

    fun getMaxCollisionPoints(): Int {
        return krunchSettings.maxCollisionPoints
    }

    fun setMaxCollisionPointDepth(maxCollisionPointDepth: Double) {
        krunchSettings.maxCollisionPointDepth = maxCollisionPointDepth
    }

    fun getMaxCollisionPointDepth(): Double {
        return krunchSettings.maxCollisionPointDepth
    }
}
