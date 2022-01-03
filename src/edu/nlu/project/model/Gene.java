package edu.nlu.project.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Gene {

    private int[] gen;
    private boolean[] checked; // lưu lại 9 trạng thái sau khi check một mảng đưa vào
    private Random random;

    // kiểm tra ô đã điền chưa nếu chưa thì tạo ngẫu nhiên số nằm trong khoảng 1 -> 9 và phải thỏa tính chất
    public Gene(int[] gen) {
        this.gen = gen.clone();
        this.checked = checkBoxIsFilled();
        random = new Random();
        generateNumber();
    }

    // copy gene được tạo
    public Gene(Gene g) {
        this.gen = new int[9];

        for (int i = 0; i < gen.length; i++) {
            gen[i] = g.getGen()[i];
        }
    }

    public boolean[] getChecked() {
        return checked;
    }

    public void setChecked(boolean[] checked) {
        this.checked = checked;
    }

    public int[] getGen() {
        return gen;
    }

    public void setGen(int[] gen) {
        this.gen = gen;
    }

    // check xem trong mảng ô nào đã được điền và ô nào chưa được điền
    public boolean[] checkBoxIsFilled() {
        boolean[] check = new boolean[gen.length]; // 9

        for (int i = 0; i < gen.length; i++) {
            if (gen[i] != 0) {
                check[i] = true;
            }
        }

        return check;
    }

    // tạo số ngẫu nhiên để điền vào các vị trí còn thiếu
    public void generateNumber() {
        int value;
        List<Integer> list = new ArrayList<>();

        // lưu các số đã được điền vào list
        for (int i = 0; i < gen.length; i++) {
            if (checked[i])
                list.add(gen[i]);
        }

//  những ô chưa được điền sẽ tạo ngẫu nhiên số để điền (các số ngẫu nhiên sẽ được kiểm tra xem đã có trong list chưa)
        for (int i = 0; i < gen.length; i++) {
            if (!checked[i]) {
                while (true) {
                    value = random.nextInt(gen.length) + 1;
                    if (!list.contains(value)) {
                        gen[i] = value;
                        list.add(value);
                        break;
                    }
                }
            }
        }
    }

    // đổi giá trị của 2 cột gene chỉ định trên cùng 1 hàng
    public void swapGeneColumn(int column1, int column2) {
        int temp = this.gen[column1];
        this.gen[column1] = this.gen[column2];
        this.gen[column2] = temp;
    }

    public static void main(String[] args) {
        int[] arr = {0, 0, 7, 8, 0, 0, 9, 0, 0};
        Gene gen1 = new Gene(arr);
        System.out.println(Arrays.toString(gen1.getChecked()));
        System.out.println(Arrays.toString(gen1.getGen()));
        gen1.swapGeneColumn(0, 3);
        System.out.println(Arrays.toString(gen1.getGen()));

        Gene list = new Gene(gen1);
        System.out.println(Arrays.toString(list.getGen()));
    }
}
