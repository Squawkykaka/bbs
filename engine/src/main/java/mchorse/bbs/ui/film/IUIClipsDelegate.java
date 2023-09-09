package mchorse.bbs.ui.film;

import mchorse.bbs.camera.Camera;
import mchorse.bbs.data.types.BaseType;
import mchorse.bbs.film.Film;
import mchorse.bbs.settings.values.ValueInt;
import mchorse.bbs.settings.values.base.BaseValue;
import mchorse.bbs.ui.framework.elements.UIElement;
import mchorse.bbs.utils.clips.Clip;
import mchorse.bbs.utils.undo.IUndo;

import java.util.function.Consumer;

public interface IUIClipsDelegate
{
    public Film getFilm();

    public Camera getCamera();

    public Clip getClip();

    public void pickClip(Clip clip);

    public int getCursor();

    public void setCursor(int tick);

    public boolean isRunning();

    public void togglePlayback();

    public boolean canUseKeybinds();

    public void fillData();

    public void embedView(UIElement element);

    /* Undo/redo */

    public void markLastUndoNoMerging();

    public void updateClipProperty(ValueInt property, int value);
}