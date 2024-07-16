import 'dart:convert';
import 'package:flutter/foundation.dart';
import 'package:http/http.dart' as http;
import 'package:workmanager/workmanager.dart';

const String taskName = "simplePeriodicTask";

void callbackDispatcher() {
  Workmanager().executeTask((task, inputData) async {
    switch (task) {
      case taskName:
        await sendMessage();
        break;
      case Workmanager.iOSBackgroundTask:
        print("iOS background fetch delegate ran");
        break;
    }
    return Future.value(true);
  });
}

Future<void> sendMessage() async {
  print('Sending message...');
  final response = await http.post(
    Uri.parse('http://192.168.150.149:8000/api/messages'),  // Use 10.0.2.2 for Android emulator
    headers: <String, String>{
      'Content-Type': 'application/json; charset=UTF-8',
    },
    body: jsonEncode(<String, String>{
      'sender': '+1234567890',
      'content': 'This is a test message',
    }),
  );

  if (response.statusCode == 200) {
    if (kDebugMode) {
      print('Message sent successfully');
    }
  } else {
    if (kDebugMode) {
      print('Failed to send message');
    }
    if (kDebugMode) {
      print('Status code: ${response.statusCode}');
    }
    if (kDebugMode) {
      print('Response body: ${response.body}');
    }
  }
}
