package mchorse.bbs.utils.keyframes.generic.factories;

import mchorse.bbs.data.types.BaseType;
import mchorse.bbs.data.types.StringType;
import mchorse.bbs.ui.film.replays.properties.UIPropertyEditor;
import mchorse.bbs.ui.film.replays.properties.factories.UIKeyframeFactory;
import mchorse.bbs.ui.film.replays.properties.factories.UIStringKeyframeFactory;
import mchorse.bbs.utils.keyframes.generic.GenericKeyframe;

public class StringKeyframeFactory implements IGenericKeyframeFactory<String>
{
    @Override
    public String fromData(BaseType data)
    {
        return data.isString() ? data.asString() : "";
    }

    @Override
    public BaseType toData(String value)
    {
        return new StringType(value);
    }

    @Override
    public String copy(String value)
    {
        return value;
    }

    @Override
    public String create()
    {
        return null;
    }

    @Override
    public UIKeyframeFactory<String> createUI(GenericKeyframe<String> keyframe, UIPropertyEditor editor)
    {
        return new UIStringKeyframeFactory(keyframe, editor);
    }
}