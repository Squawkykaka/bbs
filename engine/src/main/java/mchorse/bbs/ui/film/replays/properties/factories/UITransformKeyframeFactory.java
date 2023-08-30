package mchorse.bbs.ui.film.replays.properties.factories;

import mchorse.bbs.ui.film.replays.properties.UIPropertyEditor;
import mchorse.bbs.ui.world.objects.objects.UIPropTransform;
import mchorse.bbs.utils.Transform;
import mchorse.bbs.utils.keyframes.generic.GenericKeyframe;

public class UITransformKeyframeFactory extends UIKeyframeFactory<Transform>
{
    private UIPropTransform transform;

    public UITransformKeyframeFactory(GenericKeyframe<Transform> keyframe, UIPropertyEditor editor)
    {
        super(keyframe, editor);

        this.transform = new UIPropTransform((t) -> keyframe.value = t);
        this.transform.verticalCompact();
        this.transform.setTransform(keyframe.value);

        this.add(this.transform);
    }
}