package br.com.fiap.recipes.screens

import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Patterns
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.recipes.R
import br.com.fiap.recipes.model.User
import br.com.fiap.recipes.navigation.Destination
import br.com.fiap.recipes.repository.RoomUserRepository
import br.com.fiap.recipes.repository.SharedPreferencesUserRepository
import br.com.fiap.recipes.ui.theme.RecipesTheme
import br.com.fiap.recipes.utils.convertBitmapToByteArray

@Composable
fun SignUpScreen(navController: NavHostController) {

    val context = LocalContext.current

    // variável que armazenará a imagem padrão de perfil do user
    val placeholderImage = BitmapFactory
        .decodeResource(
            Resources.getSystem(),
            android.R.drawable.ic_menu_gallery
        )

    // imagem do profile do user em uma variável bitmap
    var profileImage by remember {
        mutableStateOf<Bitmap>(placeholderImage)
    }

    // criando lançador de atividade para abrir a galeria
    val launchImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {uri ->
        if (Build.VERSION.SDK_INT < 28){
            profileImage = MediaStore
                .Images
                .Media
                .getBitmap(
                    context.contentResolver,
                    uri
                )
        } else {
            if (uri != null){
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                profileImage = ImageDecoder.decodeBitmap(source)
            } else{
                profileImage = placeholderImage
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background
            )
    ) {
        TopEndCard(modifier = Modifier.align(Alignment.TopEnd))
        BottomStartCard(modifier = Modifier.align(Alignment.BottomStart))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleComponent()
            Spacer(modifier = Modifier.height(48.dp))
            UserImage(profileImage, launchImage)
            SignUpUserForm(navController, profileImage)
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    locale = "en",
    showSystemUi = true
)
@Composable
private fun SignUpScreenPreview() {
    RecipesTheme {
        SignUpScreen(rememberNavController())
    }

}

@Composable
fun TitleComponent(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.sign_up_title),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge

        )

        Text(
            text = stringResource(R.string.sign_up_subtitle),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun TitleComponentPreview() {
    RecipesTheme {
        TitleComponent()
    }
}

@Composable
fun UserImage(profileImage: Bitmap, launchImage: ManagedActivityResultLauncher<String, Uri?>) {
    Box(
        modifier = Modifier
            .size(120.dp)
    ) {
        Image(
            bitmap = profileImage.asImageBitmap(),
            contentDescription = stringResource(R.string.user_image),
            modifier = Modifier
                .clip(shape = CircleShape)
                .size(110.dp)
                .align(alignment = Alignment.Center),
            contentScale = ContentScale.Crop
        )
        Icon(
            imageVector = Icons.Default.AddAPhoto,
            contentDescription = stringResource(R.string.camera_icon),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(alignment = Alignment.BottomEnd)
                .clickable(
                    onClick = {
                        launchImage.launch("image/*")
                    }
                )
        )
    }
}

@Preview
@Composable
private fun UserImagePreview() {
    RecipesTheme {
//        UserImage(profileImage, launchImage)
    }
}

@Composable
fun SignUpUserForm(navController: NavController, profileImage: Bitmap) {

    var name by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    // variáveis para fazer a verificação se os dados estão corretos (geralmente isso é feito por meio de uma API)
    var isNameError by remember { mutableStateOf(false) }
    var isEmailError by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }

    // variável de estado para controlar exibição da mensagem de erro
    var showDialogueError by remember { mutableStateOf(false) }
    var showDialogueSuccess by remember { mutableStateOf(false) }

    // função para verificar se os dados estão corretos
    fun validateSignUp(): Boolean {
        isNameError = name.length < 3
        isEmailError = email.length < 6 || !Patterns.EMAIL_ADDRESS.matcher(email).matches()
        isPasswordError = password.length < 8

        return !isNameError && !isEmailError && !isPasswordError
    }

    // criando uma instância do SharedPreferencesUserRepository
    //val userRepository = SharedPreferencesUserRepository(LocalContext.current)
    val userRepository = RoomUserRepository(LocalContext.current)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
    ) {
        //  Caixa de texto para inserir o nome
        OutlinedTextField(
            value = name,
            onValueChange = { nameValue ->
                name = nameValue
            },
            modifier = Modifier
                .fillMaxWidth(),
            label = {
                Text(
                    text = stringResource(R.string.your_name),
                    style = MaterialTheme.typography.labelSmall
                )
            },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults
                .colors(
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary
                ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = stringResource(R.string.person_icon),
                    tint = MaterialTheme.colorScheme.tertiary
                )
            },

            isError = isNameError,
            trailingIcon = {
                if (isNameError) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = stringResource(R.string.error),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            },
            supportingText = {
                if (isNameError) {
                    Text(
                        text = stringResource(R.string.name_error),
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        )

        // Caixa para e-mail do usuário
        OutlinedTextField(
            value = email,
            onValueChange = { emailValue ->
                email = emailValue
            },
            modifier = Modifier
                .fillMaxWidth(),
            label = {
                Text(
                    text = stringResource(R.string.your_e_mail),
                    style = MaterialTheme.typography.labelSmall
                )
            },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults
                .colors(
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary
                ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = stringResource(R.string.e_mail_icon),
                    tint = MaterialTheme.colorScheme.tertiary
                )
            },
            isError = isEmailError,
            trailingIcon = {
                if (isEmailError) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = stringResource(R.string.error),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            },
            supportingText = {
                if (isEmailError) {
                    Text(
                        text = stringResource(R.string.email_error),
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        )

        // Caixa de texto para a senha
        OutlinedTextField(
            value = password,
            onValueChange = { passwordValue ->
                password = passwordValue
            },
            modifier = Modifier
                .fillMaxWidth(),
            label = {
                Text(
                    text = stringResource(R.string.your_password),
                    style = MaterialTheme.typography.labelSmall
                )
            },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults
                .colors(
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary
                ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = stringResource(R.string.lock_icon),
                    tint = MaterialTheme.colorScheme.tertiary
                )
            },
//            trailingIcon = {
//                Icon(
//                    imageVector = Icons.Default.RemoveRedEye,
//                    contentDescription = stringResource(R.string.eye_icon),
//                    tint = MaterialTheme.colorScheme.tertiary
//                )
//            },
            isError = isPasswordError,
            trailingIcon = {
                if (isPasswordError) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = stringResource(R.string.error),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            },
            supportingText = {
                if (isPasswordError) {
                    Text(
                        text = stringResource(R.string.password_error),
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (validateSignUp()) {
                    userRepository.saveUser(
                        User(
                            name = name,
                            email = email,
                            password = password,
                            userImage = convertBitmapToByteArray(profileImage)
                        )
                    )

                    showDialogueSuccess = true
                } else {
                    showDialogueError
                }


            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = stringResource(R.string.create_account),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }

    // mensagem de diálogo de sucesso
    if (showDialogueSuccess) {
        AlertDialog(
            onDismissRequest = { showDialogueError = false },
            title = {
                Text(
                    text = stringResource(R.string.success),
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.account_signup_success)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        navController
                            .navigate(
                                Destination.LoginScreen.route
                            )
                    }
                ) {
                    Text(text = "OK")
                }
            }
        )
    }

    // mensagem de diálogo de erro
    if (showDialogueError) {
        AlertDialog(
            onDismissRequest = { showDialogueError = false },
            title = {
                Text(
                    text = stringResource(R.string.error),
                    color = MaterialTheme.colorScheme.error
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.signup_error_msg)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialogueError = false
                    }
                ) {
                    Text(text = "OK")
                }
            }

        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun SignUpUserFormPreview() {
    RecipesTheme {
//        SignUpUserForm(rememberNavController(), profileImage)
    }
}