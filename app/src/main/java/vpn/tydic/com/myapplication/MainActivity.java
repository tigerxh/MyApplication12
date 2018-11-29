package vpn.tydic.com.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import butterknife.BindView;
import butterknife.OnClick;
import vpn.tydic.com.myapplication.beans.LoginRequestBean;
import vpn.tydic.com.myapplication.http.LoadingResponseListener;
import vpn.tydic.com.myapplication.http.ResultModel;
import vpn.tydic.com.myapplication.http.ServerLogic;

public class MainActivity extends BaseActivity {
    //分支test1

    @BindView(R.id.button)
    Button btnLogin;
    private ConnectionFactory factory = new ConnectionFactory();// 声明ConnectionFactory对象
    private Thread subscribeThread;
    // 处理handler发送的消息，然后进行操作（在主线程）
    private Handler incomingMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // 获取RabbitMQ的消息数据
            String messageData = msg.getData().getString("msg");
            Log.e("reciver", messageData);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @OnClick({R.id.button, R.id.button1, R.id.button2,R.id.button3})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                login();
                break;
            case R.id.button1:
                RxLogin();
                break;
            case R.id.button2:
                subscribeMessage();
                break;
            case R.id.button3:
                publishMessage();
                break;
        }
    }
    private void login() {

        LoginRequestBean loginBean = new LoginRequestBean();
        loginBean.userAcct = "wanghua";
        loginBean.userPwd = "a1234567" ;


        new ServerLogic().login(loginBean, new LoadingResponseListener<ResultModel>(this) {
            @Override
            public void onSuccess(String json) {
                Log.e("login", "登录成功");
            }
        });

    }
    private void RxLogin() {

        LoginRequestBean loginBean = new LoginRequestBean();
        loginBean.userAcct = "wanghua";
        loginBean.userPwd = "a1234567" ;
        new ServerLogic().RxLogin(loginBean, new LoadingResponseListener<ResultModel>(this) {
            @Override
            public void onSuccess(String json) {
                Log.e("login", "登录成功");
            }
        });

    }
    private void publishMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost("192.168.0.109");
                factory.setUsername("rollen");
                factory.setPassword("root");
                factory.setPort(5672);
                Connection connection = null;
                try {
                    String queueName = System.currentTimeMillis() + "queueNameCar";
                    connection = factory.newConnection();
                    Channel channel = connection.createChannel();
                    channel.queueDeclare(queueName, false, false, false, null);
                    String message = "Hello World!";
                    String message1 = "test1";
                    channel.basicPublish(MQ_EXCHANGE_CAR, MQ_ROUTINGKEY_CAR, null, message.getBytes());
                    System.out.println(" [x] Sent '" + message + "'");
                    channel.close();
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void subscribeMessage() {

//        setUpConnectionFactory();
        //开启消费者线程
        subscribe(incomingMessageHandler);




    }
    /**
     * 连接设置
     */
    private void setUpConnectionFactory() {
        factory.setHost("192.168.0.104");//主机地址：
        factory.setPort(5762);// 端口号
        factory.setUsername("rollen");// 用户名
        factory.setPassword("root");// 密码
        factory.setAutomaticRecoveryEnabled(true);// 设置连接恢复
    }
    /**
     * 创建消费者线程，获取小车的数据
     * @param //handler
     */
    Connection connection_car;
    private static String MQ_EXCHANGE_CAR="exchange_car";
    private static String MQ_ROUTINGKEY_CAR="routingkey_car";
    void subscribe(final Handler handler){
        subscribeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 需要再次初始化数据的时候就关闭上一个连接
                    if(connection_car != null){
                        connection_car.close();
                    }
                    ConnectionFactory factory = new ConnectionFactory();
                    factory.setHost("192.168.0.104");
                    factory.setUsername("rollen");
                    factory.setPassword("root");
                    factory.setPort(5672);
                    factory.setAutomaticRecoveryEnabled(true);// 设置连接恢复
                    // 创建新的连接
                    connection_car = factory.newConnection();
                    // 创建通道
                    Channel channel = connection_car.createChannel();
                    // 处理完一个消息，再接收下一个消息
                    channel.basicQos(1);
                    // 随机命名一个队列名称
                    String queueName = System.currentTimeMillis() + "queueNameCar";
                    // 声明交换机类型
                    channel.exchangeDeclare(MQ_EXCHANGE_CAR, "direct", true);

                    // 声明队列（持久的、非独占的、连接断开后队列会自动删除）
                    AMQP.Queue.DeclareOk q = channel.queueDeclare(queueName, true, false, true, null);// 声明共享队列
                    // 根据路由键将队列绑定到交换机上（需要知道交换机名称和路由键名称）
                    channel.queueBind(q.getQueue(), MQ_EXCHANGE_CAR, MQ_ROUTINGKEY_CAR);
                    // 创建消费者获取rabbitMQ上的消息。每当获取到一条消息后，就会回调handleDelivery（）方法，该方法可以获取到消息数据并进行相应处理
                    Consumer consumer = new DefaultConsumer(channel){
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                            super.handleDelivery(consumerTag, envelope, properties, body);
                            // 通过geiBody方法获取消息中的数据
                            String message = new String(body);
                            // 发消息通知UI更新
                            Message message1 = handler.obtainMessage();
                            Bundle bundle=new Bundle();
                            bundle.putString("msg",message);
                            message1.setData(bundle);
                            handler.sendMessage(message1);
                        }
                    };
                    channel.basicConsume(q.getQueue(), true, consumer);

                } catch (Exception e){
                    e.printStackTrace();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        subscribeThread.start();// 开启线程获取RabbitMQ推送消息
    }





}
