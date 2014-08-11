package net.sldt_team.gameEngine;

import net.sldt_team.gameEngine.renderengine.assetSystem.AssetType;

public interface IGame {
    public void renderGameOverlay();
    public void updateGameOverlay();
    public void initGame();
    public void exitGame();
    public String getIconPackage();
    public AssetType getAssetsFileType();
}
