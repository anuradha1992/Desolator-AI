/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import connection.GameSession;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import sprites.Brick;
import sprites.Bullet;
import sprites.CoinPile;
import sprites.GameObject;
import sprites.LifePack;
import sprites.Tank;
import support.KeyInputAdapter;

/**
 *
 * @author Anuradha
 */
public class GamePanel extends JPanel implements ActionListener, Observer {

    private final int INITIAL_X = 0;
    private final int INITIAL_Y = 0;
    private final int INITIAL_DELAY = 1000;
    private final int INITIAL_DIRECTION = 1;
    private final int PERIOD_INTERVAL = 250;

    private int cellCount = 20;

    private int turn = 0;

    private List<Rectangle> cells;

    private int time = 0;

    private static final int width = 600;
    private static final int height = 600;
    private static final int width_of_block = 30;
    private static final int height_of_block = 30;

    private Tank tank;
    private Tank[] opponents;
    private Image bgground;
    private Timer timer;

    private boolean gridNeeded;

    GameSession gs;
    private boolean isPackExpirationUpdate;

//    private class packExpirationUpdater extends Observable{
//        @Override
//        public void update(Observer ob,Object obj){
//            
//        }
//    }
    public GamePanel(GameSession gs, boolean gridNeeded) {
        this.gs = gs;
        this.gridNeeded = gridNeeded;
//        tank = new Tank("P0", INITIAL_X, INITIAL_Y, INITIAL_DIRECTION);

        setTanks();

        cells = new ArrayList<>(cellCount * cellCount);
        isPackExpirationUpdate = false;
        initPanel();
    }

    private void setTanks() {
        String tankName = gs.getPlayerName();
        Tank[] tanks = gs.getTanks();
        if (tanks != null) {
            opponents = new Tank[tanks.length - 1];
            int i = 0;
            for (Tank t : tanks) {
                if (t != null) {
                    if (t.getName().equals(tankName)) {
                        tank = t;
                    } else {
                        opponents[i] = t;
                        i++;
                    }
                }

            }
        }

    }

    public static int getWidthOfGamePanel() {
        return width;
    }

    public static int getHeightOfGamePanel() {
        return height;
    }

    public static int getWidthOfGameBlock() {
        return width_of_block;
    }

    public static int getHeightOfGameBlock() {
        return height_of_block;
    }

    private void initPanel() {

        addKeyListener(new KeyInputAdapter(tank));
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(width, height));

        setDoubleBuffered(true);
        setFocusable(true);

        loadBackgroundImage();

