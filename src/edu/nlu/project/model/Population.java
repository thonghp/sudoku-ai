package edu.nlu.project.model;

import java.util.*;

public class Population {
    // 1 individual là 1 matrix 9x9
    private ArrayList<Individual> individualList = new ArrayList<>(); // chứa 3000 bài giải khác nhau
    private ArrayList<Individual> fatherList = new ArrayList<>();
    private ArrayList<Individual> motherList = new ArrayList<>();
    private ArrayList<Individual> childList = new ArrayList<>();
    private int[][] question;
    private Random random = new Random();

//    public ArrayList<Individual> getIndividualList() {
//        return individualList;
//    }
//
//    public ArrayList<Individual> getFatherList() {
//        return fatherList;
//    }
//
//    public ArrayList<Individual> getMotherList() {
//        return motherList;
//    }
//
//    public ArrayList<Individual> getChildList() {
//        return childList;
//    }

    // nhận vào question sau đó giải 3000 lần và lưu lại
    public Population(int[][] question) {
        this.question = question;
        for (int i = 0; i < 3000; i++) {
            // lưu 3000 cách giải thông qua question đưa vào trong individual để giải
            individualList.add(new Individual(question));
        }
    }

    // Tạo quần thể mother chứa 20 cá thể được lấy từ trong danh sách 3000
    public void mother() {
//		performPopulationArrangement();
        for (int i = 0; i < 20; i++) {
            this.motherList.add(this.individualList.get(i));
        }
//        ArrayList<Individual> caThe = listMother.get(0);
    }


    // Tạo quần thể father chứa 20 cá thể được lấy từ trong danh sách 3000
    public void father() {
//		performPopulationArrangement();
        for (int i = 0; i < 20; i++) {
            this.fatherList.add(this.individualList.get(i));
        }
    }

    // Hàm lai giữa 2 cá thể cha và mẹ
    public Individual createNewIndividual(Individual father, Individual mother) {
        ArrayList<Gene> newListGen = new ArrayList<>();
        int t = random.nextInt(8);
        for (int i = 0; i < 9; i++) {
            if (i <= t) {
                Gene gen1 = new Gene(father.getGeneList().get(i));
                newListGen.add(gen1);
            } else {
                Gene gen1 = new Gene(mother.getGeneList().get(i));
                newListGen.add(gen1);
            }
        }

        Individual newIndividual = new Individual(newListGen);
        return newIndividual;
    }

