package test.example.com.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.ArrayList;
import rabbitMQ.QueueConsumer;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> mensajesarray = new ArrayList<String>();

    private ListView mensajesview;
    private ArrayAdapter arrayAdapter;
    private Toolbar toolbar;

    //RabbitMQ listener handler
    QueueConsumer listenerHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        //agrego elementos
        mensajesarray.add("Mensajes here...");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        /*Defino boton de pruebas
        Button btn = (Button) findViewById(R.id.button);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mensajesarray.add("nuevo elemento...");
                arrayAdapter.notifyDataSetChanged();
            }
        };
        btn.setOnClickListener(listener);
*/

        mensajesview = (ListView) findViewById(R.id.mensajes);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mensajesarray);
        mensajesview.setAdapter(arrayAdapter);


        try{
             listenerHandler = new QueueConsumer("sarem"){
                @Override
                public void handleDelivery(String consumerTag, Envelope env, AMQP.BasicProperties props, byte[] body) throws IOException{
                    String mensaje = new String(body,"utf-8");
                    //TextView tv = (TextView) findViewById(R.id.textView);
                    //tv.append(mensaje);
                    Log.d("RABBITMQ",mensaje);

                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();

                    bundle.putString("msg", mensaje);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            };

        }catch (Exception e){
            Log.d("ERROR","setear handler...");
            e.printStackTrace();
        }

        new Consumidor().execute();

    }


    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String message = msg.getData().getString("msg");
            mensajesarray.add(0,message);
            arrayAdapter.notifyDataSetChanged();
        }
    };


    private class Consumidor extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... Message) {
            try{
                listenerHandler.startupConnection();
                listenerHandler.run();
            }catch (Exception e){
                Log.d("ERRO","DO BACKGROUND...");
            }

            return null;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

/*
    ConnectionFactory factory = new ConnectionFactory();
    private void setupConnectionFactory() {
        String uri = "amqp://zquztoqc:OKzBDVlGU6H3xQ12OpTEP8OaEysrW0r4@black-boar.rmq.cloudamqp.com/zquztoqc";
        try {
            //factory.setAutomaticRecoveryEnabled(false);
            Log.d("","CONECTING...");
            factory.setUri(uri);
        } catch (KeyManagementException | NoSuchAlgorithmException | URISyntaxException e1) {
            e1.printStackTrace();
        }
    }
    void subscribe(final Handler handler)
    {
        factory = new ConnectionFactory();
        subscribeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Log.d("login", "[CONECTANDO...]" );
                        Connection connection = factory.newConnection();
                        Log.d("login", "[CONECTANDO2...]" );

                        Channel channel = connection.createChannel();
                        Log.d("login", "[CONECTANDO3...]" );

                        //channel.basicQos(1);
                        AMQP.Queue.DeclareOk q = channel.queueDeclare();
                        Log.d("login", "[CONECTANDO4...]" );
                        channel.queueBind(q.getQueue(), "", "sarem");
                        Log.d("logon", "[CONECTANDO5...]");

                        QueueingConsumer consumer = new QueueingConsumer(channel);

                        while (true) {
                            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                            String message = new String(delivery.getBody());
                            Log.d("", "[r] " + message);
                            Message msg = handler.obtainMessage();
                            Bundle bundle = new Bundle();
                            bundle.putString("msg", message);
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }
                    } catch (InterruptedException e) {
                        Log.d("", "QUE mierda...: " + e.getClass().getName());

                        break;
                    } catch (Exception e1) {
                        Log.d("", "Connection broken: " + e1.getClass().getName());
                        try {
                            Thread.sleep(5000); //sleep and then try again
                        } catch (InterruptedException e) {
                            Log.d("", "QUE mierda...: " + e.getClass().getName());
                            break;
                        }
                    }
                }
            }
        });
        Log.d("FINISH","LOG");
        subscribeThread.start();
    }

*/


}
