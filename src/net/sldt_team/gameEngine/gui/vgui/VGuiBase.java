package net.sldt_team.gameEngine.gui.vgui;

import net.sldt_team.gameEngine.GameApplication;
import net.sldt_team.gameEngine.renderengine.FontRenderer;
import net.sldt_team.gameEngine.renderengine.RenderEngine;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

/**
 * VirtualGui Library, used by Gui system to help making good gui. Principe is to use a parenting system : positions are calculated with parenting systems.
 */
public abstract class VGuiBase {

    /**
     * X Coord of this VGui
     */
    protected int vguiX;

    /**
     * Y Coord of this VGui
     */
    protected int vguiY;

    /**
     * Width of this VGui
     */
    protected int vguiW;

    /**
     * Height of this VGui
     */
    protected int vguiH;

    private int childX;
    private int childY;
    private int childW;
    private int childH;

    /**
     * Is this VGui parrented to another VGui ?
     */
    protected boolean isParented;

    private List<VGuiBase> childs;
    private List<Runnable> runList;

    /**
     * When u make a control using VGuiBase, you can set the default position/size using args x, y, w, and h
     */
    public VGuiBase(int x, int y, int w, int h){
        isParented = false;

        childs = new ArrayList<VGuiBase>();
        runList = new ArrayList<Runnable>();

        setPos(x, y);
        setSize(w, h);

        setDraggable(false);
    }

    /**
     * Adds a component to this VGuiBase
     */
    public void add(final VGuiBase component){
        if (!canHaveChilds()){
            return;
        }
        runList.add(new Runnable() {
            public void run() {
                component.isParented = true;
                component.onAdded();
                childs.add(component);
            }
        });
    }

    /**
     * Removed a component from this VGui
     */
    public void remove(final VGuiBase component){
        if (!canHaveChilds()){
            return;
        }
        runList.add(new Runnable() {
            public void run() {
                component.isParented = false;
                component.onRemoved();
                childs.remove(component);
            }
        });
    }

    /**
     * @exclude
     */
    public void doRendering(RenderEngine renderEngine, FontRenderer fontRenderer){
        if (shouldBeRendered()) {
            render(renderEngine, fontRenderer);
        }

        for (VGuiBase parent : childs){
            if (parent.shouldBeRendered()){
                parent.render(renderEngine, fontRenderer);
            }
        }
    }

    /**
     * @exclude
     */
    public void doUpdating(){
        if (shouldBeUpdated()){
            update();
        }

        if (isDraggable()){
            doMouseDraggingCheck();
        }

        for (Runnable r : runList){
            r.run();
        }

        for (VGuiBase parent : childs) {
            if (parent.shouldBeUpdated()) {
                parent.update();
            }
        }
    }

    private void doMouseDraggingCheck(){
        if (Mouse.isButtonDown(0)) {
            int mX = Mouse.getX();
            int mY = GameApplication.getScreenHeight() - Mouse.getY();

            int[] rect = computeDraggingRect();
            if (mX >= rect[0] && mX <= rect[2]){
                if (mY >= rect[1] && mY <= rect[3]){
                    int movedX = mX - (rect[2] / rect[0]);
                    int movedY = mY - (rect[3] / rect[1]);
                    onDragged(movedX, movedY);
                    setPos(movedX, movedY);
                }
            }
        }
    }

    /**
     * Returns position of this VGui component
     */
    public int[] getPos(){
        return new int[]{vguiX, vguiY};
    }

    /**
     * Sets position of this VGui (using parent calculations)
     */
    public void setPos(int x, int y){
        if (!isParented){
            vguiX = x;
            vguiY = y;
            updateChildsPositions(this);
            return;
        }
        vguiX = vguiX + x;
        vguiY = vguiY + y;
        childX = x;
        childY = y;
    }

    private void updateChildsPositions(VGuiBase child){
        for (VGuiBase c : child.childs){
            if (c.childs.size() > 0){
                updateChildsPositions(c);
            }
            c.setPos(c.childX, c.childY);
        }
    }

    /**
     * Returns size of this VGui
     */
    public int[] getSize(){
        return new int[]{vguiW, vguiH};
    }

    /**
     * Sets size of this VGui (using parent calculations)
     */
    public void setSize(int w, int h){
        if (!isParented){
            vguiW = w;
            vguiH = h;
        }
        vguiW = vguiX + w;
        vguiH = vguiY + h;
        childW = w;
        childH = h;
    }

    private int[] computeDraggingRect(){
        int[] r = getDraggingRect();
        int i = vguiX + r[0];
        int j = vguiY + r[1];
        int k = vguiX + r[2];
        int l = vguiY + r[3];

        return new int[]{i, j, k, l};
    }

    /**
     * Whenever this VGui can have some childs
     */
    public abstract boolean canHaveChilds();

    /**
     * When creating a VGui component you need to specify what to do when user will call setDraggable
     */
    public abstract void setDraggable(boolean b);

    /**
     * Whenever this VGui component can be dragged
     */
    public abstract boolean isDraggable();

    /**
     * Returns exact position/size of the rect where you start dragging this VGui
     */
    public abstract int[] getDraggingRect();

    /**
     * Called every time the user has is dragging this component
     */
    protected abstract void onDragged(int newX, int newY);

    /**
     * Renders this VGui component
     */
    protected abstract void render(RenderEngine renderEngine, FontRenderer fontRenderer);

    /**
     * Updates this VGui component (called at each game ticks)
     */
    protected abstract void update();

    /**
     * Called when this component is removed
     */
    protected abstract void onRemoved();

    /**
     * Called when this component is added
     */
    protected abstract void onAdded();

    /**
     * Whenever this component should be rendered
     */
    protected abstract boolean shouldBeRendered();

    /**
     * Whenever this component should be updated
     */
    protected abstract boolean shouldBeUpdated();
}
