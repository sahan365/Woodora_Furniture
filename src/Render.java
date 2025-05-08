import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
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

public class Render implements GLEventListener, KeyListener {
    float Xmodel, Zmodel, YModel = 0;
    private GLU glu = new GLU();
    private static int texture, steelTexture;
    float lightPosX,lightPosY,lightPosZ = 0.0f;
    private float rotationX, rotationY, rotationZ;
    static GLCanvas glcanvas;
    float XposAll, YposAll;
    static float ScaleVal = 1;
    static float[] topColor,legColor, roomColor, roomSize;
    static String roomId;
    static boolean light = true;

    String id;

    public Render(RenderParams renderParams){
        topColor = renderParams.getTopColor();
        legColor = renderParams.getLegColor();
        roomId = renderParams.getRoomId();
        roomSize = renderParams.getRoomDim();
        roomColor = renderParams.getRoomColors();
        ScaleVal = renderParams.getScaleVal()*2;
    }



    @Override
    public void display(GLAutoDrawable drawable) {

        GL2 gl = drawable.getGL().getGL2();
        if(!light){
            gl.glEnable(GL2.GL_LIGHTING);
            gl.glEnable(GL2.GL_LIGHT0);
        }
        gl.glClearColor(0.8196f, 0.7411f, 0.6352f, 1.0f);


        float[] lightPos = {lightPosX, lightPosY, lightPosZ, 1.0f};
        float[] lightAmbient = {0.6f, 0.6f, 0.5f, 1.0f}; // Increase ambient light
        float[] lightDiffuse = {1.0f, 1.0f, 1.0f, 1.0f};
        float[] lightSpecular = {1.0f, 1.0f, 1.0f, 1.0f};

        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmbient, 0); // Set ambient light
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDiffuse, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, lightSpecular, 0);

        // Material properties
        float[] matAmbient = {0.7f, 0.7f, 0.7f, 1.0f}; // Increase ambient material reflectivity
        float[] matDiffuse = {0.7f, 0.7f, 0.7f, 1.0f}; // Increase diffuse material reflectivity
        float[] matSpecular = {1.0f, 1.0f, 1.0f, 1.0f};
        float matShininess = 100.0f; // Increase shininess for stronger specular highlights

        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, matAmbient, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, matDiffuse, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, matSpecular, 0);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, matShininess);


        gl.glBindTexture(GL2.GL_TEXTURE_2D, steelTexture);
        switch (roomId) {
            case "1":
                renderRoom(drawable);
                break;
            case "2":
                renderCorner(drawable);
                break;
            case "3":
                renderRoundRoom(drawable);
                break;
        }

        gl.glBindTexture(GL2.GL_TEXTURE_2D, texture);

        if(light){
            gl.glDisable(GL2.GL_LIGHTING);
        }
        switch (Product.currentId) {
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

        // Draw indicator for light
        gl.glColor3f(1f, 0f, 0.0f); // Set the color directly to red
        gl.glPushMatrix();
        gl.glTranslatef(lightPosX, lightPosY, lightPosZ);
        GLUquadric quadric = glu.gluNewQuadric();
        glu.gluSphere(quadric, 0.1, 16, 16); // Draw a small red sphere as the indicator
        gl.glPopMatrix();

        gl.glFlush();
        gl.glDisable(GL2.GL_LIGHTING);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glClearColor(0f, 0f, 0f, 0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
        gl.glEnable(GL2.GL_TEXTURE_2D);

        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);

        float[] lightPos = {lightPosX, lightPosY, lightPosZ, 1.0f};
        float[] lightAmbient = {0.6f, 0.6f, 0.5f, 1.0f}; // Increase ambient light
        float[] lightDiffuse = {1.0f, 1.0f, 1.0f, 1.0f};
        float[] lightSpecular = {1.0f, 1.0f, 1.0f, 1.0f};

        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmbient, 0); // Set ambient light
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDiffuse, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, lightSpecular, 0);

        // Material properties
        float[] matAmbient = {0.7f, 0.7f, 0.7f, 1.0f}; // Increase ambient material reflectivity
        float[] matDiffuse = {0.7f, 0.7f, 0.7f, 1.0f}; // Increase diffuse material reflectivity
        float[] matSpecular = {1.0f, 1.0f, 1.0f, 1.0f};
        float matShininess = 100.0f; // Increase shininess for stronger specular highlights

        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, matAmbient, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, matDiffuse, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, matSpecular, 0);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, matShininess);

        try{
            File im = new File("data/wood-txtr1.jpg");
            File steelIm = new File("data/floor1.jpg");
            Texture steelT = TextureIO.newTexture(steelIm, true);
            Texture t = TextureIO.newTexture(im, true);
            texture= t.getTextureObject(gl);
            steelTexture = steelT.getTextureObject(gl);
        }catch(IOException e){
            e.printStackTrace();
        }

        rotationX = 0.0f;
        rotationY = 0.0f;
        XposAll = 0.0f;
        YposAll = 0.0f;
        rotationZ = 0;
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        final GL2 gl = drawable.getGL().getGL2();

        if (height <= 0) height = 1;
        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, h, 1.0, 20.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_W:
                lightPosY += 1f; // Move light up
                break;
            case KeyEvent.VK_S:
                lightPosY -= 1f; // Move light down
                break;
            case KeyEvent.VK_A:
                lightPosX -= 1f; // Move light left
                break;
            case KeyEvent.VK_D:
                lightPosX += 1f; // Move light right
                break;
            case KeyEvent.VK_1:
                lightPosZ += 0.5f; // Move light closer
                break;
            case KeyEvent.VK_2:
                lightPosZ -= 0.5f; // Move light farther
                break;
            case KeyEvent.VK_LEFT:
                Xmodel -= 1; // Move model left
                break;

            case KeyEvent.VK_RIGHT:
                Xmodel += 1; // Move model right
                break;

            case KeyEvent.VK_UP:
                Zmodel -= 1; // Move model forward
                break;

            case KeyEvent.VK_DOWN:
                Zmodel += 1; // Move model backward
                break;

            case KeyEvent.VK_U:
                YposAll += 1; // Raise entire object
                break;

            case KeyEvent.VK_I:
                YposAll -= 1; // Lower entire object
                break;

            case KeyEvent.VK_J:
                rotationY += 1; // Rotate model clockwise
                break;

            case KeyEvent.VK_H:
                rotationY -= 1; // Rotate model counterclockwise
                break;

            case KeyEvent.VK_P:
                XposAll += 1; // Move entire object right
                break;

            case KeyEvent.VK_O:
                XposAll -= 1; // Move entire object left
                break;

            case KeyEvent.VK_K:
                YModel += 0.1f; // Move object up
                break;

            case KeyEvent.VK_L:
                YModel -= 0.1f; // Move object down
                break;

            case KeyEvent.VK_X:
                ScaleVal += 0.05f; // Increase scale
                break;

            case KeyEvent.VK_Z:
                ScaleVal -= 0.05f; // Decrease scale
                break;

            case KeyEvent.VK_SPACE:
                light = !light; // Toggle light on/off
                break;

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    public void renderStool(GLAutoDrawable drawable){
        GL2 gl = drawable.getGL().getGL2();
        gl.glPushMatrix();
        gl.glColor3f(topColor[0], topColor[1], topColor[2]);
        gl.glScalef(0.5f,0.5f,0.5f);
        gl.glRotatef(rotationX, 1.0f, 0.0f, 0.0f); // Rotate around X-axis
        gl.glRotatef(rotationY, 0.0f, 1.0f, 0.0f); // Rotate around Y-axis

        gl.glScalef(1f*ScaleVal,1f*ScaleVal,1f*ScaleVal);
        gl.glTranslatef(2,0,0);

        gl.glTranslatef(0.0f+Xmodel, -4f+YModel, 0f+Zmodel); // Move the cylinder back in the z-axis
        gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f); // Rotate around the x-axis

        GLUquadric quadric = glu.gluNewQuadric();
        glu.gluQuadricTexture(quadric, true);

        glu.gluQuadricDrawStyle(quadric, GLU.GLU_FILL);
        glu.gluQuadricNormals(quadric, GLU.GLU_SMOOTH);
        glu.gluCylinder(quadric, 1.0, 1.0, 0.15, 32, 32); // Draw a cylinder

        gl.glPushMatrix();
        gl.glRotatef(180.0f, 1.0f, 0.0f, 0.0f); // Rotate to face the bottom
        glu.gluDisk(quadric, 0.0, 1.0, 32, 1); // Draw a disk at the bottom
        gl.glPopMatrix();

        gl.glColor3f(legColor[0], legColor[1], legColor[2]);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, steelTexture); // add steel texture to the legs

        gl.glPushMatrix();
        gl.glTranslatef(-0.8f, 0f, 0.0f); // Position of the first leg
        glu.gluCylinder(quadric, 0.1, 0.1, 2.0, 32, 32); // Draw the first leg
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.4f, -0.6f, 0.0f); // Position of the second leg
        glu.gluCylinder(quadric, 0.1, 0.1, 2.0, 32, 32); // Draw the second leg
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.4f, 0.6f, 0f); // Position of the first leg
        glu.gluCylinder(quadric, 0.1, 0.1, 2.0, 32, 32); // Draw the first leg
        gl.glPopMatrix();

        glu.gluDeleteQuadric(quadric);
        gl.glFlush();
        gl.glPopMatrix();
    }
    public void renderTableCircle(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
//        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glPushMatrix();
        gl.glColor3f(topColor[0], topColor[1], topColor[2]);
        gl.glScalef(0.5f,0.5f,0.5f);
        gl.glRotatef(rotationX, 1.0f, 0.0f, 0.0f); // Rotate around X-axis
        gl.glRotatef(rotationY, 0.0f, 1.0f, 0.0f); // Rotate around Y-axis

        gl.glScalef(1f*ScaleVal,1f*ScaleVal,1f*ScaleVal);
        gl.glTranslatef(2+Xmodel,YModel,Zmodel);

        gl.glTranslatef(0.0f, -3f, 0f); // Move the cylinder back in the z-axis
        gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f); // Rotate around the x-axis


        GLUquadric quadric = glu.gluNewQuadric();
        glu.gluQuadricTexture(quadric, true);

        glu.gluQuadricDrawStyle(quadric, GLU.GLU_FILL);
        glu.gluQuadricNormals(quadric, GLU.GLU_SMOOTH);
        glu.gluCylinder(quadric, 3.0, 3.0, 0.15, 32, 32); // Draw a cylinder


        gl.glPushMatrix();
        gl.glRotatef(180.0f, 1.0f, 0.0f, 0.0f); // Rotate to face the bottom
        glu.gluDisk(quadric, 0.0, 3.0, 32, 1); // Draw a disk at the bottom
        gl.glPopMatrix();

        gl.glColor3f(legColor[0], legColor[1], legColor[2]);

        gl.glPushMatrix();
        gl.glTranslatef(-2.5f, -0.5f, 0.0f); //leg1
        glu.gluCylinder(quadric, 0.15, 0.15, 3.0, 32, 32);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(2.5f, -0.5f, 0.0f); // leg 2
        glu.gluCylinder(quadric, 0.15, 0.15, 3.0, 32, 32);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, -2.5f, 0f); // leg4
        glu.gluCylinder(quadric, 0.15, 0.15, 3.0, 32, 32);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, 2.5f, 0f); // leg5
        glu.gluCylinder(quadric, 0.15, 0.15, 3.0, 32, 32);
        gl.glPopMatrix();
        glu.gluDeleteQuadric(quadric);
        gl.glFlush();
        gl.glPopMatrix();
    }
    public void renderChairShort(GLAutoDrawable drawable){
        GL2 gl = drawable.getGL().getGL2();
        gl.glPushMatrix();
        gl.glColor3f(topColor[0], topColor[1], topColor[2]);
        gl.glScalef(0.5f,0.5f,0.5f);
        gl.glRotatef(rotationX, 1.0f, 0.0f, 0.0f); // Rotate around X-axis
        gl.glRotatef(rotationY, 0.0f, 1.0f, 0.0f); // Rotate around Y-axis

        gl.glScalef(0.6f*ScaleVal,0.6f*ScaleVal,0.6f*ScaleVal);
        gl.glTranslatef(4+Xmodel,-7f+YModel,0+Zmodel);
        // Draw the chair back
        gl.glBegin(GL2.GL_QUADS);
        // Front face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, 2f, -1.2f);  // Top Left
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, 2f, -1.2f);   // Top Right
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -1f, -1.2f);  // Bottom Right
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, -1.2f); // Bottom Left

        // Back face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, 2f, -1.5f);  // Top Left
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, 2f, -1.5f);   // Top Right
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -1f, -1.5f);  // Bottom Right
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, -1.5f); // Bottom Left

        // Top face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, 2f, -1.2f); // Front Top Left
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, 2f, -1.2f);  // Front Top Right
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, 2f, -1.5f);  // Back Top Right
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, 2f, -1.5f); // Back Top Left

        // Bottom face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.2f); // Front Bottom Left
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -1f, -1.2f);  // Front Bottom Right
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -1f, -1.5f);  // Back Bottom Right
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, -1.5f); // Back Bottom Left

        // Right face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.5f, 2f, -1.2f); // Top Front Right
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, 2f, -1.5f); // Top Back Right
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -1f, -1.5f); // Bottom Back Right
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.5f, -1f, -1.2f); // Bottom Front Right

        // Left face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, 2f, -1.2f); // Top Front Left
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, 2f, -1.5f); // Top Back Left
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, -1.5f); // Bottom Back Left
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, -1.2f); // Bottom Front Left
        gl.glEnd();


        // Draw the chair left side
        gl.glBegin(GL2.GL_QUADS);
