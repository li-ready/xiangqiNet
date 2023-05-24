package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Gameinstance.ChessKid.*;
import com.mygdx.game.Gameinstance.GameSet;
import com.mygdx.game.Gameinstance.cameraInputTest;
import com.mygdx.game.Gameinstance.chess;
import com.mygdx.game.itf.Client;
import com.mygdx.game.itf.Shape;
import com.mygdx.game.tools.stools;

//imgui包
//imgui引入的包

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class xiangqiNet extends InputAdapter implements ApplicationListener, Client {
    private int huiqiCount=0;
    private DengLu dengluqi=null;
    private float pngWaitTime=0;
    private float pngLifeTIme=0;
    private int PngTime=0;
    private String initmap="o";
    private int res=0;//默认退出则输掉比赛
    private int getres=0;
    private int his_O_roleid=-1;
    private int his_x=-100;
    private int his_y=-100;
    private int his_dead_roleid=-1;
    private Button button2=null;
    private boolean yicishuaxing=true;
    private boolean danren=false;
    public xiangqiNet(){

    }
    public xiangqiNet(String sse){
        System.out.println("xiangqi initmap:"+sse);
        initmap=sse;
    }
    public xiangqiNet(String sse,boolean danren1){
        System.out.println("xiangqi initmap:"+sse);
        initmap=sse;
        danren=danren1;
    }
    public int getClient_id() {
        return client_id;
    }
    private int client_id=1000;
    private boolean RBhasSet=false;

    public NetManagerC getManagerC() {
        return managerC;
    }
    public void setManagerC(NetManagerC managerC1) {
        managerC = managerC1;
        RB= managerC.port==6666;
        roundControl=-1;
    }
    //imgui的测试
    private Texture image;
    //2d的ui的舞台
    private Texture PngTexture;
    private SpriteBatch batch;
    private boolean isconnect=false;
    private String StrTemp1;
    private String StrTemp2;
    private NetManagerC managerC;
    private Vector2 vector2Temp;
    private Vector3 vector3Temp1;
    private Vector3 vector3Temp2;private Vector3 vector3Temp3;private Vector3 vector3Temp4;
   //上一帧结束到现在经过的时间
    private float chessheight;
    private float deltaTime;
    //判断是否正在加载
    public boolean loading;
    public AssetManager assets;
    private ModelBatch modelBatch;
    private Environment environment;
    private PerspectiveCamera cam;
    private ModelLoader loader;
    private Model model;
    private ModelInstance instance;
    private cameraInputTest camController;
    //模型集合,所有模型都在这,进行统一的模型资源管理
    public ModelInstance space;
    private int step;
    private Vector3 direction;
    private boolean key0;
    private Array<chess> chessArray;
    private Array<chess> RedTakenChessArray;
    private Array<chess> BlackTakenChessArray;
    private float chessy=0;
    private int circle=0;
    private int[][] chessFenbu;
    private Array<Integer> reddeadchess;
    private Array<Integer> blackdeadchess;
    private Material selectionMaterial;
    private Material originalMaterial;
    private BoundingBox bounds;
    private Shape shape0;
    private int visibleCount;
    //以下是二维场景渲染4大件
    private Table table;
    protected Stage stage;
    protected Label label;
    protected BitmapFont font;
    protected StringBuilder stringBuilder;
    private int roundControl=-1;
    //回合的属性  -1:不是自己的回合  1:正在选棋  2:正在选步
    private int waitTaken;
    private int selecting=-1;//正在被选的棋子编号
    private int selected=-1;//已经被选的棋子编号
    private float waitTakenTime =0;
    public boolean RB=true;
    private Vector2[][] qipanDot;
    private float time;
    //多路输入控制器,将需要绑定的对象绑定
    private InputMultiplexer inputMultiplexer=new InputMultiplexer();
    @Override
    public void create () {
        huiqiCount=2;
        {//imgui的创建
        }
        {//悬浮效果
            batch = new SpriteBatch();
        }
        Gdx.gl.glClearColor(0, 0, 0, 0);
        vector2Temp=new Vector2();
        RedTakenChessArray=new Array<chess>();
        BlackTakenChessArray=new Array<chess>();
        waitTaken=-1;
        selectionMaterial = new Material();
        originalMaterial=new Material();
        visibleCount = 0;

        bounds=new BoundingBox();
        chessFenbu =new int[9][11];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 11; j++) {
                chessFenbu[i][j]=0;//0代表空
            }
        }
        vector3Temp1=new Vector3();
        vector3Temp2=new Vector3();
        vector3Temp3=new Vector3();
        vector3Temp4=new Vector3();
        chessArray=new Array<chess>();
        key0=false;
        deltaTime=0;
        direction=new Vector3(-3,0,-3);
        step=0;
        modelBatch = new ModelBatch();
        Gdx.graphics.setWindowedMode(1000,750);
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(7f, 7f, 100f);
        cam.lookAt(0,0,0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();
        {//ui舞台的初始化
            //stage = new Stage();备份
            stage = new Stage(new ScreenViewport());
            //绑定stage到输入控制
            inputMultiplexer.addProcessor(stage);
            font = new BitmapFont();
            label = new Label(" ", new Label.LabelStyle(font, Color.WHITE));
            table=new Table().right();
            table.setFillParent(true);
            table.setDebug(true);
            //加入按钮
                Button button1=stools.createButton("baocun");
                table.add(button1);
                button1.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        System.out.println("baocun");
                        String s=getChessMap();
                        try {
                            File folder = new File("./save");
                            folder.mkdir();
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                            System.out.println(formatter.format(new Date()));
                            File file = new File("./save/"+formatter.format(new Date()) +".txt");
                            file.createNewFile();
                            FileWriter writer = new FileWriter(file);
                            writer.write(s);
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        CreatePngAnime("baocunchenggong",0,1.5f);
                        //todo:保存按钮功能
                    }
                });
            //悔棋功能
            table.row();
            button2=stools.createButton("huiqi");
            table.add(button2);
            button2.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(huiqiCount>0){
                        roundControl=1;
                        huiqiCount--;
                        managerC.MessToServer(client_id+"Huiqi000:");
                        if (getchessByRole_id_InRedTaken(his_dead_roleid) != null) {
                            getchessByRole_id_InRedTaken(his_dead_roleid).setqipanLocation(chessArray.get(getchessByRole_id_toArrayid(his_O_roleid)).getQipanx(),chessArray.get(getchessByRole_id_toArrayid(his_O_roleid)).getQipany());
                            getchessByRole_id_InRedTaken(his_dead_roleid).qipanLocationRefresh();
                            for (int i = 0; i < RedTakenChessArray.size; i++) {
                                if(RedTakenChessArray.get(i).getRole_id()==his_dead_roleid){
                                    chessArray.add(RedTakenChessArray.get(i));
                                    RedTakenChessArray.removeIndex(i);
                                }
                            }
                        }
                        if (getchessByRole_id_InBlackTaken(his_dead_roleid) != null) {
                            getchessByRole_id_InBlackTaken(his_dead_roleid).setqipanLocation(chessArray.get(getchessByRole_id_toArrayid(his_O_roleid)).getQipanx(),chessArray.get(getchessByRole_id_toArrayid(his_O_roleid)).getQipany());
                            getchessByRole_id_InBlackTaken(his_dead_roleid).qipanLocationRefresh();
                            for (int i = 0; i < BlackTakenChessArray.size; i++) {
                                if(BlackTakenChessArray.get(i).getRole_id()==his_dead_roleid){
                                    chessArray.add(BlackTakenChessArray.get(i));
                                    BlackTakenChessArray.removeIndex(i);
                                }
                            }
                        }
                        chessArray.get(getchessByRole_id_toArrayid(his_O_roleid)).setqipanLocation(his_x,his_y);
                        chessArray.get(getchessByRole_id_toArrayid(his_O_roleid)).qipanLocationRefresh();
                        if(huiqiCount==0){
                            Texture myTexture = new Texture(Gdx.files.internal(stools.assetaddress("huiqi1.png")));
                            TextureRegion myTextureRegion = new TextureRegion(myTexture);
                            Drawable drawable=new TextureRegionDrawable(myTextureRegion);
                            button2.getStyle().up=drawable;
                        }
                    }
                }
            });
            //认输功能
            if(!danren) {
                table.row();
                Button button3 = stools.createButton("renshu");
                table.add(button3);
                button3.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        getres = 1;
                        res = 0;
                        managerC.MessToServer(client_id + "Result00:0");
                    }
                });
            }
            //退出功能
            table.row();
            Button button5=stools.createButton("tuichu");
            table.add(button5);
            button5.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.exit();
                }
            });

            stage.addActor(table);
            stage.addActor(label);
            stringBuilder = new StringBuilder();
            /*//添加一个表格,表格管理ui
            table = new Table();
            table.setFillParent(true);
            stage.addActor(table);
            table.setDebug(true); // This is optional, but enables debug lines for tables.
            //添加两个按钮
            TextButton button1 = new TextButton("Button 1",);
            table.add(button1);
            TextButton button2 = new TextButton("Button 2", );
            table.add(button2);*/
        }
        assets = new AssetManager();
        assets.load(stools.assetaddress("source/ChineseChess.g3db"), Model.class);
        loading = true;
        //绑定视角转动器
        camController = new cameraInputTest(cam);
        inputMultiplexer.addProcessor(this);
        //绑定stage到输入控制
        inputMultiplexer.addProcessor(camController);
        //多路输入绑定到输入器
        //Gdx.input.setInputProcessor(inputMultiplexer);
        Gdx.input.setInputProcessor(inputMultiplexer);
        qipanDot=new Vector2[9][11];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 11; j++) {
                if(j==5) {
                    qipanDot[i][j]=new Vector2(0,0);
                    qipanDot[i][j].x = stools.qipanLocationToLocation(i - 4, j - 5, vector3Temp1).z;
                    qipanDot[i][j].y=0;
                    //System.out.println("dotx= "+i+"doty= "+j);
                }
                else {
                    //System.out.println("rightway dotx= "+i+"  doty= "+j);
                    //System.out.println("create  i= "+i+" j= "+j);//待删除
                    qipanDot[i][j] = new Vector2(0, 0);
                    qipanDot[i][j].x = stools.qipanLocationToLocation(i - 4, j - 5, vector3Temp1).z;
                    qipanDot[i][j].y = stools.qipanLocationToLocation(i - 4, j - 5, vector3Temp1).x;
                    //System.out.println("go x= "+qipanDot[i][j].x+"go y="+qipanDot[i][j].y);//待删除
                }
            }
        }
    }

    private void CreatePngAnime(String name,float png_wait_time,float png_life_time) {
        pngWaitTime=png_wait_time;
        pngLifeTIme=png_life_time;
        PngTexture=new Texture(Gdx.files.internal(stools.assetaddress(name+".png")));
    }

    @Override
    public void resize(int width, int height) {
        //窗口被改变,调整ui的布局
        stage.getViewport().update(width, height, true);
    }
    private void refreshChessDistribution()
    {

    }
    //完成加载fbx-conv -f -v qipan.fbx qipan.g3db
    private void doneLoading() {
        //
        int role_id_count=2;
        String id;
        Model model = assets.get(stools.assetaddress("source/ChineseChess.g3db"), Model.class);
        for (Node node1:model.nodes) {
            id = node1.id;
            ModelInstance model0=new ModelInstance(model,id,true);
            if(id.equals("QiPan"))
            {
                space=model0;
                continue;
            }
            else
            {
                chess chess0=new chess(model,id,true);
                switch (id) {
                    case "R-che001":
                        chess0=new Che(model,id,true);
                        chess0.setqipanLocation(-4,-5);
                        chess0.setCamp(true);
                        break;
                    case "R-ma001":
                        chess0=new Ma(model,id,true);
                        chess0.setqipanLocation(-3,-5);chess0.setCamp(true);
                        break;
                    case "R-xiang001":
                        chess0=new Xiang(model,id,true);
                        chess0.setqipanLocation(-2,-5);chess0.setCamp(true);
                        break;
                    case "R-shi001":
                        chess0=new Shi(model,id,true);
                        chess0.setqipanLocation(-1,-5);chess0.setCamp(true);
                        break;
                    case "R-jiang001":
                        chess0=new Jiang(model,id,true);
                        chess0.setqipanLocation(0,-5);chess0.setCamp(true);
                        break;
                    case "R-shi002":
                        chess0=new Shi(model,id,true);
                        chess0.setqipanLocation(1,-5);chess0.setCamp(true);
                        break;
                    case "R-xiang002":
                        chess0=new Xiang(model,id,true);
                        chess0.setqipanLocation(2,-5);chess0.setCamp(true);
                        break;
                    case "R-ma002":
                        chess0=new Ma(model,id,true);
                        chess0.setqipanLocation(3,-5);chess0.setCamp(true);
                        break;
                    case "R-che002":
                        chess0=new Che(model,id,true);
                        chess0.setqipanLocation(4,-5);chess0.setCamp(true);
                        break;
                    case "R-bing001":
                        chess0=new Bing(model,id,true);
                        chess0.setqipanLocation(-4,-2);chess0.setCamp(true);
                        break;
                    case "R-bing002":
                        chess0=new Bing(model,id,true);
                        chess0.setqipanLocation(-2,-2);chess0.setCamp(true);
                        break;
                    case "R-bing003":chess0=new Bing(model,id,true);
                        chess0.setqipanLocation(0,-2);chess0.setCamp(true);
                        break;
                    case "R-bing004":chess0=new Bing(model,id,true);
                        chess0.setqipanLocation(2,-2);chess0.setCamp(true);
                        break;
                    case "R-bing005":chess0=new Bing(model,id,true);
                        chess0.setqipanLocation(4,-2);chess0.setCamp(true);
                        break;
                    case "R-pao001":chess0=new Pao(model,id,true);
                        chess0.setqipanLocation(-3,-3);chess0.setCamp(true);
                        break;
                    case "R-pao002":chess0=new Pao(model,id,true);
                        chess0.setqipanLocation(3,-3);chess0.setCamp(true);
                        break;
                    case "B-che001":chess0=new Che(model,id,true);
                        chess0.setqipanLocation(-4,5);chess0.setCamp(false);
                        break;
                    case "B-ma001":chess0=new Ma(model,id,true);
                        chess0.setqipanLocation(-3,5);chess0.setCamp(false);
                        break;
                    case "B-xiang001":chess0=new Xiang(model,id,true);
                        chess0.setqipanLocation(-2,5);chess0.setCamp(false);
                        break;
                    case "B-shi001":chess0=new Shi(model,id,true);
                        chess0.setqipanLocation(-1,5);chess0.setCamp(false);
                        break;
                    case "B-jiang001":chess0=new Jiang(model,id,true);
                        chess0.setqipanLocation(0,5);chess0.setCamp(false);
                        break;
                    case "B-shi002":chess0=new Shi(model,id,true);
                        chess0.setqipanLocation(1,5);chess0.setCamp(false);
                        break;
                    case "B-xiang002":chess0=new Xiang(model,id,true);
                        chess0.setqipanLocation(2,5);chess0.setCamp(false);
                        break;
                    case "B-ma002":chess0=new Ma(model,id,true);
                        chess0.setqipanLocation(3,5);chess0.setCamp(false);
                        break;
                    case "B-che002":chess0=new Che(model,id,true);
                        chess0.setqipanLocation(4,5);chess0.setCamp(false);
                        break;
                    case "B-bing002":chess0=new Bing(model,id,true);
                        chess0.setqipanLocation(-4,2);chess0.setCamp(false);
                        break;
                    case "B-bing003":chess0=new Bing(model,id,true);
                        chess0.setqipanLocation(-2,2);chess0.setCamp(false);
                        break;
                    case "B-bing004":chess0=new Bing(model,id,true);
                        chess0.setqipanLocation(0,2);chess0.setCamp(false);
                        break;
                    case "B-bing005":chess0=new Bing(model,id,true);
                        chess0.setqipanLocation(2,2);chess0.setCamp(false);
                        break;
                    case "B-bing006":chess0=new Bing(model,id,true);
                        chess0.setqipanLocation(4,2);chess0.setCamp(false);
                        break;
                    case "B-pao001":chess0=new Pao(model,id,true);
                        chess0.setqipanLocation(-3,3);chess0.setCamp(false);
                        break;
                    case "B-pao002":chess0=new Pao(model,id,true);
                        chess0.setqipanLocation(3,3);chess0.setCamp(false);
                        break;
                }

                int x=chess0.getArrayQx();int y=chess0.getArrayQy();
                if(chess0.isCamp())
                chessFenbu[x][y]=1;
                else
                    chessFenbu[x][y]=-1;
                //System.out.println(id+" hh");//待删除
                if(id.equals("R-jiang001")){
                    //System.out.println("hhhhhhhhhhhhhhh");//待删除
                    chess0.setRole_id(0);
                    chessArray.insert(0, chess0);
                }
                else{
                if (id.equals("B-jiang001")) {
                    chess0.setRole_id(1);
                    chessArray.insert(0, chess0);
                    //System.out.println("hhhxhhhhhhhhhhh");//待删除
                }
                else {
                    chessArray.add(chess0);
                    chess0.setRole_id(role_id_count);
                    role_id_count++;
                }
            }
            }
        }
        for (chess chess2:chessArray
             ) {
            chess2.qipanLocationRefresh();
/*
            System.out.println("chessX= "+chess0.GetLocation().x+"  chessY= "+chess0.GetLocation().y+"  chessZ= "+chess0.GetLocation().z);
*/
            //待删除
        }
        Vector3 location = chessArray.first().GetLocation();
        chessheight=location.y;
        System.out.println("chessheight = "+chessheight);//待删除
        //System.out.println(location.x+" ; "+location.y+" ; "+location.z);
        //qizi.first().
        //qizi.first().transform.setToTranslation(location.x,location.y,location.z);
        //待删除
       /*for (chess chess1 : chessArray) {
            System.out.println(chess1.nodes.get(0).id);
        }*/
        loading = false;

    }
    @Override
    public void render() {
        if((!loading)&&(!initmap.equals("o"))&&(managerC!=null)&&(yicishuaxing)) {
            if(managerC.connect!=null) {
                yicishuaxing = false;
                readChessMap(initmap);
                System.out.println("beauty save make");
                readChessMap(initmap);
                managerC.MessToServer(client_id + "refresh0:" + initmap);
                initmap = "o";
            }

        }
        if(getres==1){
            getres=2;
            getGameResult();
        }
        visibleCount = 0;
        time+=Gdx.graphics.getDeltaTime();
        //time+=roundControl==-1?Gdx.graphics.getDeltaTime():0;
        if (loading && assets.update())
            doneLoading();
        deltaTime=Gdx.graphics.getDeltaTime();
        if(waitTaken!=-1)
        {
            waitTakenTime-=deltaTime;
            if(waitTakenTime<=0f)
            {
               takeChessDone();
            }
        }
        for (chess chess1 : chessArray
             ) {
            chess1.runPathAnimation(deltaTime,false);
            if(time>=2f) {
                chess1.qipanLocationRefresh();
            }
        }
        if(time>=2f){
            System.out.println(roundControl);
            /*if(roundControl==-1){
                System.out.println("you round");//待删除
                roundControl=1;
                RB=!RB;
            }*/
            if(client_id==1000){
                send_this_id();
            }
            time=0f;
        }
        cam.update();
        key0=Gdx.input.isTouched();
        //Gdx.gl20.glEnable(GL20.GL_SAMPLE_ALPHA_TO_COVERAGE);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));
        modelBatch.begin(cam);
        //渲染instances集合,把所有模型都渲染
        for (final chess instance : chessArray) {
            if (instance.isVisible(cam)) {
                modelBatch.render(instance, environment);
                visibleCount++;
            }
        }
        for (final chess instance : RedTakenChessArray) {
            if (instance.isVisible(cam)) {
                modelBatch.render(instance, environment);
                visibleCount++;
            }
        }
        for (final chess instance : BlackTakenChessArray) {
            if (instance.isVisible(cam)) {
                modelBatch.render(instance, environment);
                visibleCount++;
            }
        }

        if (space != null)
            modelBatch.render(space);
        modelBatch.end();
        {//imgui的渲染
        }
        {//ui的stage的刷新
            {//性能信息
                stringBuilder.setLength(0);
                stringBuilder.append(" FPS: ").append(Gdx.graphics.getFramesPerSecond());
                stringBuilder.append(" Visible: ").append(visibleCount);
                stringBuilder.append(" Selected: ").append(roundControl);
                stringBuilder.append(" Selecting: ").append(selecting);
                stringBuilder.append(" changkuan: ").append(Gdx.graphics.getHeight()+" "+Gdx.graphics.getWidth());
                label.setText(stringBuilder);
            }
            {//ui
                stage.act(deltaTime);
            }
            stage.draw();
        }
        if(pngWaitTime!=0){
            pngWaitTime-=deltaTime;
            pngWaitTime=(pngWaitTime<0)?0:pngWaitTime;
        }
        if((pngWaitTime==0)&&(pngLifeTIme!=0)){
            pngLifeTIme-=deltaTime;
            //System.out.println(PngTexture.getWidth()+"  "+PngTexture.getHeight());
            batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
            batch.begin();
            float centerX = Gdx.graphics.getWidth() / 2f;
            float centerY = Gdx.graphics.getHeight() / 2f;
            float spriteX = centerX - PngTexture.getWidth() / 2f;
            float spriteY = centerY - PngTexture.getHeight() / 2f;
            batch.draw(PngTexture,spriteX,spriteY);
            batch.end();
            pngLifeTIme=(pngLifeTIme<0)?0:pngLifeTIme;
            if((pngLifeTIme==0)&&(getres==2)){
                Gdx.app.exit();
            }
        }
    }
    public void takeChessDone(){
        waitTakenTime=0f;
        System.out.println(waitTaken);
        if(chessArray.get(waitTaken).isCamp()){
            BlackTakenChessArray.add(chessArray.get(waitTaken));
            vector3Temp1= stools.qipanLocationToLocation(false,BlackTakenChessArray.size,vector3Temp1);
        }
        else{
            vector3Temp1= stools.qipanLocationToLocation(true,RedTakenChessArray.size,vector3Temp1);
            RedTakenChessArray.add(chessArray.get(waitTaken));
        }
        //todo:输出吃棋特效,待删除
        System.out.println("takechess");
        chessFenbu[chessArray.get(waitTaken).getArrayQx()][chessArray.get(waitTaken).getArrayQy()]=0;
        chessArray.get(waitTaken).transform.setTranslation(vector3Temp1);
        if(chessArray.get(waitTaken).getRole_id()<=1){
            if(roundControl==1){
                res=0;
            }
            else {
            res=2;
            }
            getres=1;
        }
        chessArray.removeIndex(waitTaken);
        //System.out.println(RedTakenChessArray.get(0).nodes.get(0).id);
        waitTaken=-1;
    }
    public void takeChess(chess chessO,int chessP) {
        if(waitTaken==-1){
            chessO.createPathAnimation(chessArray.get(chessP).getQipanx(),chessArray.get(chessP).getQipany(), chessFenbu);
            waitTaken=chessP;
            waitTakenTime =1f/chessO.getPathAnimationspeed();
        }
    }
    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        //if(roundControl>0)
        if(roundControl!=-1) {
            selecting = getObject(screenX, screenY);//选择一个棋子,没选上就返回-1
            if (selected >= 0)//选到了棋子
            {

            }
        }
        return selecting >= 0;
    }
    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button) {
        boolean out = false;
        if(roundControl!=-1) {
            boolean isGetchess = false;
            boolean isGetDot = false;
            int i = 0;
            int j = 0;

            if (selecting >= 0) {
                if (selecting == getObject(screenX, screenY)) {
                    //???
                    out = true;
                    isGetchess = true;
                }
            }
            if (!out) {
                //选择棋盘上的一块区域
                Ray ray = cam.getPickRay(screenX, screenY);
                vector3Temp3 = (vector3Temp1.set(cam.position).add(vector3Temp2.set(ray.direction).setLength((cam.position.y - chessheight) / (ray.direction.dot(0, -1, 0) / ray.direction.len()))));
                //以上是棋盘表面上的3d坐标
                vector2Temp.x = vector3Temp3.z ;//- 32.194542f;
                vector2Temp.y = vector3Temp3.x ;//- 45.48245f;
                //待删除
                //System.out.println("chessX= " + chessArray.first().transform.getTranslation(vector3Temp1).z + "  chessY= " + chessArray.first().transform.getTranslation(vector3Temp1).x);
                System.out.println("rayX= " + vector2Temp.x + "  rayY= " + vector2Temp.y);
                boolean l = true;
                int cc = 0;
                for (int k = 0; k < 9; k++) {
                    if (vector2Temp.x > qipanDot[i][j].x) {
                        i++;
                        //System.out.println("i= "+i);
                    }
                }
                for (int k = 0; k < 11; k++) {
                    if (vector2Temp.y > qipanDot[0][j].y) {
                        j++;
                        //System.out.println("j= "+j);
                    }
                }
                //System.out.println("i= " + i + "  j= " + j);
                if (i > 0 && i < 9 && j > 0 && j < 11) {
                    float ds = 0;
                    if (!isGetDot&&vector2Temp.dst(qipanDot[i][j]) < 4f) {
                        isGetDot = j!=5;

                    }
                    if (!isGetDot&&vector2Temp.dst(qipanDot[i][j - 1]) < 4f) {
                        isGetDot = j!=6;
                        j-=isGetDot?1:0;
                    }
                    if (!isGetDot&&vector2Temp.dst(qipanDot[i - 1][j]) < 4f) {
                        isGetDot = j!=5;
                        i-=isGetDot?1:0;
                    }
                    //System.out.println("i= " + i + "  j= " + j);//待删除
                    if (!isGetDot&&vector2Temp.dst(qipanDot[i - 1][j - 1]) < 4f) {
                        isGetDot = j!=6;
                        i-=isGetDot?1:0;
                        j-=isGetDot?1:0;
                    }
                    out = false;

                }
            }
            if (isGetDot) System.out.println("qipanDOt: " + i + "  " + j);//待删除
            if (roundControl != -1) ;
            {
                if (isGetchess) {
                    if (roundControl == 1 && (chessArray.get(selecting).isCamp() == RB)) {
                        System.out.println("batch1");//待删除
                        //待删除
                        //System.out.println("selecting chess 3Dx= " +chessArray.get(selecting).transform.getTranslation(vector3Temp3).z+"  y= "+chessArray.get(selecting).transform.getTranslation(vector3Temp3).x);
                        //System.out.println("selecting chess x= " +chessArray.get(selecting).getQipanx()+"  y= "+chessArray.get(selecting).getQipany());
                        setSelected(selecting);
                        selecting = -1;
                        roundControl = 2;
                    } else {
                        if (roundControl == 1 && !(chessArray.get(selecting).isCamp() == RB)) {
                            //输出:你无法选择这个棋子
                        } else {
                            if (roundControl == 2 && (chessArray.get(selecting).isCamp() == RB)) {
                                //待删除
                                //System.out.println("selecting chess 3Dx= " +chessArray.get(selecting).transform.getTranslation(vector3Temp3).z+"  y= "+chessArray.get(selecting).transform.getTranslation(vector3Temp3).x);
                                //System.out.println("selecting chess x= " +chessArray.get(selecting).getQipanx()+"  y= "+chessArray.get(selecting).getQipany());
                                setSelected(selecting);
                                selecting = -1;
                                roundControl = 2;
                            } else if (roundControl == 2 && !(chessArray.get(selecting).isCamp() == RB)) {
                                if (chessArray.get(selected).takeqiJudge(chessArray.get(selecting), chessFenbu)) {
                                    roundControl = -1;
                                    //服务器功能待恢复
                                    takeqi(chessArray.get(selected).getRole_id(),chessArray.get(selecting).getRole_id());
                                    takeChess(chessArray.get(selected), selecting);
                                    setSelected(-1);
                                    selecting = -1;
                                    //发送信号:吃棋
                                    //调用将军提醒
                                } else {
                                    //你不能这么吃棋
                                }
                            }
                        }
                    }
                } else {
                    if (isGetDot && roundControl == 2) {
                        System.out.println("bath2");//待删除
                        if (chessArray.get(selected).zouqiJudge(i - 4, j - 5, chessFenbu)) {
                            System.out.println("bath3");//待删除
                            roundControl = -1;
                            //服务器功能待恢复
                            zouqi(chessArray.get(selected).getRole_id(),i-4,j-5);
                            //向服务器发送走棋指令
                            System.out.println("x out="+(i-4)+"  y out="+(j-5));
                            chessArray.get(selected).createPathAnimation(i - 4, j - 5, chessFenbu);
                            setSelected(-1);
                            //发信服务器
                            //将军提醒
                        } else {
                            //输出,你的走棋不合法
                        }
                    }
                }
            }
            {//随机动画测试,待删除
           /* Random random = new Random();
            int randomx = random.nextInt(9) - 4;
            int randomy = random.nextInt(10) - 5;
            if (randomy >= 0) {
                randomy++;
            }
            chessArray.first().createPathAnimation(randomx, randomy,chessFenbu);*/
            }
        }
        return out;
    }

    public void setSelected (int value) {//若传入-1则去除所有的选棋
        if (selected == value) return;//选择棋子没变
        if (selected >= 0) {//返回原来的被选棋的效果
            Material mat = chessArray.get(selected).materials.get(0);
            mat.clear();
            mat.set(originalMaterial);
        }
        selected = value;
        if (selected >= 0) {
            Material mat = chessArray.get(selected).materials.get(0);
            originalMaterial.clear();
            originalMaterial.set(mat);
            mat.clear();
            selectionMaterial.clear();
            selectionMaterial.set(originalMaterial);
            selectionMaterial.set(ColorAttribute.createDiffuse(Color.GOLD));
            mat.set(selectionMaterial);
        }
    }

    public int getObject (int screenX, int screenY) {
        Ray ray = cam.getPickRay(screenX, screenY);
        int result = -1;
        float distance = -1;
        for (int i = 0; i < chessArray.size; ++i) {
            final float dist2 = chessArray.get(i).intersects(ray);
            if (dist2 >= 0f && (distance < 0f || dist2 <= distance)) {
                result = i;
                distance = dist2;
            }
        }
        return result;
    }
    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        System.out.println("game over");
        modelBatch.dispose();
        chessArray.clear();
        assets.dispose();
        //输赢决定,释放连接
        managerC.MessToServer(client_id+"Result00:"+res);
        managerC.dispose();
        {//imgui的释放
        }
       /* stage2D.dispose();*/
    }

    public chess getchessByRole_id(int id){
        for (chess chess0 : chessArray) {
            if(id==chess0.getRole_id())
                return chess0;
        }
        return null;
    }
    public chess getchessByRole_id_InRedTaken(int id){
        for (chess chess0 : RedTakenChessArray) {
            if(id==chess0.getRole_id())
                return chess0;
        }
        return null;
    }
    public chess getchessByRole_id_InBlackTaken(int id){
        for (chess chess0 : BlackTakenChessArray) {
            if(id==chess0.getRole_id())
                return chess0;
        }
        return null;
    }
    public int getchessByRole_id_toArrayid(int id){
        for (int i = 0; i < chessArray.size; i++) {
            if(chessArray.get(i).getRole_id()==id)
                return i;
        }
        return -1;
    }
    public int getServertMess(String inputLine){
        System.out.println("get information");//待删除
        System.out.println(inputLine);
        StrTemp1=inputLine.substring(0,8);
        System.out.println(StrTemp1);
        int i=inputLine.indexOf(":");
        //System.out.println("i= "+i);
        if(i!=-1){
            StrTemp2=inputLine.substring(i+1,inputLine.length());
        }
        System.out.println(StrTemp2);
        switch (StrTemp1){
            case "refresh0": {
                System.out.println("I have save refresh");
                initmap=StrTemp2;
                break;
            }
            case "Zouqi000":{ String[] result = StrTemp2.split(",");
                if(result.length>2) {
                    NetZouqi(Integer.parseInt(result[0]), Integer.parseInt(result[1]), Integer.parseInt(result[2]));
                }
                break;
            }
            case "RBset000":{//被设置为红方,roundControl=1
               RB=Integer.parseInt(StrTemp2)==0;
               if(RB&&!RBhasSet){
                   roundControl=1;
               }
                if(!RBhasSet) {
                    dengluqi.jieguoRet();
                    System.out.println("RB has been set:"+RB);
                    System.out.println("RBset initpmap:"+initmap);

                }
                RBhasSet=true;
               break;
            }
            case "Takeqi00":{
                String[] result = StrTemp2.split(",");
                if(result.length>1) {
                    NetTakeqi(Integer.parseInt(result[0]), Integer.parseInt(result[1]));
                }
                break;
            }
            case "ExData00":{
                NetExtendData(StrTemp2);break;
            }
            case "Disconn0":{
                NetDisconnect();break;
            }
            case "HasloseS":{
                //client_id是发信息的id
                Client_LoseConnectWait();break;
            }
            case "Idset000":{
                //client_id是发信息的id
                client_id=Integer.parseInt(StrTemp2);break;
            }
            case "Result00":{
                //client_id是发信息的id
                res=Integer.parseInt(StrTemp2);
                getres=1;
                managerC.MessToServer(client_id+ "Result00:"+res);
                break;
            }
            case "Huiqi000":{
                //client_id是发信息的id
                huiqiCount++;
                roundControl=-1;
                if(huiqiCount>0){
                    huiqiCount--;
                    if (getchessByRole_id_InRedTaken(his_dead_roleid) != null) {
                        getchessByRole_id_InRedTaken(his_dead_roleid).setqipanLocation(chessArray.get(getchessByRole_id_toArrayid(his_O_roleid)).getQipanx(),chessArray.get(getchessByRole_id_toArrayid(his_O_roleid)).getQipany());
                        getchessByRole_id_InRedTaken(his_dead_roleid).qipanLocationRefresh();
                        for (int j = 0; j < RedTakenChessArray.size; j++) {
                            if(RedTakenChessArray.get(j).getRole_id()==his_dead_roleid){
                                chessArray.add(RedTakenChessArray.get(j));
                                RedTakenChessArray.removeIndex(j);
                            }
                        }
                    }
                    if (getchessByRole_id_InBlackTaken(his_dead_roleid) != null) {
                        getchessByRole_id_InBlackTaken(his_dead_roleid).setqipanLocation(chessArray.get(getchessByRole_id_toArrayid(his_O_roleid)).getQipanx(),chessArray.get(getchessByRole_id_toArrayid(his_O_roleid)).getQipany());
                        getchessByRole_id_InBlackTaken(his_dead_roleid).qipanLocationRefresh();
                        for (int i1 = 0; i1 < BlackTakenChessArray.size; i1++) {
                            if(BlackTakenChessArray.get(i1).getRole_id()==his_dead_roleid){
                                chessArray.add(BlackTakenChessArray.get(i1));
                                BlackTakenChessArray.removeIndex(i1);
                            }
                        }
                    }
                    chessArray.get(getchessByRole_id_toArrayid(his_O_roleid)).setqipanLocation(his_x,his_y);
                    chessArray.get(getchessByRole_id_toArrayid(his_O_roleid)).qipanLocationRefresh();
                }
                FenbuRefresh();
                break;
            }
        }
        isconnect=true;//接到信息已经连接
        return 0;
    }
    public void getGameResult(){
        System.out.println("res0:"+res);
        inputMultiplexer.clear();
        switch (res){
            case 0:{
               //输掉游戏
                CreatePngAnime("ninshule",0,4f);
                break;
            }
            case 1:{
                //平局游戏
                CreatePngAnime("pingju",0,4f);
                break;
            }
            case 2:{
                //赢得游戏
                CreatePngAnime("ninyingle",0,4f);
                break;
            }
        }
    }
    public void Client_LoseConnectWait(){//connect线程依然在运行,并调用了这个方法
        isconnect=false;
        boolean jixu = true;
        long timestart = System.currentTimeMillis();
        long time1 = 0;
        while (jixu) {
            try {

                Thread.sleep(1000);
                time1 = timestart - System.currentTimeMillis();
                    if (time1 > 30000) {
                        jixu = false;
                    }
                    if(isconnect){jixu=false;}
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (time1 < 30000) {
        } else {
            System.out.println("you lose");
            //待添加,id的player已经断连,启动输赢方法
        }
    }
    //接收服务器信息方法,有Net前缀的都是处理服务器发来的信息的方法
    public void NetRefresh(String Output){
        readChessMap(Output);
    }
    public void NetExtendData(String str){
    }
    public void NetDisconnect(){
    }
    public void NetZouqi(int ORole_id, int x, int y) {
        his_O_roleid=ORole_id;
        his_x=chessArray.get(getchessByRole_id_toArrayid(ORole_id)).getQipanx();
        his_y=chessArray.get(getchessByRole_id_toArrayid(ORole_id)).getQipany();
        his_dead_roleid=-1;
        chess chess0=getchessByRole_id(ORole_id);
        if(chess0!=null){
            chess0.createPathAnimation(x , y, chessFenbu);
            roundControl=1;
        }
    }
    public void NetTakeqi(int ORole_id, int PRole_id) {
        his_O_roleid=ORole_id;
        his_x=chessArray.get(getchessByRole_id_toArrayid(ORole_id)).getQipanx();
        his_y=chessArray.get(getchessByRole_id_toArrayid(ORole_id)).getQipany();
        his_dead_roleid=PRole_id;
        chess chess0=getchessByRole_id(ORole_id);
        int x=getchessByRole_id_toArrayid(PRole_id);
        if((chess0!=null)&&(x!=-1)){
            takeChess(chess0,x);
            roundControl=1;
        }
    }
    public void NetRefresh() {
    }
    //发信方法
    public void ask_client_id(){
    }
    public void zouqi(int ORole_id, int x, int y) {
        his_O_roleid=ORole_id;
        his_x=chessArray.get(getchessByRole_id_toArrayid(ORole_id)).getQipanx();
        his_y=chessArray.get(getchessByRole_id_toArrayid(ORole_id)).getQipany();
        his_dead_roleid=-1;
        System.out.println("zouqi output: "+client_id+ "Zouqi000:"+ORole_id+","+x+","+y);
        managerC.MessToServer(client_id+ "Zouqi000:"+ORole_id+","+x+","+y);
    }
    public void takeqi(int ORole_id, int PRole_id) {
        his_O_roleid=ORole_id;
        his_x=chessArray.get(getchessByRole_id_toArrayid(ORole_id)).getQipanx();
        his_y=chessArray.get(getchessByRole_id_toArrayid(ORole_id)).getQipany();
        his_dead_roleid=PRole_id;
        managerC.MessToServer(client_id+"Takeqi00:"+ORole_id+","+PRole_id);
    }
    public void AskRefresh() {
    }
    public void ExtendData(String data) {
    }
    public boolean send_this_id(){
        System.out.println("send imformation: "+client_id+ "Idsend00:"+client_id);
        managerC.MessToServer(client_id+ "Idsend00:"+client_id);
        return true;
    }
    public String getChessMap(){//获取整个棋盘
        String s=new String();
        for (chess chess0 : chessArray) {
            s=s+"("+chess0.getRole_id()+","+chess0.getQipanx()+","+chess0.getQipany()+")";
        }
        for (chess chess0 : RedTakenChessArray) {
            s=s+"("+chess0.getRole_id()+")";
        }
        for (chess chess0 : BlackTakenChessArray) {
            s=s+"("+chess0.getRole_id()+")";
        }
        return s;
    }
    public void readChessMap(String s){//从流中加载对局
        int a;int b;int c;
        String temp1="";
        String temp2="";
        while(s.contains("(")) {
            a=s.indexOf("(");b=s.indexOf(")");//a为
            temp1=s.substring(a+1,b);
            s=s.substring(b+1);
            if(temp1.contains(",")){//棋子是活的
                temp2=temp1.substring(0,temp1.indexOf(","));
                a=Integer.parseInt(temp2);
                temp1=temp1.substring(temp1.indexOf(",")+1);
                temp2=temp1.substring(0,temp1.indexOf(","));
                b=Integer.parseInt(temp2);
                temp1=temp1.substring(temp1.indexOf(",")+1);
                c=Integer.parseInt(temp1);
                if(getchessByRole_id(a)!=null) {
                    getchessByRole_id(a).setqipanLocation(b, c);
                    getchessByRole_id(a).qipanLocationRefresh();
                }
                else {
                    if (getchessByRole_id_InRedTaken(a) != null) {
                        for (int i = 0; i < RedTakenChessArray.size; i++) {
                            if(RedTakenChessArray.get(i).getRole_id()==a){
                                chessArray.add(RedTakenChessArray.get(i));
                                RedTakenChessArray.removeIndex(i);
                            }
                        }
                        getchessByRole_id(a).setqipanLocation(b, c);
                        getchessByRole_id(a).qipanLocationRefresh();
                    }else {
                        if (getchessByRole_id_InBlackTaken(a) != null) {
                            for (int i = 0; i < BlackTakenChessArray.size; i++) {
                                if(BlackTakenChessArray.get(i).getRole_id()==a){
                                    chessArray.add(BlackTakenChessArray.get(i));
                                    BlackTakenChessArray.removeIndex(i);
                                }
                            }
                            getchessByRole_id(a).setqipanLocation(b, c);
                            getchessByRole_id(a).qipanLocationRefresh();
                        }
                    }
                }
            }
            else {
                    if(getchessByRole_id(a)!=null){
                        if(chessArray.get(getchessByRole_id_toArrayid(a)).isCamp()){
                            BlackTakenChessArray.add(chessArray.get(getchessByRole_id_toArrayid(a)));
                        }else {
                            RedTakenChessArray.add(chessArray.get(getchessByRole_id_toArrayid(a)));
                        }
                        chessArray.removeIndex(getchessByRole_id_toArrayid(a));
                    }

            }
        }
        FenbuRefresh();
    }

    public DengLu getDengluqi() {
        return dengluqi;
    }
    public void setDengluqi(DengLu dengluqi) {
        this.dengluqi = dengluqi;
    }
    public void jiangJUN_judge(int O_role_id,int x,int y){

    }
    public void setHistory_step(int O_role_id,int history_x,int history_y,int dead_role_id){//死掉的棋子，若没死掉的棋子置为-1
        his_O_roleid=O_role_id;
        his_x=history_x;
        his_y=history_y;
        his_dead_roleid=dead_role_id;
    }
    public void FenbuRefresh(){
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 11; j++) {
                chessFenbu[i][j]=0;//0代表空
            }
        }
        for (chess chess1 : chessArray) {
            int x=chess1.getArrayQx();int y=chess1.getArrayQy();
            if(chess1.isCamp())
                chessFenbu[x][y]=1;
            else
                chessFenbu[x][y]=-1;
        }
    }
}

