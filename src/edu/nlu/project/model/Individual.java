package edu.nlu.project.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Individual implements Comparable<Individual> {

    private List<Gene> geneList; // danh sách dùng để chứa các hàng gene sau khi được điền (9x9)
    private Random rd = new Random();
    private int[][] individual; // dùng để gán bằng với matrix đưa vào ban đầu

    // nhận vào matrix câu hỏi sau đó giải từng hàng và lưu các hàng đã giải vào geneList
    public Individual(int[][] matrix) {
        this.individual = new int[matrix.length][matrix.length];
        this.geneList = new ArrayList<>();
        setMatrix(matrix);
        generateIndividual();
    }

    // nhận vào 1 cá thể và mảng câu hỏi sau đó giải từng hàng và lưu các hàng đã giải vào geneList
    public Individual(Individual in, int[][] matrix) {
        this.individual = new int[matrix.length][matrix.length];
        this.geneList = new ArrayList<>();
        setMatrix(matrix);

        for (int i = 0; i < 9; i++) {
            Gene gen = new Gene(in.getGeneList().get(i));
            geneList.add(gen);
        }
    }

    // nhận vào một gene list và gán nó với geneList của cá thể
    public Individual(ArrayList<Gene> geneList) {
        this.geneList = geneList;
    }

    public void setMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                this.individual[i][j] = matrix[i][j];
            }
        }
    }

    // lấy từng hàng của matrix truyền vào trong individual để đưa qua gene điền những vị trí còn thiếu sau đó lưu vô list
    public void generateIndividual() {
        for (int i = 0; i < individual.length; i++) {
            Gene gen = new Gene(individual[i]);
            geneList.add(gen);
        }
    }

    // thay đổi hàng gen chỉ định thành hàng gen khác
    public void changeGen(int[] row, int index) {
        this.geneList.set(index, new Gene(row));
    }

    public List<Gene> getGeneList() {
        return geneList;
    }

    public void setGeneList(ArrayList<Gene> geneList) {
        this.geneList = geneList;
    }

    public void setInsertGen(int index, Gene gen) {
        this.geneList.add(index, gen);
    }

    // Hàm đánh giá độ tốt cá thể
    public int evaluate() {
        return evaluateCol() + evalueteSquare();
    }

    /**
     * Hàm đánh giá độ tốt cá thể theo tổng cột
     */
    public int evaluateCol() {
        int evaluate = 0;

        for (int index = 0; index < this.geneList.size(); index++) { // 0 -> 9
            evaluate += onlyEvalueteRow(index);
        }

        return evaluate;
    }

    // tính độ tốt của từng cột
    public int onlyEvalueteRow(int col) {
        int evaluate = 0;
        boolean checkCount = false;

        // check số lần trùng của 1 cột (công gộp tất cả số)
        for (int dem = 1; dem < this.geneList.size() + 1; dem++) { // 1 -> 9 (do sudoku số đánh dấu từ 1 đến 9)
            int count = 0;
            for (int k = 0; k < this.geneList.size(); k++) { // 0 -> 8 ( duyệt qua 9 vị trí trên 1 cột)
                int valueGen = geneList.get(k).getGen()[col];

                if (dem == valueGen) {
                    count++;

                    if (count == 1) {
                        checkCount = true;
                    }
                }
            }

            if (checkCount) {
                evaluate += count - 1;
            }

            checkCount = false;
        }

        return evaluate;
    }


    /**
     * Tính độ tốt theo ô vuông
     */
    public int evalueteSquare() {
        int result = 0;
        int checkCol = 0;
        // block
        while (checkCol <= 6) {
            int checkRow = 0;

            while (checkRow <= 6) {
                for (int num = 1; num < geneList.size() + 1; num++) {
                    int countCollision = 0;
                    boolean checkCount = false;

                    for (int i = checkRow; i < checkRow + 3; i++) {
                        for (int j = checkCol; j < checkCol + 3; j++) {
                            int t = geneList.get(i).getGen()[j];

                            if (num == t) {
                                countCollision++;

                                if (countCollision == 2) {
                                    checkCount = true;
                                }
                            }
                        }
                    }

                    if (checkCount) {
                        result += countCollision - 1;
                    }
                }

                checkRow += 3;
            }

            checkCol += 3;
        }

        return result;
    }

    /**
     * Tìm cột có độ tốt thấp nhất
     */
    public int lowGood() {
        int low = 0;
        for (int i = 1; i < geneList.size(); i++) {
            if (onlyEvalueteRow(i) > onlyEvalueteRow(low)) {
                low = i;
            }
        }
        return low;
    }

