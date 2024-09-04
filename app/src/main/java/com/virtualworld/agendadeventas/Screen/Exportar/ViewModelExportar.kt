package com.virtualword3d.salesregister.Screen.Exportar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.virtualword3d.salesregister.CasoUso.CasoUsoFirebase
import com.virtualword3d.salesregister.Data.Entity.Mensajes
import com.virtualword3d.salesregister.Data.Entity.Vendido
import com.virtualword3d.salesregister.Repocitory.VendidoLocalDataSourse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelExportar @Inject constructor(private val casoUsoFirebase: CasoUsoFirebase,
                                            private val vendidoRepo: VendidoLocalDataSourse) :
    ViewModel()
{

    private val _autentificado = MutableLiveData("")
    val autentificado: LiveData<String> = _autentificado

    private val _stateCall = MutableLiveData(Mensajes.NEUTRO)
    val stateCall: LiveData<Mensajes> = _stateCall


    fun onChangeAutentificado()
    {
        _autentificado.value = Firebase.auth.currentUser?.email.toString()
    }

    fun onChangeLoading(load: Mensajes)
    {
        _stateCall.value = load
    }


    fun startSecion(email: String, password: String)
    {
        viewModelScope.launch {
            casoUsoFirebase.startSecion(email, password) { mensaje ->

                onChangeLoading(mensaje)

                if (mensaje == Mensajes.BIEN)
                {
                    onChangeAutentificado()
                }
            }
        }
    }

    fun createUser(email: String, password: String)
    {
        viewModelScope.launch {
            casoUsoFirebase.createUser(email, password) { mensaje ->

                onChangeLoading(mensaje)

                if (mensaje == Mensajes.BIEN)
                {
                    onChangeAutentificado()
                }
            }
        }
    }

    fun closeSecion()
    {
        viewModelScope.launch() {

            casoUsoFirebase.closeSecion { mensaje ->

                onChangeLoading(mensaje)

                if (mensaje == Mensajes.BIEN)
                {
                    onChangeAutentificado()
                }
            }
        }
    }

    fun importSales()
    {
        viewModelScope.launch {
            casoUsoFirebase.importSales({ message ->
                                            onChangeLoading(message)
                                        },

                                        { listSales ->
                                            salveSales(listSales)
                                        })
        }
    }

    private fun salveSales(listSales: List<Vendido>)
    {
       vendidoRepo.cleanSales { response->

           Log.e("efecto",response.toString())

           vendidoRepo.addProductoVendido(listSales)

       }
    }

    fun exportSales()
    {
        viewModelScope.launch {
            var connection = false

            vendidoRepo.getAllVendidos { listSales ->


                casoUsoFirebase.exportSales(listSales, { message ->
                    onChangeLoading(message)
                }) {
                    connection = it
                }
            }

            delay(5000)
            if (!connection)
            {
                onChangeLoading(Mensajes.ERROR)
                Log.e("efecto", "incompleto")
                FirebaseFirestore.getInstance().terminate()
            }
        }
    }


}