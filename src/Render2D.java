
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

public class Render2D extends JFrame implements GLEventListener, KeyListener {

    static float[] roomDim = new float[2];
    static float[] roomColors = new float[3];
    int texture, woodTexture;
    static String roomId;
    float xPos, yPos = 0f;
    static float ScaleVal;
    float Xmodel, Ymodel;
    static float[] legColor, topColor;

    public Render2D(RenderParams renderParams) {
        topColor = renderParams.getTopColor();
        legColor = renderParams.getLegColor();
        roomId = renderParams.getRoomId();
        roomDim = renderParams.getRoomDim();
        roomColors = renderParams.getRoomColors();

        ScaleVal = renderParams.getScaleVal();


    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        try {
            File im = new File("data/floor1.jpg");
            File imWood = new File("data/wood-txtr1.jpg");
            Texture t = TextureIO.newTexture(im, true);
            Texture tWood = TextureIO.newTexture(imWood, true);
            texture = t.getTextureObject(gl);
            woodTexture = tWood.getTextureObject(gl);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            //e.printStackTrace();
        }

        gl.glEnable(GL2.GL_TEXTURE_2D);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.8196f, 0.7411f, 0.6352f, 1.0f);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        // Draw a 2D quad


        gl.glBindTexture(GL2.GL_TEXTURE_2D, texture);
        switch (roomId) {
            case "1":
                renderRoom(drawable);
                break;
            case "2":
                renderCornerRoom(drawable);
                break;
            case "3":
                renderRoundRoom(drawable);
                break;
        }

        gl.glBindTexture(GL2.GL_TEXTURE_2D, woodTexture);
        gl.glPushMatrix();
        gl.glTranslatef(0.0f + Xmodel, Ymodel, 0f);
        String model = Product.currentId;
        switch (model) {
            case "1":
                renderTableLong(drawable);
                break;
            case "2":
                renderTableSquare(drawable);
                break;
            case "3":
                renderTableCircle(drawable);
                break;
            case "4":
                renderChairShort(drawable);
                break;
            case "5":
                renderChairDining(drawable);
                break;
            case "6":
                renderSofa(drawable);
                break;
            case "7":
                renderStool(drawable);
                break;
        }
        gl.glPopMatrix();

        gl.glBindTexture(GL2.GL_TEXTURE_2D, texture);

        gl.glFlush();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        final GL2 gl = drawable.getGL().getGL2();

        if (height <= 0) {
            height = 1;
        }

        // Set the viewport to the full size of the canvas
        gl.glViewport(0, 0, width, height);