        if (tank != null) {
            timer = new Timer(PERIOD_INTERVAL, this);
            timer.start();
        }
    }

    private void drawTank(Graphics g) {

        if (tank.getHealth() > 0) {
            g.drawImage(tank.getImage(), tank.getX() * width_of_block, tank.getY() * height_of_block, this);
            Toolkit.getDefaultToolkit().sync();
        }

    }

    private void drawBackground(Graphics g) {
        g.drawImage(bgground, 0, 0, null);
    }

    private void loadBackgroundImage() {
        ImageIcon ii = new ImageIcon("../Desolator/src/images/grass.png");
        bgground = ii.getImage();
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        drawBackground(g);
        if (gridNeeded) {
            drawGrid(g);
        }
        setTanks();

        checkCollisions();
        drawMap(g);

//        if (!isPackExpirationUpdate) {
        drawTank(g);
        drawOpponents(g);
//        drawMap(g);
        checkBulletCollisions();
        drawBullets(g);
//        }
        isPackExpirationUpdate = false;

        g.dispose();
    }

    private void drawBullets(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        ArrayList bullets = tank.getBullets();

        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = (Bullet) bullets.get(i);
            g2d.drawImage(bullet.getImage(), bullet.getX() * width_of_block, bullet.getY() * height_of_block, this);
        }

        for (Tank t : opponents) {
            if (t != null && !t.getName().equals(gs.getPlayerName())) {
                ArrayList opponentBullets = t.getBullets();
                if (opponentBullets != null) {
                    for (int i = 0; i < opponentBullets.size(); i++) {
                        Bullet bullet = (Bullet) opponentBullets.get(i);
                        g2d.drawImage(bullet.getImage(), bullet.getX() * width_of_block, bullet.getY() * height_of_block, this);
                    }
                }

            }
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void drawOpponents(Graphics g) {
        int i = 0;
        for (Tank t : opponents) {
            if (t != null) {
                i++;
                if (t.getImage() == null) {
                    String[] url = Tank.getImageURL();
                    t.setImageURL(url[i]);
                }
                if (t.getHealth() > 0) {
                    g.drawImage(t.getImage(), t.getX() * width_of_block, t.getY() * height_of_block, this);
                }
            }
        }

    }

    private void drawMap(Graphics g) {
        Map map = gs.getMap();
        GameObject[][] objects = map.getObjectMap();
        for (int i = 0; i < cellCount; i++) {
            for (int j = 0; j < cellCount; j++) {
                if (objects[i][j] != null) {
                    g.drawImage(objects[i][j].getImage(), objects[i][j].getX() * width_of_block, objects[i][j].getY() * height_of_block, this);
                    if (objects[i][j].getClass().getName().contains("CoinPile")) {
                        CoinPile cpile = (CoinPile) objects[i][j];
                        String msg = "";
                        if (cpile.getLifeTime() == Integer.MAX_VALUE) {
                            msg += "INF ms";
                        } else {
                            msg += "" + cpile.getLifeTime() + "ms";
                        }

                        Font small = new Font("Helvetica", Font.BOLD, 14);
                        FontMetrics metr = this.getFontMetrics(small);
                        g.setColor(Color.black);
                        g.setFont(small);
                        g.drawString(msg, (cpile.getX() * width_of_block) + ((width_of_block - metr.stringWidth(msg)) / 2), (cpile.getY() * height_of_block) + ((height_of_block - metr.getHeight()) / 2));
                        String msg2 = "" + cpile.getValue() + "$";
                        if (msg2.length() >= 5) {
                            small = new Font("Helvetica", Font.BOLD, 12);
                        }
                        g.drawString(msg2, (cpile.getX() * width_of_block) + ((width_of_block - metr.stringWidth(msg)) / 2), (cpile.getY() * height_of_block) + ((height_of_block - metr.getHeight()) / 2) + metr.getHeight());
                    } else if (objects[i][j].getClass().getName().contains("LifePack")) {
                        LifePack lpack = (LifePack) objects[i][j];
                        String msg = "" + lpack.getLifeTime() + "ms";
                        Font small = new Font("Helvetica", Font.BOLD, 14);
                        FontMetrics metr = this.getFontMetrics(small);
                        g.setColor(Color.black);
                        g.setFont(small);
                        g.drawString(msg, (lpack.getX() * width_of_block) + ((width_of_block - metr.stringWidth(msg)) / 2), (lpack.getY() * height_of_block) + ((height_of_block - metr.getHeight()) / 2));
                    } else if (objects[i][j].getClass().getName().contains("Brick")) {
                        Brick brick = (Brick) objects[i][j];
                        int brickLevel = 100 - brick.getPercentage() * 25;
                        String msg = "" + brickLevel + "%";
                        Font small = new Font("Helvetica", Font.BOLD, 14);
                        FontMetrics metr = this.getFontMetrics(small);
                        g.setColor(Color.white);
                        g.setFont(small);
                        g.drawString(msg, (brick.getX() * width_of_block) + ((width_of_block - metr.stringWidth(msg)) / 2), (brick.getY() * height_of_block) + ((height_of_block - metr.getHeight()) / 2));
                    }

                }
//                else {
//                    ImageIcon iicell = new ImageIcon("../Desolator/src/images/grass-cell.png");
//                    g.drawImage(iicell.getImage(), i * width_of_block, j * height_of_block, this);
//                }

            }
        }
    }

    private void drawGrid(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        int width = getWidth();
        int height = getHeight();

        int cellWidth = width / cellCount;
        int cellHeight = height / cellCount;

        int xOffset = (width - (cellCount * cellWidth)) / 2;
        int yOffset = (height - (cellCount * cellHeight)) / 2;

        if (cells.isEmpty()) {
            for (int row = 0; row < cellCount; row++) {
                for (int col = 0; col < cellCount; col++) {
                    Rectangle cell = new Rectangle(
                            xOffset + (col * cellWidth),
                            yOffset + (row * cellHeight),
                            cellWidth,
                            cellHeight);
                    cells.add(cell);
                }
            }
        }

        g2d.setColor(Color.green);
        for (Rectangle cell : cells) {
            g2d.draw(cell);
        }
        Rectangle border = new Rectangle(0, 0, width - 1, height - 1);
        g2d.draw(border);

        g2d.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        time += 250;

        if ((time % 1000) == 0) {
            tank.move();

            repaint();
            tank.stop();

        }

        ArrayList bullets = tank.getBullets();

        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = (Bullet) bullets.get(i);
            if (bullet.isVisible()) {
                bullet.move();
            } else {
                bullets.remove(i);
            }
        }
        repaint();

    }

    public void checkCollisions() {

        Map map = gs.getMap();
        GameObject[][] objects = map.getObjectMap();

        for (int i = 0; i < cellCount; i++) {
            for (int j = 0; j < cellCount; j++) {
                if (objects[i][j] != null) {
                    if (objects[i][j].getClass().getName().contains("CoinPile")) {
                        CoinPile cpile = (CoinPile) objects[i][j];

                        Rectangle coinR = cpile.getBounds();

                        for (Tank t : opponents) {
                            if (t != null) {
                                Rectangle opponentR = t.getBounds();
//                            System.out.println("INTERSECT Coin = "+coinR.toString()+" Opponent = "+opponentR.toString());
                                if (t.getHealth() != 0 && opponentR.intersects(coinR)) {
                                    map.removeGameObject(i, j);
                                }
                            }

                        }
                        Rectangle playerR = tank.getBounds();
//                        System.out.println("INTERSECT Coin = "+coinR.toString()+" Tank = "+playerR.toString());
                        if (tank.getHealth() != 0 && playerR.intersects(coinR)) {
                            map.removeGameObject(i, j);
                        }

                    } else if (objects[i][j].getClass().getName().contains("LifePack")) {
                        LifePack lpack = (LifePack) objects[i][j];

                        Rectangle lpackR = lpack.getBounds();

                        for (Tank t : opponents) {
                            if (t != null) {
                                Rectangle opponentR = t.getBounds();
                                if (opponentR.intersects(lpackR)) {
                                    map.removeGameObject(i, j);
                                }
                            }

                        }

                        Rectangle playerR = tank.getBounds();
                        if (playerR.intersects(lpackR)) {
                            map.removeGameObject(i, j);
                        }

                    }

                }

            }
        }

    }

    public void checkBulletCollisions() {

        Map map = gs.getMap();
        GameObject[][] objects = map.getObjectMap();

        ArrayList<Bullet> bullets = tank.getBullets();

        int bulletLength = bullets.size();

        for (int i = 0; i < bulletLength; i++) {
            boolean bulletRemoved = false;
            Bullet bullet = (Bullet) bullets.get(i);
            if (bullet.isToBeRemoved()) {
                bullets.remove(bullet);
                bulletRemoved = true;
                bulletLength -= 1;
                i--;
            }
            int x = bullet.getX();
            int y = bullet.getY();
            if (x < cellCount && x >= 0 && y < cellCount && y >= 0) {
                if (!bulletRemoved) {
                    if (objects[x][y] != null) {
                        if (objects[x][y].toString().equals("Brick") || objects[x][y].toString().equals("Stone")) {
                            bullet.setToBeRemoved(true);
                        }
                    }
                }

            }
            if (!bulletRemoved) {
                for (Tank t : opponents) {
                    if (t != null && t.getHealth() != 0) {
                        Rectangle opponentBounds = t.getBounds();
                        Rectangle bulletBounds = bullet.getBounds();
                        if (opponentBounds.intersects(bulletBounds)) {
                            bullet.setToBeRemoved(true);
                        }
                    }
                }
            }
        }

        tank.setBullets(bullets);

        for (Tank t : opponents) {
            if (t != null && t.getHealth() != 0) {
                ArrayList<Bullet> opponentBullets = t.getBullets();
                if (opponentBullets != null) {
                    int opponentBulletLength = opponentBullets.size();
                    for (int i = 0; i < opponentBulletLength; i++) {
                        boolean bulletRemoved = false;
                        Bullet bullet = (Bullet) opponentBullets.get(i);
                        if (bullet.isToBeRemoved()) {
                            opponentBullets.remove(bullet);
                            bulletRemoved = true;
                            opponentBulletLength -= 1;
                            i--;
                        }
                        int x = bullet.getX();
                        int y = bullet.getY();
                        if (x < cellCount && x >= 0 && y < cellCount && y >= 0) {
                            if (objects[x][y] != null) {
                                if (objects[x][y].toString().equals("Brick") || objects[x][y].toString().equals("Stone")) {
                                    bullet.setToBeRemoved(true);
                                }
                            }
                            if (!bulletRemoved && tank.getHealth() != 0) {
                                Rectangle tankBounds = tank.getBounds();
                                Rectangle bulletBounds = bullet.getBounds();
                                if (tankBounds.intersects(bulletBounds)) {
                                    bullet.setToBeRemoved(true);
                                }
                            }
                            if (!bulletRemoved) {
                                for (Tank t2 : opponents) {
                                    if (t2 != null && t2.getHealth() != 0 && !t2.getName().equals(t.getName())) {
                                        Rectangle opponentBounds = t2.getBounds();
                                        Rectangle bulletBounds = bullet.getBounds();
                                        if (opponentBounds.intersects(bulletBounds)) {
                                            bullet.setToBeRemoved(true);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    t.setBullets(opponentBullets);
                }

            }

        }

    }

    @Override
    public void update(Observable o, Object arg) {
        isPackExpirationUpdate = true;
        repaint();
    }

}
