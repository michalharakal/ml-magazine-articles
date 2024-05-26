package de.jugda.nn

import kotlinx.serialization.json.Json
import java.io.File
import kotlinx.serialization.Serializable

@Serializable
data class ArrayValues(
    val unique_parameter_name: String,
    val array_values: ArrayValue
)

@Serializable
data class ArrayValue(
    val values: List<Double>
)

fun loadWeights(jsonFile: File):List<ArrayValues> {
    // Example: Loading JSON from a file
    var jsonString = ""
    try {
        jsonString = jsonFile.readText(Charsets.UTF_8)
    } catch (oome: OutOfMemoryError) {
        //Log the info
        System.err.println("Array size too large")
        System.err.println("Max JVM memory: " + Runtime.getRuntime().maxMemory())
    }

    // Initialize Json object
    val json = Json { ignoreUnknownKeys = true }

    // Deserialize JSON to Kotlin objects
    return json.decodeFromString(jsonString)
}