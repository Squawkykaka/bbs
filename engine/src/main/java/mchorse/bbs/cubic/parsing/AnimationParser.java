package mchorse.bbs.cubic.parsing;

import mchorse.bbs.cubic.data.animation.Animation;
import mchorse.bbs.cubic.data.animation.AnimationChannel;
import mchorse.bbs.cubic.data.animation.AnimationInterpolation;
import mchorse.bbs.cubic.data.animation.AnimationPart;
import mchorse.bbs.cubic.data.animation.AnimationVector;
import mchorse.bbs.data.types.BaseType;
import mchorse.bbs.data.types.ListType;
import mchorse.bbs.data.types.MapType;
import mchorse.bbs.data.types.StringType;
import mchorse.bbs.math.Constant;
import mchorse.bbs.math.molang.MolangParser;
import mchorse.bbs.math.molang.expressions.MolangExpression;
import mchorse.bbs.math.molang.expressions.MolangValue;

import java.util.Map;

public class AnimationParser
{
    public static Animation parse(MolangParser parser, String key, MapType data)
    {
        Animation animation = new Animation(key);

        if (data.has("duration"))
        {
            animation.setLength(data.getDouble("duration"));
        }

        if (data.has("groups"))
        {
            for (Map.Entry<String, BaseType> entry : data.getMap("groups"))
            {
                animation.parts.put(entry.getKey(), parsePart(parser, (MapType) entry.getValue()));
            }
        }

        return animation;
    }

    private static AnimationPart parsePart(MolangParser parser, MapType data)
    {
        AnimationPart part = new AnimationPart();

        if (data.has("translate")) parseChannel(parser, part.position, data.get("translate"), false);
        if (data.has("scale")) parseChannel(parser, part.scale, data.get("scale"), true);
        if (data.has("rotate")) parseChannel(parser, part.rotation, data.get("rotate"), false);

        return part;
    }

    private static void parseChannel(MolangParser parser, AnimationChannel channel, BaseType data, boolean scale)
    {
        if (BaseType.isList(data))
        {
            for (BaseType keyframe : (ListType) data)
            {
                channel.keyframes.add(parseAnimationVector(parser, keyframe, scale));
            }

            channel.sort();
        }
    }

    private static AnimationVector parseAnimationVector(MolangParser parser, BaseType data, boolean scale)
    {
        ListType values = (ListType) data;
        AnimationVector vector = new AnimationVector();

        if (values.size() >= 5)
        {
            vector.time = values.getDouble(0);
            vector.interp = AnimationInterpolation.byName(values.getString(1));
            vector.x = parseValue(parser, values.get(2), scale);
            vector.y = parseValue(parser, values.get(3), scale);
            vector.z = parseValue(parser, values.get(4), scale);
        }

        return vector;
    }

    private static MolangExpression parseValue(MolangParser parser, BaseType element, boolean scale)
    {
        if (element.isNumeric())
        {
            return new MolangValue(parser, new Constant(element.asNumeric().doubleValue()));
        }

        try
        {
            return parser.parseExpression(((StringType) element).value);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return scale ? MolangParser.ONE : MolangParser.ZERO;
    }
}