// Front face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.35f, 0.7f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.35f, 0.7f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.35f, -1f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.35f, -1f, 1.5f);

// Back face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, 0.7f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, 0.7f, 1.5f);

// Top face
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.35f, 0.7f, -1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.35f, 0.7f, -1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.35f, 0.7f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.35f, 0.7f, 1.5f);

// Bottom face
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.35f, -1f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.35f, -1f, 1.5f);

// Right face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.35f, 0.7f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.35f, -1f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, 0.7f, -1.5f);

// Left face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.35f, 0.7f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, 0.7f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.35f, -1f, 1.5f);
        gl.glEnd();

        // Draw the chair right side
        gl.glBegin(GL2.GL_QUADS);
        // Front face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.5f, 0.7f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, 0.7f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -1f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.5f, -1f, 1.5f);

// Back face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.35f, 0.7f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.35f, -1f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.35f, -1f, 1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.35f, 0.7f, 1.5f);

// Top face
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.5f, 0.7f, -1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.5f, 0.7f, -1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, 0.7f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, 0.7f, 1.5f);

// Bottom face
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -1f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.35f, -1f, -1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.35f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -1f, 1.5f);

// Right face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, 0.7f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -1f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.35f, -1f, -1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.35f, 0.7f, -1.5f);

// Left face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.5f, 0.7f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.35f, 0.7f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.35f, -1f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.5f, -1f, 1.5f);
        gl.glEnd();



        // Draw the chair seat
        gl.glBegin(GL2.GL_QUADS);

