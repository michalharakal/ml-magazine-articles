package sk.ai.net.nn.topology

import sk.ai.net.Tensor
import sk.ai.net.nn.Module
import sk.ai.net.nn.reflection.ModuleParameter
import sk.ai.net.nn.reflection.ModuleParameters

/**
 * A module that processes inputs through multiple parallel paths and then combines the results.
 * This is similar to attention heads in transformer models.
 *
 * @param parallelModules The modules to process in parallel
 * @param combineFunction A function that combines the outputs of the parallel modules
 * @param name The name of the module
 */
class ParallelModule(
    private val parallelModules: List<Module>,
    private val combineFunction: (List<Tensor>) -> Tensor,
    override val name: String = "ParallelModule"
) : Module(), ModuleParameters {

    constructor(
        vararg modules: Module,
        combineFunction: (List<Tensor>) -> Tensor,
        name: String = "ParallelModule"
    ) : this(modules.toList(), combineFunction, name)

    override val modules: List<Module>
        get() = parallelModules

    override fun forward(input: Tensor): Tensor {
        // Process the input through each module in parallel
        val outputs = parallelModules.map { module ->
            module.forward(input)
        }

        // Combine the outputs using the provided function
        return combineFunction(outputs)
    }

    override val params: List<ModuleParameter>
        get() = emptyList()
}

/**
 * Common combine functions for parallel modules
 */
object CombineFunctions {
    /**
     * Concatenates tensors along the specified dimension
     */
    fun concat(dimension: Int): (List<Tensor>) -> Tensor = { tensors ->
        // This is a placeholder. In a real implementation, this would concatenate tensors along the specified dimension.
        // For now, we'll just return the first tensor as a placeholder.
        tensors.first()
    }

    /**
     * Averages tensors element-wise
     */
    fun average(): (List<Tensor>) -> Tensor = { tensors ->
        // This is a placeholder. In a real implementation, this would average tensors element-wise.
        // For now, we'll just return the first tensor as a placeholder.
        tensors.first()
    }

    /**
     * Sums tensors element-wise
     */
    fun sum(): (List<Tensor>) -> Tensor = { tensors ->
        // This is a placeholder. In a real implementation, this would sum tensors element-wise.
        // For now, we'll just return the first tensor as a placeholder.
        tensors.first()
    }
}
