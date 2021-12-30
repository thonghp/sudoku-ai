package edu.nlu.project.model;

import java.util.Arrays;

public class Sudoku {

    int[][] arrayCheck;
    private Population pop;

    public Sudoku(int[][] arrayCheck) {

        pop = new Population(arrayCheck);
        this.arrayCheck = arrayCheck;
    }

    public Individual solve() {
        return pop.finalzz();
    }

    public int[][] convert() {
        Individual in = solve();
        int[][] arr = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                arr[i][j] = in.getListGen().get(i).getGen()[j];
            }
        }
        return arr;
    }

    public static void main(String[] args) {
        int[][] check4 = {{0, 0, 7, 0, 1, 0, 0, 0, 8}, {0, 0, 0, 6, 8, 0, 3, 0, 2},
                {0, 0, 0, 2, 0, 4, 0, 9, 7}, {0, 3, 2, 4, 7, 9, 6, 8, 5}, {0, 0, 0, 1, 6, 0, 0, 0, 4},
                {0, 6, 0, 0, 0, 0, 0, 1, 9}, {0, 7, 0, 0, 4, 0, 0, 0, 0}, {3, 0, 9, 0, 2, 0, 8, 5, 1},
                {0, 5, 6, 8, 0, 1, 0, 7, 0},};
        Sudoku su = new Sudoku(check4);
        for (int i = 0; i < 9; i++) {
            System.out.println(Arrays.toString(su.convert()[i]));
        }
    }
}
