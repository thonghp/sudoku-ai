package edu.nlu.project.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Gen {

    private int[] gen;
    private boolean[] tracking;
    private Random rd;
    private ArrayList<Boolean> trackingCheck;

    // kiểm tra ô đã điền chưa nếu chưa thì tạo ngẫu nhiên số nằm trong khoảng 1 -> 9 và phải thỏa tính chất
    public Gen(int[] gen) {
        trackingCheck = new ArrayList<>();
        this.gen = gen.clone();
        this.tracking = tracking();
        rd = new Random();
        generateNumber();
    }

    public Gen(Gen g) {
        this.gen = new int[9];
        for (int i = 0; i < gen.length; i++) {
            gen[i] = g.getGen()[i];
        }
    }

    public ArrayList<Boolean> getTrackingCheck() {
        return trackingCheck;
    }

    public void setTrackingCheck(ArrayList<Boolean> trackingCheck) {
        this.trackingCheck = trackingCheck;
    }

    public boolean[] getTracking() {
        return tracking;
    }

    public void setTracking(boolean[] tracking) {
        this.tracking = tracking;
    }

    public int[] getGen() {
        return gen;
    }

    public void setGen(int[] gen) {
        this.gen = gen;
    }

    // check xem trong mảng ô nào đã được điền và ô nào chưa được điền
    public boolean[] tracking() {
        boolean[] tracking = new boolean[gen.length]; // 9

        for (int i = 0; i < gen.length; i++) {

            if (gen[i] != 0) {
                tracking[i] = true;
                if (trackingCheck.size() < 9) {
                    trackingCheck.add(true);
                }
            } else {
                if (trackingCheck.size() < 9) {
                    trackingCheck.add(false);
                }
            }
        }

        return tracking;
    }

    // tạo số khi chưa điền
    public void generateNumber() {
        int value;
        ArrayList<Integer> rdList = new ArrayList<>();

        // if ô nào đã có số được điền thì sẽ thêm vào list
        for (int i = 0; i < gen.length; i++) {
            if (tracking[i])
                rdList.add(gen[i]);
        }

        // tạo ngẫu nhiên số để điền vào các ô chưa có
        for (int i = 0; i < gen.length; i++) {
            if (!tracking[i]) {
                while (true) {
                    value = rd.nextInt(gen.length) + 1;
                    if (!rdList.contains(value)) {
                        gen[i] = value;
                        rdList.add(value);
                        break;
                    }
                }
            }
        }
    }

    // đổi vị trí giữa 2 gen
    public void swapGen(int valueMax, int valueMax2) {
        int temp = this.gen[valueMax];
        this.gen[valueMax] = this.gen[valueMax2];
        this.gen[valueMax2] = temp;
    }

    public static void main(String[] args) {
        int[] arr = {0, 0, 7, 8, 0, 0, 9, 0, 0};
        Gen gen = new Gen(arr);
        for (int i = 0; i < 3; i++) {
            System.out.println(Arrays.toString(gen.getGen()));
        }
        System.out.println(gen.getTrackingCheck());

    }
}
