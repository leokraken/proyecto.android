package rabbitMQ;

import android.os.Handler;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

public class QueueConsumer extends Handler implements Consumer{

    protected Channel channel;
    protected Connection connection;
    protected String endPointName;

    public QueueConsumer(String endPointName){
        //super(endPointName);
        this.endPointName = endPointName;
    }

    public void startupConnection() throws IOException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException, TimeoutException{
        //Create a connection factory
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://zquztoqc:OKzBDVlGU6H3xQ12OpTEP8OaEysrW0r4@black-boar.rmq.cloudamqp.com/zquztoqc");

        //hostname of your rabbitmq server
        //factory.setHost("localhost");

        //getting a connection
        connection = factory.newConnection();

        //creating a channel
        channel = connection.createChannel();

        //declaring a queue for this channel. If queue does not exist,
        //it will be created on the server.
        //channel.queueDeclare(endpointName, false, false, false, null);
    }


    public void run() {
        try {
            //start consuming messages. Auto acknowledge messages.
            channel.basicConsume(endPointName, true,this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when consumer is registered.
     */
    public void handleConsumeOk(String consumerTag) {
        System.out.println("Consumer "+consumerTag +" registered");
    }

    /**
     * Called when new message is available.
     */
    public void handleDelivery(String consumerTag, Envelope env,
                               BasicProperties props, byte[] body) throws IOException {
        //Map map = (HashMap)SerializationUtils.deserialize(body);
        System.out.println("mensaje recibido");//("Message Number "+ map.get("message number") + " received.");

    }

    public void handleCancel(String consumerTag) {}
    public void handleCancelOk(String consumerTag) {}
    public void handleRecoverOk(String consumerTag) {}
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException arg1) {}



    public void close() throws IOException, TimeoutException{
        this.channel.close();
        this.connection.close();
    }



}