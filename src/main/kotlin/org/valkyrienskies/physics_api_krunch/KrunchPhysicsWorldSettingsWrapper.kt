package org.valkyrienskies.physics_api_krunch

import org.valkyrienskies.krunch.KrunchPhysicsWorldSettings

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

    fun setRestitutionCorrectionIterations(restitutionCorrectionIterations: Int) {
        krunchSettings.restitutionCorrectionIterations = restitutionCorrectionIterations
    }

    fun getRestitutionCorrectionIterations(): Int {
        return krunchSettings.restitutionCorrectionIterations
    }

    fun setSpeculativeContactDistance(speculativeContactDistance: Double) {
        krunchSettings.speculativeContactDistance = speculativeContactDistance
    }

    fun getSpeculativeContactDistance(): Double {
        return krunchSettings.speculativeContactDistance
    }
}
