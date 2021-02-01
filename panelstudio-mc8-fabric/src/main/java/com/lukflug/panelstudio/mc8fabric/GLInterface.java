package com.lukflug.panelstudio.mc8fabric;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Stack;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import com.lukflug.panelstudio.Interface;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.TextureUtils;
import net.minecraft.util.Identifier;

/**
 * Implementation of Interface for OpenGL in minecraft.
 * Ported to 1.8 legacy fabric by
 * @author NirvanaNevermind
 * @author lukflug
 */
public abstract class GLInterface implements Interface {
	/**
	 * Buffer to store current modelview matrix.
	 */
	private static final FloatBuffer MODELVIEW = BufferUtils.createFloatBuffer(16);
	/**
	 * Buffer to store current projection matrix.
	 */
	private static final FloatBuffer PROJECTION = BufferUtils.createFloatBuffer(16);
	/**
	 * Buffer to store current viewport.
	 */
	private static final IntBuffer VIEWPORT = BufferUtils.createIntBuffer(16);
	/**
	 * Buffer used to calculate coordinates using gluProject.
	 */
	private static final FloatBuffer COORDS = BufferUtils.createFloatBuffer(3);
	/**
	 * Clipping rectangle stack.
	 */
	private Stack<Rectangle> clipRect=new Stack<Rectangle>();
	/**
	 * Boolean indicating whether to clip in the horizontal direction 
	 */
	protected boolean clipX;
	
	/**
	 * Constructor.
	 * @param clipX whether to clip in the horizontal direction
	 */
	public GLInterface (boolean clipX) {
		this.clipX=clipX;
	}

