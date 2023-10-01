package nl.johnny.heartfailure.ui

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import nl.johnny.heartfailure.BuildConfig
import nl.johnny.heartfailure.R
import nl.johnny.heartfailure.ui.components.TextButton
import nl.johnny.heartfailure.ui.components.TextField
import nl.johnny.heartfailure.ui.theme.Danger
import nl.johnny.heartfailure.ui.theme.HeartfailureTheme
import nl.johnny.heartfailure.ui.theme.Success
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.NumberFormatException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import kotlin.coroutines.suspendCoroutine

class MainActivity : ComponentActivity() {

    companion object {
        const val TAG = "MAIN_ACTIVITY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {

            var result by remember { mutableStateOf(-1) }
            var age by remember { mutableStateOf(43) }
            var restingBP by remember { mutableStateOf(115) }
            var cholesterol by remember { mutableStateOf(0) }
            var fastingBS by remember { mutableStateOf(0) }
            var maxHR by remember { mutableStateOf(145) }
            var exerciseAngina by remember { mutableStateOf(1) }
            var oldPeak by remember { mutableStateOf(2.0) }
            var isMale by remember { mutableStateOf(false) }
            var chestPainType by remember { mutableStateOf(0) }
            var restingECG by remember { mutableStateOf(0) }
            var stSlope by remember { mutableStateOf(0) }


            HeartfailureTheme {


                Column(modifier= Modifier.verticalScroll(rememberScrollState())) {
                    Text(
                        text = "Predict my case", fontSize=21.sp , modifier= Modifier.padding(bottom = 10.dp))

                    Text(text = "Age", Modifier.padding(top=10.dp))
                    TextField(age.toString(), onValueChange = {
                        try {
                            age = it.toInt()
                        } catch(e: NumberFormatException) {

                        }
                    })

                    Text(text = "Resting blood pressure", Modifier.padding(top=10.dp))
                    TextField(restingBP.toString(), onValueChange = {
                        try {
                            restingBP = it.toInt()
                        } catch(e: NumberFormatException) {

                        }
                    })

                    Text(text = "Cholesterol", Modifier.padding(top=10.dp))
                    TextField(cholesterol.toString(), onValueChange = {
                        try {
                            cholesterol = it.toInt()
                        } catch(e: NumberFormatException) {

                        }
                    })

                    Text(text = "Max heart rate", Modifier.padding(top=10.dp))
                    TextField(maxHR.toString(), onValueChange = {
                        try {
                            maxHR = it.toInt()
                        } catch(e: NumberFormatException) {

                        }
                    })

                    Text(text = "FastingBS", Modifier.padding(top=10.dp))
                    TextField(fastingBS.toString(), onValueChange = {
                        try {
                            fastingBS = it.toInt()
                        } catch(e: NumberFormatException) {

                        }
                    })

                    Text(text = "Exercise angina", Modifier.padding(top=10.dp))
                    TextField(exerciseAngina.toString(), onValueChange = {
                        try {
                            exerciseAngina = it.toInt()
                        } catch(e: NumberFormatException) {

                        }
                    })

                    Text(text = "Old peak", Modifier.padding(top=10.dp))
                    TextField(oldPeak.toString(), onValueChange = {
                        try {
                            oldPeak = it.toDouble()
                        } catch(e: NumberFormatException) {

                        }
                    })

                    Text(text = "Male or female", Modifier.padding(top=10.dp))
                    TextField(isMale.toString(), onValueChange = {
                        try {
                            isMale = it.toBoolean()
                        } catch(e: NumberFormatException) {

                        }
                    })

                    Text(text = "Chest pain type", Modifier.padding(top=10.dp))
                    TextField(chestPainType.toString(), onValueChange = {
                        try {
                            chestPainType = it.toInt()
                        } catch(e: NumberFormatException) {

                        }
                    })

                    Text(text = "RestingECG", Modifier.padding(top=10.dp))
                    TextField(chestPainType.toString(), onValueChange = {
                        try {
                            restingECG = it.toInt()
                        } catch(e: NumberFormatException) {

                        }
                    })


                    Text(text = "stSlope", Modifier.padding(top=10.dp))
                    TextField(stSlope.toString(), onValueChange = {
                        try {
                            stSlope = it.toInt()
                        } catch(e: NumberFormatException) {

                        }
                    })


                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = when (result) {
                                1 -> "High risk of heart failure"
                                0 -> "Low risk of heart failure"
                                else -> "Press predict to make a prediction"
                            }
                        )

                        Icon(
                            painter = painterResource(
                                id = when (result) {
                                    0 -> R.drawable.thumbs_up_solid
                                    1 -> R.drawable.thumbs_down_solid
                                    else -> R.drawable.heart_solid
                                }
                            ),
                            contentDescription = null,
                            tint = when(result) {
                                0 -> Success
                                1 -> Danger
                                else -> colorScheme.inverseSurface
                            },
                            modifier = Modifier
                                .height(20.dp)
                                .padding(horizontal = 10.dp)
                        )
                    }




                    TextButton(text = "Predict", modifier = Modifier.fillMaxWidth()) {
                        runBlocking {
                            result = predictAsync(
                                age,
                                restingBP,
                                cholesterol,
                                fastingBS,
                                maxHR,
                                exerciseAngina,
                                oldPeak,
                                isMale,
                                chestPainType,
                                restingECG,
                                stSlope
                            )

                        }
                    }

                }
            }
        }
    }

    private suspend fun predictAsync(
        age: Int,
        restingBP: Int,
        cholesterol: Int,
        fastingBS: Int,
        maxHR: Int,
        exerciseAngina: Int,
        oldPeak: Double,
        isMale: Boolean,
        chestPainType: Int,
        restingECG: Int,
        stSlope: Int
    ): Int = withContext(Dispatchers.IO) {

        // Requested path
        val url = URL("${BuildConfig.API_URL}/api/predict")

        // Create a http or https connection with the API url
        val connection: HttpURLConnection = try {
            url.openConnection() as HttpURLConnection
        } catch (e: IOException) {
            Log.e(TAG, "$e.message?")
            return@withContext -1
        }

        val method = "POST"

        // Configure the connection
        connection.requestMethod = method
        connection.connectTimeout = 2000 // Set timeout to 2 seconds
        connection.readTimeout = 2000 // Set read timeout to 2 seconds
        connection.setRequestProperty("Accept-Charset", "utf-8")
        connection.setRequestProperty("Content-Type", "application/json;charset=utf-8")

        // Connect with the API
        try {
            connection.connect()
        } catch (e: IOException) {
            Log.e(TAG, "$e.message?")
            return@withContext -1
        } catch (e: SocketTimeoutException) {
            Log.e(TAG, "$e.message?") // Connection timeout
            return@withContext -1
        }


        /*
#   "Age": 43,
#   "RestingBP": 115,
#   "Cholesterol": 0,
#   "FastingBS": 0,
#   "MaxHR": 145,
#   "ExerciseAngina": 1,
#   "Oldpeak": 2.0,
#   "Male": 1,
#   "Female": 0,
#   "ChestPainType_ASY": 1,
#   "ChestPainType_ATA": 0,
#   "ChestPainType_NAP": 0,
#   "ChestPainType_TA": 0,
#   "HasCholesterol": 0,
#   "RestingECG_Normal": 1,
#   "RestingECG_ST": 0,
#   "RestingECG_LVH": 0,
#   "ST_Slope_Down": 0,
#   "ST_Slope_Flat": 1,
#   "ST_Slope_Up": 0
# }
*/

        val data = JsonObject()
        data.addProperty("Age", age)
        data.addProperty("RestingBP", restingBP)
        data.addProperty("Cholesterol", cholesterol)
        data.addProperty("FastingBS", fastingBS)
        data.addProperty("MaxHR", maxHR)
        data.addProperty("ExerciseAngina", exerciseAngina)
        data.addProperty("Oldpeak", oldPeak)
        data.addProperty("Male", if (isMale) { 1 } else { 0 })
        data.addProperty("Female", if (!isMale) { 1 } else { 0 })
        data.addProperty("ChestPainType_ASY", 0)
        data.addProperty("ChestPainType_ATA", 0)
        data.addProperty("ChestPainType_NAP", 0)
        data.addProperty("ChestPainType_TA", 0)
        data.addProperty("HasCholesterol", if(cholesterol != 0) { 1 } else { 0 })
        data.addProperty("RestingECG_Normal", 0)
        data.addProperty("RestingECG_ST", 0)
        data.addProperty("RestingECG_LVH", 0)
        data.addProperty("ST_Slope_Down", 0)
        data.addProperty("ST_Slope_Flat", 0)
        data.addProperty("ST_Slope_Up", 0)

        connection.outputStream.use { stream ->
            stream.write(Gson().toJson(data).toByteArray(Charsets.UTF_8))
            stream.flush()
        }

        Log.d(TAG, "Response code: ${connection.responseCode}")

        // Acquire the input stream from the connection
        val inputStream: InputStream = try {
            connection.inputStream
        } catch (e: IOException) {
            return@withContext -1
        }

        // Construct a buffered reader
        val reader = BufferedReader(InputStreamReader(inputStream))

        // Read result of http request line by line until there are no lines left
        var result = ""
        for (line: String in reader.lines()) {
            result += line
        }

        // Close the stream so that any further reads are blocked
        reader.close()
        inputStream.close()

        val jsonObject: JsonObject = Gson().fromJson(result, JsonObject::class.java)

        jsonObject.get("message").asInt
    }

}