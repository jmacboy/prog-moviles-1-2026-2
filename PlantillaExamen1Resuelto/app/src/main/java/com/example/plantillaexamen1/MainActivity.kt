package com.example.plantillaexamen1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.plantillaexamen1.ui.Order
import com.example.plantillaexamen1.ui.theme.CoffeeBrown
import com.example.plantillaexamen1.ui.theme.CoffeeTransparent
import com.example.plantillaexamen1.ui.theme.CoffeeTransparentLight
import com.example.plantillaexamen1.ui.theme.PlantillaExamen1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlantillaExamen1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CoffeeScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun CoffeeScreen(modifier: Modifier = Modifier, vm: MainViewModel = viewModel()) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    val sizeOptions = listOf("Pequeño", "Mediano", "Grande")
    val milkOptions = listOf("Normal", "Deslactosada", "Vegetal")

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Mi cafetería",
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.drinkName,
            onValueChange = {
                vm.setDrinkName(it)
            },
            placeholder = { Text("Ej. Capuccino") },
            label = { Text("Nombre de Bebida") },
            supportingText = {
                if (uiState.showDrinkError) {
                    Text("El nombre de la bebida no puede estar vacío", color = Color.Red)
                }
            },
            trailingIcon = {
                if (uiState.showDrinkError) {
                    Text("!", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        )
        SizeOptions(
            sizeOptions = sizeOptions,
            selectedSize = uiState.selectedSize,
            onSizeSelected = { vm.setSelectedSize(it) },
            showError = uiState.showSizeError
        )
        MilkOptions(
            milkOptions = milkOptions,
            selectedMilk = uiState.selectedMilk,
            onMilkSelected = { vm.setSelectedMilk(it) }
        )
        Extras(
            canelaSelected = uiState.canelaSelected,
            onCanelaSelected = { vm.toggleCanela() },
            chocolateSelected = uiState.chocolateSelected,
            onChocolateSelected = { vm.toggleChocolate() },
            lecheExtraSelected = uiState.lecheExtraSelected,
            onLecheExtraSelected = { vm.toggleLecheExtra() }
        )
        Button(
            onClick = {
              vm.addToOrder()
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        ) {
            Text("Agregar al Pedido")
        }
        OrderDetails(currentOrder = uiState.currentOrder, onCleanOrderClicked = { vm.cleanOrder() })

    }
}

@Composable
fun OrderDetails(currentOrder: Order?, onCleanOrderClicked: () -> Unit) {
    if (currentOrder != null) {
        HorizontalDivider(
            color = CoffeeTransparent,
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CoffeeTransparentLight)
                    .padding(8.dp)

            ) {
                Text("Pedido Actual")
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    "Limpiar",
                    color = CoffeeBrown,
                    modifier = Modifier.clickable {
                        onCleanOrderClicked()
                    }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column {
                    Text(
                        text = "${currentOrder?.name}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${currentOrder?.size}",
                        fontSize = 10.sp
                    )
                    Text(
                        fontSize = 10.sp,
                        text = "Leche ${currentOrder?.milkType} " +
                                (if (currentOrder?.extraCinnamon == true) "Canela, " else "") +
                                (if (currentOrder?.extraChocolate == true) "Chocolate, " else "") +
                                (if (currentOrder?.extraMilk == true) "Leche Extra" else "")
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Bs. ${currentOrder?.calculatePrice()}",
                    fontWeight = FontWeight.Bold,
                    color = CoffeeBrown,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            HorizontalDivider(
                color = CoffeeTransparent,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 2.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = "Total:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Bs. ${currentOrder?.calculatePrice()}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = CoffeeBrown
                )
            }

        }
    }
}

@Composable
fun Extras(
    canelaSelected: Boolean,
    onCanelaSelected: (Boolean) -> Unit,
    chocolateSelected: Boolean,
    onChocolateSelected: (Boolean) -> Unit,
    lecheExtraSelected: Boolean,
    onLecheExtraSelected: (Boolean) -> Unit
) {

    Text("Extras")
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = canelaSelected,
            onCheckedChange = { onCanelaSelected(it) },
        )
        Text(
            text = "Canela (+ Bs. 1)",
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = chocolateSelected,
            onCheckedChange = { onChocolateSelected(it) },
        )
        Text(
            text = "Chocolate (+ Bs. 3)",
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = lecheExtraSelected,
            onCheckedChange = { onLecheExtraSelected(it) },
        )
        Text(
            text = "Leche Extra (+ Bs. 2)",
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Composable
fun MilkOptions(milkOptions: List<String>, selectedMilk: String, onMilkSelected: (String) -> Unit) {
    Text("Tipo de Leche")
    Row(modifier = Modifier.fillMaxWidth()) {
        milkOptions.forEach { milk ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(4.dp)
                    .weight(1f)
                    .border(
                        1.dp, CoffeeBrown,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .background(if (selectedMilk == milk) CoffeeTransparent else Color.Transparent)
                    .padding(4.dp)
                    .clickable {
                        onMilkSelected(milk)
                    }
            ) {
                Text(
                    text = milk,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )

            }
        }
    }
}

@Composable
fun SizeOptions(
    sizeOptions: List<String>,
    selectedSize: String,
    onSizeSelected: (String) -> Unit,
    showError: Boolean
) {
    Text("Tamaño")
    Row(modifier = Modifier.fillMaxWidth()) {
        sizeOptions.forEach { size ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(4.dp)
                    .weight(1f)
                    .border(
                        1.dp, CoffeeBrown,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .background(if (selectedSize == size) CoffeeTransparent else Color.Transparent)
                    .padding(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                ) {
                    RadioButton(
                        selected = selectedSize == size,
                        onClick = { onSizeSelected(size) },
                    )
                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = size,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            lineHeight = 10.sp,
                            modifier = Modifier.padding(start = 0.dp)
                        )
                        Text(
                            text = when (size) {
                                "Pequeño" -> "Bs. 15"
                                "Mediano" -> "Bs. 20"
                                "Grande" -> "Bs. 25"
                                else -> ""
                            },
                            fontSize = 10.sp,
                            modifier = Modifier.padding(start = 0.dp, top = 0.dp)
                        )
                    }
                }
            }
        }
    }
    if (showError) {
        Text("Debe seleccionar un tamaño", color = Color.Red, fontSize = 10.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun CoffeeScreenPreview() {
    PlantillaExamen1Theme {
        CoffeeScreen()
    }
}