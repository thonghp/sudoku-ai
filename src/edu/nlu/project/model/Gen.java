package edu.nlu.project.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Gen {

	private int[] gen;
	private boolean[] tracking;
	private Random rd;
	private ArrayList<Boolean> trackingCheck;

	public Gen(int[] gen) {
		trackingCheck = new ArrayList<Boolean>();
		this.gen = gen.clone();
		this.tracking = tracking();
		rd = new Random();
		generate();
	}

	public Gen(Gen g) {
		this.gen = new int[9];
		for (int i = 0; i < gen.length; i++) {
			gen[i] = g.getGen()[i];
		}
	}

	public boolean[] tracking() {
		boolean[] tracking = new boolean[gen.length];
		for (int i = 0; i < gen.length; i++) {
			if (gen[i] != 0) {
				tracking[i] = true;
				if (trackingCheck.size() < 9) {
//					System.out.println(trackingCheck.size());
					trackingCheck.add(true);
				}
			} else {
				if (trackingCheck.size() < 9) {
//					System.out.println(trackingCheck.size());
					trackingCheck.add(false);
				}

			}
		}
//		System.out.println(Arrays.toString(tracking));
//		System.out.println(trackingCheck);
		return tracking;

	}

	public ArrayList<Boolean> getTrackingCheck() {
		return trackingCheck;
	}

	public void setTrackingCheck(ArrayList<Boolean> trackingCheck) {
		this.trackingCheck = trackingCheck;
	}

	public void generate() {
		int value = 0;
		ArrayList<Integer> rdList = new ArrayList<Integer>();
		for (int i = 0; i < gen.length; i++) {
			if (tracking[i] == true)
				rdList.add(gen[i]);
		}
		for (int i = 0; i < gen.length; i++) {
			if (tracking[i] == false) {
				while (true) {
					value = rd.nextInt(gen.length) + 1;
					if (rdList.contains(value)) {
						continue;
					} else {
						gen[i] = value;
						rdList.add(value);
						break;
					}
				}
			}
		}
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

	public void setGen1(int valueMax, int valueMax2) {
		int temp = this.gen[valueMax];
		this.gen[valueMax] = this.gen[valueMax2];
		this.gen[valueMax2] = temp;
	}

	public static void main(String[] args) {
		int[] arr = { 0, 0, 7, 8, 0, 0, 9, 0, 0 };
		Gen gen = new Gen(arr);
		System.out.println(Arrays.toString(gen.tracking()));
		for (int i = 0; i < 3; i++) {
			System.out.println(Arrays.toString(gen.getGen()));
//			gen.setGen(8, 1, gen);
//			System.out.println(Arrays.toString(gen.getGen()));
		}
		System.out.println(gen.getTrackingCheck());

	}
}
