/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import connection.GameSession;
import connection.Interpreter;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import sprites.Tank;

/**
 *
 * @author Anuradha
 */
public class GameView extends javax.swing.JFrame implements Observer {

    /**
     * Creates new form GameView
     */
    GameSession gs;
    boolean gridNeeded;
    GamePanel gamePanel;

    public GameView(GameSession gs, boolean gridNeeded) {
        initComponents();
        this.gs = gs;
        this.gridNeeded = gridNeeded;
        initGamePanel();
        gameScorePanel.setBackground(new Color(0, 0, 0, 100));
        setLocationRelativeTo(null);
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        FileInputStream imgStream = null;
        try {
            File imgfile = new File("..\\Desolator\\src\\images\\Tank-Bonus-icon.png");
            imgStream = new FileInputStream(imgfile);
            BufferedImage bi = ImageIO.read(imgStream);
            ImageIcon myImg = new ImageIcon(bi);
            this.setIconImage(myImg.getImage());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GameView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GameView.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                imgStream.close();
            } catch (IOException ex) {
                Logger.getLogger(GameView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setGameSession(GameSession gs) {
        this.gs = gs;
    }

    public void initGamePanel() {
        GamePanel gamePanel = new GamePanel(gs, gridNeeded);
        this.gamePanel = gamePanel;
        getContentPane().add(gamePanel);
        gamePanel.setBounds(30, 80, 600, 600);
    }

    public GamePanel getGamePanel() {
        return this.gamePanel;
    }

    @Override
    public void update(Observable o, Object arg) {
        String className = arg.getClass().toString();
        if (className.endsWith("Interpreter")) {
            Interpreter i = (Interpreter) arg;
            String playerName = i.getPlayerName();
            String display = "";
            if (i.getCommunicator().equals("player")) {
                switch (playerName) {
                    case "P0":
                        display += "Player 1 | Message : ";
                        P0NameLabel.setForeground(Color.GREEN);
                        P0HealthLabel.setForeground(Color.GREEN);
                        P0CoinLabel.setForeground(Color.GREEN);
                        P0ScoreLabel.setForeground(Color.GREEN);
                        P0RankLabel.setForeground(Color.GREEN);
                        break;
                    case "P1":
                        display += "Player 2 | Message : ";
                        P1NameLabel.setForeground(Color.GREEN);
                        P1HealthLabel.setForeground(Color.GREEN);
                        P1CoinLabel.setForeground(Color.GREEN);
                        P1ScoreLabel.setForeground(Color.GREEN);
                        P1RankLabel.setForeground(Color.GREEN);
                        break;
                    case "P2":
                        display += "Player 3 | Message : ";
                        P2NameLabel.setForeground(Color.GREEN);
                        P2HealthLabel.setForeground(Color.GREEN);
                        P2CoinLabel.setForeground(Color.GREEN);
                        P2ScoreLabel.setForeground(Color.GREEN);
                        P2RankLabel.setForeground(Color.GREEN);
                        break;
                    case "P3":
                        display += "Player 4 | Message : ";
                        P3NameLabel.setForeground(Color.GREEN);
                        P3HealthLabel.setForeground(Color.GREEN);
                        P3CoinLabel.setForeground(Color.GREEN);
                        P3ScoreLabel.setForeground(Color.GREEN);
                        P3RankLabel.setForeground(Color.GREEN);
                        break;
                    case "P4":
                        display += "Player 5 | Message : ";
                        P4NameLabel.setForeground(Color.GREEN);
                        P4HealthLabel.setForeground(Color.GREEN);
                        P4CoinLabel.setForeground(Color.GREEN);
                        P4ScoreLabel.setForeground(Color.GREEN);
                        P4RankLabel.setForeground(Color.GREEN);
                        break;
                }
            } else {
                switch (playerName) {
                    case "P0":
                        P0NameLabel.setForeground(Color.GREEN);
                        P0HealthLabel.setForeground(Color.GREEN);
                        P0CoinLabel.setForeground(Color.GREEN);
                        P0ScoreLabel.setForeground(Color.GREEN);
                        P0RankLabel.setForeground(Color.GREEN);
                        break;
                    case "P1":
                        P1NameLabel.setForeground(Color.GREEN);
                        P1HealthLabel.setForeground(Color.GREEN);
                        P1CoinLabel.setForeground(Color.GREEN);
                        P1ScoreLabel.setForeground(Color.GREEN);
                        P1RankLabel.setForeground(Color.GREEN);
                        break;
                    case "P2":
                        P2NameLabel.setForeground(Color.GREEN);
                        P2HealthLabel.setForeground(Color.GREEN);
                        P2CoinLabel.setForeground(Color.GREEN);
                        P2ScoreLabel.setForeground(Color.GREEN);
                        P2RankLabel.setForeground(Color.GREEN);
                        break;
                    case "P3":
                        P3NameLabel.setForeground(Color.GREEN);
                        P3HealthLabel.setForeground(Color.GREEN);
                        P3CoinLabel.setForeground(Color.GREEN);
                        P3ScoreLabel.setForeground(Color.GREEN);
                        P3RankLabel.setForeground(Color.GREEN);
                        break;
                    case "P4":
                        P4NameLabel.setForeground(Color.GREEN);
                        P4HealthLabel.setForeground(Color.GREEN);
                        P4CoinLabel.setForeground(Color.GREEN);
                        P4ScoreLabel.setForeground(Color.GREEN);
                        P4RankLabel.setForeground(Color.GREEN);
                        break;
                }
                display += "Server | Message : ";
            }

            display += i.getMessage();
            messageLabel.setText(display);
        } else {
            //Tank array is got as arg
            Tank[] tanks = (Tank[]) arg;
            for (Tank t : tanks) {
                if (t != null) {
                    String name = t.getName();
                    switch (name) {
                        case "P0":
                            P0HealthLabel.setText("" + t.getHealth() + "%");
                            P0CoinLabel.setText("" + t.getCoins());
                            P0ScoreLabel.setText("" + t.getScore());
                            P0RankLabel.setText("" + t.getRank());
                            break;
                        case "P1":
                            P1HealthLabel.setText("" + t.getHealth() + "%");
                            P1CoinLabel.setText("" + t.getCoins());
                            P1ScoreLabel.setText("" + t.getScore());
                            P1RankLabel.setText("" + t.getRank());
                            break;
                        case "P2":
                            P2HealthLabel.setText("" + t.getHealth() + "%");
                            P2CoinLabel.setText("" + t.getCoins());
                            P2ScoreLabel.setText("" + t.getScore());
                            P2RankLabel.setText("" + t.getRank());
                            break;
                        case "P3":
                            P3HealthLabel.setText("" + t.getHealth() + "%");
                            P3CoinLabel.setText("" + t.getCoins());
                            P3ScoreLabel.setText("" + t.getScore());
                            P3RankLabel.setText("" + t.getRank());
                            break;
                        case "P4":
                            P4HealthLabel.setText("" + t.getHealth() + "%");
                            P4CoinLabel.setText("" + t.getCoins());
                            P4ScoreLabel.setText("" + t.getScore());
                            P4RankLabel.setText("" + t.getRank());
                            break;
                    }
                }

            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        gamePanelLayeredPane = new javax.swing.JLayeredPane();
        gameBoardLayeredPane = new javax.swing.JLayeredPane();
        gameScorePanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        P4NameLabel = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        P1NameLabel = new javax.swing.JLabel();
        P2NameLabel = new javax.swing.JLabel();
        P3NameLabel = new javax.swing.JLabel();
        P0NameLabel = new javax.swing.JLabel();
        P4HealthLabel = new javax.swing.JLabel();
        P4CoinLabel = new javax.swing.JLabel();
        P1HealthLabel = new javax.swing.JLabel();
        P2HealthLabel = new javax.swing.JLabel();
        P3HealthLabel = new javax.swing.JLabel();
        P0HealthLabel = new javax.swing.JLabel();
        P0CoinLabel = new javax.swing.JLabel();
        P1CoinLabel = new javax.swing.JLabel();
        P2CoinLabel = new javax.swing.JLabel();
        P3CoinLabel = new javax.swing.JLabel();
        P0ScoreLabel = new javax.swing.JLabel();
        P1ScoreLabel = new javax.swing.JLabel();
        P2ScoreLabel = new javax.swing.JLabel();
        P3ScoreLabel = new javax.swing.JLabel();
        P4ScoreLabel = new javax.swing.JLabel();
        P0RankLabel = new javax.swing.JLabel();
        P1RankLabel = new javax.swing.JLabel();
        P2RankLabel = new javax.swing.JLabel();
        P3RankLabel = new javax.swing.JLabel();
        P4RankLabel = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        messageLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Desolator Tank Game");
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(1365, 850));
        setUndecorated(true);
        getContentPane().setLayout(null);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tw-logo.png"))); // NOI18N
        getContentPane().add(jLabel2);
        jLabel2.setBounds(570, 0, 600, 80);

        gamePanelLayeredPane.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(102, 102, 102)));
        gamePanelLayeredPane.setPreferredSize(new java.awt.Dimension(600, 600));
        getContentPane().add(gamePanelLayeredPane);
        gamePanelLayeredPane.setBounds(30, 80, 600, 600);

        gameScorePanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(102, 102, 102)));
        gameScorePanel.setLayout(null);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/rome-2-logo-md.png"))); // NOI18N
        gameScorePanel.add(jLabel3);
        jLabel3.setBounds(170, 20, 330, 170);

        jLabel4.setFont(new java.awt.Font("Tempus Sans ITC", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Rank");
        gameScorePanel.add(jLabel4);
        jLabel4.setBounds(560, 260, 70, 30);

        jLabel5.setFont(new java.awt.Font("Tempus Sans ITC", 1, 36)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Game Board");
        gameScorePanel.add(jLabel5);
        jLabel5.setBounds(220, 190, 230, 30);

        P4NameLabel.setBackground(new java.awt.Color(0, 0, 0));
        P4NameLabel.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        P4NameLabel.setForeground(new java.awt.Color(255, 255, 255));
        P4NameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        P4NameLabel.setText("Player 5");
        P4NameLabel.setOpaque(true);
        gameScorePanel.add(P4NameLabel);
        P4NameLabel.setBounds(70, 480, 80, 30);

        jLabel7.setFont(new java.awt.Font("Tempus Sans ITC", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Health");
        gameScorePanel.add(jLabel7);
        jLabel7.setBounds(190, 260, 100, 30);

        jLabel8.setFont(new java.awt.Font("Tempus Sans ITC", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Coins");
        gameScorePanel.add(jLabel8);
        jLabel8.setBounds(330, 260, 70, 30);

        jLabel9.setFont(new java.awt.Font("Tempus Sans ITC", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Score");
        gameScorePanel.add(jLabel9);
        jLabel9.setBounds(450, 260, 80, 30);

        jLabel10.setFont(new java.awt.Font("Tempus Sans ITC", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Player");
        gameScorePanel.add(jLabel10);
        jLabel10.setBounds(70, 260, 80, 30);

        P1NameLabel.setBackground(new java.awt.Color(0, 0, 0));
        P1NameLabel.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        P1NameLabel.setForeground(new java.awt.Color(255, 255, 255));
        P1NameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        P1NameLabel.setText("Player 2");
        P1NameLabel.setOpaque(true);
        gameScorePanel.add(P1NameLabel);
        P1NameLabel.setBounds(70, 360, 80, 30);

        P2NameLabel.setBackground(new java.awt.Color(0, 0, 0));
        P2NameLabel.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        P2NameLabel.setForeground(new java.awt.Color(255, 255, 255));
        P2NameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        P2NameLabel.setText("Player 3");
        P2NameLabel.setOpaque(true);
        gameScorePanel.add(P2NameLabel);
        P2NameLabel.setBounds(70, 400, 80, 30);

        P3NameLabel.setBackground(new java.awt.Color(0, 0, 0));
        P3NameLabel.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        P3NameLabel.setForeground(new java.awt.Color(255, 255, 255));
        P3NameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        P3NameLabel.setText("Player 4");
        P3NameLabel.setOpaque(true);
        gameScorePanel.add(P3NameLabel);
        P3NameLabel.setBounds(70, 440, 80, 30);

        P0NameLabel.setBackground(new java.awt.Color(0, 0, 0));
        P0NameLabel.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        P0NameLabel.setForeground(new java.awt.Color(255, 255, 255));
        P0NameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        P0NameLabel.setText("Player 1");
        P0NameLabel.setOpaque(true);
        gameScorePanel.add(P0NameLabel);
        P0NameLabel.setBounds(70, 320, 80, 30);

        P4HealthLabel.setBackground(new java.awt.Color(0, 0, 0));
        P4HealthLabel.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        P4HealthLabel.setForeground(new java.awt.Color(255, 255, 255));
        P4HealthLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        P4HealthLabel.setText("-");
        P4HealthLabel.setOpaque(true);
        gameScorePanel.add(P4HealthLabel);
        P4HealthLabel.setBounds(190, 480, 80, 30);

        P4CoinLabel.setBackground(new java.awt.Color(0, 0, 0));
        P4CoinLabel.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        P4CoinLabel.setForeground(new java.awt.Color(255, 255, 255));
        P4CoinLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        P4CoinLabel.setText("-");
        P4CoinLabel.setOpaque(true);
        gameScorePanel.add(P4CoinLabel);
        P4CoinLabel.setBounds(310, 480, 100, 30);

        P1HealthLabel.setBackground(new java.awt.Color(0, 0, 0));
        P1HealthLabel.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        P1HealthLabel.setForeground(new java.awt.Color(255, 255, 255));
        P1HealthLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        P1HealthLabel.setText("-");
        P1HealthLabel.setOpaque(true);
        gameScorePanel.add(P1HealthLabel);
        P1HealthLabel.setBounds(190, 360, 80, 30);

        P2HealthLabel.setBackground(new java.awt.Color(0, 0, 0));
        P2HealthLabel.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        P2HealthLabel.setForeground(new java.awt.Color(255, 255, 255));
        P2HealthLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        P2HealthLabel.setText("-");
        P2HealthLabel.setOpaque(true);
        gameScorePanel.add(P2HealthLabel);
        P2HealthLabel.setBounds(190, 400, 80, 30);

        P3HealthLabel.setBackground(new java.awt.Color(0, 0, 0));
        P3HealthLabel.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        P3HealthLabel.setForeground(new java.awt.Color(255, 255, 255));
        P3HealthLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        P3HealthLabel.setText("-");
        P3HealthLabel.setOpaque(true);
        gameScorePanel.add(P3HealthLabel);
        P3HealthLabel.setBounds(190, 440, 80, 30);

        P0HealthLabel.setBackground(new java.awt.Color(0, 0, 0));
        P0HealthLabel.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        P0HealthLabel.setForeground(new java.awt.Color(255, 255, 255));
        P0HealthLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        P0HealthLabel.setText("-");
        P0HealthLabel.setOpaque(true);
        gameScorePanel.add(P0HealthLabel);
        P0HealthLabel.setBounds(190, 320, 80, 30);

        P0CoinLabel.setBackground(new java.awt.Color(0, 0, 0));
        P0CoinLabel.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        P0CoinLabel.setForeground(new java.awt.Color(255, 255, 255));
        P0CoinLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        P0CoinLabel.setText("-");
        P0CoinLabel.setOpaque(true);
        gameScorePanel.add(P0CoinLabel);
        P0CoinLabel.setBounds(310, 320, 100, 30);

        P1CoinLabel.setBackground(new java.awt.Color(0, 0, 0));
        P1CoinLabel.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        P1CoinLabel.setForeground(new java.awt.Color(255, 255, 255));
        P1CoinLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        P1CoinLabel.setText("-");
        P1CoinLabel.setOpaque(true);
        gameScorePanel.add(P1CoinLabel);
        P1CoinLabel.setBounds(310, 360, 100, 30);

        P2CoinLabel.setBackground(new java.awt.Color(0, 0, 0));
        P2CoinLabel.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        P2CoinLabel.setForeground(new java.awt.Color(255, 255, 255));
        P2CoinLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        P2CoinLabel.setText("-");
        P2CoinLabel.setOpaque(true);
        gameScorePanel.add(P2CoinLabel);
        P2CoinLabel.setBounds(310, 400, 100, 30);

        P3CoinLabel.setBackground(new java.awt.Color(0, 0, 0));
        P3CoinLabel.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        P3CoinLabel.setForeground(new java.awt.Color(255, 255, 255));
        P3CoinLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        P3CoinLabel.setText("-");
        P3CoinLabel.setOpaque(true);
        gameScorePanel.add(P3CoinLabel);
        P3CoinLabel.setBounds(310, 440, 100, 30);

        P0ScoreLabel.setBackground(new java.awt.Color(0, 0, 0));
        P0ScoreLabel.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        P0ScoreLabel.setForeground(new java.awt.Color(255, 255, 255));
        P0ScoreLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        P0ScoreLabel.setText("-");
        P0ScoreLabel.setOpaque(true);
        gameScorePanel.add(P0ScoreLabel);
        P0ScoreLabel.setBounds(440, 320, 100, 30);

        P1ScoreLabel.setBackground(new java.awt.Color(0, 0, 0));
        P1ScoreLabel.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        P1ScoreLabel.setForeground(new java.awt.Color(255, 255, 255));
        P1ScoreLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        P1ScoreLabel.setText("-");
        P1ScoreLabel.setOpaque(true);
        gameScorePanel.add(P1ScoreLabel);
        P1ScoreLabel.setBounds(440, 360, 100, 30);

        P2ScoreLabel.setBackground(new java.awt.Color(0, 0, 0));
        P2ScoreLabel.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        P2ScoreLabel.setForeground(new java.awt.Color(255, 255, 255));
        P2ScoreLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        P2ScoreLabel.setText("-");
        P2ScoreLabel.setOpaque(true);
        gameScorePanel.add(P2ScoreLabel);
        P2ScoreLabel.setBounds(440, 400, 100, 30);

        P3ScoreLabel.setBackground(new java.awt.Color(0, 0, 0));
        P3ScoreLabel.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        P3ScoreLabel.setForeground(new java.awt.Color(255, 255, 255));
        P3ScoreLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        P3ScoreLabel.setText("-");
        P3ScoreLabel.setOpaque(true);
        gameScorePanel.add(P3ScoreLabel);
        P3ScoreLabel.setBounds(440, 440, 100, 30);

        P4ScoreLabel.setBackground(new java.awt.Color(0, 0, 0));
        P4ScoreLabel.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        P4ScoreLabel.setForeground(new java.awt.Color(255, 255, 255));
        P4ScoreLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        P4ScoreLabel.setText("-");
        P4ScoreLabel.setOpaque(true);
        gameScorePanel.add(P4ScoreLabel);
        P4ScoreLabel.setBounds(440, 480, 100, 30);

        P0RankLabel.setBackground(new java.awt.Color(0, 0, 0));
        P0RankLabel.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        P0RankLabel.setForeground(new java.awt.Color(255, 255, 255));
        P0RankLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        P0RankLabel.setText("-");
        P0RankLabel.setOpaque(true);
        gameScorePanel.add(P0RankLabel);
        P0RankLabel.setBounds(570, 320, 50, 30);

        P1RankLabel.setBackground(new java.awt.Color(0, 0, 0));
        P1RankLabel.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        P1RankLabel.setForeground(new java.awt.Color(255, 255, 255));
        P1RankLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        P1RankLabel.setText("-");
        P1RankLabel.setOpaque(true);
        gameScorePanel.add(P1RankLabel);
        P1RankLabel.setBounds(570, 360, 50, 30);

        P2RankLabel.setBackground(new java.awt.Color(0, 0, 0));
        P2RankLabel.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        P2RankLabel.setForeground(new java.awt.Color(255, 255, 255));
        P2RankLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        P2RankLabel.setText("-");
        P2RankLabel.setOpaque(true);
        gameScorePanel.add(P2RankLabel);
        P2RankLabel.setBounds(570, 400, 50, 30);

        P3RankLabel.setBackground(new java.awt.Color(0, 0, 0));
        P3RankLabel.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        P3RankLabel.setForeground(new java.awt.Color(255, 255, 255));
        P3RankLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        P3RankLabel.setText("-");
        P3RankLabel.setOpaque(true);
        gameScorePanel.add(P3RankLabel);
        P3RankLabel.setBounds(570, 440, 50, 30);

        P4RankLabel.setBackground(new java.awt.Color(0, 0, 0));
        P4RankLabel.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        P4RankLabel.setForeground(new java.awt.Color(255, 255, 255));
        P4RankLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        P4RankLabel.setText("-");
        P4RankLabel.setOpaque(true);
        gameScorePanel.add(P4RankLabel);
        P4RankLabel.setBounds(570, 480, 50, 30);

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tankOrangeSmallResized.png"))); // NOI18N
        gameScorePanel.add(jLabel11);
        jLabel11.setBounds(30, 480, 30, 30);

        jLabel36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tankRedSmallResized.png"))); // NOI18N
        gameScorePanel.add(jLabel36);
        jLabel36.setBounds(30, 320, 30, 30);

        jLabel39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tankYellowSmallResized.png"))); // NOI18N
        gameScorePanel.add(jLabel39);
        jLabel39.setBounds(30, 360, 30, 30);

        jLabel40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tankBlueSmallResized.png"))); // NOI18N
        gameScorePanel.add(jLabel40);
        jLabel40.setBounds(30, 400, 30, 30);

        jLabel41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tankPinkSmallResized.png"))); // NOI18N
        gameScorePanel.add(jLabel41);
        jLabel41.setBounds(30, 440, 30, 30);

        gameBoardLayeredPane.add(gameScorePanel);
        gameScorePanel.setBounds(670, 80, 660, 600);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/home.jpg"))); // NOI18N
        gameBoardLayeredPane.add(jLabel1);
        jLabel1.setBounds(0, 0, 1390, 710);

        getContentPane().add(gameBoardLayeredPane);
        gameBoardLayeredPane.setBounds(0, -1, 1390, 720);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setLayout(null);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/close-button_opt.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(1310, 10, 60, 60);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/minimize_opt.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);
        jButton2.setBounds(1250, 10, 60, 60);

        messageLabel.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        messageLabel.setForeground(new java.awt.Color(255, 255, 255));
        messageLabel.setText("Player 3 | Message :");
        jPanel1.add(messageLabel);
        messageLabel.setBounds(30, 20, 1200, 40);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 700, 1390, 70);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.setState(JFrame.ICONIFIED);
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GameView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GameView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GameView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GameView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new GameView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel P0CoinLabel;
    private javax.swing.JLabel P0HealthLabel;
    private javax.swing.JLabel P0NameLabel;
    private javax.swing.JLabel P0RankLabel;
    private javax.swing.JLabel P0ScoreLabel;
    private javax.swing.JLabel P1CoinLabel;
    private javax.swing.JLabel P1HealthLabel;
    private javax.swing.JLabel P1NameLabel;
    private javax.swing.JLabel P1RankLabel;
    private javax.swing.JLabel P1ScoreLabel;
    private javax.swing.JLabel P2CoinLabel;
    private javax.swing.JLabel P2HealthLabel;
    private javax.swing.JLabel P2NameLabel;
    private javax.swing.JLabel P2RankLabel;
    private javax.swing.JLabel P2ScoreLabel;
    private javax.swing.JLabel P3CoinLabel;
    private javax.swing.JLabel P3HealthLabel;
    private javax.swing.JLabel P3NameLabel;
    private javax.swing.JLabel P3RankLabel;
    private javax.swing.JLabel P3ScoreLabel;
    private javax.swing.JLabel P4CoinLabel;
    private javax.swing.JLabel P4HealthLabel;
    private javax.swing.JLabel P4NameLabel;
    private javax.swing.JLabel P4RankLabel;
    private javax.swing.JLabel P4ScoreLabel;
    private javax.swing.JLayeredPane gameBoardLayeredPane;
    private javax.swing.JLayeredPane gamePanelLayeredPane;
    private javax.swing.JPanel gameScorePanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel messageLabel;
    // End of variables declaration//GEN-END:variables

}
