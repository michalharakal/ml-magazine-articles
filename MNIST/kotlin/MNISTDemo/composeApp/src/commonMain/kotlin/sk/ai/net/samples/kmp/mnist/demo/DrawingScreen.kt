package sk.ai.net.samples.kmp.mnist.demo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kkon.kmp.mnist.demo.DigitClassifier
import kotlinx.coroutines.launch
import kotlinx.io.Source
import mnistdemo.composeapp.generated.resources.Res
import mnistdemo.composeapp.generated.resources.img_1
import mnistdemo.composeapp.generated.resources.img_10
import mnistdemo.composeapp.generated.resources.img_11
import mnistdemo.composeapp.generated.resources.img_12
import mnistdemo.composeapp.generated.resources.img_13
import mnistdemo.composeapp.generated.resources.img_14
import mnistdemo.composeapp.generated.resources.img_15
import mnistdemo.composeapp.generated.resources.img_16
import mnistdemo.composeapp.generated.resources.img_17
import mnistdemo.composeapp.generated.resources.img_18
import mnistdemo.composeapp.generated.resources.img_19
import mnistdemo.composeapp.generated.resources.img_2
import mnistdemo.composeapp.generated.resources.img_20
import mnistdemo.composeapp.generated.resources.img_21
import mnistdemo.composeapp.generated.resources.img_22
import mnistdemo.composeapp.generated.resources.img_23
import mnistdemo.composeapp.generated.resources.img_24
import mnistdemo.composeapp.generated.resources.img_25
import mnistdemo.composeapp.generated.resources.img_26
import mnistdemo.composeapp.generated.resources.img_27
import mnistdemo.composeapp.generated.resources.img_28
import mnistdemo.composeapp.generated.resources.img_29
import mnistdemo.composeapp.generated.resources.img_3
import mnistdemo.composeapp.generated.resources.img_30
import mnistdemo.composeapp.generated.resources.img_31
import mnistdemo.composeapp.generated.resources.img_32
import mnistdemo.composeapp.generated.resources.img_33
import mnistdemo.composeapp.generated.resources.img_34
import mnistdemo.composeapp.generated.resources.img_35
import mnistdemo.composeapp.generated.resources.img_36
import mnistdemo.composeapp.generated.resources.img_37
import mnistdemo.composeapp.generated.resources.img_38
import mnistdemo.composeapp.generated.resources.img_39
import mnistdemo.composeapp.generated.resources.img_4
import mnistdemo.composeapp.generated.resources.img_40
import mnistdemo.composeapp.generated.resources.img_41
import mnistdemo.composeapp.generated.resources.img_42
import mnistdemo.composeapp.generated.resources.img_43
import mnistdemo.composeapp.generated.resources.img_44
import mnistdemo.composeapp.generated.resources.img_45
import mnistdemo.composeapp.generated.resources.img_46
import mnistdemo.composeapp.generated.resources.img_47
import mnistdemo.composeapp.generated.resources.img_48
import mnistdemo.composeapp.generated.resources.img_49
import mnistdemo.composeapp.generated.resources.img_5
import mnistdemo.composeapp.generated.resources.img_50
import mnistdemo.composeapp.generated.resources.img_51
import mnistdemo.composeapp.generated.resources.img_52
import mnistdemo.composeapp.generated.resources.img_53
import mnistdemo.composeapp.generated.resources.img_54
import mnistdemo.composeapp.generated.resources.img_55
import mnistdemo.composeapp.generated.resources.img_56
import mnistdemo.composeapp.generated.resources.img_57
import mnistdemo.composeapp.generated.resources.img_58
import mnistdemo.composeapp.generated.resources.img_59
import mnistdemo.composeapp.generated.resources.img_6
import mnistdemo.composeapp.generated.resources.img_60
import mnistdemo.composeapp.generated.resources.img_61
import mnistdemo.composeapp.generated.resources.img_62
import mnistdemo.composeapp.generated.resources.img_63
import mnistdemo.composeapp.generated.resources.img_64
import mnistdemo.composeapp.generated.resources.img_65
import mnistdemo.composeapp.generated.resources.img_66
import mnistdemo.composeapp.generated.resources.img_67
import mnistdemo.composeapp.generated.resources.img_68
import mnistdemo.composeapp.generated.resources.img_69
import mnistdemo.composeapp.generated.resources.img_7
import mnistdemo.composeapp.generated.resources.img_70
import mnistdemo.composeapp.generated.resources.img_71
import mnistdemo.composeapp.generated.resources.img_72
import mnistdemo.composeapp.generated.resources.img_73
import mnistdemo.composeapp.generated.resources.img_74
import mnistdemo.composeapp.generated.resources.img_75
import mnistdemo.composeapp.generated.resources.img_76
import mnistdemo.composeapp.generated.resources.img_77
import mnistdemo.composeapp.generated.resources.img_78
import mnistdemo.composeapp.generated.resources.img_79
import mnistdemo.composeapp.generated.resources.img_8
import mnistdemo.composeapp.generated.resources.img_80
import mnistdemo.composeapp.generated.resources.img_81
import mnistdemo.composeapp.generated.resources.img_82
import mnistdemo.composeapp.generated.resources.img_83
import mnistdemo.composeapp.generated.resources.img_84
import mnistdemo.composeapp.generated.resources.img_85
import mnistdemo.composeapp.generated.resources.img_86
import mnistdemo.composeapp.generated.resources.img_87
import mnistdemo.composeapp.generated.resources.img_88
import mnistdemo.composeapp.generated.resources.img_89
import mnistdemo.composeapp.generated.resources.img_9
import mnistdemo.composeapp.generated.resources.img_90
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.painterResource
import kotlin.math.ceil
import kotlin.math.max

