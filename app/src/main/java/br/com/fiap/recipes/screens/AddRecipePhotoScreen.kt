package br.com.fiap.recipes.screens

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.recipes.R
import br.com.fiap.recipes.navigation.Destination
import br.com.fiap.recipes.repository.uploadImage
import br.com.fiap.recipes.ui.theme.RecipesTheme
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

@Composable
fun AddRecipePhotoScreen(recipeId: Int, navController: NavController) {

    val context = LocalContext.current

    // cria uma variável que armazena uma
    // imagem default para a receita
    val placeholderImage = BitmapFactory
        .decodeResource(
            Resources.getSystem(),
            android.R.drawable.ic_menu_gallery
        )

    // Armazena a imagem da receita
    // em uma variável de estado do tipo Bitmap
    var recipeImageBitmap by remember {
        mutableStateOf<Bitmap>(placeholderImage)
    }

    // variável para armazenar o arquivo da imagem
    var recipeImageFile by remember {
        mutableStateOf(File(""))
    }

    // Criar o escopo de coroutine que será utilizado
    // para chamar a gravação da imagem da receita
    val scope = rememberCoroutineScope()

    // Cria um lançador de atividade para
    // abrir a galeria de imagens
    val saveRecipeImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (Build.VERSION.SDK_INT < 28) {
            recipeImageBitmap = MediaStore
                .Images
                .Media
                .getBitmap(
                    context.contentResolver,
                    uri
                )
        } else {
            if (uri != null) {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                recipeImageBitmap = ImageDecoder.decodeBitmap(source)
            } else {
                recipeImageBitmap = placeholderImage
            }
        }

        // Converter o bitmap para File
        val recipeFileName = "${UUID.randomUUID()}.jpg"
        recipeImageFile = File(context.cacheDir, recipeFileName)

        FileOutputStream(recipeImageFile).use {
            recipeImageBitmap.compress(
                Bitmap.CompressFormat.JPEG,
                100,
                it
            )
        }

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme
                    .colorScheme.background
            )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            TopEndCard(modifier = Modifier.align(Alignment.TopEnd))
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "We're almost there...",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Last, but not least, add a really nice picture of your recipe.",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(
                        top = 2.dp,
                        bottom = 8.dp
                    )
                )
                // Card responsável por exibir a foto da receita
                Box(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .fillMaxWidth()
                        .height(230.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Image(
                                bitmap = recipeImageBitmap?.asImageBitmap()!!,
                                contentDescription = stringResource(R.string.imagem_da_receita),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .align(Alignment.Center),
                                contentScale = if (recipeImageBitmap != placeholderImage)
                                    ContentScale.Crop
                                else
                                    ContentScale.None
                            )
                        }
                    }
                    Card(
                        modifier = Modifier
                            .size(56.dp)
                            .align(Alignment.BottomCenter),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = CircleShape
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            IconButton(
                                onClick = {
                                    saveRecipeImage.launch("image/*")
                                },
                                modifier = Modifier
                                    .align(Alignment.Center)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PhotoCamera,
                                    contentDescription = "Photo Icon",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                    }
                }

            }
            TextButton(
                onClick = {
                    scope.launch {
                        uploadImage(
                            recipeId = recipeId,
                            file = recipeImageFile
                        )
                        // navega para a tela home
                        navController.navigate(Destination.HomeScreen.createRoute(""))
                    }
                },
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Text(
                    text = "FINISH",
                    style = MaterialTheme.typography.titleSmall,
                    fontSize = 28.sp
                )

            }
        }
    }
}

@Preview
@Composable
private fun AddRecipePhotoScreenPreview() {
    RecipesTheme {
        AddRecipePhotoScreen(0, rememberNavController())
    }
}