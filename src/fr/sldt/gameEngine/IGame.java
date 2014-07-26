package fr.sldt.gameEngine;

import fr.sldt.gameEngine.renderengine.assetSystem.AssetType;

public interface IGame {
    public void renderGameOverlay();
    public void updateGameOverlay();
    public void initGame();
    public void exitGame();
    public String getIconPackage();
    public AssetType getAssetsFileType();
}
