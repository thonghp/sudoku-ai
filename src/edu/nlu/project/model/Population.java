package edu.nlu.project.model;

import java.util.*;

public class Population {
    private ArrayList<Individual> listIndividual = new ArrayList<>();
    private ArrayList<Individual> listFather = new ArrayList<>();
    private ArrayList<Individual> listMother = new ArrayList<>();
    private ArrayList<Individual> listChild = new ArrayList<>();
    private int[][] question;

    public Population(int[][] question) {
        this.question = question;
        for (int i = 0; i < 3000; i++) {
            // 1 question là 1 bài đã được điền full chín hàng chưa thỏa tính chất cột
            listIndividual.add(new Individual(question));
        }
    }

    /*
     * Tạo quần thể mother
     */

    public void mother() {
//		arrangeAndSelectPopu();
        for (int i = 0; i < 20; i++) {
            this.listMother.add(this.listIndividual.get(i));
        }
//        ArrayList<Individual> caThe = listMother.get(0);
    }

    /**
     * Tạo quần thể father
     */

    public void father() {
//		arrangeAndSelectPopu();
        for (int i = 0; i < 20; i++) {
            this.listFather.add(this.listIndividual.get(i));
        }
    }

    /*
     * Hàm lai
     */
    public Individual createNewIndividual1(Individual father, Individual mother) {
        Random random = new Random();
        ArrayList<Gene> newListGen = new ArrayList<Gene>();
        int t = random.nextInt(8);
        for (int i = 0; i < 9; i++) {

            if (i <= t) {
                Gene gen1 = new Gene(father.getListGen().get(i));

                newListGen.add(gen1);
            } else {
                Gene gen1 = new Gene(mother.getListGen().get(i));

                newListGen.add(gen1);
            }
        }

        Individual newIndividual = new Individual(newListGen);
        return newIndividual;
    }

    public Individual createNewIndividual2(Individual father, Individual mother) {
        ArrayList<Gene> newListGen = new ArrayList<Gene>();
        for (int i = 0; i < 9; i++) {
            if (i <= 1 || (i >= 4 && i <= 5) || (i > 7)) {
                Gene gen1 = new Gene(father.getListGen().get(i));

                newListGen.add(gen1);
            } else {
                Gene gen1 = new Gene(mother.getListGen().get(i));

                newListGen.add(gen1);
            }
        }
        Individual newIndividual = new Individual(newListGen);
        return newIndividual;
    }

    public void crossover() {
        Random rd = new Random();
        int check = rd.nextInt(1);
        father();
        mother();
//		listChild = new ArrayList<Individual>();
//		if (check == 0) {
        for (int i = 0; i < listFather.size(); i++) {
            for (int j = 0; j < listMother.size(); j++) {
                listChild.add(createNewIndividual1(listFather.get(i), listMother.get(j)));
            }
        }
//		} else {
//			for (int i = 0; i < listFather.size(); i++) {
//				for (int j = 0; j < listMother.size(); j++) {
//					listChild.add(createNewIndividual2(listFather.get(i), listMother.get(j)));
//				}
//			}
//		}
        listFather.clear();
        listMother.clear();

    }

    /**
     * tính độ tốt trung bình
     */
    public int averageGood() {
        int average = 0;
        int sum = listIndividual.size() + listChild.size();

        for (int i = 0; i < listIndividual.size(); i++) {
            average += listIndividual.get(i).evaluate();
        }
        for (int i = 0; i < listChild.size(); i++) {
            average += listChild.get(i).evaluate();
        }

        return (average / sum);
    }

    /**
     * Tạo hàm chọn lọc
     */

    public void selection() {

        for (int i = 0; i < listChild.size(); i++) {
            Individual in = new Individual(listChild.get(i), question);
            this.listIndividual.add(in);
        }
        arrangeAndSelectPopu();

        for (int i = 50; i < listIndividual.size(); i++) {

            listIndividual.remove(i);
            i--;
        }

    }

