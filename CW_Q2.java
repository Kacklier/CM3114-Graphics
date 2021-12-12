import static com.jogamp.opengl.GL3.*;

import java.io.IOException;
import java.io.File;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.swing.JFrame;

import Basic.ShaderProg;
import Basic.Transform;
import Basic.Vec4;
//import Basic.Vec3;
import Objects.SObject;
import Objects.SSphere;
import Objects.STeapot;
import Objects.SCone;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
//import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class CW_Q2 extends JFrame{

    final GLCanvas canvas; //Define a canvas
    final FPSAnimator animator=new FPSAnimator(60, true);
    final Renderer renderer = new Renderer();

    public CW_Q2() {
        GLProfile glp = GLProfile.get(GLProfile.GL3);
        GLCapabilities caps = new GLCapabilities(glp);
        canvas = new GLCanvas(caps);

        add(canvas, java.awt.BorderLayout.CENTER); // Put the canvas in the frame
        canvas.addGLEventListener(renderer); //Set the canvas to listen GLEvents
        canvas.addMouseListener(renderer);
        canvas.addMouseMotionListener(renderer);
        canvas.addKeyListener(renderer);

        animator.add(canvas);

        setTitle("CW Q1");
        setSize(700,700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        animator.start();
        canvas.requestFocus();
    }

    public static void main(String[] args) {
        new CW_Q2();

    }

    static class Renderer implements GLEventListener, MouseListener, MouseMotionListener, KeyListener {

        private final Transform T = new Transform();

        //VAOs and VBOs parameters
        private int idPoint = 0;
        private final int numVAOs = 1;
        private int idBuffer = 0;
        private final int numVBOs = 1;
        private int idElement = 0;
        private final int numEBOs = 1;
        private final int[] VAOs = new int[numVAOs];
        private final int[] VBOs = new int[numVBOs];
        private final int[] EBOs = new int[numEBOs];

        //Model parameters
        private final int[] numElements = new int[numEBOs];
        private int vPosition;
        private int vColor;

        //Transformation parameters
        private int numVertices = 36;
        private int ModelView;
        private int Projection;

        private float scale = 1;
        private int i = 0;
        private float tx = 0;
        private float ty = 0;
        private float rx = 0;
        private float ry = 0;

        //Mouse position
        private int xMouse = 0;
        private int yMouse = 0;

        @Override
        public void display(GLAutoDrawable drawable) {
            //This function is where the objects get initialised, and they're translations are put into affect.

            GL3 gl = drawable.getGL().getGL3(); // Get the GL pipeline object this

            gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            T.initialize();

            T.scale(0.5f, 0.5f, 0.5f);

            // a simple counter keeps the object in constant rotation
            i++;
            T.rotateA(i, 0.4f,1,0.4f);

            //Locate camera
            //T.lookAt(0, 0, ty, tx, 0, -100, 0, 1, 0);    //Default

            //shape 1

            gl.glUniformMatrix4fv( ModelView, 1, true, T.getTransformv(), 0 );
            gl.glDrawArrays(GL_TRIANGLES, 0, numVertices);
        }

        @Override
        public void dispose(GLAutoDrawable drawable) {
            // TODO Auto-generated method stub
        }

        @Override
        public void init(GLAutoDrawable drawable) {
            GL3 gl = drawable.getGL().getGL3(); // Get the GL pipeline object this

            gl.glGenVertexArrays(numVAOs,VAOs,0);
            gl.glBindVertexArray(VAOs[idPoint]);

            float [] vertexArray = {
                    // The object is made up of triangles, with the bottom face having 2 to make the square
                    // front face
                    0,  1,  0,
                    -1, -1,  1,
                    1, -1,  1,
                    // back face
                    0,  1, 0,
                    1, -1, -1,
                    -1, -1, -1,
                    // left face
                    0,  1, 0,
                    -1, -1, -1,
                    -1, -1,  1,
                    // right face
                    0,  1, 0,
                    1, -1,  1,
                    1, -1, -1,
                    // bottom face
                    1, -1,  1,
                    -1, -1,  1,
                    1, -1, -1,
                    -1, -1,  1,
                    -1, -1, -1,
                    1, -1, -1

            };


            float [] colorArray = {
                    //These are the colours that are mapped to the object
                    //front face
                    1, 0, 1,
                    1, 0, 1,
                    1, 0, 1,
                    //back face
                    0, 1, 0,
                    0, 1, 0,
                    0, 1, 0,
                    //left face
                    0, 0, 1,
                    0, 0, 1,
                    0, 0, 1,
                    //right face
                    1, 1, 0,
                    1, 1, 0,
                    1, 1, 0,
                    //bottom
                    1, 0, 1,
                    1, 0, 1,
                    1, 0, 1,
                    1, 0, 1,
                    1, 0, 1,
                    1, 0, 1,
            };

            //The buffers are loaded and filled in this section

            FloatBuffer vertices = FloatBuffer.wrap(vertexArray);
            FloatBuffer colors = FloatBuffer.wrap(colorArray);

            gl.glGenBuffers(numVBOs, VBOs,0);
            gl.glBindBuffer(GL_ARRAY_BUFFER, VBOs[idBuffer]);

            long vertexSize = vertexArray.length*(Float.SIZE/8);
            long colorSize = colorArray.length*(Float.SIZE/8);
            gl.glBufferData(GL_ARRAY_BUFFER, vertexSize +colorSize,
                    null, GL_STATIC_DRAW);

            gl.glBufferSubData( GL_ARRAY_BUFFER, 0, vertexSize, vertices );
            gl.glBufferSubData( GL_ARRAY_BUFFER, vertexSize, colorSize, colors );

            //These functions allow for the use of transparency in the faces

            gl.glEnable(GL_BLEND);
            gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

            ShaderProg shaderproc = new ShaderProg(gl, "Blend.vert", "Blend.frag");
            int program = shaderproc.getProgram();
            gl.glUseProgram(program);

            // Initialize the vertex position attribute in the vertex shader
            vPosition = gl.glGetAttribLocation( program, "vPosition" );
            gl.glEnableVertexAttribArray(vPosition);
            gl.glVertexAttribPointer(vPosition, 3, GL_FLOAT, false, 0, 0L);

            // Initialize the vertex color attribute in the vertex shader.
            // The offset is the same as in the glBufferSubData, i.e., vertexSize
            // It is the starting point of the color data
            vColor = gl.glGetAttribLocation( program, "vColor" );
            gl.glEnableVertexAttribArray(vColor);
            gl.glVertexAttribPointer(vColor, 3, GL_FLOAT, false, 0, vertexSize);

            //Get connected with the ModelView matrix in the vertex shader
            ModelView = gl.glGetUniformLocation(program, "ModelView");
            Projection = gl.glGetUniformLocation(program, "Projection");

            // This is necessary. Otherwise, the The color on back face may display
//		    gl.glDepthFunc(GL_LESS);
            gl.glEnable(GL_DEPTH_TEST);
        }

        @Override
        public void reshape(GLAutoDrawable drawable, int x, int y, int w,
                            int h) {

            GL3 gl = drawable.getGL().getGL3(); // Get the GL pipeline object this

            gl.glViewport(x, y, w, h);

            T.initialize();

            if (h < 1) {
                h = 1;
            }
            if (w < 1) {
                w = 1;
            }
            float a = (float) w / h;   //aspect
            if (w < h) {
                T.ortho(-1, 1, -1 / a, 1 / a, -1, 1);
            } else {
                T.ortho(-1 * a, 1 * a, -1, 1, -1, 1);
            }

            T.reverseZ();
            gl.glUniformMatrix4fv(Projection, 1, true, T.getTransformv(), 0);

        }

        public void bindObject(GL3 gl) {
            gl.glBindVertexArray(VAOs[idPoint]);
            gl.glBindBuffer(GL_ARRAY_BUFFER, VBOs[idBuffer]);
            gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBOs[idElement]);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void mousePressed(MouseEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void mouseExited(MouseEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
}