typealias ARGB = Int

// List of image resources
val IMAGE_RESOURCES = listOf(
    Res.drawable.img_1,
    Res.drawable.img_2,
    Res.drawable.img_3,
    Res.drawable.img_4,
    Res.drawable.img_5,
    Res.drawable.img_6,
    Res.drawable.img_7,
    Res.drawable.img_8,
    Res.drawable.img_9,
    Res.drawable.img_10,
    Res.drawable.img_11,
    Res.drawable.img_12,
    Res.drawable.img_13,
    Res.drawable.img_14,
    Res.drawable.img_15,
    Res.drawable.img_16,
    Res.drawable.img_17,
    Res.drawable.img_18,
    Res.drawable.img_19,
    Res.drawable.img_20,
    Res.drawable.img_21,
    Res.drawable.img_22,
    Res.drawable.img_23,
    Res.drawable.img_24,
    Res.drawable.img_25,
    Res.drawable.img_26,
    Res.drawable.img_27,
    Res.drawable.img_28,
    Res.drawable.img_29,
    Res.drawable.img_30,
    Res.drawable.img_31,
    Res.drawable.img_32,
    Res.drawable.img_33,
    Res.drawable.img_34,
    Res.drawable.img_35,
    Res.drawable.img_36,
    Res.drawable.img_37,
    Res.drawable.img_38,
    Res.drawable.img_39,
    Res.drawable.img_40,
    Res.drawable.img_41,
    Res.drawable.img_42,
    Res.drawable.img_43,
    Res.drawable.img_44,
    Res.drawable.img_45,
    Res.drawable.img_46,
    Res.drawable.img_47,
    Res.drawable.img_48,
    Res.drawable.img_49,
    Res.drawable.img_50,
    Res.drawable.img_51,
    Res.drawable.img_52,
    Res.drawable.img_53,
    Res.drawable.img_54,
    Res.drawable.img_55,
    Res.drawable.img_56,
    Res.drawable.img_57,
    Res.drawable.img_58,
    Res.drawable.img_59,
    Res.drawable.img_60,
    Res.drawable.img_61,
    Res.drawable.img_62,
    Res.drawable.img_63,
    Res.drawable.img_64,
    Res.drawable.img_65,
    Res.drawable.img_66,
    Res.drawable.img_67,
    Res.drawable.img_68,
    Res.drawable.img_69,
    Res.drawable.img_70,
    Res.drawable.img_71,
    Res.drawable.img_72,
    Res.drawable.img_73,
    Res.drawable.img_74,
    Res.drawable.img_75,
    Res.drawable.img_76,
    Res.drawable.img_77,
    Res.drawable.img_78,
    Res.drawable.img_79,
    Res.drawable.img_80,
    Res.drawable.img_81,
    Res.drawable.img_82,
    Res.drawable.img_83,
    Res.drawable.img_84,
    Res.drawable.img_85,
    Res.drawable.img_86,
    Res.drawable.img_87,
    Res.drawable.img_88,
    Res.drawable.img_89,
    Res.drawable.img_90
)

