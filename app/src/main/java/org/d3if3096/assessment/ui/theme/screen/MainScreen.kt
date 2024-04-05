package org.d3if3096.assessment.ui.theme.screen

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3096.assessment.R
import org.d3if3096.assessment.navigation.Screen
import org.d3if3096.assessment.ui.theme.theme.AssessmentTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {navController.navigate(Screen.About.route)}) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(id = R.string.tentang_aplikasi),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) {padding ->

        ScreenContent(Modifier.padding(padding))

    }
}

@Composable
fun ScreenContent(modifier: Modifier){
    var jumlah by rememberSaveable {
        mutableStateOf("")
    }
    var jumlahError by rememberSaveable {
        mutableStateOf(false)
    }

    var harga by rememberSaveable {
        mutableStateOf("")
    }
    var hargaError by rememberSaveable {
        mutableStateOf(false)
    }

    var total by rememberSaveable {
        mutableFloatStateOf(0f)
    }
    val context = LocalContext.current
    Column (
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(17.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(
            text = stringResource(id = R.string.penjelasan),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = jumlah,
            onValueChange = { jumlah = it },
            label = { Text(text = stringResource(id = R.string.jumlah)) },
            isError = jumlahError,
            trailingIcon = { IconPick(isError = jumlahError, unit = "Kg") },
            supportingText = { ErrorHint(isError = jumlahError) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )
        OutlinedTextField(
            value = harga,
            onValueChange = { harga = it },
            label = { Text(text = stringResource(id = R.string.harga)) },
            isError = hargaError,
            trailingIcon = { IconPick(isError = hargaError, unit = "Rp") },
            supportingText = { ErrorHint(isError = hargaError) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )
        Button(
            onClick = {
                jumlahError = (jumlah == "" || jumlah == "0")
                hargaError = (harga == "" || harga == "0")

                if (jumlahError || hargaError) return@Button


                total = hitungTotal(jumlah.toFloat(), harga.toFloat())
            })
        {
            Text(text = stringResource(id = R.string.hitung))

        }
        if ( total != 0f){
            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp
            )

            Text(text = stringResource(id = R.string.total, total))
            Button(
                onClick = {
                    shareData(
                        context = context,
                        message = context.getString(R.string.isi_bagikan,total)
                    )
                },
                modifier = Modifier.padding(top = 8.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(text = stringResource(id = R.string.bagikan))
            }
        }

    }
}

private fun shareData(context: Context, message: String){
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT,message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null){
        context.startActivity(shareIntent)
    }
}


private fun hitungTotal(jumlah: Float, harga: Float): Float{
    return jumlah * harga
}

@Composable
fun IconPick( isError: Boolean, unit: String){
    if (isError){
        Icon(
            imageVector = Icons.Filled.Warning,
            contentDescription = null
        )
    }else{
        Text(text = unit)
    }
}

@Composable
fun ErrorHint ( isError: Boolean) {
    if (isError){
        Text(text = stringResource(id = R.string.input_invalid))
    }
}



@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun GreetingPreview() {
    AssessmentTheme {

        MainScreen(rememberNavController())
    }
}