package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.Gameinstance.GameSet;
import com.mygdx.game.itf.Client;
import com.mygdx.game.tools.stools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DengLu extends InputAdapter implements ApplicationListener, Client {
    private boolean zhoumu=false;
    private Integer jieguo;
    private xiangqiNet game=null;
    private XiangqiNetStore gS;
    private Lwjgl3Application application=null;
    private GameSet set;
    private Stage stage;
    private Table table;
    private Button button_denglu;
    private Button button_zhuce;
    private String username0;
    private String password0;
    private int PngTime=0;
    private int meunState=0;
    private int meunStateChangeTime=0;
    private SpriteBatch batch;
    private Texture PngTexture;
    private NetManagerC managerC;
    private boolean RB;
    private exittime exit2;
    private int roundControl;
    private String chessmap="o";

    public DengLu(boolean zhoumu1,exittime exit1){
        zhoumu=zhoumu1;
        exit2=exit1;
    }
    public void setGame(XiangqiNetStore game1){
        gS=game1;
        gS.game=null;
    }
    @Override
    public void create() {
        if(zhoumu){
            meunState=1;
            meunStateChangeTime=1;
        }
        //todo:如果set.roundcontrol=10那么则跳过登录页
        batch = new SpriteBatch();
        username0=" ";
        password0=" ";
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.setDebug(true);
        //按钮风格
        TextureRegion upRegion =new TextureRegion(new Texture("C:/Users/19447/Pictures/b3891dd84a83aeff6c9589f65606d856.jpg"),533,300);
        TextureRegion downRegion =new TextureRegion();
        BitmapFont buttonFont =new BitmapFont();

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = new TextureRegionDrawable(upRegion);
        style.down = new TextureRegionDrawable(upRegion);
        style.font = buttonFont;

        //读取earth_ui的json

        Skin skin = new Skin(Gdx.files.internal("skin/flat-earth-ui.json"));
        //读取earth_ui的astle
        //添加按钮
        if(!zhoumu) {
            Label nameLabel = new Label("username:", skin);
            TextField nameText = new TextField("", skin);
            Label addressLabel = new Label("password:", skin);
            TextField addressText = new TextField("", skin);
            //一个按钮
            table.add(stools.createButton("username"));
            table.add(nameText).width(200);
            table.row();
            table.add(stools.createButton("password"));
            table.add(addressText).width(200);
            table.row();
            button_denglu = stools.createButton("denglu");
            button_denglu.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    //todo:登录按键功能
                    //System.out.println((nameText.getText().equals("123456"))+"  "+(nameText.getText()=="qwer")+"  "+(nameText.getText()==username0));
                    if (((nameText.getText().equals("123456") || (nameText.getText().equals("qwer")) || (nameText.getText().equals(username0))) && ((addressText.getText().equals("123")) || (addressText.getText().equals("321")) || (addressText.getText().equals(password0))))) {
                        //todo:登录成功
                        PngTexture = new Texture(Gdx.files.internal(stools.assetaddress("dengluchenggong.png")));
                        PngTime = 2 * Gdx.graphics.getFramesPerSecond();
                        System.out.println("dengluchenggong");
                        meunStateChangeTime = Gdx.graphics.getFramesPerSecond() * 1;
                        meunState = 1;
                    } else {
                        //todo:登录失败
                        PngTexture = new Texture(Gdx.files.internal(stools.assetaddress("denglushibai.png")));
                        PngTime = 2 * Gdx.graphics.getFramesPerSecond();
                        System.out.println("denglushibai");
                    }
                }
            });
            table.add(button_denglu);
            button_zhuce = stools.createButton("zhuce");
            button_zhuce.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    //todo:注册按键功能
                    username0 = nameText.getText();
                    password0 = addressText.getText();
                    //todo:注册成功
                    PngTexture = new Texture(Gdx.files.internal(stools.assetaddress("zhucechenggong.png")));
                    PngTime = 2 * Gdx.graphics.getFramesPerSecond();
                    System.out.println(PngTime);
                    System.out.println("zhucechenggong");
                    System.out.println("username:" + username0 + "  password:" + password0);
                }
            });
            table.add(button_zhuce).left();
            //登录页退出
            button_zhuce = stools.createButton("tuichu");
            button_zhuce.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                  exit2.hh=false;
                  Gdx.app.exit();
                }
            });
            table.add(button_zhuce).left();
        }
    }

    @Override
    public void resize(int i, int i1) {
        stage.getViewport().update(i, i1, true);
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        if(PngTime>0){
            PngTime--;
            batch.begin();
            batch.draw(PngTexture,100,50);
            batch.end();
        }
        if(meunStateChangeTime>0){
            meunStateChangeTime--;
            if(meunStateChangeTime==0){
                table.clearChildren();
                if(meunState==1){
                    Button button1=stools.createButton("danrenyouxi");
                    table.add(button1).center();
                    button1.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {//点击单人游戏
                            meunState=2;
                            meunStateChangeTime=1;
                        }
                    });
                    table.row();
                    Button button2=stools.createButton("lianjiyouxi");
                    table.add(button2).center();
                    button2.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {//点击联机游戏
                            meunState=3;
                            meunStateChangeTime=1;
                        }
                    });
                    table.row();
                    Button button3=stools.createButton("tuichuyouxi");
                    table.add(button3).center();
                    table.row();
                    button3.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {//点击联机游戏
                           exit2.hh=false;
                            Gdx.app.exit();
                        }
                    });
                }
                if(meunState==2){//单人游戏
                    table.clearChildren();
                    System.out.println();
                    Button button5=stools.createButton("xindedanrenyouxi");
                    table.add(button5).center();
                    table.row();
                    Button button6=stools.createButton("duqudanrenyouxi");
                    table.add(button6).center();
                    table.row();
                    button5.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            meunState=7;
                            meunStateChangeTime=1;
                        }
                    });
                    button6.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            meunState=8;
                            meunStateChangeTime=1;
                        }
                    });
                }
                if(meunState==3){//联机游戏
                    table.clearChildren();
                    meunState=3;
                    System.out.println();
                    Button button5=stools.createButton("xindelianjiyouxi");
                    table.add(button5).center();
                    table.row();
                    Button button6=stools.createButton("duqulianjiyouxi");
                    table.add(button6).center();
                    table.row();
                    button5.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            meunState=4;
                            meunStateChangeTime=1;
                        }
                    });
                    button6.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            meunState=5;
                            meunStateChangeTime=1;
                        }
                    });
                }
                if(meunState==4){//新的联机游戏
                    table.clearChildren();
                    //todo:新的联机游戏,等待连接功能
                    Button button7=stools.createButton("zhengzaidengdaiduishou");
                    table.add(button7);
                    table.row();
                    Button button8=stools.createButton("tuichulianjie");
                    table.add(button8);
                    //建立新游戏
                    Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
                    config.setForegroundFPS(120);
                    config.setTitle("Chess Game");
                    game=new xiangqiNet();
                    game.setDengluqi(this);
                    NetManagerC managerC= null;
                    managerC = new NetManagerC(game);
                    game.setManagerC(managerC);
                    /*new Thread() {
                        public void run() {
                            new Lwjgl3Application(game,config);
                        }
                    }.start();*/
                    button8.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {//退出连接等待功能
                            game.dispose();
                        }
                    });
                }
                if(meunState==5){//读取联机游戏
                    System.out.println("hhhhhhhhhhhhhhhh");
                    table.clearChildren();
                    Skin skin = new Skin(Gdx.files.internal("skin/flat-earth-ui.json"));
                    TextField Text = new TextField("", skin);
                    Text.setHeight(50);
                    //一个按钮
                    table.add(Text).width(300);
                    Button button_duqu=stools.createButton("duqu");
                    table.add(button_duqu);
                    button_duqu.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {//读取游戏
                            System.out.println("duqu qidong!!!");
                            chessmap= duqusave(Text.getText());
                            System.out.println("text1:"+chessmap);
                            meunState=6;
                            meunStateChangeTime=1;
                        }
                    });
                }
                if(meunState==6){//读取联机游戏成功，等待连接
                    table.clearChildren();
                    //todo:新的联机游戏,等待连接功能
                    //建立新游戏
                    Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
                    config.setForegroundFPS(120);
                    config.setTitle("Chess Game");
                    game=new xiangqiNet(chessmap);
                    game.setDengluqi(this);
                    NetManagerC managerC= new NetManagerC(game);
                    game.setManagerC(managerC);
                    /*new Thread() {
                        public void run() {
                            new Lwjgl3Application(game,config);
                        }
                    }.start();*/
                }
                if(meunState==7){//新的单人游戏
                    table.clearChildren();
                    //todo:新的联机游戏,等待连接功能
                    //建立新游戏
                    Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
                    config.setForegroundFPS(120);
                    config.setTitle("Chess Game");
                    game=new xiangqiNet("o",true);
                    game.setDengluqi(this);
                    NetManagerC managerC= null;
                    managerC = new NetManagerC(game);
                    game.setManagerC(managerC);
                    /*new Thread() {
                        public void run() {
                            new Lwjgl3Application(game,config);
                        }
                    }.start();*/
                }
                if(meunState==8){//读取单人游戏
                    System.out.println("hhhhhhhhhhhhhhhh");
                    table.clearChildren();
                    Skin skin = new Skin(Gdx.files.internal("skin/flat-earth-ui.json"));
                    TextField Text = new TextField("", skin);
                    Text.setHeight(50);
                    //一个按钮
                    table.add(Text).width(300);
                    Button button_duqu=stools.createButton("duqu");
                    table.add(button_duqu);
                    button_duqu.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {//读取游戏
                            System.out.println("duqu qidong!!!");
                            chessmap= duqusave(Text.getText());
                            System.out.println("text1:"+chessmap);
                            meunState=9;
                            meunStateChangeTime=1;
                        }
                    });
                }
                if(meunState==9){//读取联机游戏成功，等待连接
                    table.clearChildren();
                    //todo:新的联机游戏,等待连接功能
                    //建立新游戏
                    Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
                    config.setForegroundFPS(120);
                    config.setTitle("Chess Game");
                    game=new xiangqiNet(chessmap,true);
                    game.setDengluqi(this);
                    NetManagerC managerC= new NetManagerC(game);
                    game.setManagerC(managerC);
                    /*new Thread() {
                        public void run() {
                            new Lwjgl3Application(game,config);
                        }
                    }.start();*/

                }
        }
    }
    }
    public void jieguoRet(){
        gS.game=game;
        System.out.println("kaishiyouxi");
        Gdx.app.exit();
    }
    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        //stage.dispose();
    }

    @Override
    public boolean send_this_id() {

        return true;
    }

    @Override
    public int getServertMess(String inputLine) {

        return 0;
    }
    public String duqusave(String name1){
        File file=new File("D:/ah/sss/save/"+name1+".txt");
        FileReader fileReader;
        BufferedReader bufferedReader;
        String s="o";
        try {
            fileReader=new FileReader(file);
            bufferedReader=new BufferedReader(fileReader);
            s=bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }
}