        // Use an orthographic projection for 2D rendering
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        double aspect = (double) width / (double) height;
//        gl.glOrtho(-aspect, aspect, -1.0, 1.0, -1.0, 1.0);
//        gl.glOrtho(-aspect, aspect, -0.1, 0.1, -1.0, 1.0);
//        gl.glOrtho(-aspect * 0.2, aspect * 0.2, -0.2, 0.2, -1.0, 1.0);
        gl.glOrtho(-aspect * 0.55, aspect * 0.55, -0.55, 0.55, -1.0, 1.0);



        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void renderRoundRoom(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();

        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3f(roomColors[0], roomColors[1], roomColors[2]); // Green color
        float width = (float) roomDim[0] / 2;

        // Wall center
        gl.glVertex2f(-width, roomDim[1]); // Top left
        gl.glVertex2f(width, roomDim[1]); // Top right
        gl.glVertex2f(width, 0); // Bottom right
        gl.glVertex2f(-width, 0); // Bottom left

        // right wall
        gl.glVertex2f(width, roomDim[1]); // Top left
        gl.glVertex2f(width * 1.5f, -roomDim[1] * 0.7f + roomDim[1]); // Top right
        gl.glVertex2f(width * 1.5f, -roomDim[1] * 0.7f); // Bottom right
        gl.glVertex2f(width, 0); // Bottom left

        // left wall
        gl.glVertex2f(-width, roomDim[1]); // Top left
        gl.glVertex2f(-width * 1.5f, -roomDim[1] * 0.7f + roomDim[1]); // Top right
        gl.glVertex2f(-width * 1.5f, -roomDim[1] * 0.7f); // Bottom right
        gl.glVertex2f(-width, 0); // Bottom left

        gl.glEnd();

        //outlines
        gl.glBegin(GL2.GL_LINES);
        gl.glColor3f(0, 0, 0);
        gl.glVertex2f(-width, roomDim[1]); // Top left
        gl.glVertex2f(-width, 0f); // Top right

        gl.glVertex2f(width, roomDim[1]); // Bottom right
        gl.glVertex2f(width, 0); // Bottom left
        gl.glColor3f(1, 1, 1);
        gl.glEnd();


        gl.glPushMatrix();
        gl.glColor3f(1f, 1f, 1f); // White color
        gl.glTranslatef(0.0f, 0f, 0f);
        int numSegments = 32;
        double radius = 0.1;
        gl.glScalef(5f, 1.1f, 0f);
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glTexCoord2f(0.5f, 0.5f); // Texture coordinates for center
        gl.glVertex2f(0.0f, 0.0f); // Center point
        for (int i = 0; i <= numSegments; i++) {
            double theta = 2.0f * Math.PI * i / numSegments;
            double x = radius * Math.cos(theta);
            double y = radius * Math.sin(theta);
            gl.glTexCoord2f((float) (x + 0.5), (float) (y + 0.5));
            gl.glVertex2f((float) x, (float) y);
        }
        gl.glEnd();
        gl.glPopMatrix();

        // Draw the floor with texture
        gl.glPushMatrix();
        gl.glColor3f(1f, 1f, 1f);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, texture);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(width, 0); // Bottom right
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(-width, 0); // Bottom left
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(-width * 1.5f, -roomDim[1] * 0.7f); // Bottom left
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(width * 1.5f, -roomDim[1] * 0.7f); // Bottom left
        gl.glEnd();
        gl.glPopMatrix();
    }

    public void renderRoom(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3f(roomColors[0], roomColors[1], roomColors[2]); // Green color
        float width = (float) roomDim[0] / 2;

        // Wall center
        gl.glVertex2f(-width, roomDim[1]); // Top left
        gl.glVertex2f(width, roomDim[1]); // Top right
        gl.glVertex2f(width, 0); // Bottom right
        gl.glVertex2f(-width, 0); // Bottom left

        // right wall
        gl.glVertex2f(width, roomDim[1]); // Top left
        gl.glVertex2f(width * 1.5f, -roomDim[1] * 0.7f + roomDim[1]); // Top right
        gl.glVertex2f(width * 1.5f, -roomDim[1] * 0.7f); // Bottom right
        gl.glVertex2f(width, 0); // Bottom left

        // left wall
        gl.glVertex2f(-width, roomDim[1]); // Top left
        gl.glVertex2f(-width * 1.5f, -roomDim[1] * 0.7f + roomDim[1]); // Top right
        gl.glVertex2f(-width * 1.5f, -roomDim[1] * 0.7f); // Bottom right
        gl.glVertex2f(-width, 0); // Bottom left

        gl.glEnd();

        //outlines
        gl.glBegin(GL2.GL_LINES);
        gl.glColor3f(0, 0, 0);
        gl.glVertex2f(-width, roomDim[1]); // Top left
        gl.glVertex2f(-width, 0f); // Top right

        gl.glVertex2f(width, roomDim[1]); // Bottom right
        gl.glVertex2f(width, 0); // Bottom left
        gl.glColor3f(1, 1, 1);
        gl.glEnd();


        // Draw the floor with texture
        gl.glPushMatrix();
        gl.glColor3f(1f, 1f, 1f);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, texture);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(width, 0); // Bottom right
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(-width, 0); // Bottom left
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(-width * 1.5f, -roomDim[1] * 0.7f); // Bottom left
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(width * 1.5f, -roomDim[1] * 0.7f); // Bottom left
        gl.glEnd();
        gl.glPopMatrix();
    }

    public void renderCornerRoom(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3f(roomColors[0], roomColors[1], roomColors[2]);
        float x = roomDim[0];
        float y = roomDim[1];

        // Wall right
        gl.glVertex2f(0, x / 2 + y); // Top left
        gl.glVertex2f(x * 0.86f, y); // Top right
        gl.glVertex2f(x * 0.86f, 0); // Bottom right
        gl.glVertex2f(0, x / 2); // Bottom left

        // wall left
        gl.glVertex2f(0, x / 2 + y); // Top left
        gl.glVertex2f(-x * 0.86f, y); // Top right
        gl.glVertex2f(-x * 0.86f, 0); // Bottom right
        gl.glVertex2f(0, x / 2); // Bottom left

        gl.glEnd();

//        //outlines
//        gl.glBegin(GL2.GL_LINES);
//        gl.glColor3f(0,0,0);
//        gl.glVertex2f(-width, roomDim[1]); // Top left
//        gl.glVertex2f(-width, 0f); // Top right
//
//        gl.glVertex2f(width, roomDim[1]); // Bottom right
//        gl.glVertex2f(width, 0); // Bottom left
//        gl.glColor3f(1,1,1);
//        gl.glEnd();

        // Draw the floor with texture
        gl.glPushMatrix();
        gl.glColor3f(1f, 1f, 1f);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, texture);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(0, x / 2); // top
        gl.glTexCoord2f(1f, 1.0f);
        gl.glVertex2f(x * 0.86f, 0); // right
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(0, -x / 2); // Bottom
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(-x * 0.86f, 0f); //left
        gl.glEnd();
        gl.glPopMatrix();
    }

    public void renderStool(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glScalef(0.3f * ScaleVal, 0.3f * ScaleVal, 0f);
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0f, 0f);
        gl.glScalef(4f, 1f, 0f);

        // Bind the wood texture for the table-top circle
        gl.glBindTexture(GL2.GL_TEXTURE_2D, woodTexture);

        gl.glPushMatrix();
        gl.glScalef(0.25f, 2f, 0f);
        gl.glScalef(0.1f, 0.2f, 0f);
        gl.glTranslatef(0.4f + xPos, 0f + yPos, 0f);

        // Bind the wood texture again for the legs
        gl.glBindTexture(GL2.GL_TEXTURE_2D, woodTexture);

        // Draw the legs with texture
        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3f(1f, 1f, 1f);

        gl.glColor3f(legColor[0], legColor[1], legColor[2]); // White color

        // top right leg
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(1.5f, 0.3f); //top left
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(2f, 0.3f); //top right
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(2f, -1.2f); //bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(1.5f, -1.2f); //bottom left
        gl.glEnd();

        // top left leg
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(-2.0f, 0.3f); // Top left
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(-1.5f, 0.3f); // Top right
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(-1.5f, -1.2f); // Bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(-2.0f, -1.2f); // Bottom left
        gl.glEnd();

        // bottom right leg
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(1.0f, 0f); // Top left
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(0.5f, 0f); // Top right
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(0.5f, -1.4f); // Bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(1.0f, -1.4f); // Bottom left
        gl.glEnd();

        // bottom left leg
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(-3.0f, 0f); // Top left
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(-2.5f, 0f); // Top right
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(-2.5f, -1.4f); // Bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(-3.0f, -1.4f); // Bottom left
        gl.glEnd();
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.04f, 0f);
        gl.glColor3f(topColor[0], topColor[1], topColor[2]); // White color
        int numSegments = 32;
        double radius = 0.1;
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glTexCoord2f(0.5f, 0.5f); // Texture coordinates for center
        gl.glVertex2f(0.0f, 0.0f); // Center point
        for (int i = 0; i <= numSegments; i++) {
            double theta = 2.0f * Math.PI * i / numSegments;
            double x = radius * Math.cos(theta);
            double y = radius * Math.sin(theta);
            gl.glTexCoord2f((float) (x + 0.5), (float) (y + 0.5)); // Offset texture coordinates by 0.5 for normalization
            gl.glVertex2f((float) x, (float) y);
        }
        gl.glEnd();
        gl.glPopMatrix();

        gl.glPopMatrix();
    }

    public void renderTableCircle(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glScalef(1.0f * ScaleVal, 1.0f * ScaleVal, 0f);
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0f, 0f);
        gl.glScalef(4f, 1f, 0f);

        // Bind the wood texture for the table-top circle
        gl.glBindTexture(GL2.GL_TEXTURE_2D, woodTexture);
        gl.glPushMatrix();

        gl.glScalef(0.25f, 1f, 0f);
        gl.glScalef(0.1f, 0.2f, 0f);
        gl.glTranslatef(0.4f + xPos, 0f + yPos, 0f);

        // Bind the wood texture again for the legs
        gl.glBindTexture(GL2.GL_TEXTURE_2D, woodTexture);

        gl.glColor3f(legColor[0], legColor[1], legColor[2]); // add color
        // Draw the legs with texture
        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3f(1f, 1f, 1f);

        // top right leg
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(1.8f, 0.3f); //top left
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(2f, 0.3f); //top right
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(2f, -0.7f); //bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(1.8f, -0.7f); //bottom left
        gl.glEnd();

        // top left leg
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(-2.0f, 0.3f); // Top left
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(-1.8f, 0.3f); // Top right
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(-1.8f, -0.7f); // Bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(-2.0f, -0.7f); // Bottom left
        gl.glEnd();

        // bottom right leg
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(1.0f, 0f); // Top left
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(0.8f, 0f); // Top right
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(0.8f, -1.4f); // Bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(1.0f, -1.4f); // Bottom left
        gl.glEnd();

        // bottom left leg
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(-3.0f, 0f); // Top left
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(-2.8f, 0f); // Top right
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(-2.8f, -1.4f); // Bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(-3.0f, -1.4f); // Bottom left
        gl.glEnd();
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.04f, 0f);
        gl.glColor3f(topColor[0], topColor[1], topColor[2]); // add color
        int numSegments = 32;
        double radius = 0.1;
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glTexCoord2f(0.5f, 0.5f); // Texture coordinates for center
        gl.glVertex2f(0.0f, 0.0f); // Center point
        for (int i = 0; i <= numSegments; i++) {
            double theta = 2.0f * Math.PI * i / numSegments;
            double x = radius * Math.cos(theta);
            double y = radius * Math.sin(theta);
            gl.glTexCoord2f((float) (x + 0.5), (float) (y + 0.5)); // Offset texture coordinates by 0.5 for normalization
            gl.glVertex2f((float) x, (float) y);
        }
        gl.glEnd();
        gl.glPopMatrix();

        gl.glPopMatrix();
    }

    public void renderTableLong(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glPushMatrix();

        gl.glScalef(0.2f * ScaleVal, 0.2f * ScaleVal, 0f);
        gl.glTranslatef(0.5f + xPos, 0f + yPos, 0f);

        gl.glColor3f(legColor[0], legColor[1], legColor[2]); // add color
        // top right leg
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(1.8f, 0.3f); //top left
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(2f, 0.3f); //top right
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(2f, -0.7f); //bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(1.8f, -0.7f); //bottom left
        gl.glEnd();

        // top left leg

        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(-2.0f, 0.3f); // Top left
        gl.glTexCoord2f(0.1f, 1.0f);
        gl.glVertex2f(-1.8f, 0.3f); // Top right
        gl.glTexCoord2f(0.1f, 0.0f);
        gl.glVertex2f(-1.8f, -0.7f); // Bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(-2.0f, -0.7f); // Bottom left
        gl.glEnd();

        // bottom right leg

        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(1.0f, 0f); // Top left
        gl.glTexCoord2f(0.1f, 1.0f);
        gl.glVertex2f(0.8f, 0f); // Top right
        gl.glTexCoord2f(0.1f, 0.0f);
        gl.glVertex2f(0.8f, -1.4f); // Bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(1.0f, -1.4f); // Bottom left
        gl.glEnd();

        // bottom left leg

        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(-3.0f, 0f); // Top left
        gl.glTexCoord2f(0.1f, 1.0f);
        gl.glVertex2f(-2.8f, 0f); // Top right
        gl.glTexCoord2f(0.1f, 0.0f);
        gl.glVertex2f(-2.8f, -1.4f); // Bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(-3.0f, -1.4f); // Bottom left
        gl.glEnd();

        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3f(topColor[0], topColor[1], topColor[2]); // add color
        // table-top
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(-2.2f, 0.3f); //top left
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(2.2f, 0.3f); //top right
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(1.2f, 0f); //bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(-3.2f, 0f); //bottom left
        gl.glEnd();
        gl.glPopMatrix();
    }

    public void renderTableSquare(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glPushMatrix();

        gl.glScalef(0.1f * ScaleVal, 0.2f * ScaleVal, 0f);
        gl.glTranslatef(0.4f + xPos, 0f + yPos, 0f);
        gl.glColor3f(legColor[0], legColor[1], legColor[2]); // add color
        // top right leg
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(1.8f, 0.3f); //top left
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(2f, 0.3f); //top right
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(2f, -0.7f); //bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(1.8f, -0.7f); //bottom left
        gl.glEnd();

        // top left leg

        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(-2.0f, 0.3f); // Top left
        gl.glTexCoord2f(0.1f, 1.0f);
        gl.glVertex2f(-1.8f, 0.3f); // Top right
        gl.glTexCoord2f(0.1f, 0.0f);
        gl.glVertex2f(-1.8f, -0.7f); // Bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(-2.0f, -0.7f); // Bottom left
        gl.glEnd();

        // bottom right leg

        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(1.0f, 0f); // Top left
        gl.glTexCoord2f(0.1f, 1.0f);
        gl.glVertex2f(0.8f, 0f); // Top right
        gl.glTexCoord2f(0.1f, 0.0f);
        gl.glVertex2f(0.8f, -1.4f); // Bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(1.0f, -1.4f); // Bottom left
        gl.glEnd();

        // bottom left leg

        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(-3.0f, 0f); // Top left
        gl.glTexCoord2f(0.1f, 1.0f);
        gl.glVertex2f(-2.8f, 0f); // Top right
        gl.glTexCoord2f(0.1f, 0.0f);
        gl.glVertex2f(-2.8f, -1.4f); // Bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(-3.0f, -1.4f); // Bottom left
        gl.glEnd();

        gl.glColor3f(topColor[0], topColor[1], topColor[2]); // add color
        gl.glBegin(GL2.GL_QUADS);

        // table-top
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(-2.2f, 0.3f); //top left
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(2.2f, 0.3f); //top right
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(1.2f, 0f); //bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(-3.2f, 0f); //bottom left

        gl.glEnd();
        gl.glPopMatrix();
    }

    public void renderChairDining(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glPushMatrix();

        gl.glScalef(0.07f * ScaleVal, 0.2f * ScaleVal, 0f);
        gl.glTranslatef(0.4f + xPos, 0f + yPos, 0f);

        gl.glColor3f(legColor[0], legColor[1], legColor[2]); // add color
        // top right leg
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(1.5f, 0.3f); //top left
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(2f, 0.3f); //top right
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(2f, -0.5f); //bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(1.5f, -0.5f); //bottom left
        gl.glEnd();

        // top left leg

        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(-2.0f, 0.3f); // Top left
        gl.glTexCoord2f(0.1f, 1.0f);
        gl.glVertex2f(-1.5f, 0.3f); // Top right
        gl.glTexCoord2f(0.1f, 0.0f);
        gl.glVertex2f(-1.5f, -0.5f); // Bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(-2.0f, -0.5f); // Bottom left
        gl.glEnd();

        // bottom right leg

        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(1.0f, 0f); // Top left
        gl.glTexCoord2f(0.1f, 1.0f);
        gl.glVertex2f(0.5f, 0f); // Top right
        gl.glTexCoord2f(0.1f, 0.0f);
        gl.glVertex2f(0.5f, -1.1f); // Bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(1.0f, -1.1f); // Bottom left
        gl.glEnd();

        // bottom left leg

        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(-3.0f, 0f); // Top left
        gl.glTexCoord2f(0.1f, 1.0f);
        gl.glVertex2f(-2.5f, 0f); // Top right
        gl.glTexCoord2f(0.1f, 0.0f);
        gl.glVertex2f(-2.5f, -1.1f); // Bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(-3.0f, -1.1f); // Bottom left
        gl.glEnd();

        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3f(topColor[0], topColor[1], topColor[2]); // add color

        // chair seat
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(-2f, 0.3f); //top left
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(2f, 0.3f); //top right
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(1f, 0f); //bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(-3f, 0f); //bottom left

        // chair back
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(-2f, 2.5f); //top left
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(2f, 2.5f); //top right
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(2f, 0.3f); //bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(-2f, 0.3f); //bottom left
        gl.glEnd();
        gl.glPopMatrix();
    }

    public void renderSofa(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glPushMatrix();

        gl.glScalef(0.21f * ScaleVal, 0.2f * ScaleVal, 0f);
        gl.glTranslatef(0.4f + xPos, 0f + yPos, 0f);

        // top right leg
        gl.glColor3f(legColor[0], legColor[1], legColor[2]); // add color
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(1.5f, 0.3f); //top left
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(2f, 0.3f); //top right
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(2f, 0f); //bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(1.5f, 0f); //bottom left
        gl.glEnd();

        // top left leg
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(-2.0f, 0.2f); // Top left
        gl.glTexCoord2f(0.1f, 1.0f);
        gl.glVertex2f(-1.5f, 0.2f); // Top right
        gl.glTexCoord2f(0.1f, 0.0f);
        gl.glVertex2f(-1.5f, 0f); // Bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(-2.0f, 0f); // Bottom left
        gl.glEnd();

        // bottom right leg

        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(1.0f, 0f); // Top left
        gl.glTexCoord2f(0.1f, 1.0f);
        gl.glVertex2f(0.5f, 0f); // Top right
        gl.glTexCoord2f(0.1f, 0.0f);
        gl.glVertex2f(0.5f, -0.4f); // Bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(1.0f, -0.4f); // Bottom left
        gl.glEnd();

        // bottom left leg

        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(-3.0f, 0f); // Top left
        gl.glTexCoord2f(0.1f, 1.0f);
        gl.glVertex2f(-2.5f, 0f); // Top right
        gl.glTexCoord2f(0.1f, 0.0f);
        gl.glVertex2f(-2.5f, -0.4f); // Bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(-3.0f, -0.4f); // Bottom left
        gl.glEnd();

        gl.glColor3f(topColor[0], topColor[1], topColor[2]); // add color
        gl.glBegin(GL2.GL_QUADS);

        // chair seat
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(-2f, 0.3f); //top left
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(2f, 0.3f); //top right
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(1f, 0f); //bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(-3f, 0f); //bottom left

        //seat cushion
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(-3f, -0.2f); //top left
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(1f, -0.2f); //top right
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(1f, 0f); //bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(-3f, 0f); //bottom left

        // chair right hand
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(1f, 0.7f); //top left
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(2f, 1f); //top right
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(2f, 0.1f); //bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(1f, -0.2f); //bottom left

        // chair left hand
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(-3f, 0.7f); //top left
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(-2f, 1f); //top right
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(-2f, 0.3f); //bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(-3f, 0f); //bottom left

        // chair back
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(-2f, 1.8f); //top left
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(2f, 1.8f); //top right
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(2f, 0.3f); //bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(-2f, 0.3f); //bottom left
        gl.glEnd();

        //outlines
        gl.glBegin(GL2.GL_LINES);
        gl.glColor3f(0.0f, 0.0f, 0.0f); // Red color
        gl.glVertex2f(1f, 0.7f); // right hand
        gl.glVertex2f(2f, 1f);

        gl.glVertex2f(1f, 0.7f);
        gl.glVertex2f(1f, -0.2f);

        gl.glVertex2f(1f, 0f); //seat
        gl.glVertex2f(-3f, 0f);
        gl.glEnd();

        gl.glFlush();

        gl.glPopMatrix();
    }

    public void renderChairShort(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glPushMatrix();

        gl.glScalef(0.07f * ScaleVal, 0.2f * ScaleVal, 0f);
        gl.glTranslatef(0.4f + xPos, 0f + yPos, 0f);
        gl.glColor3f(legColor[0], legColor[1], legColor[2]); // add color
        // top right leg
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(1.5f, 0.3f); //top left
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(2f, 0.3f); //top right
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(2f, -0.7f); //bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(1.5f, -0.7f); //bottom left
        gl.glEnd();

        // top left leg

        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(-2.0f, 0.2f); // Top left
        gl.glTexCoord2f(0.1f, 1.0f);
        gl.glVertex2f(-1.5f, 0.2f); // Top right
        gl.glTexCoord2f(0.1f, 0.0f);
        gl.glVertex2f(-1.5f, -0.7f); // Bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(-2.0f, -0.7f); // Bottom left
        gl.glEnd();

        // bottom right leg

        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(1.0f, 0f); // Top left
        gl.glTexCoord2f(0.1f, 1.0f);
        gl.glVertex2f(0.5f, 0f); // Top right
        gl.glTexCoord2f(0.1f, 0.0f);
        gl.glVertex2f(0.5f, -1.2f); // Bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(1.0f, -1.2f); // Bottom left
        gl.glEnd();

        // bottom left leg

        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(-3.0f, 0f); // Top left
        gl.glTexCoord2f(0.1f, 1.0f);
        gl.glVertex2f(-2.5f, 0f); // Top right
        gl.glTexCoord2f(0.1f, 0.0f);
        gl.glVertex2f(-2.5f, -1.2f); // Bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(-3.0f, -1.2f); // Bottom left
        gl.glEnd();

        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3f(1f, 1f, 1f);

        gl.glColor3f(topColor[0], topColor[1], topColor[2]); // add color
        // chair seat
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(-2f, 0.3f); //top left
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(2f, 0.3f); //top right
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(1f, 0f); //bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(-3f, 0f); //bottom left
        gl.glEnd();

        gl.glBegin(GL2.GL_LINES);
        gl.glVertex2f(-2f, 0.3f); //top left
        gl.glVertex2f(2f, 0.3f); //top right
        gl.glEnd();

        gl.glBegin(GL2.GL_QUADS);
        //seat cushion
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(-3f, -0.1f); //top left
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(1f, -0.1f); //top right
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(1f, 0f); //bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(-3f, 0f); //bottom left

        // chair right hand
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(1f, 0.7f); //top left
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(2f, 1f); //top right
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(2f, 0.3f); //bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(1f, 0f); //bottom left

        // chair left hand
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(-3f, 0.7f); //top left
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(-2f, 1f); //top right
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(-2f, 0.3f); //bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(-3f, 0f); //bottom left

        // chair back
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(-2f, 1.8f); //top left
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(2f, 1.8f); //top right
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(2f, 0.3f); //bottom right
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(-2f, 0.3f); //bottom left
        gl.glEnd();

        //outlines
        gl.glBegin(GL2.GL_LINES);
        gl.glColor3f(0.0f, 0.0f, 0.0f); // Red color
        gl.glVertex2f(1f, 0.7f); // right hand
        gl.glVertex2f(2f, 1f);

        gl.glVertex2f(1f, 0.7f);
        gl.glVertex2f(1f, 0f);

        gl.glVertex2f(-2f, 1f);
        gl.glVertex2f(-2f, 0.3f);

        gl.glVertex2f(-2f, 0.3f);
        gl.glVertex2f(-3f, 0f);

        gl.glEnd();
        gl.glFlush();
        gl.glPopMatrix();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                Xmodel -= 0.05f;
                break;
            case KeyEvent.VK_RIGHT:
                Xmodel += 0.05f;
                break;
            case KeyEvent.VK_DOWN:
                Ymodel -= 0.05f;
                break;
            case KeyEvent.VK_UP:
                Ymodel += 0.05f;
                break;
            case KeyEvent.VK_X:
                ScaleVal += 0.05f;
                break;
            case KeyEvent.VK_Z:
                ScaleVal -= 0.05f;
                break;

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
