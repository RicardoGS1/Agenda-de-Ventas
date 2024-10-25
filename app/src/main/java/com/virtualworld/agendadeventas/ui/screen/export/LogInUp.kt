package com.virtualworld.agendadeventas.ui.screen.export

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.virtualworld.agendadeventas.R

@Composable
fun LogInUp(
    authenticateUser: (nameUser: String, password: String, isNewUser: Boolean) -> Unit,
) {

    val userName = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }


    Column() {

        Text(
            text = stringResource(id = R.string.export_registrarse),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onPrimary,
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp)),
            value = userName.value,
            onValueChange = { userName.value = it },
            label = {
                Text(text = stringResource(id = R.string.export_correo))
            })


        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp)),
            value = password.value,
            onValueChange = { password.value = it },
            label = {
                Text(stringResource(id = R.string.export_contracena))
            })

        TextButton(modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = { authenticateUser(userName.value, password.value, false) }) {
            Text(
                text = stringResource(id = R.string.export_iniciar_secion),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        TextButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = { authenticateUser(userName.value, password.value, true) },
        ) {
            Text(
                text = stringResource(id = R.string.crear_cuenta),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

    }
}