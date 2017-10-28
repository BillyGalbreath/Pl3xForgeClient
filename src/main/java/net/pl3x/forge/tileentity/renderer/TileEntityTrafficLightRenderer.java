package net.pl3x.forge.tileentity.renderer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.pl3x.forge.tileentity.TileEntityTrafficLight;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityTrafficLightRenderer extends TileEntitySpecialRenderer<TileEntityTrafficLight> {
    private static final double TWICE_PI = Math.PI * 2;
    private final static int sides = 20;
    private final static double radius = 0.08;

    public void render(TileEntityTrafficLight te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);

        GlStateManager.translate(x + 0.5, 0, z + 0.5);
        GlStateManager.rotate(te.rot, 0, 1, 0);
        GlStateManager.translate(-(x + 0.5), 0, -(z + 0.5));

        x += TileEntityTrafficLight.ColorState.x;
        y += te.colorState == null ? TileEntityTrafficLight.ColorState.RED.y : te.colorState.y;
        z += TileEntityTrafficLight.ColorState.z;

        if (te.isLightOn()) {
            drawLight(x, y, z, te.colorState);
        }

        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    private void drawLight(double x, double y, double z, TileEntityTrafficLight.ColorState colorState) {
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        setColor(colorState);
        for (int i = 0; i <= sides; i++) {
            double angle = (TWICE_PI * i / sides) + Math.toRadians(180);
            GL11.glVertex3d(x + Math.cos(angle) * radius, y + Math.sin(angle) * radius, z);
        }
        GL11.glEnd();
    }

    private void setColor(TileEntityTrafficLight.ColorState colorState) {
        if (colorState == null || colorState == TileEntityTrafficLight.ColorState.RED) {
            GL11.glColor4f(1, 0, 0, 1); // red
        } else if (colorState == TileEntityTrafficLight.ColorState.YELLOW) {
            GL11.glColor4f(1, 1, 0, 1); // yellow
        } else if (colorState == TileEntityTrafficLight.ColorState.GREEN) {
            GL11.glColor4f(0, 1, 0, 1); // green
        }
    }
}