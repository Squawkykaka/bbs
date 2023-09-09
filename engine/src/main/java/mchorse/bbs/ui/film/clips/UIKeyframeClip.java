package mchorse.bbs.ui.film.clips;

import mchorse.bbs.camera.clips.overwrite.KeyframeClip;
import mchorse.bbs.camera.data.Position;
import mchorse.bbs.l10n.keys.IKey;
import mchorse.bbs.ui.UIKeys;
import mchorse.bbs.ui.film.IUIClipsDelegate;
import mchorse.bbs.ui.film.utils.keyframes.UICameraDopeSheetEditor;
import mchorse.bbs.ui.film.utils.keyframes.UICameraGraphEditor;
import mchorse.bbs.ui.film.utils.keyframes.UICameraKeyframesEditor;
import mchorse.bbs.ui.framework.UIContext;
import mchorse.bbs.ui.framework.elements.buttons.UIButton;
import mchorse.bbs.ui.utils.UI;
import mchorse.bbs.utils.keyframes.KeyframeChannel;

public class UIKeyframeClip extends UIClip<KeyframeClip>
{
    public UIButton all;
    public UIButton x;
    public UIButton y;
    public UIButton z;
    public UIButton yaw;
    public UIButton pitch;
    public UIButton roll;
    public UIButton fov;

    public UICameraGraphEditor graph;
    public UICameraDopeSheetEditor dope;

    private IKey title = IKey.EMPTY;
    private UICameraKeyframesEditor current;

    public UIKeyframeClip(KeyframeClip clip, IUIClipsDelegate editor)
    {
        super(clip, editor);
    }

    @Override
    protected void registerUI()
    {
        super.registerUI();

        this.graph = new UICameraGraphEditor(this.editor);
        this.dope = new UICameraDopeSheetEditor(this.editor);

        this.all = new UIButton(UIKeys.CAMERA_PANELS_ALL, (b) -> this.selectChannel(null, 0));
        this.x = new UIButton(UIKeys.X, (b) -> this.selectChannel(this.clip.x, 1));
        this.y = new UIButton(UIKeys.Y, (b) -> this.selectChannel(this.clip.y, 2));
        this.z = new UIButton(UIKeys.Z, (b) -> this.selectChannel(this.clip.z, 3));
        this.yaw = new UIButton(UIKeys.CAMERA_PANELS_YAW, (b) -> this.selectChannel(this.clip.yaw, 4));
        this.pitch = new UIButton(UIKeys.CAMERA_PANELS_PITCH, (b) -> this.selectChannel(this.clip.pitch, 5));
        this.roll = new UIButton(UIKeys.CAMERA_PANELS_ROLL, (b) -> this.selectChannel(this.clip.roll, 6));
        this.fov = new UIButton(UIKeys.CAMERA_PANELS_FOV, (b) -> this.selectChannel(this.clip.fov, 7));
    }

    @Override
    protected void registerPanels()
    {
        super.registerPanels();

        this.panels.add(UIClip.label(UIKeys.CAMERA_PANELS_KEYFRAMES).marginTop(12));
        this.panels.add(UI.row(this.all));
        this.panels.add(UI.row(this.x, this.y, this.z));
        this.panels.add(UI.row(this.yaw, this.pitch));
        this.panels.add(UI.row(this.roll, this.fov));
    }

    @Override
    public void updateDuration(int duration)
    {
        this.dope.updateConverter();
        this.dope.keyframes.setDuration(duration);

        this.graph.updateConverter();
        this.graph.keyframes.setDuration(duration);
    }

    @Override
    public void editClip(Position position)
    {
        long tick = this.editor.getCursor() - this.clip.tick.get();

        this.clip.x.insert(tick, position.point.x);
        this.clip.y.insert(tick, position.point.y);
        this.clip.z.insert(tick, position.point.z);
        this.clip.yaw.insert(tick, position.angle.yaw);
        this.clip.pitch.insert(tick, position.angle.pitch);
        this.clip.roll.insert(tick, position.angle.roll);
        this.clip.fov.insert(tick, position.angle.fov);
    }

    @Override
    public void fillData()
    {
        super.fillData();

        this.graph.updateConverter();
        this.graph.keyframes.setDuration(this.clip.duration.get());
        this.dope.updateConverter();
        this.dope.keyframes.setDuration(this.clip.duration.get());

        this.dope.setClip(this.clip);
    }

    public void selectChannel(KeyframeChannel channel, int id)
    {
        this.title = UICameraDopeSheetEditor.TITLES[id];
        this.dope.setVisible(id == 0);
        this.graph.setVisible(id != 0);

        this.current = id == 0 ? this.dope : this.graph;

        if (channel != null)
        {
            this.graph.setChannel(channel, UICameraDopeSheetEditor.COLORS[id - 1]);
        }

        this.editor.embedView(this.current);
        this.current.resetView();
    }

    @Override
    public void render(UIContext context)
    {
        /* Draw title of the channel */
        int x = this.area.ex() - context.font.getWidth(this.title.get()) - 10;
        int y = this.graph.area.y - context.font.getHeight() - 5;

        context.batcher.textShadow(this.title.get(), x, y);

        super.render(context);
    }
}