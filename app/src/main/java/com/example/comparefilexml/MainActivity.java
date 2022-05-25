package com.example.comparefilexml;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity ntp_";

    String folder = "my folder hihi";
    String fileName = "mytest" + ".txt";
    String fileCompare = "mytest2" + ".txt";
    String result = "result" + ".txt";
    BootReciever myBroadcastReceiver;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main);
            // ... rest of body of onCreateView() ...
        } catch (Exception e) {
            Log.e(TAG, "onCreateView", e);
            throw e;
        }
        Log.d(TAG, "onCreateView");

        myBroadcastReceiver = new BootReciever();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
        intentFilter.addAction(Intent.ACTION_REBOOT);
        intentFilter.addAction(Intent.ACTION_USER_PRESENT);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_USER_FOREGROUND);
        intentFilter.addAction(Intent.ACTION_USER_BACKGROUND);
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_USER_INITIALIZE);
        intentFilter.addAction(Intent.ACTION_USER_UNLOCKED);
        intentFilter.addAction(Intent.CATEGORY_LAUNCHER);
        registerReceiver(myBroadcastReceiver, intentFilter);

        requestPermissions();
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // myMethod();
            }
        });
        if (!checkPermission()) {
            requestPermission();
        } else {
            //Log.d(TAG, " PERMISSION done");
           // myMethod();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy unregisterReceiver");
        unregisterReceiver(myBroadcastReceiver);
    }

    private void requestPermissions() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            Log.d(TAG, "requestPermissions");
