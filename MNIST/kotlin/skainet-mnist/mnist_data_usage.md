# MNIST Data Usage Guide

## Introduction

This guide explains how to use the MNIST dataset with the skainet library. The MNIST dataset is a collection of handwritten digits commonly used for training image processing systems and machine learning models.

## Setup

### Adding Dependencies

To use the MNIST data loader in your project, make sure you have the `model-zoo` module as a dependency:

```kotlin
// In your build.gradle.kts
dependencies {
    implementation(project(":model-zoo"))
}
```

### Configuration

The MNIST loader can be configured with the following options:

```kotlin
// Default configuration
val defaultConfig = MNISTLoaderConfig()

// Custom configuration
val customConfig = MNISTLoaderConfig(
    cacheDir = "custom-cache-dir", // Directory to store downloaded files
    useCache = true                // Whether to use cached files if available
)
```

## Downloading the Data

The MNIST loader handles downloading and caching the dataset automatically. Here's how to use it:

```kotlin
// Assuming these imports:
// import sk.ai.net.io.data.mnist.MNISTLoaderFactory
// import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    // Create a loader with default configuration
    val loader = MNISTLoaderFactory.create()

    // Or with custom configuration
    // val loader = MNISTLoaderFactory.create("custom-cache-dir")
    // val loader = MNISTLoaderFactory.create(customConfig)

    // Load training data
    val trainingData = loader.loadTrainingData()
    println("Loaded ${trainingData.size} training images")

    // Load test data
    val testData = loader.loadTestData()
    println("Loaded ${testData.size} test images")
}
```

The first time you run this code, it will download the MNIST dataset from the internet. Subsequent runs will use the cached files if available.

## Converting Data to Tensors

To use the MNIST data with neural networks, you need to convert the images to tensors:

```kotlin
// Assuming these imports:
// import sk.ai.net.Shape
// import sk.ai.net.impl.DoublesTensor
// import sk.ai.net.io.data.mnist.MNISTConstants
// import sk.ai.net.io.data.mnist.MNISTImage

// Convert a single image to a tensor
fun convertImageToTensor(image: MNISTImage): DoublesTensor {
    // Normalize pixel values to range [0, 1]
    val normalizedPixels = DoubleArray(MNISTConstants.IMAGE_PIXELS) { i ->
        (image.image[i].toInt() and 0xFF) / 255.0
    }

    // Create a tensor with shape [1, 28, 28]
    return DoublesTensor(
        Shape(1, MNISTConstants.IMAGE_SIZE, MNISTConstants.IMAGE_SIZE),
        normalizedPixels
    )
}

// Convert a label to a one-hot encoded tensor
fun convertLabelToTensor(label: Byte): DoublesTensor {
    val oneHot = DoubleArray(10) { 0.0 }
    oneHot[label.toInt()] = 1.0

    // Create a tensor with shape [10]
    return DoublesTensor(Shape(10), oneHot)
}
```

## Batch Processing for Training

For efficient training, you'll want to process the data in batches:

```kotlin
// Assuming these imports:
// import sk.ai.net.io.data.mnist.MNISTDataset

// Create batches from a dataset
fun createBatches(dataset: MNISTDataset, batchSize: Int): List<List<MNISTImage>> {
    return dataset.images.chunked(batchSize)
}

// Process a batch
fun processBatch(batch: List<MNISTImage>): Pair<DoublesTensor, DoublesTensor> {
    // Create input tensor with shape [batchSize, 28, 28]
    val inputShape = Shape(batch.size, MNISTConstants.IMAGE_SIZE, MNISTConstants.IMAGE_SIZE)
    val inputData = DoubleArray(batch.size * MNISTConstants.IMAGE_PIXELS)

    // Create output tensor with shape [batchSize, 10]
    val outputShape = Shape(batch.size, 10)
    val outputData = DoubleArray(batch.size * 10)

    batch.forEachIndexed { index, mnistImage ->
        // Normalize and copy image data
        for (i in 0 until MNISTConstants.IMAGE_PIXELS) {
            inputData[index * MNISTConstants.IMAGE_PIXELS + i] = 
                (mnistImage.image[i].toInt() and 0xFF) / 255.0
        }

        // One-hot encode the label
        outputData[index * 10 + mnistImage.label.toInt()] = 1.0
    }

    return Pair(
        DoublesTensor(inputShape, inputData),
        DoublesTensor(outputShape, outputData)
    )
}
```

## Complete Example

Here's a complete example showing how to load the MNIST data, create batches, and convert them to tensors for training:

```kotlin
// Assuming these imports:
// import kotlinx.coroutines.runBlocking
// import sk.ai.net.io.data.mnist.MNISTLoaderFactory
// import sk.ai.net.Shape
// import sk.ai.net.impl.DoublesTensor
// import sk.ai.net.io.data.mnist.MNISTConstants

fun main() = runBlocking {
    // Create a loader
    val loader = MNISTLoaderFactory.create()

    // Load training data
    val trainingData = loader.loadTrainingData()
    println("Loaded ${trainingData.size} training images")

    // Create batches
    val batchSize = 32
    val batches = trainingData.images.chunked(batchSize)
    println("Created ${batches.size} batches")

    // Process the first batch
    val firstBatch = batches[0]
    val (inputTensor, outputTensor) = processBatch(firstBatch)

    println("Input tensor shape: ${inputTensor.shape}")
    println("Output tensor shape: ${outputTensor.shape}")

    // Now you can use these tensors for training your neural network
    // network.train(inputTensor, outputTensor)
}
```

## Tips for Efficient Training

1. **Shuffle the data**: Before creating batches, shuffle the dataset to ensure that each batch contains a diverse set of examples.

```kotlin
val shuffledImages = trainingData.images.shuffled()
val batches = shuffledImages.chunked(batchSize)
```

2. **Data augmentation**: To improve model generalization, consider applying transformations to the images, such as small rotations or shifts.

3. **Validation set**: Split your training data into a training set and a validation set to monitor for overfitting.

```kotlin
val validationSplit = 0.1 // 10% for validation
val splitIndex = (trainingData.size * (1 - validationSplit)).toInt()
val trainingSet = trainingData.subset(0, splitIndex)
val validationSet = trainingData.subset(splitIndex, trainingData.size)
```

4. **Caching**: The MNIST loader automatically caches downloaded files. You can also cache the processed tensors to speed up training across multiple epochs.

## Conclusion

This guide showed you how to set up, download, and convert MNIST data into tensors for training neural networks using the skainet library. The multiplatform implementation allows you to use the same code on JVM, Android, iOS, and WASM JS platforms.