// Base face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -1f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);

// Top face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -0.7f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -0.7f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -0.7f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -0.7f, -1.5f);

// Front face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -0.7f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -0.7f, 1.5f);

// Back face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -0.7f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -0.7f, -1.5f);

// Left face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -0.7f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -0.7f, 1.5f);

// Right face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -0.7f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.5f, -0.7f, 1.5f);

        gl.glEnd();

        gl.glColor3f(legColor[0], legColor[1], legColor[2]);
        // Draw the chair legs
        gl.glBegin(GL2.GL_QUADS);

        // Back right leg
        // First face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.5f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1f, -3f, -1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.5f, -3f, -1.2f);

        // Back face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -3f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1f, -3f, -1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1f, -1f, -1.5f);

        // Top face
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.5f, -3f, -1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.5f, -3f, -1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1f, -3f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1f, -3f, -1.2f);

        // Bottom face
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -1f, -1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1f, -1f, -1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -1f, -1.5f);

        // Right face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1f, -3f, -1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1f, -3f, -1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1f, -1f, -1.5f);

        // Left face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.5f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -3f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.5f, -3f, -1.2f);

// Back right leg
// First face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -3f, -1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -3f, -1.2f);

// Back face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -3f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -3f, -1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);

// Top face
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -3f, -1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -3f, -1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -3f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -3f, -1.2f);

