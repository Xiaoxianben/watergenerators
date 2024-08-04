package com.xiaoxianben.watergenerators.gui.guiContainer;

import com.xiaoxianben.watergenerators.gui.container.ContainerBasic;
import com.xiaoxianben.watergenerators.math.PrivateMath;
import com.xiaoxianben.watergenerators.tileEntity.generator.TEGeneratorBase;
import com.xiaoxianben.watergenerators.util.ModInformation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class GuiGeneratorBasic extends GuiEnergyBase {

    private final TEGeneratorBase tileEntity;

    protected List<String> drawStringList = new ArrayList<>();
    protected int textStartX;
    protected int textStartY;
    protected int textWidth;
    protected int guiId = 0;


    public GuiGeneratorBasic(ContainerBasic inventorySlotsIn,
                             TEGeneratorBase tileEntity, int ID, int textStartX, int textStartY, int textWidth) {
        super(inventorySlotsIn, tileEntity, ID);
        this.tileEntity = tileEntity;

        this.textStartX = textStartX;
        this.textStartY = textStartY;
        this.textWidth = textWidth;
    }


    @Override
    public void initGui() {
        super.initGui();

        this.addButton(new Button(0, this.guiLeft + this.xSize + 1, this.guiTop + 1,
                new ResourceLocation(ModInformation.MOD_ID, "textures/items/information_finder.png"),
                16, 16)
        );

    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            guiId = 1 + (-1 * guiId); // 1 + (-1 * 1) = 0, 1 + (-1 * 0) = 1
        }
    }

    @Override
    protected void drawAllGUIText() {
        int textStartXTemp = textStartX;
        int textStartYTemp = textStartY;
        int textWidthTemp = textWidth;

        switch (guiId) {
            case 0:
                this.updateDefaultStringList();
                break;
            case 1:
                this.updateDrawStringList();
                textStartXTemp = 4;
                textStartYTemp = 4 + 8;
                textWidthTemp = 166;
                break;
        }
        int XString = this.guiLeft + textStartXTemp;
        StringBuilder s = new StringBuilder();

        for (String string : this.drawStringList) {
            s.append(string).append("\n");
        }
        s.delete(s.length() - 1, s.length());
        this.fontRenderer.drawSplitString(s.toString(), XString, this.guiTop + textStartYTemp, textWidthTemp, 0x000000);


    }

    @Override
    public java.util.List<Rectangle> getGuiExtraAreas() {
        List<Rectangle> rectangles = super.getGuiExtraAreas();

        for (GuiButton button : this.buttonList) {
            rectangles.add(new Rectangle(button.x, button.y, button.width, button.height));
        }

        return rectangles;
    }

    @Override
    protected void drawAllMouseRect(int mouseX, int mouseY) {
        if (guiId == 0) {
            this.drawDefaultMouseRect(mouseX, mouseY);
        }
    }

    @Override
    protected void drawAllGUITextures() {
        if (guiId == 0) {
            this.drawDefaultGUITextures();
        } else {
            this.mc.getTextureManager().bindTexture(new ResourceLocation(ModInformation.MOD_ID, "textures/gui/0.png"));
            this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        }
    }

    protected void drawDefaultGUITextures() {
        super.drawAllGUITextures();
    }

    protected void drawDefaultMouseRect(int mouseX, int mouseY) {
        super.drawAllMouseRect(mouseX, mouseY);
    }


    protected void updateDefaultStringList() {
        this.drawStringList.clear();
        this.drawStringList.add(I18n.format("gui.basePowerGeneration.text", PrivateMath.getRoughData(this.tileEntity.getRealPowerGeneration())));
        this.drawStringList.add(I18n.format("gui.energyIncreaseDecrease.text", PrivateMath.getRoughData(this.tileEntity.getFinallyReceiveEnergy()), PrivateMath.getRoughData(this.tileEntity.getFinallyExtractEnergy())));
    }

    protected void updateDrawStringList() {
        this.updateDefaultStringList();
    }


    @SideOnly(Side.CLIENT)
    static class Button extends GuiButton {
        private final ResourceLocation iconTexture;
        private final int iconWidth;
        private final int iconHeight;

        protected Button(int buttonId, int x, int y, ResourceLocation iconTextureIn, int iconWidth, int iconHeight) {
            super(buttonId, x, y, 22, 22, "");
            this.iconTexture = iconTextureIn;

            this.iconWidth = iconWidth;
            this.iconHeight = iconHeight;
        }

        /**
         * Draws this button to the screen.
         */
        public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if (this.visible) {
                mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/beacon.png"));
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

                int j = 0;

                if (!this.enabled) {
                    j += this.width * 2;
                } else if (this.hovered) {
                    j += this.width * 3;
                }

                this.drawTexturedModalRect(this.x, this.y, j, 219, this.width, this.height);

                mc.getTextureManager().bindTexture(this.iconTexture);

                drawModalRectWithCustomSizedTexture(
                        this.x + (this.width - this.iconWidth) / 2, this.y + (this.height - this.iconHeight) / 2, 0, 0,
                        this.iconWidth, this.iconHeight, this.iconWidth, this.iconHeight);
            }
        }
    }

}
