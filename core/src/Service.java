import java.io.IOException;

public class Service {
    public static final int MAX_H = 9;		//最大列数
    public static final int MAX_V = 10;		//最大行数
    public static final String IP = "222.210.112.210";
    public static final int PORT = 3000;

    public static final byte O_READY = 0;
    public static final byte O_CANCLE_READY = 1;
    public static final byte O_ASK_GOBACK = 2;
    public static final byte O_ALLOW_GOBACK = 3;
    public static final byte O_REFUSE_GOBACK = 4;
    public static final byte O_GIVEUP = 5;
    public static final byte O_NOWIN = 6;
    public static final byte O_ALLOW_NOWIN = 7;
    public static final byte O_REFUSE_NOWIN = 8;
    public static final byte O_FIELD = 9;
    public static final byte O_PLAYERS_COUNT = 10;
    public static final byte O_MOVE = 11;
    public static final byte O_CLOSE = 12;

    public static final byte I_START = 13;
    public static final byte I_MOVE = 14;
    public static final byte I_WIN = 15;
    public static final byte I_ASK_GOBACK = 16;
    public static final byte I_GIVEUP = 17;
    public static final byte I_NOWIN = 18;
    public static final byte I_ALLOW_NOWIN = 19;
    public static final byte I_REFUSE_NOWIN = 20;
    public static final byte I_CLOSE = 21;

    public static final byte[][] board = new byte[MAX_H][MAX_V];

    public static final int PVP = 0;
    public static final int PVS = 1;
    public static int mode;
    public static boolean isStart = false;
    public static boolean isPause = false;



}