// Bottom face
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -1f, -1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, -1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, -1.5f);

// Right face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -3f, -1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -3f, -1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);

// Left face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -3f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -3f, -1.2f);

// FRONT LEFT
// First face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -3f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -3f, 1.5f);

// Back face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, 1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -3f, 1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -3f, 1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.2f);

// Top face
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -3f, 1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -3f, 1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -3f, 1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -3f, 1.5f);

// Bottom face
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -1f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, 1.2f);

// Right face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -3f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -3f, 1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.2f);

// Left face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, 1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -3f, 1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -3f, 1.5f);

        //FRONT RIGHT

// First face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.0f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.0f, -3f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.5f, -3f, 1.5f);

// Back face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -1f, 1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -3f, 1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.0f, -3f, 1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.0f, -1f, 1.2f);

// Top face
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.5f, -3f, 1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.5f, -3f, 1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.0f, -3f, 1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.0f, -3f, 1.5f);

// Bottom face
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -1f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.0f, -1f, 1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.0f, -1f, 1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -1f, 1.2f);

// Right face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.0f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.0f, -3f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.0f, -3f, 1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.0f, -1f, 1.2f);

// Left face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -1f, 1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -3f, 1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.5f, -3f, 1.5f);


        gl.glEnd();
        gl.glPopMatrix();
    }
    public void renderChairDining(GLAutoDrawable drawable){
        GL2 gl = drawable.getGL().getGL2();
        gl.glPushMatrix();
        gl.glColor3f(topColor[0], topColor[1], topColor[2]);
        gl.glRotatef(rotationX, 1.0f, 0.0f, 0.0f); // Rotate around X-axis
        gl.glRotatef(rotationY, 0.0f, 1.0f, 0.0f); // Rotate around Y-axis

        gl.glScalef(0.3f*ScaleVal,0.3f*ScaleVal,0.3f*ScaleVal);
        gl.glTranslatef(2+Xmodel,-6f+YModel,0+Zmodel);

        // Draw the chair back
        gl.glBegin(GL2.GL_QUADS);
        // Front face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, 3.5f, -1.2f);  // Top Left
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, 3.5f, -1.2f);   // Top Right
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -1f, -1.2f);  // Bottom Right
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, -1.2f); // Bottom Left

        // Back face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, 3.5f, -1.5f);  // Top Left
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, 3.5f, -1.5f);   // Top Right
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -1f, -1.5f);  // Bottom Right
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, -1.5f); // Bottom Left

        // Top face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, 3.5f, -1.2f); // Front Top Left
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, 3.5f, -1.2f);  // Front Top Right
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, 2f, -1.5f);  // Back Top Right
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, 2f, -1.5f); // Back Top Left

        // Bottom face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.2f); // Front Bottom Left
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -1f, -1.2f);  // Front Bottom Right
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -1f, -1.5f);  // Back Bottom Right
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, -1.5f); // Back Bottom Left

        // Right face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.5f, 3.5f, -1.2f); // Top Front Right
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, 3.5f, -1.5f); // Top Back Right
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -1f, -1.5f); // Bottom Back Right
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.5f, -1f, -1.2f); // Bottom Front Right

        // Left face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, 3.5f, -1.2f); // Top Front Left
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, 3.5f, -1.5f); // Top Back Left
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, -1.5f); // Bottom Back Left
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, -1.2f); // Bottom Front Left
        gl.glEnd();

        // Draw the chair seat
        gl.glBegin(GL2.GL_QUADS);

// Base face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -1f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);

// Top face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -0.7f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -0.7f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -0.7f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -0.7f, -1.5f);

// Front face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -0.7f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -0.7f, 1.5f);

// Back face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -0.7f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -0.7f, -1.5f);

// Left face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -0.7f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -0.7f, 1.5f);

// Right face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -0.7f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.5f, -0.7f, 1.5f);

        gl.glEnd();

        gl.glColor3f(legColor[0], legColor[1], legColor[2]);

        // draw the chair legs
        gl.glBegin(GL2.GL_QUADS);

        // Back right leg
        // First face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.5f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1f, -4f, -1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.5f, -4f, -1.2f);

        // Back face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -4f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1f, -4f, -1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1f, -1f, -1.5f);

        // Top face
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.5f, -4f, -1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.5f, -4f, -1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1f, -4f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1f, -4f, -1.2f);

        // Bottom face
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -1f, -1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1f, -1f, -1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -1f, -1.5f);

        // Right face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1f, -4f, -1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1f, -4f, -1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1f, -1f, -1.5f);

        // Left face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.5f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -4f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.5f, -4f, -1.2f);

// Back right leg
// First face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -4f, -1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -4f, -1.2f);

// Back face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -4f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -4f, -1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);

// Top face
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -4f, -1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -4f, -1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -4f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -4f, -1.2f);

// Bottom face
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -1f, -1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, -1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, -1.5f);

// Right face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -4f, -1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -4f, -1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);

// Left face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -4f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -4f, -1.2f);

// FRONT LEFT
// First face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -4f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -4f, 1.5f);

// Back face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, 1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -4f, 1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -4f, 1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.2f);

// Top face
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -4f, 1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -4f, 1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -4f, 1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -4f, 1.5f);

// Bottom face
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -1f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, 1.2f);

// Right face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -3f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -4f, 1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.2f);

