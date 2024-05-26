package de.jugda

import de.jugda.nn.ArrayValues
import de.jugda.nn.DenseNet
import de.jugda.nn.Layer
import de.jugda.nn.Neuron

private fun List<ArrayValues>.getBy(name: String): FloatArray {
    firstOrNull { neuron -> neuron.unique_parameter_name == name }?.let {
        return@getBy FloatArray(it.array_values.values.size) { i -> it.array_values.values[i].toFloat() }
    }
    return FloatArray(0)
}

fun create(weightAndBiases: List<ArrayValues>, neuronsInHiddenLayer: Int): DenseNet {
    val input = Layer(
        listOf(
            Neuron(
                FloatArray(0),
                0.0f
            ) { value -> value }
        )
    )
    val relu = { x: Float -> if (x > 0) x else 0.0f }
    val hidden1 = Layer(
        List(neuronsInHiddenLayer) { index ->
            Neuron(
                floatArrayOf(weightAndBiases.getBy("layer1.weight")[index]),
                weightAndBiases.getBy("layer1.bias")[index],
                relu
            )
        }
    )
    val allWeights = weightAndBiases.getBy("layer2.weight")
    val hidden2 = Layer(
        List(neuronsInHiddenLayer) { index ->
            val neuronWeights = allWeights.copyOfRange(
                index * neuronsInHiddenLayer,
                index * neuronsInHiddenLayer + neuronsInHiddenLayer
            )
            Neuron(
                neuronWeights,
                weightAndBiases.getBy("layer2.bias")[index],
                relu
            )
        }
    )
    val output = Layer(
        listOf(
            Neuron(
                weightAndBiases.getBy("output_layer.weight"),
                weightAndBiases.getBy("output_layer.bias")[0],
                relu
            )
        )
    )

    return DenseNet(listOf(input, hidden1, hidden2, output))
}