    /**
     * Đột biến ngẫu nhiên 1/3 quần thể
     */
    public void clearChild() {
        int check = listChild.size();
        if (check > 2000) {
            for (int i = 0; i < check / 2; i++) {
                this.listChild.remove(i);
            }
        }
    }

    public void mutation() {
        int[][] k = question;
        clearChild();

        for (int i = (listChild.size() * (1 / 2)); i < listChild.size(); i++) {
            this.listChild.get(i).setIndexGen(k);
        }
    }

    public void mutationVer2() {
        int[][] k = question;
        clearChild();

        for (int i = (listChild.size() * (1 / 2)); i < listChild.size(); i++) {
            this.listChild.get(i).setIndexGenVer2(k);
        }
    }

    /*
     * Sắp xếp quần thể theo thứ tự
     */
    public void arrangeAndSelectPopu() {
        Collections.sort(this.listIndividual);

    }

    public void arrangeAndSelectPopuChild() {
        Collections.sort(this.listChild);

    }

    /**
     * Hàm lấy con tốt nhất trong quần thể (evaluate nào trùng ít nhất sẽ lấy)
     */
    public Individual fitnessChild() {
        Individual max = listIndividual.get(0);
        for (int i = 1; i < listIndividual.size(); i++) {
            if (max.evaluate() > listIndividual.get(i).evaluate()) {
                max = listIndividual.get(i);
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
            crossover();
            if (check <= 4) {
                mutationVer2();
            } else {
                mutation();
            }
            selection();

            System.out.println(listChild.size());
            System.out.println();
            System.err.println("Thế hệ thứ: " + count);
            System.out.println();
            for (int i = 0; i < listIndividual.size(); i++) {
                System.out.print(listIndividual.get(i).evaluate() + "|");

            }
            System.out.println();
            if (count >= 100) {
                break main;
            }
        }
        return new Population(question).individualFitness();
    }

    public Individual finalzz() {
        return individualFitness();
    }

    public static void main(String[] args) throws InterruptedException {
//		int[][] check2 = { { 0, 5, 7, 0, 0, 0, 0, 0, 6 }, { 3, 0, 0, 0, 0, 0, 2, 1, 8 }, { 2, 0, 4, 0, 0, 0, 0, 0, 0 },
//				{ 0, 0, 9, 0, 1, 0, 0, 2, 0 }, { 8, 3, 0, 0, 0, 2, 0, 0, 0 }, { 0, 0, 0, 4, 8, 0, 5, 3, 0 },
//				{ 4, 0, 0, 0, 0, 0, 9, 0, 2 }, { 0, 6, 0, 9, 0, 4, 0, 0, 0 }, { 9, 0, 8, 0, 6, 5, 0, 0, 4 },
//
//		};
        int[][] check4 = {{0, 0, 7, 0, 1, 0, 0, 0, 8}, {0, 0, 0, 6, 8, 0, 3, 0, 2}, {0, 0, 0, 2, 0, 4, 0, 9, 7},
                {0, 3, 2, 4, 7, 9, 6, 8, 5}, {0, 0, 0, 1, 6, 0, 0, 0, 4}, {0, 6, 0, 0, 0, 0, 0, 1, 9},
                {0, 7, 0, 0, 4, 0, 0, 0, 0}, {3, 0, 9, 0, 2, 0, 8, 5, 1}, {0, 5, 6, 8, 0, 1, 0, 7, 0}};
        Population po = new Population(check4);
        ArrayList<Individual> caThe = po.listIndividual;
        Individual cathe1 = caThe.get(0);
        List<Gene> gen = cathe1.getListGen();
        System.out.println(Arrays.toString(gen.get(0).getGen()));

//        po.individualFitness().print();
//        System.out.println("--------");
//        po.finalzz().print();
		po.mother();
//		po.father();

//		po.createNewIndividual2(po.listFather.get(0), po.listMother.get(0)).print();

    }

}
