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
    private GuardsmenModel guardsmenmodel;
    private ItemsModel itemsModel;

    public GameModel() {
        playerModel = new PlayerModel();
        bulletsModel = new BulletsModel(playerModel);
        enemiesModel = new EnemiesModel();
        mainMenuModel = new MainMenuModel();
        gameOverMenuModel = new GameOverMenuModel();
        dialogueBoxesModel = new DialogueBoxesModel();
        gameClearMenuModel = new GameClearMenuModel();
        guardsmenmodel = new GuardsmenModel();
        itemsModel = new ItemsModel();
        mapModel = new MapModel(playerModel, enemiesModel, guardsmenmodel, itemsModel);
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

    public GuardsmenModel getGuardsmenModel() {
        return guardsmenmodel;
    }

    public ItemsModel getItemsModel() {
        return itemsModel;
    }
}
