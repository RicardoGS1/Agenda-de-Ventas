package com.virtualworld.agendadeventas.ui.Screen.Inicio

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar

/**muestra un botón con un DatePickerDialog. Al hacer clic en el botón, se abre el diálogo para que
el usuario seleccione una fecha. La fecha seleccionada se formatea y se muestra en el botón.
Si no se ha seleccionado ninguna fecha, se muestra un texto predeterminado definido por titleButton.
 * @param modifier Para personalizar el diseño del botón.
 * @param stateFecha La fecha seleccionada la ultima vez que se abrio el dialogo.
 * @param titleButton El texto a mostrar en el botón si no se ha seleccionado ninguna fecha.
 * @param changerDateEnd  Función que se llama cuando se selecciona una fecha en el diálogo.
 */

@Composable
fun ButtonSelctFechaView(
    modifier: Modifier,
    stateFecha: Long?,
    titleButton: String,
    h:Int,
    m:Int,
    s:Int,
    changerDateEnd: (selectedTimeInMillis: Long) -> Unit,
) {
    val currentDate = LocalDate.now()
    val year = currentDate.year
    val month = currentDate.monthValue-1
    val day = currentDate.dayOfMonth

    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy") }
    val context = LocalContext.current

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay,h,m,s)
                val selectedTimeInMillis = selectedCalendar.timeInMillis
                changerDateEnd(selectedTimeInMillis)
            }, year, month, day
        )
    }

    val texto by remember(stateFecha) {
        derivedStateOf {
            stateFecha?.let {
                val localDate =
                    Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                localDate.format(dateFormatter).toString()
            } ?: titleButton
        }
    }

    OutlinedButton(
        onClick = { datePickerDialog.show() },
        modifier = modifier
            .padding(horizontal = 10.dp)
            .padding(bottom = 6.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onPrimaryContainer)
    ) {
        Text(
            text = texto.toString(),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primaryContainer,
        )
    }
}