//	/**
//	 * thay đổi gen X
//	 */
//	public void setIndexGen(int indexGen) {
//		listGen.get(indexGen).setGen(2, 9);
//
//	}

    /**
     * Tính xung đột theo ô
     */
    public int evalueGetGenRect(int gen_i, int a) {
        int h = a;
        int k = gen_i;
        for (int i = 0; i < 3; i++) {

            if (h != 0)
                h--;
            if (k != 0)
                k--;
        }
        int evaluate = 0;
        for (int i = k; i < k + 3; i++) {
            for (int j = h; j < h + 3; j++) {

                int valueGen = geneList.get(a).getGen()[gen_i];
                if (geneList.get(i).getGen()[j] == valueGen && j != gen_i) {
                    evaluate += 1;

                }
            }

        }
        return evaluate;
    }

    // lấy giá trị ở cột chỉ định và hàng chỉ định so với các hàng còn lại trên cột chỉ định (ko so với hàng chỉ định) nếu trùng + 1
    public int evaluateGeneColumn(int col, int row) {
        int evaluate = 0;

        for (int k = 0; k < this.geneList.size(); k++) {
            int valueGen = geneList.get(row).getGen()[col];

            if (geneList.get(k).getGen()[col] == valueGen && k != row) {
                evaluate += 1;
            }
        }

        return evaluate;
    }

    /**
     * tổng xung đột của gen
     */
