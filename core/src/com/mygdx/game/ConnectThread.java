package com.mygdx.game;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectThread extends Thread{
    public boolean live=true;
    private String inputLine;
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    private xiangqiNet game;
    public int id=1000;
    private boolean cz=true;

    public ConnectThread(Socket socket0,xiangqiNet game1)  {
        game=game1;
        socket=socket0;
        try {
            socket.setSoTimeout(5000);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            live =false;
            e.printStackTrace();
        }
        inputLine=new String();
    }
    @Override
    public void run() {
        super.run();
        System.out.println("MessFromServer");
       /* new Thread(){
            private long time;
            private long deltatime;
            @Override
            public void run() {
                super.run();
                while(true){
                    System.out.println("what????");
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(cz) {
                        time = System.currentTimeMillis();
                        cz=false;
                    }
                    deltatime = System.currentTimeMillis() - time;
                    System.out.println("deltatime= "+deltatime);
                    if(deltatime>5000){
                        System.out.println("diconncet has been seen");
                        live=false;break;
                    }
                }
            }
        }.start();*/
        try {
            while ((game!=null)&&((inputLine = in.readLine()) != null)) {
                cz=true;//收到信息了,重置超时器
                    game.getServertMess(inputLine);
            }
        } catch (Exception e) {
            System.out.println("Error handling client: " + e.getMessage());
            live=false;
            game.getServertMess("HasloseS:");//断线处理
        } finally {
            live=false;
        }
    }
    public void MessToServer(String output){
        out.println(output);
    }
}
