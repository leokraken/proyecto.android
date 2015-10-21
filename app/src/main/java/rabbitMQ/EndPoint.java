package rabbitMQ;

/**
 * Created by leonardo on 10/21/15.
 */
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public abstract class EndPoint{

    protected Channel channel;
    protected Connection connection;
    protected String endPointName;

    public EndPoint(String endpointName) throws IOException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException, TimeoutException{
        this.endPointName = endpointName;

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



    public void close() throws IOException, TimeoutException{
        this.channel.close();
        this.connection.close();
    }
}