    public Individual createNewIndividual2(Individual father, Individual mother) {
        ArrayList<Gene> newListGen = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (i <= 1 || (i >= 4 && i <= 5) || (i > 7)) {
                Gene gen1 = new Gene(father.getGeneList().get(i));

                newListGen.add(gen1);
            } else {
                Gene gen1 = new Gene(mother.getGeneList().get(i));

                newListGen.add(gen1);
            }
        }
        Individual newIndividual = new Individual(newListGen);
        return newIndividual;
    }

    public void crossover() {
//        int check = random.nextInt(1);
        father();
        mother();
//		listChild = new ArrayList<Individual>();
//		if (check == 0) {

        for (int i = 0; i < fatherList.size(); i++) {
            for (int j = 0; j < motherList.size(); j++) {
                childList.add(createNewIndividual(fatherList.get(i), motherList.get(j)));
            }
        }
//		} else {
//			for (int i = 0; i < listFather.size(); i++) {
//				for (int j = 0; j < listMother.size(); j++) {
//					listChild.add(createNewIndividual2(listFather.get(i), listMother.get(j)));
//				}
//			}
//		}

        fatherList.clear();
        motherList.clear();
    }

    /**
     * tính độ tốt trung bình
     */
    public int averageGood() {
        int average = 0;
        int sum = individualList.size() + childList.size();

        for (int i = 0; i < individualList.size(); i++) {
            average += individualList.get(i).evaluate();
        }
        for (int i = 0; i < childList.size(); i++) {
            average += childList.get(i).evaluate();
        }

        return (average / sum);
    }

    /**
     * Tạo hàm chọn lọc
     */

    // thêm danh sách trong mảng individual ban đầu, sắp xếp lại và chỉ giữ 50 cá thể tốt nhất trong individualList
    public void selection() {

        for (int i = 0; i < childList.size(); i++) {
            Individual in = new Individual(childList.get(i), question);
            this.individualList.add(in);
        }
        performPopulationArrangement();

        for (int i = 50; i < individualList.size(); i++) {

            individualList.remove(i);
            i--;
        }
    }

    /**
     * Đột biến ngẫu nhiên 1/4 quần thể
     */
    public void clearChild() {
        int check = childList.size(); // cao nhất là childList sẽ được gọi crossover sáu lần lúc này sẽ giảm nửa mảng
        if (check > 2000) {
            for (int i = 0; i < check / 2; i++) {
                this.childList.remove(i);
            }
        }
    }

    // đổi cột chứa cá thể xấu với một cột ngẫu nhiên trên 1 hàng sau đó lưu lại giá trị đã đổi trong childList
    public void mutation() {
        int[][] k = question;
        clearChild();
        for (int i = 0; i < childList.size(); i++) {
            this.childList.get(i).setGeneIndex(k);
        }
    }

    public void mutationVer2() {
        int[][] k = question;
        clearChild();

        for (int i = 0; i < childList.size(); i++) {
            this.childList.get(i).setGeneIndexVer2(k);
        }
    }

    /*
     * Sắp xếp quần thể theo thứ tự evaluate
     */
    public void performPopulationArrangement() {
        Collections.sort(this.individualList);

    }

    public void arrangeAndSelectPopuChild() {
        Collections.sort(this.childList);

    }

    /**
     * Hàm lấy con tốt nhất trong quần thể (evaluate nào trùng ít nhất sẽ lấy)
     */
    public Individual fitnessChild() {
        Individual max = individualList.get(0);
        for (int i = 1; i < individualList.size(); i++) {
            if (max.evaluate() > individualList.get(i).evaluate()) {
                max = individualList.get(i);
            }
        }
        return max;
    }

    public Individual individualFitness() {
        int count = 0;
        int check;
        main:
        while (true) {
            check = fitnessChild().evaluate();
            if (fitnessChild().evaluate() == 0) {
                return fitnessChild();
            }
            count++;
            crossover(); // lai ra 400 cá thể con (mỗi lần gọi method thì childList sẽ thay đổi)
            if (check <= 4) {
                mutationVer2(); // đổi ngẫu nhiên 2 cột trong cùng 1 hàng của 1 cá thể con trong quần thể con
            } else {
                mutation(); // đổi ngẫu nhiên 1 cột với cột có xung đột cao nhất trên cùng 1 hàng của 1 cá thế
            }
            selection(); // luôn giữ 50 cá thể tốt nhất từ individualList

            System.out.println(childList.size());
            System.out.println();
            System.err.println("Thế hệ thứ: " + count);
            System.out.println();
            for (int i = 0; i < individualList.size(); i++) { // 50 do selection chỉ giữ lại 50 cá thể tốt nhất
                System.out.print(individualList.get(i).evaluate() + "|");

            }
            System.out.println();
            if (count >= 100) {
                break main;
            }
        }
        return new Population(question).individualFitness();
    }

    public Individual doFinal() {
        return individualFitness();
    }

    public static void main(String[] args) {
//        int[][] check2 = {{0, 5, 7, 0, 0, 0, 0, 0, 6}, {3, 0, 0, 0, 0, 0, 2, 1, 8}, {2, 0, 4, 0, 0, 0, 0, 0, 0},
//                {0, 0, 9, 0, 1, 0, 0, 2, 0}, {8, 3, 0, 0, 0, 2, 0, 0, 0}, {0, 0, 0, 4, 8, 0, 5, 3, 0},
//                {4, 0, 0, 0, 0, 0, 9, 0, 2}, {0, 6, 0, 9, 0, 4, 0, 0, 0}, {9, 0, 8, 0, 6, 5, 0, 0, 4}};

        int[][] check4 = {{0, 0, 7, 0, 1, 0, 0, 0, 8}, {0, 0, 0, 6, 8, 0, 3, 0, 2}, {0, 0, 0, 2, 0, 4, 0, 9, 7},
                {0, 3, 2, 4, 7, 9, 6, 8, 5}, {0, 0, 0, 1, 6, 0, 0, 0, 4}, {0, 6, 0, 0, 0, 0, 0, 1, 9},
                {0, 7, 0, 0, 4, 0, 0, 0, 0}, {3, 0, 9, 0, 2, 0, 8, 5, 1}, {0, 5, 6, 8, 0, 1, 0, 7, 0}};
        Population po = new Population(check4);
        po.crossover();
        po.mutation();
        po.selection();

//        ArrayList<Individual> con = po.getIndividualList();
//        System.out.println(con.size());

        po.crossover();
        po.mutation();
        po.selection();

        po.crossover();
        po.mutation();
        po.selection();

//        po.crossover();
//        po.crossover();
//        po.crossover();
//        po.crossover();
//        po.crossover();
//        po.mutation();


//        Individual child = con.get(0);
//        child.print();
//        System.out.println("*******************");
//        po.mutation();
//        child.print();

//        po.mother();
//        po.father();
//        ArrayList<Individual> con = po.getMotherList();
//        Individual individualc = con.get(0);
//        individualc.print();
//        ArrayList<Individual> individuals = po.getMotherList();
//        Individual individual1 = individuals.get(0);
//        individual1.print();
//        System.out.println("*******************");
//        ArrayList<Individual> individuals2 = po.getFatherList();
//        Individual individual2 = individuals2.get(0);
//        individual2.print();


//        Individual individual2 = individuals.get(1);
//        individual2.print();

//        po.mother();
//        po.father();
//        System.out.println(po.getListFather().size());
//        po.crossover();
//        po.crossover();
//        System.out.println(po.getListChild().size());
//        System.out.println(po.getListFather().size());
//        System.out.println(po.getListIndividual().size());
//        System.out.println(po.getListChild().size());
//        System.out.println(po.getListFather().size());
//        System.out.println(po.getListMother().size());


//        ArrayList<Individual> caThe = po.listIndividual;
//        for (int i = 0; i < 9; i++) {
//            System.out.println(Arrays.toString(caThe.get(i).getListGen().get(i).getGen()));
//        }
//
//        Individual cathe1 = caThe.get(0);
//        List<Gene> gen = cathe1.getListGen();
//        System.out.println(Arrays.toString(gen.get(0).getGen()));

//        po.individualFitness().print();
//        System.out.println("--------");
//        po.finalzz().print();
//        po.mother();
//		po.father();

//		po.createNewIndividual2(po.listFather.get(0), po.listMother.get(0)).print();
    }

}
