package com.example.tipapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.layout.RowScopeInstance.align
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipapp.Components.InputField
import com.example.tipapp.ui.theme.TipAppTheme
import com.example.tipapp.utils.PerPersonBill
import com.example.tipapp.utils.calculateTotalTip
import com.example.tipapp.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {

           TipCalculator()
               // Text(text = "heyy 2")
            }

        }
    }
}
@Composable
fun MyApp(content: @Composable () -> Unit ) {
    TipAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            color = MaterialTheme.colors.background
        ) { content()


        }
    }
}


@Preview
@Composable
fun Topportion(totalPerPerson:Double=0.0){
    Surface(modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)
        .height(150.dp)
        .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp))), color = Color(0xFFE9D7F7)
    ) {
        Column(modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
val total="%.2f".format(totalPerPerson)
            Text(text = "total per person",
                style = MaterialTheme.typography.h4)
            Text(text = "$$total",style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.ExtraBold )
        }

    }
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun TipCalculator(){
    Surface(modifier = Modifier.padding(12.dp)) {
        Column() {
            MainContent()
        }
    }
}

@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TipAppTheme {
MyApp {
    Topportion()

}
    }
}



@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun MainContent(){
BillForm(){billamt->
    Log.d("amt", "MainContent:$billamt ")

}

}
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(modifier: Modifier=Modifier,

onValChange:(String)->Unit={}){

    val totalBillState = remember {
    mutableStateOf("")
}
    val validState= remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }
    val keyboardController= LocalSoftwareKeyboardController.current


    val sliderPositionState= remember {
    mutableStateOf(0f)
}
    val tipPercentage=(sliderPositionState.value*100).toInt()

    val splitByState= remember {
        mutableStateOf(1)
    }
    val range=IntRange(1,100)

    val tipAmntState= remember {
        mutableStateOf(0.0)
    }
    val totatPerAdmiState= remember {
        mutableStateOf(0.0)
    }
 Topportion(totalPerPerson = totatPerAdmiState.value)
    Surface(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner =
        CornerSize(8.dp)),
        border = BorderStroke(width = 1.dp,color = Color.LightGray)
    ) {
        Column(modifier = Modifier.padding(6.dp),
            verticalArrangement =Arrangement.Top,
        horizontalAlignment = Alignment.Start) {
            InputField(valueState = totalBillState,
                labelType = "EnterBill",
                enabled = true,
                isSingleLine =true , onAction = KeyboardActions{
                    if(!validState) return@KeyboardActions
                  onValChange(totalBillState.value.trim())
                    keyboardController?.hide()
                }
            )
            if (validState){
                Row(modifier=Modifier.padding(3.dp), horizontalArrangement = Arrangement.Start) {
                   Text(text = "Split",
                   modifier=Modifier.align(alignment = Alignment.CenterVertically))
              Spacer(modifier = Modifier.width(120.dp))
                Row(modifier = Modifier.padding(horizontal = 3.dp)){
RoundIconButton( imageVector = Icons.Default.Remove, onClick = { splitByState.value=
if(splitByState.value>1) splitByState.value-1
else 1
    totatPerAdmiState.value=
        PerPersonBill(totalBill = totalBillState.value.toDouble(),
            splitby = splitByState.value,
            tipPercentage = tipPercentage)
})
Text(
    text = "${splitByState.value}",
    modifier = Modifier
        .align(Alignment.CenterVertically)
        .padding(start = 9.dp, end = 9.dp)
)
                    RoundIconButton( imageVector = Icons.Default.Add, onClick = {splitByState.value=
                        if(splitByState.value<range.endInclusive) splitByState.value+1
                        else range.endInclusive
                        totatPerAdmiState.value=
                            PerPersonBill(totalBill = totalBillState.value.toDouble(), splitby = splitByState.value, tipPercentage = tipPercentage)})
                }
                }
            
            //Tip Row
            Row (modifier=Modifier.padding(horizontal = 3.dp, vertical = 12.dp)){
                Text(text = "Tip", modifier = Modifier.align(alignment=Alignment.CenterVertically))
                Spacer(modifier = Modifier.width(200.dp))
                
                Text(text = "$${tipAmntState.value}",modifier = Modifier.align(alignment=Alignment.CenterVertically))
            }
            
            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "$tipPercentage%")
                Spacer(modifier = Modifier.height(14.dp))
                //slider
                Slider(value = sliderPositionState.value, onValueChange = {newVal->
                    sliderPositionState.value=newVal
                    tipAmntState.value=
                        calculateTotalTip(totalBillState.value.toDouble(),tipPercentage=tipPercentage)
                   totatPerAdmiState.value=
                       PerPersonBill(totalBill = totalBillState.value.toDouble(), splitby = splitByState.value, tipPercentage = tipPercentage)
                }, modifier = Modifier.padding(start = 16.dp, end = 16.dp), steps = 5, onValueChangeFinished = {

                })
            }
            
           }
            else{ Box(){}
             }
        }
    }

}