//            if (!Settings.canDrawOverlays(this)) {
//                Log.d(TAG, "requestPermissions Settings");
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + this.getPackageName()));
//                startActivityForResult(intent, 232);
//            } else {
//                //Permission Granted-System will work
//            }
//        }
    }



    private boolean checkPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int result = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
            int result1 = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermission() {
        try {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
            startActivityForResult(intent, 2296);
        } catch (Exception e) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            startActivityForResult(intent, 2296);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, " onActivityResult ");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2296) {
            if (Environment.isExternalStorageManager()) {
                // perform action when allow permission success
                Log.d(TAG, " onActivityResult success");
               // myMethod();
            } else {
                Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public File generateNoteOnSD(String sFileName) {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/" + folder;
        //Log.d(TAG, "generateNoteOnSD :" + path);
        File myFile = new File(path, sFileName);
        if (!myFile.exists()) {
            myFile.getParentFile().mkdirs();
            //Log.d(TAG, "generateNoteOnSD myFile 1 :" + myFile.getPath());
        }
        //Log.d(TAG, "generateNoteOnSD myFile :" + myFile.getPath());
        return myFile;
    }

    public String readData(File file) {
        try {
            //FileInputStream in = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuffer buffer = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            //Log.d(TAG, "read-data ---- :" + buffer.toString());
            return String.valueOf(buffer);
        } catch (Exception e) {
            Toast.makeText(this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "read-data ---error - :" + e);
        }
        return null;
    }

    public void writeData(File myFile, String data) {
        try {
            FileOutputStream outputStream = new FileOutputStream(myFile);
            outputStream.write(data.getBytes());
            outputStream.close();
            //Log.d(TAG, "writeData - done:");
        } catch (Exception e) {
            Log.d(TAG, "writeData - error:" + e);
        }
    }

    public void writeData2(File myFile, String data) {
        try {
            FileWriter fileWritter = new FileWriter(myFile.getPath(), true);
            BufferedWriter bw = new BufferedWriter(fileWritter);
            bw.write(data);
            bw.close();
            //Log.d(TAG, "writeData2 - done:");
        } catch (IOException e) {
            Log.d(TAG, "writeData2 - error:" + e);
        }
    }

    public void myMethod() {
        File testFile = generateNoteOnSD(fileName);
        File compareFile = generateNoteOnSD(fileCompare);
        File resultFile = generateNoteOnSD(result);
        writeData(testFile, Construct.t);
        writeData2(resultFile, Construct.r);
        // String s = readData(testFile);
        writeData(compareFile, Construct.com);

        XMLFileCompare xmlFileCompare = new XMLFileCompare(testFile);
        XMLFileCompare xmlFileCompare2 = new XMLFileCompare(compareFile);

        // xmlFileCompare.checkTagXML();

        xmlFileCompare.doc.normalizeDocument();
        xmlFileCompare2.doc.normalizeDocument();
        List<String> diffs = new ArrayList<>();
        try {
            diff(xmlFileCompare.doc, xmlFileCompare2.doc, diffs);
            Log.d(TAG, "myMethod diff = :" + diffs.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String ss : diffs) {
            Log.d(TAG, "myMethod diff :" + ss);
        }
    }

    private boolean nodeTypeDiff = true;
    private boolean nodeValueDiff = true;

    public boolean diff(Node node1, Node node2, List<String> diffs) {

        if (node1 == null && node2 != null) {
            diffs.add("null  :::  " + getPath(node2) + " = " + node2.getAttributes().getNamedItem("id").getNodeValue());
            return true;
        }
        if (node1 != null && node2 == null) {
            diffs.add(getPath(node1) + " = " + node1.getAttributes().getNamedItem("id").getNodeValue() + "  :::  null");
            return true;
        }
        diffAttributes(node1, node2, diffs); // getAttributes of this note
        diffChild(node1, node2, diffs); // children -> children more
        return diffs.size() > 0;
    }

    public boolean diffChild(Node node1, Node node2, List<String> diffs) {
        String key = null;
        Map<String, Node> children1 = new LinkedHashMap<String, Node>();
        for (Node child1 = node1.getFirstChild(); child1 != null; child1 = child1.getNextSibling()) {
            key = child1.getNodeName();
            if (child1.getAttributes() != null && child1.getAttributes().getNamedItem("id") != null) {
                key += " " + child1.getAttributes().getNamedItem("id").getNodeValue();
            }
            children1.put(key, child1);
        }

        Map<String, Node> children2 = new LinkedHashMap<String, Node>();
        for (Node child2 = node2.getFirstChild(); child2 != null; child2 = child2.getNextSibling()) {
            key = child2.getNodeName();
            if (child2.getAttributes() != null && child2.getAttributes().getNamedItem("id") != null) {
                key += " " + child2.getAttributes().getNamedItem("id").getNodeValue();
            }
            children2.put(key, child2);
        }

        for (Map.Entry<String, Node> pair : children1.entrySet()) {
            Node child2 = children2.remove(pair.getKey());
            diff(pair.getValue(), child2, diffs);
        }

        for (Map.Entry<String, Node> pair : children2.entrySet()) {
            Node child1 = children1.get(pair.getKey());
            diff(child1, pair.getValue(), diffs);
        }
        return diffs.size() > 0;
    }



    public boolean checkAttributes(String attribute) {
        if (attribute == null) return true;
        attribute = attribute.trim().toLowerCase();
        if (attribute.contains(Construct.attributeCancel[0])) {
            return false;
        }
        for (int i = 1; i < Construct.attributeCancel.length; i++) {
            if (Construct.attributeCancel[i].equals(attribute)) {
                return false;
            }
        }
        return true;
    }

    public boolean diffAttributes(Node node1, Node node2, List<String> diffs) {
        NamedNodeMap nodeMap1 = node1.getAttributes();
        Map<String, Node> attributes1 = new LinkedHashMap<String, Node>();
        for (int index = 0; nodeMap1 != null && index < nodeMap1.getLength(); index++) {
            if(checkAttributes(nodeMap1.item(index).getNodeName())) {
                attributes1.put(nodeMap1.item(index).getNodeName(), nodeMap1.item(index));
            }
        }

        NamedNodeMap nodeMap2 = node2.getAttributes();
        Map<String, Node> attributes2 = new LinkedHashMap<String, Node>();
        for (int index = 0; nodeMap2 != null && index < nodeMap2.getLength(); index++) {
            if(checkAttributes(nodeMap2.item(index).getNodeName())) {
                attributes2.put(nodeMap2.item(index).getNodeName(), nodeMap2.item(index));
            }
        }

        for (Map.Entry<String, Node> pair : attributes1.entrySet()) {
            Node attribute2 = attributes2.remove(pair.getKey());
            //diff(pair.getValue(), attribute2, diffs);
            if (attribute2 == null || !(pair.getValue().getNodeValue().equals(attribute2.getNodeValue()))) {
                diffs.add(getPath(node1) + "  :::  " + getPath(node2) + " \n ->  " + pair.getKey() + " : " + pair.getValue().getNodeValue() + " / " + (attribute2 != null ? attribute2.getNodeValue() : "null"));
            }
        }
        for (Map.Entry<String, Node> pair : attributes2.entrySet()) {
            Node attribute1 = attributes1.get(pair.getKey());
            //diff(attribute1, pair.getValue(), diffs);
            if (attribute1 == null || !(pair.getValue().getNodeValue().equals(attribute1.getNodeValue()))) {
                diffs.add(getPath(node1) + "  :::  " + getPath(node2) + " \n ->  " + pair.getKey() + " : "  + (attribute1 != null ? attribute1.getNodeValue() : "null") + " / " + pair.getValue().getNodeValue());
            }
        }
//        for (Node attribute1 : attributes1.values()) {
//            Node attribute2 = attributes2.remove(attribute1.getNodeName());
//            //diffA(attribute1, attribute2, diffs);
//            if(attribute2 == null || !(attribute1.getNodeValue().equals(attribute2.getNodeValue()))){
//                diffs.add(getPath(node1) + ": " + getPath(node2) + " != " + attribute1.getNodeValue());
//            }
//        }
//
//        for (Node attribute2 : attributes2.values()) {
//                Node attribute1 = attributes1.get(attribute2.getNodeName());
//                //diffA(attribute1, attribute2, diffs);
//                if(attribute1 == null || !(attribute2.getNodeValue().equals(attribute1.getNodeValue()))){
//                    diffs.add(getPath(node1) + "  :::  " + getPath(node2) + "!=" + attribute2.getNodeValue());
//                }
//        }
        return diffs.size() > 0;
    }

    public boolean diffNodeExists(Node node1, Node node2, List<String> diffs) {
        if (node1 == null && node2 == null) {
            diffs.add(getPath(node2) + ":node " + node1 + "!=" + node2 + "\n");
            return true;
        }
        if (node1 == null && node2 != null) {
            diffs.add(getPath(node2) + ":node " + node1 + "!=" + node2.getNodeName());
            return true;
        }
        if (node1 != null && node2 == null) {
            diffs.add(getPath(node1) + ":node " + node1.getNodeName() + "!=" + node2);
            return true;
        }
        return false;
    }

    public boolean diffNodeType(Node node1, Node node2, List<String> diffs) {
        if (node1.getNodeType() != node2.getNodeType()) {
            diffs.add(getPath(node1) + ":type " + node1.getNodeType() + "!=" + node2.getNodeType());
            return true;
        }
        return false;
    }

    public boolean diffNodeValue(Node node1, Node node2, List<String> diffs) {
        if (node1.getNodeValue() == null && node2.getNodeValue() == null) {
            return false;
        }
        if (node1.getNodeValue() == null && node2.getNodeValue() != null) {
            diffs.add(getPath(node1) + ":type " + node1 + "!=" + node2.getNodeValue());
            return true;
        }
        if (node1.getNodeValue() != null && node2.getNodeValue() == null) {
            diffs.add(getPath(node1) + ":type " + node1.getNodeValue() + "!=" + node2);
            return true;
        }
        if (!node1.getNodeValue().equals(node2.getNodeValue())) {
            diffs.add(getPath(node1) + ":type " + node1.getNodeValue() + "!=" + node2.getNodeValue());
            return true;
        }
        return false;
    }

    public String getPath(Node node) {
        StringBuilder path = new StringBuilder();
        do {
            path.insert(0, node.getNodeName());
            path.insert(0, "/");
        }
        while (node.getNodeName() == "NoteResource" || (node = node.getParentNode()) != null);
        return path.toString();
    }

}