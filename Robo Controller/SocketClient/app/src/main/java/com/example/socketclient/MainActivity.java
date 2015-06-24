package com.example.socketclient;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Locale;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private final int REQ_CODE_SPEECH_INPUT = 100;

    TextView textResponse;
 EditText editTextAddress, editTextPort; 
 Button buttonConnect, buttonClear,buttonBackward,buttonForward,buttonLeft,buttonRight,buttonStop,buttonSmile;
 String command;
 Boolean checkupdate=false;
    private Button speak;
    private String TAG="MainActivity";
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speak");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "asdf",
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                    txtSpeechInput.setText(result.get(0));
                    Toast.makeText(getApplicationContext(),
                            ""+result.get(0),
                            Toast.LENGTH_SHORT).show();
                    String text=result.get(0);
                    if (text.equalsIgnoreCase("left"))
                    {
                        Log.v(TAG,"left");

                        command="left";
                        checkupdate=true;

                    }
                    else if(text.equalsIgnoreCase("right"))
                    {
                        Log.v(TAG,"Right");
                        command="right";
                        checkupdate=true;

                    }
                    else if(text.equalsIgnoreCase("forward"))
                    {
                        Log.v(TAG,"Forward");

                        command="forward";
                        checkupdate=true;

                    }
                    else if(text.equalsIgnoreCase("back"))
                    {
                        command="backward";
                        checkupdate=true;

                        Log.v(TAG,"Back");
                    }
                    else if(text.equalsIgnoreCase("smile"))
                    {
                        Log.v(TAG,"Smile");
                        command="smile";
                        checkupdate=true;
                    }

                    Log.v(TAG,""+result.get(0));
                }
                break;
            }
        }
    }
    @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main);
  
  editTextAddress = (EditText)findViewById(R.id.address);
  editTextPort = (EditText)findViewById(R.id.port);
  buttonConnect = (Button)findViewById(R.id.connect);
  buttonClear = (Button)findViewById(R.id.clear);
  textResponse = (TextView)findViewById(R.id.response);

     speak=(Button) findViewById(R.id.speak);

     buttonBackward=(Button) findViewById(R.id.backward);
  buttonForward=(Button) findViewById(R.id.forward);
  buttonLeft=(Button) findViewById(R.id.left);
  buttonRight=(Button) findViewById(R.id.right);
  buttonSmile=(Button) findViewById(R.id.smile);
  buttonStop=(Button) findViewById(R.id.stop);
        speak.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            Log.v(TAG,"onClick");
                promptSpeechInput();
            }
        });

  buttonStop.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			command="stop";
			checkupdate=true;
		}
		  
	  });
  buttonSmile.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			command="smile";
			checkupdate=true;
		}
		  
	  });
  buttonRight.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			command="right";
			checkupdate=true;
		}
		  
	  });
  buttonLeft.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			command="left";
			checkupdate=true;
		}
		  
	  });
  buttonBackward.setOnClickListener(new OnClickListener(){

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		command="backward";
		checkupdate=true;
	}
	  
  });
  buttonForward.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			command="forward";
			checkupdate=true;
		}
		  
	  });
  buttonConnect.setOnClickListener(buttonConnectOnClickListener);
  
  buttonClear.setOnClickListener(new OnClickListener(){

   @Override
   public void onClick(View v) {
    textResponse.setText("");
   }});
 }
 
 OnClickListener buttonConnectOnClickListener = 
   new OnClickListener(){

    @Override
    public void onClick(View arg0) {
     MyClientTask myClientTask = new MyClientTask(
       editTextAddress.getText().toString(),
       Integer.parseInt(editTextPort.getText().toString()));
     myClientTask.execute();
    }};

 public class MyClientTask extends AsyncTask<Void, Void, Void> {
  
  String dstAddress;
  int dstPort;
  String response = "";
  
  MyClientTask(String addr, int port){
   dstAddress = addr;
   dstPort = port;
  }
  @Override
	protected Void doInBackground(Void... arg0) {

		OutputStream outputStream;
		Socket socket = null;

		try {
			socket = new Socket(dstAddress, dstPort);
			Log.d("MyClient Task", "Destination Address : " + dstAddress);
			Log.d("MyClient Task", "Destination Port : " + dstPort + "");
			outputStream = socket.getOutputStream();
			PrintStream printStream = new PrintStream(outputStream);
			
			while (true) {
				if(checkupdate)
				{
					Log.d("Command", command);
					Log.d("checkUpdate", checkupdate.toString());
					printStream.print(command);
					printStream.flush();
					Log.d("Socekt connection", socket.isClosed()+"");
					checkupdate=false;
				}
			}

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response = "UnknownHostException: " + e.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response = "IOException: " + e.toString();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}


  @Override
  protected void onPostExecute(Void result) {
   textResponse.setText(response);
   super.onPostExecute(result);
  }
  
 }

}
