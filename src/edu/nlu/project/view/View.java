package edu.nlu.project.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class View extends JFrame {
    // Name-constants for the game properties
    public static final int GRID_SIZE = 9; // Size of the board
    public static JButton btnSolve;
    public static JButton btnRefresh;
    public static JButton btnCheck;

    // Name-constants for UI control (sizes, colors and fonts)
    public static final int CELL_SIZE = 60; // Cell width/height in pixels
    public static final int CANVAS_WIDTH = CELL_SIZE * GRID_SIZE;
    public static final int CANVAS_HEIGHT = CELL_SIZE * GRID_SIZE;

    // Board width/height in pixels
    public static final Color OPEN_CELL_BGCOLOR = Color.WHITE;
    public static final Color BLUE_FILL = new Color(187, 222, 251);
    public static final Color BLUE_BUTTON = new Color(0, 114, 227);
    public static final Font FONT_NUMBERS = new Font("Tahoma", Font.BOLD, 20);

    // The game board composes of 9x9 JTextFields,
    // each containing String "1" to "9", or empty String
    private JTextField[][] tfCells = new JTextField[GRID_SIZE][GRID_SIZE];
    public static JLabel notice;
    public static JLabel contentNotice;

    private boolean[][] check = new boolean[9][9];

    public View() {
        Gui();
    }

    public void Gui() {
        setLayout(new BorderLayout());

        add(viewNumber(), BorderLayout.CENTER);
        add(buttonPanel(), BorderLayout.EAST);
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Sudoku");
        setVisible(true);
    }

    public JPanel viewNumber() {
        JPanel cp = new JPanel();
        cp.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE)); // 9x9 GridLayout

        for (int row = 0; row < GRID_SIZE; ++row) {
            for (int col = 0; col < GRID_SIZE; ++col) {
                tfCells[row][col] = new JTextField();
                cp.add(tfCells[row][col]);

                Font fo = new Font("Tahoma", Font.BOLD, 25);
                tfCells[row][col].setText("");
                tfCells[row][col].setForeground(Color.BLUE);

                tfCells[row][col].setEditable(true);
                tfCells[row][col].setFont(fo);

                tfCells[row][col].setBackground(OPEN_CELL_BGCOLOR);

                tfCells[row][col].setHorizontalAlignment(JTextField.CENTER);
                tfCells[row][col].setFont(FONT_NUMBERS);
                tfCells[row][col].addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        char character = e.getKeyChar();
                        if (((character < '0') || (character > '9'))) {
                            e.consume();
                        }
                    }
                });
            }
        }

        cp.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        return cp;
    }

    public JPanel buttonPanel() {
        Font fo1 = new Font("Tahoma", Font.BOLD, 15);
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new GridLayout(6, 1));
        btnCheck = new JButton("Check");
        btnCheck.setFocusable(false);
        btnCheck.setBackground(BLUE_BUTTON);
        btnCheck.setForeground(Color.white);

        btnPanel.add(btnCheck);
        btnCheck.setFont(new Font("Tahoma", Font.PLAIN, 20));

        btnSolve = new JButton("Solve");
        btnPanel.add(btnSolve);
        btnSolve.setFont(new Font("Tahoma", Font.PLAIN, 20));
        btnSolve.setFocusable(false);
        btnSolve.setBackground(BLUE_BUTTON);
        btnSolve.setForeground(Color.white);

        btnRefresh = new JButton("Refresh");
        btnPanel.add(btnRefresh);
        btnRefresh.setFont(new Font("Tahoma", Font.PLAIN, 20));
        btnRefresh.setFocusable(false);
        btnRefresh.setBackground(BLUE_BUTTON);
        btnRefresh.setForeground(Color.white);

        JButton btnExit = new JButton("Exit");

        btnExit.setFont(new Font("Tahoma", Font.PLAIN, 20));
        btnExit.setFocusable(false);
        btnExit.setBackground(BLUE_BUTTON);
        btnExit.setForeground(Color.white);
        btnExit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        btnPanel.add(btnExit);
        notice = new JLabel("THÔNG BÁO:");
        contentNotice = new JLabel();
        contentNotice.setFont(fo1);
        contentNotice.setForeground(Color.RED);
        btnPanel.add(notice);
        btnPanel.add(contentNotice);

        return btnPanel;
    }

    // đọc số để hiện
    public int[][] getTxt() {
//        setNoticeHanle();
        int[][] gen = new int[9][9];
        for (int row = 0; row < GRID_SIZE; ++row) {
            for (int col = 0; col < GRID_SIZE; ++col) {
                String txt = tfCells[row][col].getText();
                if (txt == null || txt.equals("")) {
                    gen[row][col] = 0;

                } else {
                    check[row][col] = true; // if ô đó đã điền
                    gen[row][col] = Integer.parseInt(txt); // lưu vô gen
                }
            }

        }
        return gen;
    }

    public void setTxt(int[][] solve) {
        for (int row = 0; row < GRID_SIZE; ++row) {
            for (int col = 0; col < GRID_SIZE; ++col) {
//                setNoticeHanle();
                if (check[row][col] == false) {
                    tfCells[row][col].setText(solve[row][col] + "");
                    tfCells[row][col].setBackground(OPEN_CELL_BGCOLOR);
                } else {
                    tfCells[row][col].setText(solve[row][col] + "");
                    tfCells[row][col].setForeground(Color.RED);
                    tfCells[row][col].setBackground(BLUE_FILL);
                    check[row][col] = false;
                }
            }

        }
        setNoticeHanled(true);
    }

    public void setTxtRefesh() {
        for (int row = 0; row < GRID_SIZE; ++row) {
            for (int col = 0; col < GRID_SIZE; ++col) {
                tfCells[row][col].setText("");
            }
        }
    }

    public void setBG() {
        for (int row = 0; row < GRID_SIZE; ++row) {
            for (int col = 0; col < GRID_SIZE; ++col) {
                tfCells[row][col].setBackground(OPEN_CELL_BGCOLOR);
                tfCells[row][col].setForeground(Color.BLUE);
            }

        }
    }

    public void setNoticeHanled(boolean check) {
        if (check) {
            contentNotice.setText("đã xử lý xong");
        } else {
            contentNotice.setText("");
        }
    }

    public void noticeOfBtnCheck() {
        if (checkMatrix()) {
            JOptionPane.showMessageDialog(null, "Hợp lệ");
        } else {
            JOptionPane.showMessageDialog(null, "Vui lòng kiểm tra lại");
        }
    }

    public boolean checkMatrix() {
        int[][] matrix = getTxt();
        ArrayList<Integer> list = new ArrayList<>();

        for (int i = 0; i < matrix.length; i++) {
            list.add(i + 1);
        }

        // check xem có số nào trùng nhau trên hàng hay cột không
        for (int i = 0; i < matrix.length; i++) {
            for (Integer sum : list) {
                int count = 0;
                int count1 = 0;
                for (int j = 0; j < matrix[i].length; j++) {
                    if (sum == matrix[i][j])
                        count++;
                    if (sum == matrix[j][i])
                        count1++;

                }

                if (count >= 2 || count1 >= 2)
                    return false;
            }

        }

        // check số nào trùng trong ô 3x3
        int count1 = 0;
        for (int row = 0; row < matrix.length; row += 3) {
            for (int col = 0; col < matrix.length; col += 3) {
                for (Integer sum : list) {
                    int count = 0;

                    for (int i = row; i < row + 3; i++) {
                        for (int j = col; j < col + 3; j++) {
                            if (matrix[i][j] > 0)
                                count1++;
                            if (sum == matrix[i][j])
                                count++;

                        }
                    }
                    if (count >= 2)
                        return false;
                }
            }
        }
        return true;
    }

//    public static void main(String[] args) {
//        View check = new View();
//    }
}