// Left face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, 1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -4f, 1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -4f, 1.5f);

        //FRONT RIGHT

// First face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.0f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.0f, -4f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.5f, -4f, 1.5f);

// Back face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -1f, 1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -4f, 1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.0f, -4f, 1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.0f, -1f, 1.2f);

// Top face
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.5f, -4f, 1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.5f, -4f, 1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.0f, -4f, 1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.0f, -4f, 1.5f);

// Bottom face
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -1f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.0f, -1f, 1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.0f, -1f, 1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -1f, 1.2f);

// Right face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.0f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.0f, -4f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.0f, -4f, 1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.0f, -4f, 1.2f);

// Left face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -1f, 1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -4f, 1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.5f, -4f, 1.5f);


        gl.glEnd();
        gl.glFlush();

    }
    public void renderSofa(GLAutoDrawable drawable){
        GL2 gl = drawable.getGL().getGL2();
        gl.glPushMatrix();
        gl.glColor3f(topColor[0], topColor[1], topColor[2]);
        gl.glScalef(0.5f,0.5f,0.5f);
        gl.glRotatef(rotationX, 1.0f, 0.0f, 0.0f); // Rotate around X-axis
        gl.glRotatef(rotationY, 0.0f, 1.0f, 0.0f); // Rotate around Y-axis

        gl.glScalef(1.5f*ScaleVal,1f*ScaleVal,1f*ScaleVal);
        gl.glTranslatef(Xmodel,-4.5f+YModel,Zmodel);

        // Draw the chair back
        gl.glBegin(GL2.GL_QUADS);
        // Front face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, 2f, -1.2f);  // Top Left
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.5f, 2f, -1.2f);   // Top Right
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.5f, -1f, -1.2f);  // Bottom Right
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, -1.2f); // Bottom Left

        // Back face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, 2f, -1.5f);  // Top Left
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.5f, 2f, -1.5f);   // Top Right
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.5f, -1f, -1.5f);  // Bottom Right
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, -1.5f); // Bottom Left

        // Top face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, 2f, -1.2f); // Front Top Left
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.5f, 2f, -1.2f);  // Front Top Right
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.5f, 2f, -1.5f);  // Back Top Right
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, 2f, -1.5f); // Back Top Left

        // Bottom face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.2f); // Front Bottom Left
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.5f, -1f, -1.2f);  // Front Bottom Right
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.5f, -1f, -1.5f);  // Back Bottom Right
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, -1.5f); // Back Bottom Left

        // Right face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(5.5f, 2f, -1.2f); // Top Front Right
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.5f, 2f, -1.5f); // Top Back Right
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.5f, -1f, -1.5f); // Bottom Back Right
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(5.5f, -1f, -1.2f); // Bottom Front Right

        // Left face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, 2f, -1.2f); // Top Front Left
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, 2f, -1.5f); // Top Back Left
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, -1.5f); // Bottom Back Left
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, -1.2f); // Bottom Front Left
        gl.glEnd();


        // Draw the chair left side
        gl.glBegin(GL2.GL_QUADS);
// Front face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.35f, 0.7f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.35f, 0.7f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.35f, -1f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.35f, -1f, 1.5f);

// Back face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, 0.7f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, 0.7f, 1.5f);

// Top face
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.35f, 0.7f, -1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.35f, 0.7f, -1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.35f, 0.7f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.35f, 0.7f, 1.5f);

// Bottom face
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.35f, -1f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.35f, -1f, 1.5f);

// Right face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.35f, 0.7f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.35f, -1f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, 0.7f, -1.5f);

// Left face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.35f, 0.7f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, 0.7f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.35f, -1f, 1.5f);
        gl.glEnd();

        // Draw the chair right side
        gl.glBegin(GL2.GL_QUADS);
        // Front face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(5.5f, 0.7f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.5f, 0.7f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.5f, -1f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(5.5f, -1f, 1.5f);

// Back face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.35f, 0.7f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.35f, -1f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(5.35f, -1f, 1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(5.35f, 0.7f, 1.5f);

// Top face
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(5.5f, 0.7f, -1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(5.5f, 0.7f, -1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.5f, 0.7f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.5f, 0.7f, 1.5f);

// Bottom face
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.5f, -1f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(5.35f, -1f, -1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(5.35f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.5f, -1f, 1.5f);

// Right face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.5f, 0.7f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.5f, -1f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(5.35f, -1f, -1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(5.35f, 0.7f, -1.5f);

// Left face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(5.5f, 0.7f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.35f, 0.7f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.35f, -1f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(5.5f, -1f, 1.5f);
        gl.glEnd();



        // Draw the chair seat
        gl.glBegin(GL2.GL_QUADS);

// Base face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.5f, -1f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);

// Top face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -0.7f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.5f, -0.7f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.5f, -0.7f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -0.7f, -1.5f);

// Front face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.5f, -0.7f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -0.7f, 1.5f);

// Back face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.5f, -0.7f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -0.7f, -1.5f);

// Left face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -0.7f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -0.7f, 1.5f);

// Right face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(5.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.5f, -0.7f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(5.5f, -0.7f, 1.5f);

        gl.glEnd();
        gl.glColor3f(legColor[0], legColor[1], legColor[2]);

        // Draw the chair legs
        gl.glBegin(GL2.GL_QUADS);

        // Back right leg
        // First face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(5.5f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5f, -1.5f, -1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(5.5f, -1.5f, -1.2f);

        // Back face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.5f, -1.5f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(5f, -1.5f, -1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(5f, -1f, -1.5f);

        // Top face
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(5.5f, -1.5f, -1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(5.5f, -1.5f, -1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5f, -1.5f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5f, -1.5f, -1.2f);

        // Bottom face
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.5f, -1f, -1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(5f, -1f, -1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.5f, -1f, -1.5f);

        // Right face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5f, -1.5f, -1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(5f, -1.5f, -1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(5f, -1f, -1.5f);

        // Left face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(5.5f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.5f, -1.5f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(5.5f, -1.5f, -1.2f);

// Back left leg
// First face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -1.5f, -1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -1.5f, -1.2f);

// Back face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -1.5f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1.5f, -1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);

// Top face
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -1.5f, -1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1.5f, -1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -1.5f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -1.5f, -1.2f);

// Bottom face
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -1f, -1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, -1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, -1.5f);

// Right face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -1.5f, -1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1.5f, -1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);

// Left face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -1.5f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -1.5f, -1.2f);

// FRONT LEFT
// First face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -1.5f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -1.5f, 1.5f);

// Back face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, 1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -1.5f, 1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1.5f, 1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.2f);

// Top face
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -1.5f, 1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1.5f, 1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -1.5f, 1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -1.5f, 1.5f);

