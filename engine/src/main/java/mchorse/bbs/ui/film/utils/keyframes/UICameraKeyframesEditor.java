package mchorse.bbs.ui.film.utils.keyframes;

import mchorse.bbs.ui.film.IUIClipsDelegate;
import mchorse.bbs.ui.film.utils.CameraAxisConverter;
import mchorse.bbs.ui.framework.elements.input.keyframes.UIKeyframes;
import mchorse.bbs.ui.framework.elements.input.keyframes.UIKeyframesEditor;

/**
 * Special subclass of graph editor for fixture editor panels to allow 
 * dirtying the camera profile.
 */
public abstract class UICameraKeyframesEditor <E extends UIKeyframes> extends UIKeyframesEditor<E>
{
    public static final CameraAxisConverter CONVERTER = new CameraAxisConverter();

    protected IUIClipsDelegate editor;

    public UICameraKeyframesEditor(IUIClipsDelegate editor)
    {
        super();

        this.editor = editor;
    }

    public void updateConverter()
    {
        this.setConverter(CONVERTER);
    }
}