package com.example.arpit.project6;

import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    TextView t1,t2,t3,t4,t5,temp1,temp2,temp3,temp4,temp5,hum1,hum2,hum3,hum4,hum5,h1,h2,h3;
int count =0;
    private final int SERVER_PORT =4567;
    double hum[]=new double[100];
    double temp[]=new double[100];
  //  Server server;
int count1=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        h1=(TextView)findViewById(R.id.textView);
        h2=(TextView)findViewById(R.id.textView2);
        h3=(TextView)findViewById(R.id.textView3);

        Typeface face2 = Typeface.createFromAsset(getAssets(),
                "fonts/Kid Kosmic.ttf");
        h1.setTypeface(face2);
        h2.setTypeface(face2);
        h3.setTypeface(face2);

        t1=(TextView)findViewById(R.id.tw1);
        t2=(TextView)findViewById(R.id.tw2);
        t3=(TextView)findViewById(R.id.tw3);
        t4=(TextView)findViewById(R.id.tw4);
        t5=(TextView)findViewById(R.id.tw5);
        temp1=(TextView)findViewById(R.id.temp1);
        temp2=(TextView)findViewById(R.id.temp2);
        temp3=(TextView)findViewById(R.id.temp3);
        temp4=(TextView)findViewById(R.id.temp4);
        temp5=(TextView)findViewById(R.id.temp5);
        hum1=(TextView)findViewById(R.id.hum1);
        hum2=(TextView)findViewById(R.id.hum2);
        hum3=(TextView)findViewById(R.id.hum3);
        hum4=(TextView)findViewById(R.id.hum4);
        hum5 =(TextView)findViewById(R.id.hum5);
