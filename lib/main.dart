import 'package:flutter/material.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:workmanager/workmanager.dart';
import 'back.dart';


void main() {
  WidgetsFlutterBinding.ensureInitialized();
  Workmanager().initialize(callbackDispatcher, isInDebugMode: true);
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Background Message Sender',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  @override
  void initState() {
    super.initState();
    _requestPermissions();
  }

  Future<void> _requestPermissions() async {
    var status = await Permission.sms.status;
    if (!status.isGranted) {
      var result = await Permission.sms.request();
      if (result.isGranted) {
        print("SMS permission granted");
      } else {
        print("SMS permission denied");
      }
    } else {
      print("SMS permission already granted");
    }

    Workmanager().registerPeriodicTask(
      "1",
      taskName,
      frequency: Duration(minutes: 15), // Minimum interval is 15 minutes
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Flutter Background Message Sender'),
      ),
      body: Center(
        child: Text('Sending messages in the background...'),
      ),
    );
  }
}
