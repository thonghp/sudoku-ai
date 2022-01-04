package edu.nlu.project.controller;

import edu.nlu.project.model.Sudoku;
import edu.nlu.project.view.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;


public class Control {
    private Sudoku model;
    private View view;

    public Control() {
        init();
    }

    int count = 0;

    public void init() {
        view = new View();

        View.btnSolve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // mảng chưa được giải
                if (count == 0) {
                    if (view.checkMatrix()) { // coi có số nào vi phạm hàng ngang hàng dọc và ô 3x3

                        /*-------đề mẫu có sẵn-------*/
                        int[][] check4 = {{0, 0, 7, 0, 1, 0, 0, 0, 8}, {0, 0, 0, 6, 8, 0, 3, 0, 2},
                                {0, 0, 0, 2, 0, 4, 0, 9, 7}, {0, 3, 2, 4, 7, 9, 6, 8, 5},
                                {0, 0, 0, 1, 6, 0, 0, 0, 4}, {0, 6, 0, 0, 0, 0, 0, 1, 9},
                                {0, 7, 0, 0, 4, 0, 0, 0, 0}, {3, 0, 9, 0, 2, 0, 8, 5, 1},
                                {0, 5, 6, 8, 0, 1, 0, 7, 0}};
//
//                        // đưa vào trong individual để giải những ô còn thiếu trên từng hàng 1 thông qua gen
                        model = new Sudoku(check4);
                        /*-------đề mẫu có sẵn-------*/

                        // nhận vào mảng nếu ng dùng nhập hoặc mảng trống
//						model = new Sudoku(view.getTxt());

//                      trả về mảng sau khi giải
                        int[][] check = model.convert();
                        view.setTxt(check);
                        count = 1;
                    } else {
                        JOptionPane.showMessageDialog(null, "Vui lòng kiểm tra lại số");
                    }
                } else {
                    // nếu mảng đã giải rồi thì khi nhấn lại nút solve sẽ refresh để giải bài mới
                    view.setTxtRefesh();
                    view.setBG();
//					model = new Sudoku(check4);
                    model = new Sudoku(view.getTxt());
                    int[][] check = model.convert();
                    view.setTxt(check);
                    count = 0;
                }
            }
        });

        View.btnRefresh.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                view.setTxtRefesh();
                view.setBG();
            }
        });

        View.btnCheck.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                view.noticeOfBtnCheck();
            }
        });

    }

    public static void main(String[] args) throws InterruptedException {
        Control con = new Control();
    }
}
