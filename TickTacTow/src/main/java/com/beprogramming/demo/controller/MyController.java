package com.beprogramming.demo.controller;


import com.beprogramming.demo.service.Player;
import com.beprogramming.demo.service.TickTacTow;
import jakarta.faces.view.ViewScoped;
import org.hibernate.annotations.Table;
import org.springframework.stereotype.Component;

import java.io.Serializable;


@Component
@ViewScoped
public class MyController implements Serializable {


    private TickTacTow boardGame = new TickTacTow();

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    private String gameState = "Player X Turn";

    public String getBoardPiece(int index) {
        if (boardGame.getBoard()[index] == null)
            return " ";

        return boardGame.getBoard()[index].name();
    }

    public void playCell(int x) {
        if (gameState.endsWith(" wins"))
            return;

        Player winner = boardGame.play(x, boardGame.getCurrentPlayer());

        gameState = "Player " + boardGame.getCurrentPlayer().name() + "'s Turn";


        if (winner != null) {
            gameState = "Player " + winner.name() + " wins";
        } else if (boardGame.checkForDraw()){
            gameState = "Game Drawn, refresh the page to continue";
        }
    }

}
