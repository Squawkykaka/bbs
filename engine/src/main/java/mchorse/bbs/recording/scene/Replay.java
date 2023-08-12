package mchorse.bbs.recording.scene;

import mchorse.bbs.data.IMapSerializable;
import mchorse.bbs.data.types.MapType;
import mchorse.bbs.forms.FormUtils;
import mchorse.bbs.forms.forms.Form;
import mchorse.bbs.world.entities.Entity;
import mchorse.bbs.world.entities.components.FormComponent;

import java.util.Objects;

/**
 * Replay domain object
 *
 * This class is responsible for storing, and persisting to different sources
 * (to NBT and ByteBuf) its content.
 */
public class Replay implements IMapSerializable
{
    /* Meta data */
    public String id = "";

    /* Visual data */
    public Form form;
    public boolean enabled = true;

    public Replay()
    {}

    public Replay(String id)
    {
        this.id = id;
    }

    /**
     * Apply replay on an entity 
     */
    public void apply(Entity entity)
    {
        FormComponent component = entity.get(FormComponent.class);

        if (component != null && !Objects.equals(component.form, this.form))
        {
            component.setForm(FormUtils.copy(this.form));
        }
    }

    @Override
    public void toData(MapType data)
    {
        data.putString("id", this.id);

        if (this.form != null)
        {
            data.put("form", FormUtils.toData(this.form));
        }

        data.putBool("enabled", this.enabled);
    }

    @Override
    public void fromData(MapType data)
    {
        this.id = data.getString("id");

        if (data.has("form")) this.form = FormUtils.fromData(data.getMap("form"));
        if (data.has("enabled")) this.enabled = data.getBool("enabled");
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Replay)
        {
            Replay replay = (Replay) obj;

            return Objects.equals(replay.id, this.id)
                && Objects.equals(replay.form, this.form);
        }

        return super.equals(obj);
    }

    public Replay copy()
    {
        Replay replay = new Replay();

        replay.fromData(this.toData());

        return replay;
    }
}