// Bottom face
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -1f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, 1.2f);

// Right face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -1.5f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1.5f, 1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1.5f, 1.2f);

// Left face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, 1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -1.5f, 1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -1.5f, 1.5f);

        //FRONT RIGHT ----------------

// First face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(5.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.0f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.0f, -1.5f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(5.5f, -1.5f, 1.5f);

// Back face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.5f, -1f, 1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.5f, -1.5f, 1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(5.0f, -1.5f, 1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(5.0f, -1f, 1.2f);

// Top face
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(5.5f, -1.5f, 1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(5.5f, -1.5f, 1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.0f, -1.5f, 1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.0f, -1.5f, 1.5f);

// Bottom face
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.5f, -1f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(5.0f, -1f, 1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(5.0f, -1f, 1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.5f, -1f, 1.2f);

// Right face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.0f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.0f, -1.5f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(5.0f, -1.5f, 1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(5.0f, -1f, 1.2f);

// Left face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(5.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.5f, -1f, 1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.5f, -1.5f, 1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(5.5f, -1.5f, 1.5f);


        gl.glEnd();
        gl.glFlush();
    }
    public void renderTableLong(GLAutoDrawable drawable){
        GL2 gl = drawable.getGL().getGL2();
        gl.glPushMatrix();
        gl.glColor3f(topColor[0], topColor[1], topColor[2]);
        gl.glScalef(0.5f,0.5f,0.5f);
        gl.glRotatef(rotationX, 1.0f, 0.0f, 0.0f); // Rotate around X-axis
        gl.glRotatef(rotationY, 0.0f, 1.0f, 0.0f); // Rotate around Y-axis

        gl.glScalef(1f*ScaleVal,0.8f*ScaleVal,1f*ScaleVal);
        gl.glTranslatef(Xmodel,-1.5f+YModel,Zmodel);

        // Draw the table-top
        gl.glBegin(GL2.GL_QUADS);

// Base face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.5f, -1f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);

// Top face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -0.7f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.5f, -0.7f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.5f, -0.7f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -0.7f, -1.5f);

// Front face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.5f, -0.7f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -0.7f, 1.5f);

// Back face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.5f, -0.7f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -0.7f, -1.5f);

// Left face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -0.7f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -0.7f, 1.5f);

// Right face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(5.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.5f, -0.7f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(5.5f, -0.7f, 1.5f);

        gl.glEnd();

        // Draw the chair legs
        gl.glColor3f(legColor[0], legColor[1], legColor[2]);
        gl.glBegin(GL2.GL_QUADS);

        // Back right leg
        // First face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(5.5f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5f, -6f, -1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(5.5f, -6f, -1.2f);

        // Back face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.5f, -6f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(5f, -6f, -1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(5f, -1f, -1.5f);

        // Top face
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(5.5f, -6f, -1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(5.5f, -6f, -1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5f, -6f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5f, -6f, -1.2f);

        // Bottom face
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.5f, -1f, -1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(5f, -1f, -1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.5f, -1f, -1.5f);

        // Right face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5f, -6f, -1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(5f, -6f, -1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(5f, -1f, -1.5f);

        // Left face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(5.5f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.5f, -6f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(5.5f, -6f, -1.2f);

// Back left leg
// First face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -6f, -1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -6f, -1.2f);

// Back face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -6f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -6f, -1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);

// Top face
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -6f, -1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -6f, -1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -6f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -6f, -1.2f);

// Bottom face
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -1f, -1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, -1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, -1.5f);

// Right face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -6f, -1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -6f, -1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);

// Left face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -6f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -6f, -1.2f);

// FRONT LEFT
// First face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -6f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -6f, 1.5f);

// Back face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, 1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -6f, 1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -6f, 1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.2f);

// Top face
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -6f, 1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -6f, 1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -6f, 1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -6f, 1.5f);

// Bottom face
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -1f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, 1.2f);

// Right face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -6f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -6f, 1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -6f, 1.2f);

// Left face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, 1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -6f, 1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -6f, 1.5f);

        //FRONT RIGHT ----------------

// First face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(5.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.0f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.0f, -6f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(5.5f, -6f, 1.5f);

// Back face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.5f, -1f, 1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.5f, -6f, 1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(5.0f, -6f, 1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(5.0f, -1f, 1.2f);

