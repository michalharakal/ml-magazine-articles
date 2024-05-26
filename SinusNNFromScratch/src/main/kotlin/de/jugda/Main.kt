package de.jugda

import de.jugda.nn.loadWeights
import java.nio.file.Paths

fun main() {

    val urlEmbedding = Thread.currentThread().contextClassLoader.getResource("sinus_aproximator_weights.json")
    //val urlEmbedding = Thread.currentThread().contextClassLoader.getResource("simple_wandb.json")
    val uriEmbedding = urlEmbedding?.toURI() ?: throw IllegalArgumentException("File not found in resources.")
    val pathEmbedding = Paths.get(uriEmbedding)

    val weightAndBiases = loadWeights(pathEmbedding.toFile())
    val nn = create(weightAndBiases, 16)
    print(nn.forward(floatArrayOf((kotlin.math.PI).toFloat()))[0])
}