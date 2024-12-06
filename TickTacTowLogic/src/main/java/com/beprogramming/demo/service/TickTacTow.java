package com.beprogramming.demo.service;


public class TickTacTow {

    //@ axiom (\forall int i; 0 <= i < combinations.length; combinations[i].length > 1 && (\forall int j; 0 <= j < combinations[i].length; combinations[i][j] < board.length));

    public Player[] getBoard() {
        return board;
    }


    public Player[] board = new Player[9];

    public final int[][] combinations = {
            {0,1,2},
            {3,4,5},
            {6,7,8},
            {0,3,6},
            {1,4,7},
            {2,5,8},
            {0,4,8},
            {2,4,6}
    };

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player currentPlayer = Player.X;

    //@ requires 0 <=  x < board.length;
    public /*@ nullable */  Player play(int x, Player p) {
        if (board[x] == null) {
            board[x] = p;
            if (currentPlayer == Player.O)
                currentPlayer = Player.X;
            else
                currentPlayer = Player.O;
        }

        if (checkForDraw())
            return null;
        return checkForWinner();
    }


    //@ ensures \result == (\forall int i; 0 <= i < board.length; board[i] != null);
    public boolean checkForDraw() {
        for (Player player : board) {
            if (player == null)
                return false;
        }
        return true;
    }

    //@ requires board != null && combinations != null;
    //@ ensures (\result == null || (\exists int j; 0 <= j < combinations.length; \forall int i; 0 <= i < combinations[j].length; (0 <= combinations[j][i] < board.length) && board[combinations[j][i]] == \result));
    public /*@ nullable */ Player checkForWinner() {
        //@ maintaining 0 <= j <= combinations.length;
        //@ maintaining (\forall int i; 0 <= i < j; checkForWinner(combinations[i]) == null);
        for (int j = 0; j < combinations.length; j++) {

            /*@ nullable */
            Player winner = checkForWinner(combinations[j]);
            if (winner != null)
                return winner;
        }

        return null;
    }

    /*@
       @ requires pattern != null && pattern.length > 1;
       @ ensures (\result == null || (\result != null && (\forall int i; 0 <= i < pattern.length; (0 <= pattern[i] < board.length) && board[pattern[i]] == \result)));
       @ pure
       @*/
    private  /*@ nullable */ Player checkForWinner(int[] pattern) {
        /*@ nullable */ Player currentPlayer = null;

        //@ maintaining 0 <= index <= pattern.length;
        //@ maintaining (\forall int i; 0 <= i && i < index; 0 <= pattern[i] < board.length && board[pattern[i]] == currentPlayer);
        for (int index = 0; index < pattern.length; index++) {


            if (pattern[index] < 0 || pattern[index] >= board.length)
                return null;

            /*@ nullable */ Player cell = board[pattern[index]];

            if (cell == null) {
                return null;
            }

            if (currentPlayer == null)
                currentPlayer = cell;

            if (currentPlayer != cell)
                return null;
        }

        return currentPlayer;
    }

    //@ requires x >= 0;
    //@ requires y >= 0;
    //@ requires x * 3 + y < board.length;
    //@ requires x * 3 + y >= 0;
    //@ ensures \result == board[x * 3 + y];
    private Player getBoardPiece(int x, int y){
        return board[x * 3 + y];
    }

    //@ requires x >= 0 && x < board.length;
    private /*@ nullable */ Player getBoardPiece(int x){
        return board[x];

    }
}