// Top face
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(5.5f, -6f, 1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(5.5f, -6f, 1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.0f, -6f, 1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.0f, -6f, 1.5f);

// Bottom face
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.5f, -1f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(5.0f, -1f, 1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(5.0f, -1f, 1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.5f, -1f, 1.2f);

// Right face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.0f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.0f, -6f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(5.0f, -6f, 1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(5.0f, -1f, 1.2f);

// Left face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(5.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(5.5f, -1f, 1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(5.5f, -6f, 1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(5.5f, -6f, 1.5f);
        gl.glEnd();
        gl.glPopMatrix();
    }
    public void renderTableSquare(GLAutoDrawable drawable){
        GL2 gl = drawable.getGL().getGL2();
        gl.glPushMatrix();
        gl.glColor3f(topColor[0], topColor[1], topColor[2]);
        gl.glScalef(0.5f,0.5f,0.5f);
        gl.glRotatef(rotationX, 1.0f, 0.0f, 0.0f); // Rotate around X-axis
        gl.glRotatef(rotationY, 0.0f, 1.0f, 0.0f); // Rotate around Y-axis

        gl.glScalef(1.5f*ScaleVal,1.5f*ScaleVal,1.5f*ScaleVal);
        gl.glTranslatef(2+Xmodel,-1+YModel,Zmodel);

        // Draw the chair seat
        gl.glBegin(GL2.GL_QUADS);

// Base face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -1f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);

// Top face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -0.7f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -0.7f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -0.7f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -0.7f, -1.5f);

// Front face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -0.7f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -0.7f, 1.5f);

// Back face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -0.7f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -0.7f, -1.5f);

// Left face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -0.7f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -0.7f, 1.5f);

// Right face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -0.7f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.5f, -0.7f, 1.5f);

        gl.glEnd();

        // Draw the chair legs
        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3f(legColor[0], legColor[1], legColor[2]);
        // Back right leg
        // First face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.5f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1f, -3f, -1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.5f, -3f, -1.2f);

        // Back face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -3f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1f, -3f, -1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1f, -1f, -1.5f);

        // Top face
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.5f, -3f, -1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.5f, -3f, -1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1f, -3f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1f, -3f, -1.2f);

        // Bottom face
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -1f, -1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1f, -1f, -1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -1f, -1.5f);

        // Right face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1f, -3f, -1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1f, -3f, -1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1f, -1f, -1.5f);

        // Left face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.5f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -3f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.5f, -3f, -1.2f);

// Back right leg
// First face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -3f, -1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -3f, -1.2f);

// Back face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -3f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -3f, -1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);

// Top face
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -3f, -1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -3f, -1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -3f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -3f, -1.2f);

// Bottom face
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -1f, -1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, -1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, -1.5f);

// Right face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -3f, -1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -3f, -1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, -1.5f);

// Left face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, -1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, -1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -3f, -1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -3f, -1.2f);

// FRONT LEFT
// First face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -3f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -3f, 1.5f);

// Back face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, 1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -3f, 1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -3f, 1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.2f);

// Top face
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -3f, 1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -3f, 1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -3f, 1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -3f, 1.5f);

// Bottom face
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -1f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, 1.2f);

// Right face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.5f, -3f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.5f, -3f, 1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.5f, -1f, 1.2f);

// Left face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1f, 1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -3f, 1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -3f, 1.5f);

        //FRONT RIGHT

// First face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.0f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.0f, -3f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.5f, -3f, 1.5f);

// Back face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -1f, 1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -3f, 1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.0f, -3f, 1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.0f, -1f, 1.2f);

// Top face
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.5f, -3f, 1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.5f, -3f, 1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.0f, -3f, 1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.0f, -3f, 1.5f);

// Bottom face
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -1f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.0f, -1f, 1.5f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.0f, -1f, 1.2f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -1f, 1.2f);

// Right face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.0f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.0f, -3f, 1.5f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.0f, -3f, 1.2f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.0f, -1f, 1.2f);

