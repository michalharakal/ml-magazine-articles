package de.jugda.nn

class Neuron(private val weights: FloatArray, private val bias: Float, val activation: (Float) -> Float) {

    fun forward(inputs: FloatArray): Float {
        val sum = inputs
            .zip(weights)
            .fold(0.0f) { acc, (input, weight) ->
                acc + (input * weight)
            } + bias
        return activation(sum)
    }

    override fun toString(): String {
        return weights.joinToString(prefix = "[", postfix = "]")
    }
}

data class Layer(val neurons: List<Neuron>)

class DenseNet(private val layers: List<Layer>) {

    fun forward(inputData: FloatArray): FloatArray {
        var input = inputData
        layers.forEach { layer ->
            if (layer.neurons.isNotEmpty()) {
                val layerResult = FloatArray(layer.neurons.size)
                layer.neurons.forEachIndexed { index, neuron: Neuron ->
                    layerResult[index] = neuron.forward(input)
                }
                input = layerResult
            }
        }
        return input
    }

    override fun toString(): String {
        return layers.joinToString(prefix = "[", postfix = "]")
    }

}