//    public int evalueGetGen(int col, int row) {
//        return evaluateGeneColumn(col, row);
//    }

    // trả về cột có xung đột (số lần trùng) nhiều nhất trên cùng 1 hàng chỉ định
    public int getColBadGene(int row, boolean[] check) {
        int low = 0;
        for (int col = 1; col < geneList.size(); col++) { // 1 -> 8
//            if (evalueGetGen(i, a) > evalueGetGen(low, a) && !check[i]) {
//                low = i;
//            }
            if (evaluateGeneColumn(low, row) < evaluateGeneColumn(col, row) && !check[col]) {
                low = col;
            }
        }
        return low;
    }

    // kiểm tra xem hàng đó đã điền chưa (ít nhất phải có 2 ô chưa điền)
    public boolean validMutation(Gene gen) {
        int count = 0;
        for (int i = 0; i < 9; i++) {
            if (gen.getChecked()[i]) {
                count++;
            }
        }
        return count <= 7;
    }

    /*
     * random row và col sau đó điền gen trên row vừa random, check xem row đó giá trị cột nào trùng nhiều nhất để swap
     * với cột ngẫu nhiên khác trên cùng 1 hàng chỉ định
     */
    public void setGeneIndex(int[][] matrix) { // matix này là mảng câu hỏi đưa vào từ population chưa giải
//        System.out.println("-------------------");
//        for (int[] ints : matrix) {
//            System.out.println(Arrays.toString(ints));
//        }
//        System.out.println("--------------random row và column");
//        int randomRow = 3;
        int randomRow = rd.nextInt(9);
        int randomColIndex = rd.nextInt(9);
//        System.out.println(randomRow + "-----" + randomCol); // 4 & 8
//        System.out.println("--------------điền gen");
        Gene mutation = new Gene(matrix[randomRow]); // 4
//        System.out.println(Arrays.toString(mutation.getGen()));
//        System.out.println("--------------cột cao nhất");
        int highestConflictColumn = getColBadGene(randomRow, mutation.getChecked()); // trả về cột có xung đột cao nhất
//        System.out.println(highestConflictColumn + "-----");

        /*
         *  check xem hàng đó đã điền gần hết chưa nếu hàng đó điền còn đúng 1 chỗ hoặc điền hết r thì đổi hàng khác
         *  case random row trúng ngay hàng 3 như check 4 test ở dưới (khi đó mảng còn có 1 chổ chưa điền)
         */
        while (!validMutation(mutation)) {
            randomRow = rd.nextInt(9);
            mutation = new Gene(matrix[randomRow]);
        }

//		System.out.println("Hang duoc dot bien: " + rdRow);
//		while (true) {
//			if (mutation.getTracking()[rdIndex1] == true)
//				rdIndex1 = rd.nextInt(9);
//			else
//				break;
//		}

        // kiểm tra xem cột ngẫu nhiên này có trùng với cột xung đột cáo nhất không nếu không thì lấy cột đó
        while (true) {
            if (randomColIndex == highestConflictColumn || mutation.getChecked()[randomColIndex] == true)
                randomColIndex = rd.nextInt(9);
            else
                break;
        }

//		System.out.println(rdIndex1);
//		System.out.println(rdIndex2);
//		this.listGen.get(rdRow).setGen1(rdIndex1, rdIndex2);
//		
//		Gen gen = new Gen(this.listGen.get(rdRow));
//		addGenToIndividual(gen.getGen(), rdRow);

        // đổi giá trị giữa 2 cột khác nhau trên cũng một hàng
        this.geneList.get(randomRow).swapGeneColumn(highestConflictColumn, randomColIndex);
    }

    // đổi 2 vị trí cột ngẫu nhiên
    public void setGeneIndexVer2(int[][] matrix) {
        int randomRow = rd.nextInt(9);
        int randomColIndex1 = rd.nextInt(9);
        int randomColIndex2 = rd.nextInt(9);

        Gene mutation = new Gene(matrix[randomRow]);
//		int rdIndex1 = lowGetGenGood(rdRow, mutation.getTracking());

        while (!validMutation(mutation)) {
            randomRow = rd.nextInt(9);
            mutation = new Gene(matrix[randomRow]);
        }
//		System.out.println("Hang duoc dot bien: " + rdRow);

//      check xem cột đó đã được điền chưa (so với trạng thái lúc ban đầu hi chưa điền của hàng đó) nếu r đổi cột khác
        while (true) {
            if (mutation.getChecked()[randomColIndex1] == true)
                randomColIndex1 = rd.nextInt(9);
            else
                break;
        }

//      check xem cột random 2 có trùng vs cột random 1 không và cột đó có rơi vào vị trí đã điền ban đầu chưa
        while (true) {
            if (randomColIndex2 == randomColIndex1 || mutation.getChecked()[randomColIndex2] == true)
                randomColIndex2 = rd.nextInt(9);
            else
                break;
        }

//      swap 2 cột ngẫu nhiên
        this.geneList.get(randomRow).swapGeneColumn(randomColIndex1, randomColIndex2);
    }

    // trả về tổng xung đột (số lần trùng) của từng giá trị cột chỉ định trên các hàng
    public int sumEvalueteRow(int colIndex) {
        int sum = 0;
        for (int i = 0; i < 9; i++) { // 9 hàng
            sum += evaluateGeneColumn(colIndex, i);
        }

        return sum;
    }

    /**
     * tìm hàng có xung đột lớn nhất để đột biến
     */
    public int maxEvalue() {
        int index = 0;
        int max = sumEvalueteRow(index);

        for (int i = 1; i < geneList.size(); i++) {

            if (max < sumEvalueteRow(i)) {
                max = sumEvalueteRow(i);
                index = i;
            }
        }
        return index;
    }

    /**
     * In cá thể
     */
    public void print() {
        for (Gene gene : geneList) {
            System.out.println(Arrays.toString(gene.getGen()));
        }
    }

    @Override
    public int compareTo(Individual o) {
        if (evaluate() == o.evaluate())
            return 0;
        else if (evaluate() > o.evaluate())
            return 1;
        else
            return -1;
    }

    public static void main(String[] args) {
        int[][] check = {{0, 0, 7, 8, 0, 0, 9, 0, 0}, {0, 0, 0, 5, 0, 0, 0, 3, 1}, {9, 0, 0, 0, 0, 1, 0, 4, 0},
                {2, 1, 0, 0, 6, 0, 7, 8, 0}, {0, 0, 0, 0, 0, 3, 0, 9, 0}, {3, 0, 9, 0, 1, 0, 2, 0, 0},
                {4, 0, 0, 0, 0, 0, 0, 1, 6}, {0, 0, 0, 1, 0, 9, 0, 0, 8}, {0, 8, 0, 0, 3, 0, 0, 0, 0}};

        int[][] check4 = {{0, 0, 7, 0, 1, 0, 0, 0, 8}, {0, 0, 0, 6, 8, 0, 3, 0, 2}, {0, 0, 0, 2, 0, 4, 0, 9, 7},
                {0, 3, 2, 4, 7, 9, 6, 8, 5}, {0, 0, 0, 1, 6, 0, 0, 0, 4}, {0, 6, 0, 0, 0, 0, 0, 1, 9},
                {0, 7, 0, 0, 4, 0, 0, 0, 0}, {3, 0, 9, 0, 2, 0, 8, 5, 1}, {0, 5, 6, 8, 0, 1, 0, 7, 0}};
        Individual matrix = new Individual(check4);
        matrix.print();
        System.out.println("hàm đánh giá");
        System.out.println(matrix.evaluateCol());
        System.out.println(matrix.evalueteSquare());
        System.out.println(matrix.evaluate());
        System.out.println("đổi cột xung đột nhiều");
//        matrix.setGeneIndex(check4); // đổi cột trùng nhiều
//        matrix.print();

        System.out.println("đổi 2 cột ngẫu nhiên");
//        matrix.setGeneIndexVer2(check4); // đổi cột trùng nhiều
//        matrix.print();

        System.out.println("tổng xung đột trên cùng 1 cột");
        System.out.println(matrix.sumEvalueteRow(1));
        System.out.println("***************************");

        Individual copy = new Individual(matrix, check4);
//        copy.print();
    }
}
