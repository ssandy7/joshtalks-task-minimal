package com.example.sampletask.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.Color

@Composable
fun AppEntry() {
    var route by rememberSaveable { mutableStateOf("start") }
    val repo = remember { SimpleRepo() }

    when(route) {
        "start" -> StartScreen(onStart = { route = "noise" })
        "noise" -> NoiseTestScreen(onPass = { route = "select" })
        "select" -> TaskSelectionScreen(
            onText = { route = "text" },
            onImage = { route = "image" },
            onPhoto = { route = "photo" },
        onHistory = { route = "history" }
        )
        "text" -> TextReadingScreen(onSubmit = { task -> repo.addTask(task); route = "select" })
        "image" -> ImageDescriptionScreen(onSubmit = { task -> repo.addTask(task); route = "select" })
        "photo" -> PhotoCaptureScreen(onSubmit = { task -> repo.addTask(task); route = "select" })
        "history" -> TaskHistoryScreen(tasks = repo.getAll(), onBack = { route = "select" })
    }
}

@Composable
fun StartScreen(onStart: ()->Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Let's start with a Sample Task for practice.", fontSize = 20.sp)
        Spacer(Modifier.height(8.dp))
        Text("Pehele hum ek sample task karte hain.")
        Spacer(Modifier.height(16.dp))
        Button(onClick = onStart) { Text("Start Sample Task") }
    }
}

@Composable
fun NoiseTestScreen(onPass: ()->Unit) {
    var dB by remember { mutableStateOf(20) }
    LaunchedEffect(Unit) {
        while(true) { dB = (10..55).random(); delay(400L) }
    }
    Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Noise level: ${dB} dB", fontSize = 18.sp)
        Spacer(Modifier.height(12.dp))
        if (dB < 40) { Text("Good to proceed"); Spacer(Modifier.height(12.dp)); Button(onClick = onPass) { Text("Proceed") } }
        else { Text("Please move to a quieter place", color = Color.Red) }
    }
}

@Composable
fun TaskSelectionScreen(onText:()->Unit, onImage:()->Unit, onPhoto:()->Unit, onHistory:()->Unit) {
    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = onText, modifier = Modifier.fillMaxWidth()) { Text("Text Reading Task") }
        Spacer(Modifier.height(8.dp))
        Button(onClick = onImage, modifier = Modifier.fillMaxWidth()) { Text("Image Description Task") }
        Spacer(Modifier.height(8.dp))
        Button(onClick = onPhoto, modifier = Modifier.fillMaxWidth()) { Text("Photo Capture Task") }
        Spacer(Modifier.height(16.dp))
        TextButton(onClick = onHistory) { Text("Task History") }
    }
}

data class TaskItem(val id:String, val type:String, val text:String?, val duration:Int)
class SimpleRepo { private val list = mutableStateListOf<TaskItem>(); fun addTask(t: TaskItem) { list.add(0, t) }; fun getAll(): List<TaskItem> = list.toList() }

@Composable
fun TextReadingScreen(onSubmit:(TaskItem)->Unit) {
    var text by rememberSaveable { mutableStateOf("This is a sample passage to read.") }
    var recordingSec by remember { mutableStateOf(12) }
    Column(Modifier.fillMaxSize().padding(24.dp)) {
        Text("Read the passage aloud in your native language.")
        Spacer(Modifier.height(8.dp))
        Text(text)
        Spacer(Modifier.height(16.dp))
        Text("Simulated recording duration: ${recordingSec}s")
        Spacer(Modifier.height(8.dp))
        Button(onClick = { if (recordingSec in 10..20) onSubmit(TaskItem(java.util.UUID.randomUUID().toString(),"text", text, recordingSec)) }) { Text("Submit") }
    }
}

@Composable
fun ImageDescriptionScreen(onSubmit:(TaskItem)->Unit) {
    var recordingSec by remember { mutableStateOf(11) }
    Column(Modifier.fillMaxSize().padding(24.dp)) {
        Text("Describe what you see in your native language.")
        Spacer(Modifier.height(8.dp))
        Box(modifier = Modifier.size(180.dp).align(Alignment.CenterHorizontally)) { Text("Image") }
        Spacer(Modifier.height(12.dp))
        Text("Simulated recording duration: ${recordingSec}s")
        Spacer(Modifier.height(8.dp))
        Button(onClick = { if (recordingSec in 10..20) onSubmit(TaskItem(java.util.UUID.randomUUID().toString(),"image","image desc", recordingSec)) }) { Text("Submit") }
    }
}

@Composable
fun PhotoCaptureScreen(onSubmit:(TaskItem)->Unit) {
    var desc by rememberSaveable { mutableStateOf("") }
    var recordingSec by remember { mutableStateOf(15) }
    Column(Modifier.fillMaxSize().padding(24.dp)) {
        Text("Capture Image and describe it.")
        Spacer(Modifier.height(8.dp))
        BasicTextField(value = desc, onValueChange = { desc = it }, modifier = Modifier.fillMaxWidth(), textStyle = TextStyle(fontSize = 16.sp))
        Spacer(Modifier.height(8.dp))
        Text("Simulated recording duration: ${recordingSec}s")
        Spacer(Modifier.height(8.dp))
        Button(onClick = { if (recordingSec in 10..20) onSubmit(TaskItem(java.util.UUID.randomUUID().toString(),"photo", desc, recordingSec)) }) { Text("Submit") }
    }
}

@Composable
fun TaskHistoryScreen(tasks: List<TaskItem>, onBack:()->Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Total Tasks: ${tasks.size}")
            val totalSec = tasks.sumOf { it.duration }
            Text("Total Duration: ${totalSec}s")
        }
        Spacer(Modifier.height(12.dp))
        for (t in tasks) {
            Card(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                Column(Modifier.padding(12.dp)) {
                    Text("Type: ${t.type}")
                    Text("Duration: ${t.duration}s")
                    Text("Text: ${t.text ?: "-"}")
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        Button(onClick = onBack) { Text("Back") }
    }
}
