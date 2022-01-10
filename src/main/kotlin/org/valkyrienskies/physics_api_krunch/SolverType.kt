package org.valkyrienskies.physics_api_krunch

/**
 * The [GAUSS_SEIDEL] solver immediately updates the state of the simulation after each constraint.
 * This converges faster but introduces order dependencies (ie stacks of boxes won't behave well).
 * It cannot easily be made parallel.
 *
 * The [JACOBI] solver solves for all constraints, and then updates the state of the simulation once all constraints
 * are solved, applying them all simultaneously. This converges slower, but removes order dependencies
 * (ie stacks of boxes will behave well).
 * It can easily be made parallel.
 */
enum class SolverType(val solverName: String) {
    GAUSS_SEIDEL("gauss_seidel"), JACOBI("jacobi")
}
