package component

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.request.*
import org.jetbrains.skia.Image
import java.io.FileOutputStream
import java.net.URL
import org.apache.commons.io.FilenameUtils
import java.io.File
import androidx.compose.foundation.Image as ComposeImage


data class BitmapCaching(
    val Url: String,
    val Bitmap: ImageBitmap
){
    companion object{
        var bitmapCaching: MutableList<BitmapCaching> = mutableListOf()

        init {
            val file = File(System.getProperty("user.dir") + "/cache")
            if(!file.exists()) file.mkdirs()
        }
    }
}

fun writeByteToFile(path: String, byteArray: ByteArray){
    FileOutputStream(path).use { fos -> fos.write(byteArray) }
}

suspend fun loadPicture(url: String, saveToFile: Boolean = false): Result<ImageBitmap> {
    val urlImage = URL(url).path
    val imageName = FilenameUtils.getBaseName(urlImage)
    val imageExtension = if(FilenameUtils.getExtension(urlImage) != "") FilenameUtils.getExtension(urlImage) else "jpg"
    val pathImage = System.getProperty("user.dir") + "/cache/$imageName.$imageExtension"
    println(pathImage)

    BitmapCaching.bitmapCaching.forEach {
        if(it.Url.contentEquals(url)){
            return Result.success(it.Bitmap)
        }
    }

    val client = HttpClient(Apache)
    return try {
        val image = client.get<ByteArray>(url)
        val bitmap = Image.makeFromEncoded(image).asImageBitmap()
        BitmapCaching.bitmapCaching.add(BitmapCaching(url, bitmap))
        if(saveToFile) writeByteToFile(pathImage, image)

        Result.success(bitmap)
    } catch (e: Exception) {
        Result.failure(e)
    }
}

enum class AsyncImageLoading{
    Loaded,
    Loading
}

@ExperimentalAnimationApi
@Composable
fun AsyncImage(
    url: String,
    saveToFile: Boolean = false,
    contentScale: ContentScale = ContentScale.FillWidth,
    modifier: Modifier = Modifier,
) {
    var imageBitmap: ImageBitmap? by remember { mutableStateOf(null) }
    var loadingState by remember { mutableStateOf(AsyncImageLoading.Loading) }

    LaunchedEffect(url) {
        loadingState = AsyncImageLoading.Loading
        loadPicture(url)
            .onSuccess {
                imageBitmap = it
            }
        loadingState = AsyncImageLoading.Loaded
    }

    Crossfade(targetState = loadingState){ state ->
        when(state){
            AsyncImageLoading.Loaded -> {
                imageBitmap?.let { bitmap ->
                    ComposeImage(
                        bitmap = bitmap,
                        contentDescription = "",
                        contentScale = contentScale,
                        alpha = DefaultAlpha,
                        colorFilter = null,
                        modifier = modifier
                    )
                }
            }
            AsyncImageLoading.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}