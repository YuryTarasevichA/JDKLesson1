package GameTicTac;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Map extends JPanel {
    private boolean isGameOver;
    private boolean isInitialized;
    private static final Random RANDOM = new Random();
    private static final int DOT_PADDING = 5;
    private int gameOverType;
    private static final int STATE_DRAW = 0;
    private static final int STATE_WIN_HUMAN = 1;
    private static final int STATE_WIN_AI = 2;
    private static final String MSG_WIN_HUMAN = "Победил игрок";
    private static final String MSG_WIN_AI = "Победил компьютер!";
    private static final String MSG_DRAW = "Ничья! ";

    private final int HUMAN_DOT = 1;
    private final int AI_DOT = 2;
    private final int EMPTY_DOT = 0;
    private int filedSizeY = 3;
    private int filedSizeX = 3;
    private char[][] fieled;
    private int panelWidth;
    private int panelHeight;
    private int cellHeight;
    private int cellWidth;


    private void initMap() {
        filedSizeX = 3;
        filedSizeY = 3;
        fieled = new char[filedSizeY][filedSizeX];
        for (int i = 0; i < filedSizeY; i++) {
            for (int j = 0; j < filedSizeX; j++) {
                fieled[i][j] = EMPTY_DOT;
            }
        }
    }

    private boolean isValidCell(int x, int y) {
        return x >= 0 && x < filedSizeX && y >= 0 && y < filedSizeY;
    }

    private boolean isEmptyCell(int x, int y) {
        return fieled[y][x] == EMPTY_DOT;
    }

    private void aiTurn() {
        int x, y;
        for (int row = 0; row < filedSizeX; row++) {
            for (int col = 0; col < filedSizeY; col++) {
                if (fieled[row][col] == HUMAN_DOT) { // если находим клетку с Х пользователя
                    //Пытаемся поставить 0 рядом с крестом
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j < 1; j++) {
                            if (isEmptyCell(row + i, col + j)) { // проверяем, что клетка свободна
                                fieled[row + i][col + j] = AI_DOT;
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isMapFull() {
        for (int i = 0; i < filedSizeY; i++) {
            for (int j = 0; j < filedSizeX; j++) {
                if (fieled[i][j] == EMPTY_DOT) return false;
            }
        }
        return true;
    }

    Map() {
        isInitialized = false;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                update(e);
            }
        });
    }

    private void update(MouseEvent e) {
        int cellX = e.getX() / cellWidth;
        int cellY = e.getY() / cellHeight;
        if (!isValidCell(cellX, cellY) || !isEmptyCell(cellX, cellY)) return;
        fieled[cellY][cellX] = HUMAN_DOT;
//        System.out.printf("x=%d, y=%d\n", cellX, cellY);
        repaint();
        if (checkEndGame(HUMAN_DOT, STATE_WIN_HUMAN)) return;
        aiTurn();
        repaint();
        if (checkEndGame(AI_DOT, STATE_WIN_AI)) return;
        if (isGameOver || !isInitialized) return;
    }

    private boolean checkEndGame(int dot, int gameOverType) {
        if (checkWin(dot)) {
            isGameOver = true;
            this.gameOverType = gameOverType;
            repaint();
            return true;
        }
        if (isMapFull()) {
            isGameOver = true;
            this.gameOverType = STATE_DRAW;
            repaint();
            return true;
        }
        return false;
    }

    private boolean checkWin(int symbol) {
        if (fieled[0][0] == symbol && fieled[0][1] == symbol && fieled[0][2] == symbol) return true;
        if (fieled[1][0] == symbol && fieled[1][1] == symbol && fieled[1][2] == symbol) return true;
        if (fieled[2][0] == symbol && fieled[2][1] == symbol && fieled[2][2] == symbol) return true;
        // Проверка по диагоналям
        if (fieled[0][0] == symbol && fieled[1][1] == symbol && fieled[2][2] == symbol) return true;
        if (fieled[0][2] == symbol && fieled[1][1] == symbol && fieled[2][0] == symbol) return true;
        // Проверка по трем вертикалям
        if (fieled[0][0] == symbol && fieled[1][0] == symbol && fieled[2][0] == symbol) return true;
        if (fieled[0][1] == symbol && fieled[1][1] == symbol && fieled[2][1] == symbol) return true;
        if (fieled[0][2] == symbol && fieled[1][2] == symbol && fieled[2][2] == symbol) return true;
        return false;
    }

    void startNewGame(int mode, int fSzX, int fszY, int wLen) {
        System.out.printf("Mode: %d;\nSize: x=%d;\nWin Length: %d", mode, fSzX, fszY, wLen);
        repaint();
        isGameOver =false;
        isInitialized = true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
    }

    private void render(Graphics g) {
        panelWidth = getWidth();
        panelHeight = getHeight();
        cellHeight = panelHeight / 3;
        cellWidth = panelWidth / 3;
        g.setColor(Color.BLACK);
        for (int h = 0; h < 3; h++) {
            int y = h * cellHeight;
            g.drawLine(0, y, panelWidth, y);
        }
        for (int w = 0; w < 3; w++) {
            int x = w * cellHeight;
            g.drawLine(x, 0, x, panelHeight);
        }
        for (int y = 0; y < filedSizeY; y++) {
            for (int x = 0; x < filedSizeX; x++) {
                if (fieled[y][x] == EMPTY_DOT) continue;

                if (fieled[y][x] == HUMAN_DOT) {
                    g.setColor(Color.BLUE);
                    g.fillOval(x * cellWidth + DOT_PADDING, y * cellHeight + DOT_PADDING,
                            cellWidth - DOT_PADDING * 2, cellHeight - DOT_PADDING * 2);
                } else if (fieled[y][x] == AI_DOT) {
                    g.setColor(new Color(0xff0000));
                    g.fillOval(x * cellWidth + DOT_PADDING, y * cellHeight + DOT_PADDING,
                            cellWidth - DOT_PADDING * 2, cellHeight - DOT_PADDING * 2);
                } else {
                    throw new RuntimeException("Unexpected value " + fieled[y][x] + " in cell: x=" + x + " y= " + y);
                }
            }
        }
//        g.drawLine(0, 0, 100, 100);
        if (isGameOver) showMessageGameOver(g);
        if(!isInitialized) return;
    }

    private void showMessageGameOver(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 200, getWidth(), 70);
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Times new roman", Font.BOLD, 48));
        switch (gameOverType) {
            case STATE_DRAW:
                g.drawString(MSG_DRAW, 180, getHeight() / 2);
                break;
            case STATE_WIN_AI:
                g.drawString(MSG_WIN_AI, 20, getHeight()/2);
                break;
            case STATE_WIN_HUMAN:
                g.drawString(MSG_WIN_HUMAN, 70, getHeight()/2);
                break;
            default:
                throw new RuntimeException("Unexpected gameOver state: "+ gameOverType);
        }
    }
}
