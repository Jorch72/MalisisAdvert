/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Ordinastie
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.malisis.advert.gui.manager;

import net.malisis.advert.advert.ClientAdvert;
import net.malisis.core.client.gui.GuiRenderer;
import net.malisis.core.client.gui.MalisisGui;
import net.malisis.core.client.gui.component.container.UIListContainer;
import net.malisis.core.client.gui.component.decoration.UILabel;
import net.minecraft.util.EnumChatFormatting;

import org.apache.commons.io.FileUtils;
import org.lwjgl.opengl.GL11;

/**
 * @author Ordinastie
 *
 */
public class AdvertList extends UIListContainer<AdvertList, ClientAdvert>
{
	private UILabel emptyLabel;

	public AdvertList(MalisisGui gui)
	{
		super(gui);
		emptyLabel = new UILabel(gui);
		emptyLabel.setParent(this);
	}

	public AdvertList(MalisisGui gui, int width, int height)
	{
		this(gui);
		setSize(width, height);
	}

	@Override
	public int getElementHeight(ClientAdvert element)
	{
		return 20;
	}

	@Override
	public void drawEmtpy(GuiRenderer renderer, int mouseX, int mouseY, float partialTick)
	{
		emptyLabel.setText(EnumChatFormatting.ITALIC
				+ (ClientAdvert.isPending() ? "malisisadvert.gui.querylist" : "malisisadvert.gui.noad"));
		emptyLabel.draw(renderer, mouseX, mouseY, partialTick);
	}

	@Override
	public void drawBackground(GuiRenderer renderer, int mouseX, int mouseY, float partialTick)
	{
		if (!current.equals(selected))
			return;

		renderer.disableTextures();

		int color = /*selected ? 0xBBBBFF :*/0xBBBBEE;
		rp.colorMultiplier.set(color);

		shape.resetState();
		shape.setSize(getWidth(), getElementHeight(current));
		renderer.drawShape(shape, rp);

		renderer.next(GL11.GL_LINE_LOOP);
		GL11.glLineWidth(2);

		shape.resetState();
		shape.setSize(getWidth(), getElementHeight(current));
		rp.colorMultiplier.set(0x000000);
		renderer.drawShape(shape, rp);

		renderer.next(GL11.GL_QUADS);
		renderer.enableTextures();
	}

	@Override
	public void drawForeground(GuiRenderer renderer, int mouseX, int mouseY, float partialTick)
	{
		boolean isHovered = current.equals(hovered);

		int x = 2;
		renderer.drawText(current.getName(), x, 2, isHovered ? 0xFFFF99 : 0xFFFFFF, true);
		x += GuiRenderer.getStringWidth(current.getName()) + 6;
		renderer.setFontScale(2F / 3F);

		x = Math.max(70, x);
		String dim = current.getWidth() + "x" + current.getHeight();
		renderer.drawText(dim, x, 5, isHovered ? 0x666666 : 0x444444, false);
		x += GuiRenderer.getStringWidth(dim, 2F / 3F) + 3;

		String size = FileUtils.byteCountToDisplaySize(current.getSize());
		renderer.drawText("(" + size + ")", x, 5, isHovered ? 0x666666 : 0x444444, false);

		String url = GuiRenderer.clipString(current.getUrl(), getWidth() - 6, 2F / 3F, true);
		renderer.drawText(url, 2, 13, isHovered ? 0x666666 : 0x444444, false);

		renderer.setFontScale(1);
	}
}