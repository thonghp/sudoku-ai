package edu.nlu.project.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Individual implements Comparable<Individual> {

	private ArrayList<Gen> listGen;

	private int[][] individual;

	public Individual(int[][] matrix) {
		this.individual = new int[matrix.length][matrix.length];
		this.listGen = new ArrayList<Gen>();
		setMatrix(matrix);
		generateIndividual();
	}

	public Individual(Individual in, int[][] matrix) {
		this.individual = new int[matrix.length][matrix.length];
		this.listGen = new ArrayList<Gen>();
		setMatrix(matrix);
		for (int i = 0; i < 9; i++) {

			Gen gen = new Gen(in.getListGen().get(i));
			listGen.add(gen);
		}

	}

	public void setMatrix(int[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				this.individual[i][j] = matrix[i][j];
			}
		}
	}

	public void generateIndividual() {
		for (int i = 0; i < individual.length; i++) {
			Gen gen = new Gen(individual[i]);
			listGen.add(gen);
		}
	}

	public void addGenToIndividual(int[] row, int index) {
		this.listGen.remove(index);
		this.listGen.add(index, new Gen(row));
	}

	/**
	 * tạo con có 9 gen ngẫu nhiên
	 */

	public Individual(ArrayList<Gen> listGen) {
		super();
		this.listGen = listGen;
	}

	public ArrayList<Gen> getListGen() {
		return listGen;
	}

	public void setListGen(ArrayList<Gen> listGen) {
		this.listGen = listGen;
	}

	public void setGenIndex(int index, Gen gen) {
		this.listGen.add(index, gen);
	}

	/**
	 * Hàm đánh giá độ tốt cá thể
	 */
	public int evaluate() {
		return evaluateCol() + evalueteRect();
	}

	/**
	 * tính độ tốt theo từng cột
	 */
	public int onlyEvalueteRow(int col) {
		int evaluate = 0;
		boolean checkCount = false;

		for (int dem = 1; dem < this.listGen.size() + 1; dem++) {
			int count = 0;
			for (int k = 0; k < this.listGen.size(); k++) {
				int valueGen = listGen.get(k).getGen()[col];
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
	 * Hàm đánh giá độ tốt cá thể theo tổng cột
	 * 
	 */
	public int evaluateCol() {
		int evaluate = 0;

		for (int index = 0; index < this.listGen.size(); index++) {
			evaluate += onlyEvalueteRow(index);
		}

		return evaluate;
	}

	/**
	 * Tính độ tốt theo ô vuông
	 * 
	 * @return int
	 */
	public int evalueteRect() {
		int result = 0;
		int checkCol = 0;
		// block
		while (checkCol <= 6) {
			int checkRow = 0;
			while (checkRow <= 6) {
				for (int num = 1; num < listGen.size() + 1; num++) {
					int countCollision = 0;
					boolean checkCount = false;
					for (int i = checkRow; i < checkRow + 3; i++) {
						for (int j = checkCol; j < checkCol + 3; j++) {
							int t = listGen.get(i).getGen()[j];
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
					checkCount = false;

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
		for (int i = 1; i < listGen.size(); i++) {
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
	 * thay đổi vị 2 gen xung đột cao
	 */
	public int evalueGetGenCol(int gen_i, int a) {
		int evaluate = 0;

		for (int k = 0; k < this.listGen.size(); k++) {
			int valueGen = listGen.get(a).getGen()[gen_i];
			if (listGen.get(k).getGen()[gen_i] == valueGen && k != a) {
				evaluate += 1;
			}

		}
		return evaluate;

	}

	/**
	 * Tính xung đột theo ô
	 * 
	 * @param gen_i
	 * @param a
	 * @return
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

				int valueGen = listGen.get(a).getGen()[gen_i];
				if (listGen.get(i).getGen()[j] == valueGen && j != gen_i) {
					evaluate += 1;

				}
			}

		}
		return evaluate;
	}

	/**
	 * tổng xung đột của gen
	 * 
	 * @param gen_i
	 * @param a
	 * @return
	 */
	public int evalueGetGen(int gen_i, int a) {
		return evalueGetGenCol(gen_i, a);

	}

	/**
	 * Tính xung đột max
	 * 
	 * @param a
	 * @return
	 */
	public int lowGetGenGood(int a, boolean[] check) {
		int low = 0;
		for (int i = 1; i < listGen.size(); i++) {
			if (evalueGetGen(i, a) > evalueGetGen(low, a) && check[i] != true) {
				low = i;
			}
		}
		return low;
	}

	public boolean validMutation(Gen gen) {
		int count = 0;
		for (int i = 0; i < 9; i++) {
			if (gen.getTracking()[i]) {
				count++;
			}
		}
		if (count > 7) {
			return false;
		}
		return true;
	}

	/**
	 * thay đổi gen
	 */
	public void setIndexGen(int[][] matrix) {
		Random rd = new Random();
		int rdRow = rd.nextInt(9);
		int rdIndex2 = rd.nextInt(9);
		Gen mutation = new Gen(matrix[rdRow]);
		int rdIndex1 = lowGetGenGood(rdRow, mutation.getTracking());
		while (!validMutation(mutation)) {
			rdRow = rd.nextInt(9);
			mutation = new Gen(matrix[rdRow]);
		}
//		System.out.println("Hang duoc dot bien: " + rdRow);

//		while (true) {
//			if (mutation.getTracking()[rdIndex1] == true)
//				rdIndex1 = rd.nextInt(9);
//			else
//				break;
//		}
		while (true) {
			if (rdIndex2 == rdIndex1 || mutation.getTracking()[rdIndex2] == true)
				rdIndex2 = rd.nextInt(9);
			else
				break;
		}

//		System.out.println(rdIndex1);
//		System.out.println(rdIndex2);
//		this.listGen.get(rdRow).setGen1(rdIndex1, rdIndex2);
//		
//		Gen gen = new Gen(this.listGen.get(rdRow));
//		addGenToIndividual(gen.getGen(), rdRow);
		this.listGen.get(rdRow).setGen1(rdIndex1, rdIndex2);
	}

	public void setIndexGenVer2(int[][] matrix) {
		Random rd = new Random();
		int rdRow = rd.nextInt(9);
		int rdIndex2 = rd.nextInt(9);
		int rdIndex1 = rd.nextInt(9);
		Gen mutation = new Gen(matrix[rdRow]);
//		int rdIndex1 = lowGetGenGood(rdRow, mutation.getTracking());
		while (!validMutation(mutation)) {
			rdRow = rd.nextInt(9);
			mutation = new Gen(matrix[rdRow]);
		}
//		System.out.println("Hang duoc dot bien: " + rdRow);

		while (true) {
			if (mutation.getTracking()[rdIndex1] == true)
				rdIndex1 = rd.nextInt(9);
			else
				break;
		}
		while (true) {
			if (rdIndex2 == rdIndex1 || mutation.getTracking()[rdIndex2] == true)
				rdIndex2 = rd.nextInt(9);
			else
				break;
		}
		this.listGen.get(rdRow).setGen1(rdIndex1, rdIndex2);
	}

	/**
	 * tính xung đột 1 hàng
	 */
	public int sumEvalueteRow(int indexGen) {
		int sum = 0;
		for (int i = 0; i < 9; i++) {
			sum += evalueGetGenCol(indexGen, i);
		}

		return sum;
	}

	/**
	 * tìm hàng có xung đột lớn nhất để đột biến
	 */
	public int maxEvalue() {
		int index = 0;
		int max = sumEvalueteRow(index);

		for (int i = 1; i < listGen.size(); i++) {

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
		for (int i = 0; i < listGen.size(); i++) {
			System.out.println(Arrays.toString(listGen.get(i).getGen()));
		}
	}

	@Override
	public int compareTo(Individual o) {
		// TODO Auto-generated method stub
		if (evaluate() == o.evaluate())
			return 0;
		else if (evaluate() > o.evaluate())
			return 1;
		else
			return -1;
	}

	public static void main(String[] args) {
		int[][] check = { { 0, 0, 7, 8, 0, 0, 9, 0, 0 }, { 0, 0, 0, 5, 0, 0, 0, 3, 1 }, { 9, 0, 0, 0, 0, 1, 0, 4, 0 },
				{ 2, 1, 0, 0, 6, 0, 7, 8, 0 }, { 0, 0, 0, 0, 0, 3, 0, 9, 0 }, { 3, 0, 9, 0, 1, 0, 2, 0, 0 },
				{ 4, 0, 0, 0, 0, 0, 0, 1, 6 }, { 0, 0, 0, 1, 0, 9, 0, 0, 8 }, { 0, 8, 0, 0, 3, 0, 0, 0, 0 }, };
		Individual n = new Individual(check);
//		for (int i = 0; i < 200; i++) {
//			n.setIndexGen();
//			n.print();
//			System.out.println(n.evaluate());
//			System.out.println("----------------------------------");
//
////		}
//		n.print();
//		n.setIndexGen(check);
//		System.out.println("--------");
		n.print();
		n.setIndexGen(check);
//		boolean[] checkkk = n.getListGen().get(2).getTracking();

	}

}
