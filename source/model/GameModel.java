package model;

import view.*;

public class GameModel {
    private PlayerModel playerModel;
    private BulletsModel bulletsModel;
    private EnemiesModel enemiesModel;
    private MapModel mapModel;
    private MainMenuModel mainMenuModel;
    private GameOverMenuModel gameOverMenuModel;
    private DialogueBoxesModel dialogueBoxesModel;
    private GameClearMenuModel gameClearMenuModel;

    public GameModel() {
        playerModel = new PlayerModel();
        bulletsModel = new BulletsModel(playerModel);
        enemiesModel = new EnemiesModel();
        mapModel = new MapModel(playerModel, enemiesModel);
        mainMenuModel = new MainMenuModel();
        gameOverMenuModel = new GameOverMenuModel();
        dialogueBoxesModel = new DialogueBoxesModel();
        gameClearMenuModel = new GameClearMenuModel();
    }

    public GameClearMenuModel getGameClearMenuModel() {
        return gameClearMenuModel;
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

    public MainMenuModel getMainMenuModel() {
        return mainMenuModel;
    }

    public GameOverMenuModel getGameOverMenuModel() {
        return gameOverMenuModel;
    }

    public DialogueBoxesModel getDialogueBoxesModel() {
        return dialogueBoxesModel;
    }
}
