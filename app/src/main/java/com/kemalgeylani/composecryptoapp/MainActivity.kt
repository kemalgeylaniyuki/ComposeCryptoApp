package com.kemalgeylani.composecryptoapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kemalgeylani.composecryptoapp.model.CryptoModel
import com.kemalgeylani.composecryptoapp.service.CryptoAPI
import com.kemalgeylani.composecryptoapp.ui.theme.ComposeCryptoAppTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    //https://raw.githubusercontent.com/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeCryptoAppTheme {
                MainScreen()
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(){

    var cryptoList = remember { mutableStateListOf<CryptoModel>() }

    val BASE_URL = "https://raw.githubusercontent.com/"

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CryptoAPI::class.java)

    val call = retrofit.getData()

    call.enqueue(object : Callback<List<CryptoModel>> {
        override fun onResponse(
            call: Call<List<CryptoModel>>,
            response: Response<List<CryptoModel>>
        ) {
            if (response.isSuccessful){
                response.body()?.let {
                    cryptoList.addAll(it)
                }
            }
        }

        override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
            t.printStackTrace()
        }

    } )

    Scaffold(
        topBar = { AppBar()}
    ){
        Surface(color = Color.LightGray) {
            CryptoList(cryptos = cryptoList)
        }
    }
}

@Composable
fun CryptoList(cryptos : List<CryptoModel>){
    LazyColumn(contentPadding = PaddingValues(5.dp)){
        items(cryptos){ crypto ->
            CryptoRow(crypto = crypto)
        }
    }
}

@Composable
fun CryptoRow(crypto : CryptoModel){
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = crypto.currency,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(3.dp),
            fontWeight = FontWeight.Bold)

        Text(text = crypto.price,
            modifier = Modifier.padding(3.dp))
    }
}

@Composable
fun AppBar(){
    TopAppBar(
        title = { Text("Compose Crypto", fontSize = 25.sp)}
    )
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeCryptoAppTheme {
        MainScreen()
    }
}