//        server = new Server(this);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/Zud Juice.ttf");
        Typeface face1 = Typeface.createFromAsset(getAssets(),
                "fonts/digital-7.ttf");
        Toast.makeText(getApplicationContext(),"Make sure Application Connected to the same Network ",Toast.LENGTH_LONG).show();

        temp1.setTypeface(face1);
        temp2.setTypeface(face1);
        temp3.setTypeface(face1);
        temp4.setTypeface(face1);
        temp5.setTypeface(face1);
        hum1.setTypeface(face1);
        hum2.setTypeface(face1);
        hum3.setTypeface(face1);
        hum4.setTypeface(face1);
        hum5.setTypeface(face1);
        t1.setTypeface(face);
        t2.setTypeface(face);
        t3.setTypeface(face);
        t4.setTypeface(face);
        t5.setTypeface(face);
        getDeviceIpAddress();



            new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    //InetAddress addr = InetAddress.getByName("192.168.0.10");
                    //Create a server socket object and bind it to a port
                    ServerSocket socServer = new ServerSocket(SERVER_PORT);

                    //Create server side client socket reference
                    Socket socClient = null;

                    //Infinite loop will listen for client requests to connect
                    while (true) {
                        //Accept the client connection and hand over communication to server side client socket
                        socClient = socServer.accept();
                        //For each client new instance of AsyncTask will be created

                        ServerAsyncTask serverAsyncTask = new ServerAsyncTask();
                        //Start the AsyncTask execution
                        //Accepted client socket object will pass as the parameter
                        serverAsyncTask.execute(new Socket[] {socClient});
                        socClient.setKeepAlive(true);
                      //  socClient.setTcpNoDelay(true);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }
    public void animation(double hum[], double temp[]) {
        this.hum = hum;
        this.temp = temp;

//Toast.makeText(getApplicationContext(),"this is anim",Toast.LENGTH_LONG).show();
//        in.list(temp,Integer.parseInt(s));

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        File logFile = new File("/mnt/usbhost4/Room", "Data" + ".xls");

if (logFile.exists())
{

    count=count+1;
}



        if (count1 == 0) {
            saveLogs("\t" + "=========================" + "\t" + "HUMIDITY" + "\t" + "AND" + "\t" + "TEMPERATURE" + "\t" + "LOGS" + "\t" + "=========================", logFile);
  //          Toast.makeText(getApplicationContext(),"this is saving file",Toast.LENGTH_LONG).show();
            saveLogs1("", logFile);
            saveLogs("Date", logFile);
            saveLogs("\t", logFile);


            for (int i = 0; i < Integer.parseInt("5"); i++) {

                saveLogs("R" + i + "HUM", logFile);
                saveLogs("\t", logFile);
                saveLogs("R" + i + "TEMP", logFile);
                saveLogs("\t", logFile);

            }
            saveLogs1("", logFile);
        count1 ++;
        }
            else {
                saveLogs(currentDateTimeString, logFile);
                for (int i = 0; i < Integer.parseInt("5"); i++) {
                    saveLogs("\t", logFile);
                    saveLogs("" + temp[i], logFile);
                    saveLogs("\t", logFile);
                    saveLogs("" + hum[i], logFile);
                }

                saveLogs1("", logFile);
            }





        }










    private void saveLogs(String text,File logFile){
        if(isExternalStorageWritable()) {
    //        Toast.makeText(getApplicationContext(),"this is writing file",Toast.LENGTH_LONG).show();
            BufferedWriter buf = null;
            try {
                //BufferedWriter for performance, true to set append to file flag
                buf = new BufferedWriter(new FileWriter(logFile, true));

                buf.append(text);



            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (buf != null)
                    try {
                        buf.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

        }
    }
    private void saveLogs1(String text,File logFile){
        if(isExternalStorageWritable()) {

            BufferedWriter buf = null;
            try {
                //BufferedWriter for performance, true to set append to file flag
                buf = new BufferedWriter(new FileWriter(logFile, true));

//                buf.append(text);
                buf.newLine();


            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (buf != null)
                    try {
                        buf.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

        }
    }



    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {

            return true;
        }
        else if (Environment.MEDIA_SHARED.equals(state)) {
            //Toast.makeText(getApplicationContext(), state, Toast.LENGTH_LONG).show();
            return  true;
        }
        return false;
    }



    public void getDeviceIpAddress() {
        try {
            //Loop through all the network interface devices
            for (Enumeration<NetworkInterface> enumeration = NetworkInterface
                    .getNetworkInterfaces(); enumeration.hasMoreElements();) {
                NetworkInterface networkInterface = enumeration.nextElement();
                //Loop through all the ip addresses of the network interface devices
                for (Enumeration<InetAddress> enumerationIpAddr = networkInterface.getInetAddresses(); enumerationIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumerationIpAddr.nextElement();
                    //Filter out loopback address and other irrelevant ip addresses
                    if (!inetAddress.isLoopbackAddress() && inetAddress.getAddress().length == 4) {
                        //Print the device ip address in to the text view
                        //  tvServerIP.setText(inetAddress.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            Log.e("ERROR:", e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    /**
     * AsyncTask which handles the commiunication with clients
     */
    class ServerAsyncTask extends AsyncTask<Socket, Void, String> {
        //Background task which serve for the client
        @Override
        protected String doInBackground(Socket... params) {
            String result = null;
            //Get the accepted socket object
            Socket mySocket = params[0];
            try {
                //Get the data input stream comming from the client
                InputStream is = mySocket.getInputStream();
                //Get the output stream to the client
                PrintWriter out = new PrintWriter(
                        mySocket.getOutputStream(), true);
                //Write data to the data output stream

                   out.println("s");

mySocket.setKeepAlive(true);


                //Buffer the data input stream
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is));
                //Read the contents of the data buffer
                result = br.readLine();
                //Close the client connection

                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String msg) {
            //After finishing the execution of background task data will be write the text view

            try {
                //String id = msg.substring(0, 1);

                String id = msg.substring(msg.indexOf("id=") + 3, msg.indexOf("temp"));
                //
                // Toast.makeText(getApplicationContext(),id+"",Toast.LENGTH_LONG).show();
                double h = Double.parseDouble(msg.substring(msg.indexOf("temp=") + 5, msg.indexOf("hum")));
                double t = Double.parseDouble(msg.substring(msg.indexOf("hum=") + 4, msg.indexOf("*")));

                switch(id){

                    case "1":
                        temp1.setText(h+" °C");
                        hum1.setText(t+" %");
                        break;

                    case "2":
                        temp2.setText(h+" °C");
                        hum2.setText(t+" %");
                        break;

                    case "3":
                        temp3.setText(h+" °C");
                        hum3.setText(t+" %");
                        break;

                    case "4":
                        temp4.setText(h+" °C");
                        hum4.setText(t+" %");
                        break;

                    case "5":
                        temp5.setText(h+" °C");
                        hum5.setText(t+" %");
                        break;





                }

                int i=Integer.parseInt(id);
                hum[i] = h;
                temp[i] = t;

                if(count==5)
                {
                    //Toast.makeText(getApplicationContext(),"Data Saved",Toast.LENGTH_LONG).show();
                    animation(hum,temp);

                    count=0;

                }
                count++;


            }
            catch (StringIndexOutOfBoundsException e){Toast.makeText(getApplicationContext(),"CheckString of Room"+msg.substring(0,1),Toast.LENGTH_LONG).show();}
            catch (NumberFormatException e){Toast.makeText(getApplicationContext(),"Check number format of Room"+msg.substring(4,5),Toast.LENGTH_LONG).show();}
            catch (NullPointerException e){e.printStackTrace();}




        }


        }
    }





