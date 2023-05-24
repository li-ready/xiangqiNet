import java.util.ArrayList;

public class Robot {
    public static final int K_W = 10000;
    public static final int M_W = 150;
    public static final int E_W = 150;
    public static final int H_W = 200;
    public static final int R_W = 350;
    public static final int C_W = 300;
    public static final int P_W = 100;

    private byte camp;

    private Node bestNode;
    private int maxGride;

    public void yourTurn() {
        byte[][] board = copyBoard(Service.board);
        reversalBoard(board);
        Node rootNode = new Node();
        rootNode.board = board;
        rootNode.move = null;
        rootNode.gride = evaluate(rootNode.board, camp);
        rootNode.root = null;
        bestNode = null;
        maxGride = -100000;
        int deep = 3;
        search(rootNode, deep);
        Node nextNode = findRoot(bestNode);
        Move move = nextNode.move;
        moveTo(move.fx, (byte) (9-move.fy), move.tx, (byte) (9-move.ty));
    }

    private Node findRoot(Node node) {
        Node root = node;
        while(root.root.root != null) {
            root = root.root;
        }
        return root;
    }

    private void search(Node node, int deep) {
        if(deep == 0) {
            if(node.gride > maxGride) {
                bestNode = node;
                maxGride = node.gride;
            }
            return;
        }
        deep--;
        ArrayList<Move> movesList = calcuAllMoves(node.board, (byte) ((deep%2*2-1)*-camp));
        ArrayList<Node> nodesList = new ArrayList<Node>();
        for(int i = 0; i < movesList.size(); i++) {
            Move move = movesList.get(i);
            byte[][] board = copyBoard(node.board);
            board[move.tx][move.ty] = board[move.fx][move.fy];
            board[move.fx][move.fy] = 0;
            reversalBoard(board);
            Node newNode = new Node();
            newNode.board = board;
            newNode.move = move;
            newNode.gride = evaluate(newNode.board, (byte) ((deep%2*2-1)*-camp));
            newNode.root = node;
            nodesList.add(newNode);
        }
        select(nodesList, deep);
        for(int i = 0; i < nodesList.size(); i++) {
            search(nodesList.get(i), deep);
        }
    }

    private void select(ArrayList<Node> nodesList, int deep) {
        if(deep == 1) {
            Node bestNode = nodesList.get(0);
            int gride = bestNode.gride;
            for(int i = 1; i < nodesList.size(); i++) {
                if(nodesList.get(i).gride > gride) {
                    bestNode = nodesList.get(i);
                    gride = bestNode.gride;
                }
            }
            nodesList.clear();
            nodesList.add(bestNode);
        } else {

        }
    }

    private int evaluate(byte[][] board, byte camp) {
        int power = 0;
        for(int i = 0; i < Service.MAX_H; i++) {
            for(int j = 0; j < Service.MAX_V; j++) {
                if(board[i][j] != 0) {
                    power += pieceWeight(board[i][j]);
                }
            }
        }
        return power*camp;
    }

    private int pieceWeight(byte name) {
        int camp = name > 0 ? 1 : -1;
        switch(name*camp) {
            case Piece.K:
                return camp*K_W;
            case Piece.M:
                return camp*M_W;
            case Piece.E:
                return camp*E_W;
            case Piece.H:
                return camp*H_W;
            case Piece.R:
                return camp*R_W;
            case Piece.C:
                return camp*C_W;
            default:
                return camp*P_W;
        }
    }

    private ArrayList<Move> calcuAllMoves(byte[][] board, byte camp) {
        ArrayList<Move> movesList = new ArrayList<Move>();
        for(int i = 0; i < Service.MAX_H; i++) {
            for(int j = 0; j < Service.MAX_V; j++) {
                if(board[i][j]*camp > 0) {
                    for(int k = 0; k <100; k++) {
                    }
                }
            }
        }
        return movesList;
    }

    private class Node {
        public byte[][] board;
        public int gride;
        public Move move;
        public Node root;
    }

    private byte[][] copyBoard(byte[][] board) {
        byte[][] newBoard = new byte[Service.MAX_H][Service.MAX_V];
        for(int i = 0; i < Service.MAX_H; i++) {
            for(int j = 0; j < Service.MAX_V; j++) {
                newBoard[i][j] = board[i][j];
            }
        }
        return newBoard;
    }

    private void reversalBoard(byte[][] board) {
        for(int i = 0; i < Service.MAX_H; i++) {
            for(int j = 0; j < Service.MAX_V/2; j++) {
                byte p = board[i][j];
                board[i][j] = board[i][Service.MAX_V-1-j];
                board[i][Service.MAX_V-1-j] = p;
            }
        }
    }

    private void moveTo(byte fx, byte fy, byte tx, byte ty) {
        Service.board[tx][ty] = Service.board[fx][fy];
        Service.board[fx][fy] = 0;

    }
}