	@Override
	public void fillTriangle(Point pos1, Point pos2, Point pos3, Color c1, Color c2, Color c3) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(GL11.GL_TRIANGLES, VertexFormats.POSITION_COLOR);
			bufferbuilder.vertex(pos1.x,pos1.y,getZLevel()).color(c1.getRed()/255.0f,c1.getGreen()/255.0f,c1.getBlue()/255.0f,c1.getAlpha()/255.0f).next();
			bufferbuilder.vertex(pos2.x,pos2.y,getZLevel()).color(c2.getRed()/255.0f,c2.getGreen()/255.0f,c2.getBlue()/255.0f,c2.getAlpha()/255.0f).next();
			bufferbuilder.vertex(pos3.x,pos3.y,getZLevel()).color(c3.getRed()/255.0f,c3.getGreen()/255.0f,c3.getBlue()/255.0f,c3.getAlpha()/255.0f).next();
		tessellator.draw();
	}

	@Override
	public void drawLine(Point a, Point b, Color c1, Color c2) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(GL11.GL_LINES, VertexFormats.POSITION_COLOR);
			bufferbuilder.vertex(a.x,a.y,getZLevel()).color(c1.getRed()/255.0f,c1.getGreen()/255.0f,c1.getBlue()/255.0f,c1.getAlpha()/255.0f).next();
			bufferbuilder.vertex(b.x,b.y,getZLevel()).color(c2.getRed()/255.0f,c2.getGreen()/255.0f,c2.getBlue()/255.0f,c2.getAlpha()/255.0f).next();
		tessellator.draw();
	}

	@Override
	public void fillRect(Rectangle r, Color c1, Color c2, Color c3, Color c4) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR);
			bufferbuilder.vertex(r.x,r.y+r.height,getZLevel()).color(c4.getRed()/255.0f,c4.getGreen()/255.0f,c4.getBlue()/255.0f,c4.getAlpha()/255.0f).next();
			bufferbuilder.vertex(r.x+r.width,r.y+r.height,getZLevel()).color(c3.getRed()/255.0f,c3.getGreen()/255.0f,c3.getBlue()/255.0f,c3.getAlpha()/255.0f).next();
			bufferbuilder.vertex(r.x+r.width,r.y,getZLevel()).color(c2.getRed()/255.0f,c2.getGreen()/255.0f,c2.getBlue()/255.0f,c2.getAlpha()/255.0f).next();
			bufferbuilder.vertex(r.x,r.y,getZLevel()).color(c1.getRed()/255.0f,c1.getGreen()/255.0f,c1.getBlue()/255.0f,c1.getAlpha()/255.0f).next();
		tessellator.draw();
	}

	@Override
	public void drawRect(Rectangle r, Color c1, Color c2, Color c3, Color c4) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(GL11.GL_LINE_LOOP, VertexFormats.POSITION_COLOR);
			bufferbuilder.vertex(r.x,r.y+r.height,getZLevel()).color(c4.getRed()/255.0f,c4.getGreen()/255.0f,c4.getBlue()/255.0f,c4.getAlpha()/255.0f).next();
			bufferbuilder.vertex(r.x+r.width,r.y+r.height,getZLevel()).color(c3.getRed()/255.0f,c3.getGreen()/255.0f,c3.getBlue()/255.0f,c3.getAlpha()/255.0f).next();
			bufferbuilder.vertex(r.x+r.width,r.y,getZLevel()).color(c2.getRed()/255.0f,c2.getGreen()/255.0f,c2.getBlue()/255.0f,c2.getAlpha()/255.0f).next();
			bufferbuilder.vertex(r.x,r.y,getZLevel()).color(c1.getRed()/255.0f,c1.getGreen()/255.0f,c1.getBlue()/255.0f,c1.getAlpha()/255.0f).next();
		tessellator.draw();
	}
	
	@Override
	public synchronized int loadImage(String name) {
		try {
			Identifier rl = new Identifier(getResourcePrefix()+name);
			InputStream stream=MinecraftClient.getInstance().getResourceManager().getResource(rl).getInputStream();
			BufferedImage image=ImageIO.read(stream);
			int texture = GlStateManager.getTexLevelParameter();
			TextureUtils.method_4289(texture, image, false, false);
			return texture;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public void drawImage(Rectangle r, int rotation, boolean parity, int image) {
		if (image==0) return;
		int texCoords[][]={{0,1},{1,1},{1,0},{0,0}};
		for (int i=0;i<rotation%4;i++) {
			int temp1=texCoords[3][0],temp2=texCoords[3][1];
			texCoords[3][0]=texCoords[2][0];
			texCoords[3][1]=texCoords[2][1];
			texCoords[2][0]=texCoords[1][0];
			texCoords[2][1]=texCoords[1][1];
			texCoords[1][0]=texCoords[0][0];
			texCoords[1][1]=texCoords[0][1];
			texCoords[0][0]=temp1;
			texCoords[0][1]=temp2;
		}
		if (parity) {
			int temp1=texCoords[3][0],temp2=texCoords[3][1];
			texCoords[3][0]=texCoords[0][0];
			texCoords[3][1]=texCoords[0][1];
			texCoords[0][0]=temp1;
			texCoords[0][1]=temp2;
			temp1=texCoords[2][0];
			temp2=texCoords[2][1];
			texCoords[2][0]=texCoords[1][0];
			texCoords[2][1]=texCoords[1][1];
			texCoords[1][0]=temp1;
			texCoords[1][1]=temp2;
		}
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.bindTexture(image);
		GlStateManager.enableTexture();
		bufferbuilder.begin(GL11.GL_QUADS, VertexFormats.POSITION_TEXTURE);
			bufferbuilder.vertex(r.x,r.y+r.height,getZLevel()).tex2(texCoords[0][0],texCoords[0][1]).next();
			bufferbuilder.vertex(r.x+r.width,r.y+r.height,getZLevel()).tex2(texCoords[1][0],texCoords[1][1]).next();
			bufferbuilder.vertex(r.x+r.width,r.y,getZLevel()).tex2(texCoords[2][0],texCoords[2][1]).next();
			bufferbuilder.vertex(r.x,r.y,getZLevel()).tex2(texCoords[3][0],texCoords[3][1]).next();
		tessellator.draw();
		GlStateManager.disableTexture();
	}
	
	/**
	 * Utility function to set clipping rectangle by projecting the coordinates using gluProject.
	 * @param r the clipping rectangle
	 */
	protected void scissor (Rectangle r) {
		if (r==null) {
			GL11.glScissor(0,0,0,0);
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
			return;
		}
		float x1,y1,x2,y2;
		GLU.gluProject(r.x,r.y,getZLevel(),MODELVIEW,PROJECTION,VIEWPORT,COORDS);
		x1=COORDS.get(0);
		y1=COORDS.get(1);
		GLU.gluProject(r.x+r.width,r.y+r.height,getZLevel(),MODELVIEW,PROJECTION,VIEWPORT,COORDS);
		x2=COORDS.get(0);
		y2=COORDS.get(1);
		if (!clipX) {
			x1=VIEWPORT.get(0);
			x2=x1+VIEWPORT.get(2);
		}
		GL11.glScissor(Math.round(Math.min(x1,x2)),Math.round(Math.min(y1,y2)),Math.round(Math.abs(x2-x1)),Math.round(Math.abs(y2-y1)));
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
	}

	@Override
	public void window (Rectangle r) {
		if (clipRect.isEmpty()) {
			scissor(r);
			clipRect.push(r);
		} else {
			Rectangle top=clipRect.peek();
			if (top==null) {
				scissor(null);
				clipRect.push(null);
			} else {
				int x1,y1,x2,y2;
				x1=Math.max(r.x,top.x);
				y1=Math.max(r.y,top.y);
				x2=Math.min(r.x+r.width,top.x+top.width);
				y2=Math.min(r.y+r.height,top.y+top.height);
				if (x2>x1 && y2>y1) {
					Rectangle rect=new Rectangle(x1,y1,x2-x1,y2-y1);
					scissor(rect);
					clipRect.push(rect);
				} else {
					scissor(null);
					clipRect.push(null);
				}
			}
		}
	}

	@Override
	public void restore() {
		if (!clipRect.isEmpty()) {
			clipRect.pop();
			if (clipRect.isEmpty()) GL11.glDisable(GL11.GL_SCISSOR_TEST);
			else scissor(clipRect.peek());
		}
	}
	
	/**
	 * Update the matrix buffers.
	 */
	public void getMatrices() {
		GlStateManager.getFloat(GL11.GL_MODELVIEW_MATRIX,MODELVIEW);
		GlStateManager.getFloat(GL11.GL_PROJECTION_MATRIX,PROJECTION);
		GL11.glGetInteger(GL11.GL_VIEWPORT,VIEWPORT);
	}

	/**
	 * Set OpenGL to the state used by the rendering methods.
	 * Should be called before rendering.
	 */
	public static void begin() {
		GlStateManager.enableBlend();
		GlStateManager.disableTexture();
		GlStateManager.blendFuncSeparate(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA,GL11.GL_ONE,GL11.GL_ZERO);
		GlStateManager.shadeModel(GL11.GL_SMOOTH);
		GL11.glLineWidth(2);
	}

	/**
	 * Restore OpenGL to the state expected by Minecraft.
	 * Should be called after rendering.
	 */
	public static void end() {
		GlStateManager.shadeModel(GL11.GL_FLAT);
		GlStateManager.enableTexture();
		GlStateManager.disableBlend();
	}

	/**
	 * Get the z-coordinate to render everything.
	 * @return the z-level
	 */
	protected abstract float getZLevel();
	/**
	 * Get the Minecraft resource location string.
	 * @return the resource prefix
	 */
	protected abstract String getResourcePrefix();
}