@Composable
fun DrawingScreen(handleSource: () -> Source) {
    // Create and remember the ViewModel with rememberSaveable to persist across recompositions
    val viewModel = remember(handleSource) { DrawingScreenViewModel(handleSource) }

    // Load the model automatically when the screen is first displayed
    LaunchedEffect(Unit) {
        viewModel.loadModel()
    }

    // Animation for loading state
    val infiniteTransition = rememberInfiniteTransition()
    val loadingAlpha = infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    val image = if (viewModel.selectedImageIndex != -1) {
        imageResource(IMAGE_RESOURCES[viewModel.selectedImageIndex])
    } else {
        null
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            // Main content area with card-based UI
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (viewModel.isChooseImageMode) {
                        ChooseImagePanel(
                            modifier = Modifier.fillMaxSize(),
                            imageResources = IMAGE_RESOURCES,
                            selectedImageIndex = viewModel.selectedImageIndex,
                            onImageSelected = { viewModel.selectImage(it) }
                        )
                    } else {
                        DrawingPanel(
                            onDragStart = { offset ->
                                viewModel.onDragStart(offset)
                            },
                            onDrag = { pointerInputChange, offset ->
                                viewModel.onDrag(pointerInputChange, offset)
                            },
                            onDragEnd = {
                                viewModel.onDragEnd()
                            },
                            onDraw = {
                                viewModel.paths.forEach { drawPath(path = it, color = Color.Black, style = Stroke(10f)) }

                                if (viewModel.currentPath != null && viewModel.currentPathRef > 0) {
                                    drawPath(path = viewModel.currentPath!!, color = Color.Black, style = Stroke(10f))
                                }
                            },
                        )
                    }

                    // Loading overlay
                    if (!viewModel.isModelLoaded) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0x80000000)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .alpha(loadingAlpha.value),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "Loading model...",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            val coroutineScope = rememberCoroutineScope()
            val density = LocalDensity.current

            ButtonsPanel(
                isChooseImage = viewModel.isChooseImageMode,
                isModelLoaded = viewModel.isModelLoaded,
                classificationResult = viewModel.classificationResult,
                onLoadModel = {
                    coroutineScope.launch {
                        viewModel.loadModel()
                    }
                },
                onSwitchMode = {
                    viewModel.switchMode()
                },
                onClearCanvas = {
                    viewModel.clearCanvas()
                },
                onClassify = {
                    val grayScaledImage = if (viewModel.isChooseImageMode) {
                        image?.let {
                            createGrayScale28To28Image(it)
                        }
                    } else {
                        val w = with(density) { 300.dp.toPx().toInt() }
                        val h = with(density) { 300.dp.toPx().toInt() }
                        createGrayScale28To28Image(viewModel.paths, w, h)
                    }

                    grayScaledImage?.debugPrintInConsoleOutput()

                    // Use the ViewModel to classify the image
                    coroutineScope.launch {
                        if (grayScaledImage != null) {
                            // For now, we'll use the ViewModel's classify method with null
                            // In a real implementation, we would pass the image bitmap
                            viewModel.classify(grayScaledImage)
                        }
                    }
                },
            )
        }
    }
}

@Composable
fun ChooseImagePanel(
    modifier: Modifier,
    imageResources: List<DrawableResource>,
    selectedImageIndex: Int,
    onImageSelected: (Int) -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Select a sample digit image",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(5), // 5 columns in the grid
            modifier = Modifier.fillMaxSize(),
            content = {
                items(imageResources.size) { index ->
                    Card(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(56.dp)
                            .clickable {
                                onImageSelected.invoke(index)
                            },
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (selectedImageIndex == index) 8.dp else 2.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedImageIndex == index) 
                                MaterialTheme.colorScheme.primaryContainer 
                            else 
                                MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(imageResources[index]),
                                contentDescription = "Sample digit image ${index + 1}",
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(4.dp)
                            )
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun DrawingPanel(
    onDragStart: (Offset) -> Unit,
    onDrag: (PointerInputChange, Offset) -> Unit,
    onDragEnd: () -> Unit,
    onDraw: DrawScope.() -> Unit,
) {
    Card(
        modifier = Modifier
            .width(300.dp)
            .height(300.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .clipToBounds()
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = onDragStart,
                            onDrag = onDrag,
                            onDragEnd = onDragEnd
                        )
                    },
                onDraw = onDraw
            )
        }
    }
}

