package model;

import view.*;

public class GameModel {
    private PlayerModel playerModel;
    private BulletsModel bulletsModel;
    private EnemiesModel enemiesModel;
    private MapModel mapModel;
    private GameOverMenuModel gameOverMenuModel;
    private DialogueBoxesModel dialogueBoxesModel;

    public GameModel() {
        playerModel = new PlayerModel();
        bulletsModel = new BulletsModel(playerModel);
        enemiesModel = new EnemiesModel();
        mapModel = new MapModel(playerModel, enemiesModel);
        gameOverMenuModel = new GameOverMenuModel();
        dialogueBoxesModel = new DialogueBoxesModel();
    }

    public PlayerModel getPlayerModel() {
        return playerModel;
    }

    public BulletsModel getBulletsModel() {
        return bulletsModel;
    }

    public EnemiesModel getEnemiesModel() {
        return enemiesModel;
    }

    public MapModel getMapModel() {
        return mapModel;
    }

    public GameOverMenuModel getGameOverMenuModel() {
        return gameOverMenuModel;
    }

    public DialogueBoxesModel getDialogueBoxesModel() {
        return dialogueBoxesModel;
    }
}