// Left face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.5f, -1f, 1.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.5f, -1f, 1.2f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.5f, -3f, 1.2f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.5f, -3f, 1.5f);


        gl.glEnd();
        gl.glPopMatrix();
    }

    // room renders
    public void renderRoom(GLAutoDrawable drawable){
        final GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        gl.glTranslatef(-1.5f, 1f, -13.0f);

        gl.glRotatef(XposAll, 1.0f, 0.0f, 0.0f); // Rotate around X-axis
        gl.glRotatef(YposAll, 0.0f, 1.0f, 0.0f); // Rotate around Y-axis
        gl.glRotatef(rotationZ, 0.0f, 0.0f, 1.0f); // Rotate around Y-axis


        //wall
//        gl.glDisable(GL2.GL_LIGHTING);
        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3f(roomColor[0], roomColor[1], roomColor[2]);
        float xVal = roomSize[0];
        float yVal = roomSize[1];

        gl.glVertex3f(-3f*xVal, yVal*3f, -2f);  // Top Left
        gl.glVertex3f(6f*xVal , yVal*3f, -2f);   // Top Right
        gl.glVertex3f(6f*xVal , yVal*-3f, -2f);  // Bottom Right
        gl.glVertex3f(-3f*xVal, yVal*-3f, -2f); // Bottom Left

        gl.glVertex3f(-3f*xVal, yVal*-3f, -2f); // Bottom Left
        gl.glVertex3f(-3f*xVal, yVal*-3f, 5f);  // Bottom Right
        gl.glVertex3f(-3f*xVal, yVal*3f, 5f);   // Top Right
        gl.glVertex3f(-3f*xVal, yVal*3f, -2f);  // Top Left

        gl.glVertex3f(6f*xVal , yVal*-3f, -2f);  // Bottom Right
        gl.glVertex3f(6f*xVal , yVal*-3f, 5f);   // Bottom Left
        gl.glVertex3f(6f*xVal , yVal*3f, 5f);    // Top Left
        gl.glVertex3f(6f*xVal , yVal*3f, -2f);   // Top Right

        gl.glColor3f(1, 1, 1);
        gl.glTexCoord2f(0.0f, 0.0f);gl.glVertex3f(-3f*xVal, yVal*-3f, 5f);  // Top Left
        gl.glTexCoord2f(1.0f, 0.0f);gl.glVertex3f(6f*xVal , yVal*-3f, 5f);   // Top Right
        gl.glTexCoord2f(1.0f, 1.0f);gl.glVertex3f(6f*xVal , yVal*-3f, -2f);  // Bottom Right
        gl.glTexCoord2f(0.0f, 1.0f);gl.glVertex3f(-3f*xVal, yVal*-3f, -2f); // Bottom Left
        gl.glPopMatrix();
        gl.glEnd();
        gl.glEnable(GL2.GL_LIGHTING);
    }
    public void renderCorner(GLAutoDrawable drawable){
        final GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        gl.glTranslatef(-1.5f, 1f, -13.0f);
        gl.glRotatef(XposAll, 1.0f, 0.0f, 0.0f); // Rotate around X-axis
        gl.glRotatef(YposAll, 0.0f, 1.0f, 0.0f); // Rotate around Y-axis
        gl.glRotatef(rotationZ, 0.0f, 0.0f, 1.0f); // Rotate around Y-axis


        //wall
//        gl.glDisable(GL2.GL_LIGHTING);
        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3f(roomColor[0], roomColor[1], roomColor[2]);
        float xVal = roomSize[0];
        float yVal = roomSize[1];

        gl.glVertex3f(xVal*1.5f, 3f*yVal, -2f);
        gl.glVertex3f(xVal*6f, 3f*yVal, 1.5f);
        gl.glVertex3f(xVal*6f, -3f*yVal, 1.5f);
        gl.glVertex3f(xVal*1.5f, -3f*yVal, -2f);

        gl.glVertex3f(xVal*1.5f, 3f*yVal, -2f);
        gl.glVertex3f(xVal*-3f, 3f*yVal, 1.5f);
        gl.glVertex3f(xVal*-3f, -3f*yVal, 1.5f);
        gl.glVertex3f(xVal*1.5f, -3f*yVal, -2f);

        gl.glColor3f(1, 1, 1);
        gl.glTexCoord2f(0.0f, 0.0f);gl.glVertex3f(xVal*1.5f, -3f*yVal, -2f);
        gl.glTexCoord2f(1.0f, 0.0f);gl.glVertex3f(xVal*6f, -3f*yVal, 1.5f);
        gl.glTexCoord2f(1.0f, 1.0f);gl.glVertex3f(xVal*1.5f, -3f*yVal, 5f);
        gl.glTexCoord2f(0.0f, 1.0f);gl.glVertex3f(xVal*-3f, -3f*yVal, 1.5f);

        gl.glEnd();
        gl.glPopMatrix();
        gl.glEnable(GL2.GL_LIGHTING);
    }

    public void renderRoundRoom(GLAutoDrawable drawable){
        float xVal = roomSize[0];
        float yVal = roomSize[1];

        final GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        gl.glTranslatef(-0f, -4f, -13.0f);
        gl.glRotatef(XposAll, 1.0f, 0.0f, 0.0f); // Rotate around X-axis
        gl.glRotatef(YposAll, 0.0f, 1.0f, 0.0f); // Rotate around Y-axis
        gl.glRotatef(rotationZ, 0.0f, 0.0f, 1.0f); // Rotate around Y-axis

        gl.glRotatef(90, 1.0f, 0.0f, 0.0f); // Rotate around Y-axis

        //wall
//        gl.glDisable(GL2.GL_LIGHTING);
        float radius = 5f;
        float height = -7f;
        int segments = 50;
        gl.glColor3f(roomColor[0], roomColor[1], roomColor[2]);

        gl.glBegin(GL2.GL_TRIANGLE_STRIP);
        for (int i = segments; i >= segments/2; i--) {
            float angle = (float) (i * 2 * Math.PI / segments);
            float x = radius * (float) Math.cos(angle);
            float y = radius * (float) Math.sin(angle);

            // Vertices on the curved surface
            gl.glVertex3f(x, y, height);
            gl.glVertex3f(x, y, 0.0f);
        }
        gl.glEnd();

        gl.glRotatef(-90, 1.0f, 0.0f, 0.0f); // Rotate around Y-axis

        gl.glColor3f(1, 1, 1);
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);gl.glVertex3f(-4.5f*xVal, yVal*0f, 1f);  // Top Left
        gl.glTexCoord2f(1.0f, 0.0f);gl.glVertex3f(4.5f*xVal , yVal*0f, 1f);   // Top Right
        gl.glTexCoord2f(1.0f, 1.0f);gl.glVertex3f(4.5f*xVal , yVal*0f, -6f);  // Bottom Right
        gl.glTexCoord2f(0.0f, 1.0f);gl.glVertex3f(-4.5f*xVal, yVal*0f, -6f); // Bottom Left
        gl.glEnd();

        gl.glTranslatef(-2f, +3f, -1);

        gl.glFlush();
    }

}

