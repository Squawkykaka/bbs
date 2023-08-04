package mchorse.bbs.utils;

import mchorse.bbs.animation.IPuppet;
import mchorse.bbs.cubic.data.model.Model;
import mchorse.bbs.cubic.data.model.ModelGroup;
import mchorse.bbs.data.IMapSerializable;
import mchorse.bbs.data.types.MapType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Pose implements IMapSerializable
{
    private static Set<String> keys = new HashSet<String>();

    public boolean staticPose;

    public final Map<String, Transform> transforms = new HashMap<String, Transform>();

    public static void getAvailableKeys(String prefix, Set<String> keys)
    {
        keys.add(IPuppet.combinePaths(prefix, "x"));
        keys.add(IPuppet.combinePaths(prefix, "y"));
        keys.add(IPuppet.combinePaths(prefix, "z"));
        keys.add(IPuppet.combinePaths(prefix, "sx"));
        keys.add(IPuppet.combinePaths(prefix, "sy"));
        keys.add(IPuppet.combinePaths(prefix, "sz"));
        keys.add(IPuppet.combinePaths(prefix, "rx"));
        keys.add(IPuppet.combinePaths(prefix, "ry"));
        keys.add(IPuppet.combinePaths(prefix, "rz"));
    }

    public Transform get(String name)
    {
        Transform transform = this.transforms.get(name);

        if (transform == null)
        {
            transform = new Transform();

            this.transforms.put(name, transform);
        }

        return transform;
    }

    public void apply(Model model)
    {
        if (this.transforms.isEmpty())
        {
            return;
        }

        for (Map.Entry<String, Transform> entry : this.transforms.entrySet())
        {
            Transform transform = entry.getValue();
            ModelGroup group = model.getGroup(entry.getKey());

            if (this.staticPose)
            {
                group.current.translate.set(group.initial.translate);
                group.current.scale.set(group.initial.scale);
                group.current.rotate.set(group.initial.rotate);
            }

            if (group != null)
            {
                group.current.translate.add(transform.translate);
                group.current.scale.add(transform.scale).sub(1, 1, 1);
                group.current.rotate.add(
                    (float) Math.toDegrees(transform.rotate.x),
                    (float) Math.toDegrees(transform.rotate.y),
                    (float) Math.toDegrees(transform.rotate.z)
                );
            }
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        if (super.equals(obj))
        {
            return true;
        }

        if (obj instanceof Pose)
        {
            Pose pose = (Pose) obj;

            keys.clear();
            keys.addAll(this.transforms.keySet());
            keys.addAll(pose.transforms.keySet());

            for (String key : keys)
            {
                Transform a = this.transforms.get(key);
                Transform b = pose.transforms.get(key);

                if (a != null && b != null && !a.equals(b)) return false;
                if (a == null && !b.isDefault()) return false;
                if (b == null && !a.isDefault()) return false;
            }

            return this.staticPose == pose.staticPose;
        }

        return false;
    }

    public void copy(Pose pose)
    {
        this.staticPose = pose.staticPose;

        this.transforms.clear();

        if (pose.transforms.isEmpty())
        {
            return;
        }

        for (Map.Entry<String, Transform> entry : pose.transforms.entrySet())
        {
            if (!entry.getValue().isDefault())
            {
                this.transforms.put(entry.getKey(), entry.getValue().copy());
            }
        }
    }

    @Override
    public void toData(MapType data)
    {
        data.putBool("static", this.staticPose);

        if (this.transforms.isEmpty())
        {
            return;
        }

        MapType pose = new MapType();

        for (Map.Entry<String, Transform> entry : this.transforms.entrySet())
        {
            if (!entry.getValue().isDefault())
            {
                pose.put(entry.getKey(), entry.getValue().toData());
            }
        }

        data.put("pose", pose);
    }

    @Override
    public void fromData(MapType data)
    {
        this.staticPose = data.getBool("static");
        this.transforms.clear();

        if (data.has("pose"))
        {
            MapType pose = data.getMap("pose");

            for (String key : pose.keys())
            {
                Transform transform = new Transform();

                transform.fromData(pose.getMap(key));

                if (!transform.isDefault())
                {
                    this.transforms.put(key, transform);
                }
            }
        }
    }

    public boolean isEmpty()
    {
        return this.transforms.isEmpty();
    }
}