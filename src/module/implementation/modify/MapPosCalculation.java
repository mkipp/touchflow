package module.implementation.modify;

import java.awt.Point;

import util.AdvaMath;



public abstract class MapPosCalculation {

	protected Point[] pointPositions;
	
	public int getNumOfPoints() {
		return pointPositions.length;
	}

	public Point[] getPointPositions() {
		return pointPositions;
	}
	
	public abstract boolean isInside (Point p);
	
	public abstract double[] getDistances(Point p);
	
	
	protected double[] getDists(Point act) {
		double[] dists;
		
		dists=new double[pointPositions.length];
		
		for (int i=0; i< dists.length;i++){
			dists[i]=AdvaMath.getDist(act, pointPositions[i]);
		}
		
		return dists;
	}
	
	protected void eradicateBads(double[] dists, int numOfBests) {
		int[] beste=new int[numOfBests];
		
		//init array: fill everything with -1
		for (int i=0; i< beste.length;i++){
			beste[i]=-1;
		}
		
		for (int i=0; i< dists.length;i++){
			checkZahl(beste, dists, i);
		}
		
		
		
		//write best indices to return
		for (int i=0; i< dists.length;i++){
			if (!arrayContains(beste, i)) dists[i]=0;
		}
	}

	private boolean arrayContains(int[] arr, int zahl){
		for (int i:arr){
			if (i==zahl) return true;
		}
		return false;
	}
	
	/**
	 * prüft, ob die zahl an vals[index] besser (kleiner) als die in beste[] ist.
	 * @param beste die besten zahlen. werden so viele gesucht, wie das array groß ist
	 * @param vals eingabe-array
	 * @param index index in vals, der geprüft werden soll
	 */
	private void checkZahl(int[] beste, double[] vals, int index){
		
		//fill array
		for (int i=beste.length-1; i>=0 ;i--){
			
			if (beste[i]==-1) {
				beste[i]=index;
				return;
			}
		}
		sortArr(beste,vals);
		//drop the too large ones
		for (int i=beste.length-1; i>=0 ;i--){
			if (vals[beste[i]]>vals[index]){
				beste[i]=index;
				return;
			}
		}
	}

	private void sortArr(int[] arr, double[] vals){
		double kl=vals[arr[0]];
		if (vals[arr[1]]<kl) swap(arr,1,0);
		if (arr.length>2){
			if (vals[arr[2]]<vals[arr[0]]) swap(arr,2,0);
			if (vals[arr[2]]<vals[arr[1]]) swap(arr,2,1);
		}
	}
	private void swap(int[] arr, int ind1, int ind2){
		int buf=arr[ind1];
		arr[ind1]=arr[ind2];
		arr[ind2]=buf;
	}

}
