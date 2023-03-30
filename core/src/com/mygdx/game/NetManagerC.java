package com.mygdx.game;


import com.mygdx.game.itf.ClientNetSender;

import java.io.IOException;
import java.net.Socket;

public class NetManagerC implements ClientNetSender {
    //private volatile boolean flag = true;
    public String ipAddress="localhost";
    public int port=6666;
    private xiangqiNet game;
    private String inputLine;
    private String temp1;
    private String temp2;
    private Thread threadClient;
    private Thread threadServer;
    private ConnectThread connect;
    //private ArrayList<Socket> sockets;
    private Socket socket;
    private boolean Contiune=true;
    public int intemp=0;
    private int playNum=2;
    private int playCount=0;
    private boolean jixugongzuo=true;

    public String intTOMess(int ss){
        temp1="["+ss+"]";
        return temp1;
    }
    //主逻辑
    public NetManagerC(xiangqiNet game0){
        game = game0;
        //System.out.println(game==null);
        connect=null;
        socket=null;
        new Thread() {
                @Override
                public void run() {
                    super.run();
                        while (jixugongzuo) {
                            try {
                                Thread.sleep(500);
                                sendBeat();
                                ConnectManager();
                            } catch (Exception e) {
                                if(connect!=null) {
                                    if(connect.live==false)
                                    {
                                        connect=null;
                                    }
                                }
                                System.out.println(e);;
                            }
                        }
                }
            }.start();
    }
    public void sendBeat(){
     if(connect!=null){
         System.out.println("SendBeatTOserver");
        game.send_this_id();
     }
    }
    public synchronized void ConnectManager() throws IOException {
        if (connect != null) {
            System.out.println("ConnectManager1");
            if (!connect.live) {
                connect = null;
                System.out.println("ConnectManager2");
            }
        }
        if (connect == null) {
            connect();
        }
    }
    public void MessToServer(String output){
        if(connect!=null) {
            connect.MessToServer(output);
        }
    }
    public void connect() throws IOException {
        System.out.println("try to connect address:"+ipAddress+"  port:"+port);
        try {
            socket = new Socket(ipAddress, port);
        }catch (Exception e){
            System.out.println("connect error");
            return;
        }
        //BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //添加in的引用
        //PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        //添加out的引用
        System.out.println("connect1");
        connect=new ConnectThread(socket,game);
        System.out.println("connect2");
        connect.start();
        //创建新线程
    }

}
