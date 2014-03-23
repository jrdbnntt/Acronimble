package com.jrdbnnt.acronimble;

public class GameStats {
		final int MULTIPLIER = 10;
		private int time; //seconds?
		
		private int attempts, letters;
		
		private int completedTurns;
		private int currentTurn;
		private int[] turnTimes;
		
		
		public GameStats(int time) {
			this.time = time;
			this.currentTurn = 0;
			this.completedTurns = 0;
			this.letters = 0;
		}
		
		public void newTurn(){
			this.currentTurn += 1;
		}
		
		public void completeTurn(int numLetters) {
			this.letters += numLetters;
			this.completedTurns += 1;
		}
		
		public void addAttempt() {
			this.attempts += 1;
		}
		
		public int getscore(){
			return ((int)((double) this.completedTurns 
					/ (this.attempts - this.letters + 1) * MULTIPLIER));
		}
		
		public int getLetters(){
			return this.letters;
		}
		public int getAttempts(){
			return this.attempts;
		}
		
		public double getAvgTime(){
			return this.time / this.attempts;
		}
		
		
}