@Composable
fun ButtonsPanel(
    isChooseImage: Boolean,
    isModelLoaded: Boolean,
    classificationResult: String?,
    onLoadModel: () -> Unit,
    onSwitchMode: () -> Unit,
    onClearCanvas: () -> Unit,
    onClassify: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // FAB Row with modern design
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Switch Mode Button
            FloatingActionButton(
                onClick = onSwitchMode,
                containerColor = if (isChooseImage) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary
            ) {
                Text(
                    text = if (isChooseImage) "Select" else "Images",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // Load Model or Classify Button
            if (!isModelLoaded) {
                FloatingActionButton(
                    onClick = onLoadModel,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        text = "Load Model",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            } else {
                FloatingActionButton(
                    onClick = onClassify,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        text = "Classify",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            // Clear Canvas Button
            FloatingActionButton(
                onClick = onClearCanvas,
                containerColor = MaterialTheme.colorScheme.error
            ) {
                Text(
                    text = "Clear",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        // Result display with fixed height to prevent layout jumps
        Box(
            modifier = Modifier
                .height(80.dp)  // Fixed height container
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            // Only show the card when there's a result
            if (classificationResult != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = classificationResult,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

fun createGrayScale28To28Image(imageBitmap: ImageBitmap): DigitClassifier.GrayScale28To28Image {
    val image = DigitClassifier.GrayScale28To28Image()

    val pixelMap = imageBitmap.toPixelMap()
    val width = imageBitmap.width
    val height = imageBitmap.height

    val zoom = ceil(max(width, height) / 28f).toInt()

    for (y in 0 until height / zoom) {
        for (x in 0 until width / zoom) {
            val shift = List(zoom) { row -> List(zoom) { col -> row to col } }.flatten()

            val grayScale = shift
                .map { pixelMap[x * zoom + it.first, y * zoom + it.second] }
                .maxOfOrNull { it.toArgb().toGrayScale() } ?: 0f

            image.setPixel(x, y, grayScale)
        }
    }

    return image
}

fun createGrayScale28To28Image(
    paths: List<Path>,
    widthInPixel: Int,
    heightInPixel: Int
): DigitClassifier.GrayScale28To28Image {
    println("createGrayScale28To28Image: $widthInPixel x $heightInPixel")

    val imageBitmap = ImageBitmap(widthInPixel, heightInPixel, ImageBitmapConfig.Argb8888).apply {
        val canvas = androidx.compose.ui.graphics.Canvas(this)

        val paint = Paint().apply {
            this.color = Color.Black
            this.style = PaintingStyle.Stroke
            this.strokeWidth = 10f
        }

        paths.forEach { path ->
            canvas.drawPath(path, paint)
        }
    }

    return DigitClassifier.GrayScale28To28Image().apply {

        val pixelMap = imageBitmap.toPixelMap()
        val width = imageBitmap.width
        val height = imageBitmap.height

        val zoom = ceil(max(width, height) / 28f).toInt()

        for (y in 0 until height / zoom) {
            for (x in 0 until width / zoom) {
                val shift = List(zoom) { row -> List(zoom) { col -> row to col } }.flatten()

                val grayScale = shift
                    .map { pixelMap[x * zoom + it.first, y * zoom + it.second] }
                    .maxOfOrNull { if (it.toArgb() != 0) 1f else 0f } ?: 0f

                this.setPixel(x, y, grayScale)
            }
        }
    }
}

fun ARGB.toGrayScale(): Float {
    val r = (this shr 16) and 0xFF
    val g = (this shr 8) and 0xFF
    val b = this and 0xFF

    // Standard luminance formula for grayscale
    val gray = 0.299f * r + 0.587f * g + 0.114f * b

    // Normalize to 0..1 range
    return gray / 255f
}
