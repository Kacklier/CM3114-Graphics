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

public class CW_Q1 extends JFrame{

	final GLCanvas canvas; //Define a canvas 
	final FPSAnimator animator=new FPSAnimator(60, true);
	final Renderer renderer = new Renderer();

	public CW_Q1() {
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
		new CW_Q1();

	}
	static class Renderer implements GLEventListener, MouseListener, MouseMotionListener, KeyListener {

		private final Transform T = new Transform();

		//VAOs and VBOs parameters
		private int idPoint = 0;
		private final int numVAOs = 4;
		private int idBuffer = 0;
		private final int numVBOs = 4;
		private int idElement = 0;
		private final int numEBOs = 4;
		private final int[] VAOs = new int[numVAOs];
		private final int[] VBOs = new int[numVBOs];
		private final int[] EBOs = new int[numEBOs];

		//Model parameters
		private final int[] numElements = new int[numEBOs];
		private int vPosition;
		private int vNormal;

		//Transformation parameters
		private int ModelView;
		private int NormalTransform;
		private int Projection;

		private int AmbientProduct;
		private int DiffuseProduct;
		private int SpecularProduct;
		private int Shininess;

		private float[] ambient1;
		private float[] diffuse1;
		private float[] specular1;
		private float materialShininess1;

		private float[] ambient2;
		private float[] diffuse2;
		private float[] specular2;
		private float materialShininess2;

		private float[] ambient3;
		private float[] diffuse3;
		private float[] specular3;
		private float materialShininess3;

		private float[] ambient4;
		private float[] diffuse4;
		private float[] specular4;
		private float materialShininess4;

		private float scale = 1;
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
			//This function will need to be adjusted if more objects are to be added.

			GL3 gl = drawable.getGL().getGL3(); // Get the GL pipeline object this

			gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			T.initialize();
			T.scale(0.1f, 0.1f, 0.1f);
			T.translate(-0.2f, -0.5f, 0);

			T.scale(scale, scale, scale);
			T.rotateX(rx);
			T.rotateY(ry);
			T.translate(tx, ty, 0);

			//Locate camera
			//T.lookAt(0, 0, ty, tx, 0, -100, 0, 1, 0);    //Default

			//shape 1

			gl.glUniformMatrix4fv(ModelView, 1, true, T.getTransformv(), 0);
			gl.glUniformMatrix4fv(NormalTransform, 1, true, T.getInvTransformTv(), 0);

			//This section implements the diffusion properties of each of the shapes in turn
			gl.glUniform4fv( AmbientProduct, 1, ambient1,0 );
			gl.glUniform4fv( DiffuseProduct, 1, diffuse1, 0 );
			gl.glUniform4fv( SpecularProduct, 1, specular1, 0 );
			gl.glUniform1f( Shininess, materialShininess1);

			idPoint = 0;
			idBuffer = 0;
			idElement = 0;
			bindObject(gl);
			gl.glDrawElements(GL_TRIANGLES, numElements[idElement], GL_UNSIGNED_INT, 0);

			//shape 2

			//T.scale(0.5f,0.5f,0.5f);
			T.translate(0f, 0.8f, 0);

			gl.glUniformMatrix4fv(ModelView, 1, true, T.getTransformv(), 0);
			gl.glUniformMatrix4fv(NormalTransform, 1, true, T.getInvTransformTv(), 0);

			gl.glUniform4fv( AmbientProduct, 1, ambient2,0 );
			gl.glUniform4fv( DiffuseProduct, 1, diffuse2, 0 );
			gl.glUniform4fv( SpecularProduct, 1, specular2, 0 );
			gl.glUniform1f( Shininess, materialShininess2);

			idPoint = 1;
			idBuffer = 1;
			idElement = 1;
			bindObject(gl);
			gl.glDrawElements(GL_TRIANGLES, numElements[idElement], GL_UNSIGNED_INT, 0);

			//shape 3

			//T.scale(0.5f,0.5f,0.5f);
			T.translate(0.7f, 0f, 0);

			gl.glUniformMatrix4fv(ModelView, 1, true, T.getTransformv(), 0);
			gl.glUniformMatrix4fv(NormalTransform, 1, true, T.getInvTransformTv(), 0);

			gl.glUniform4fv( AmbientProduct, 1, ambient3,0 );
			gl.glUniform4fv( DiffuseProduct, 1, diffuse3, 0 );
			gl.glUniform4fv( SpecularProduct, 1, specular3, 0 );
			gl.glUniform1f( Shininess, materialShininess3);

			idPoint = 2;
			idBuffer = 2;
			idElement = 2;
			bindObject(gl);
			gl.glDrawElements(GL_TRIANGLES, numElements[idElement], GL_UNSIGNED_INT, 0);

			//shape 4

			//T.scale(0.5f,0.5f,0.5f);
			T.translate(0f, -0.8f, 0);

			gl.glUniformMatrix4fv(ModelView, 1, true, T.getTransformv(), 0);
			gl.glUniformMatrix4fv(NormalTransform, 1, true, T.getInvTransformTv(), 0);

			gl.glUniform4fv( AmbientProduct, 1, ambient4,0 );
			gl.glUniform4fv( DiffuseProduct, 1, diffuse4, 0 );
			gl.glUniform4fv( SpecularProduct, 1, specular4, 0 );
			gl.glUniform1f( Shininess, materialShininess4);

			idPoint = 3;
			idBuffer = 3;
			idElement = 3;
			bindObject(gl);
			gl.glDrawElements(GL_TRIANGLES, numElements[idElement], GL_UNSIGNED_INT, 0);
		}

		@Override
		public void dispose(GLAutoDrawable drawable) {
			// TODO Auto-generated method stub			
		}

		@Override
		public void init(GLAutoDrawable drawable) {
			//The objects are added and removed from this function
			GL3 gl = drawable.getGL().getGL3(); // Get the GL pipeline object this

			gl.glEnable(GL_CULL_FACE);

			gl.glGenVertexArrays(numVAOs, VAOs, 0);
			gl.glGenBuffers(numVBOs, VBOs, 0);
			gl.glGenBuffers(numEBOs, EBOs, 0);
			//The number of objects and they're relative materials can be changed and adapted from here:
			//in order to add more than 4 objects, a new collection of material varibles must be allocated at the top
			// of the file. however objects can be removed and changed with no issues.
			SObject sphere = new SSphere(3);
			idPoint = 0;
			idBuffer = 0;
			idElement = 0;

			// each object has it's own material properties that can be adjusted accordingly
			Vec4 materialAmbient1 = new Vec4(0.25f, 0.25f, 0.25f, 1f);
			Vec4 materialDiffuse1 = new Vec4(0.4f, 0.4f, 0.4f, 1f);
			Vec4 materialSpecular1 = new Vec4(0.774597f, 0.774597f, 0.774597f, 1f);
			materialShininess1 = 76.8f;

			createObject(gl, sphere, 1, materialAmbient1, materialDiffuse1, materialSpecular1);

			SObject teapot = new STeapot(3);
			idPoint = 1;
			idBuffer = 1;
			idElement = 1;

			Vec4 materialAmbient2 = new Vec4(0.1745f, 0.01175f, 0.01175f, 0.55f);
			Vec4 materialDiffuse2 = new Vec4(0.61424f, 0.04136f, 0.04136f, 0.55f);
			Vec4 materialSpecular2 = new Vec4(0.727811f, 0.626959f, 0.626959f, 0.55f);
			materialShininess2 = 76.8f;

			createObject(gl, teapot, 2, materialAmbient2, materialDiffuse2, materialSpecular2);

			SObject cone = new SCone(4, 1);
			idPoint = 2;
			idBuffer = 2;
			idElement = 2;

			Vec4 materialAmbient3 = new Vec4(0.135f, 0.2225f, 0.1575f, 0.95f);
			Vec4 materialDiffuse3 = new Vec4(0.54f, 0.89f, 0.63f, 0.95f);
			Vec4 materialSpecular3 = new Vec4(0.316228f, 0.316228f, 0.316228f, 0.95f);
			materialShininess3 = 12.8f;

			createObject(gl, cone, 3, materialAmbient3, materialDiffuse3, materialSpecular3);

			SObject sphere2 = new SSphere(1);
			idPoint = 3;
			idBuffer = 3;
			idElement = 3;

			Vec4 materialAmbient4 = new Vec4(0.24725f, 0.1995f, 0.0745f, 1);
			Vec4 materialDiffuse4 = new Vec4(0.75164f, 0.60648f, 0.22648f, 1);
			Vec4 materialSpecular4 = new Vec4(0.628281f, 0.555802f, 0.366065f, 1);
			materialShininess4 = 51.2f;

			createObject(gl, sphere2, 4, materialAmbient4, materialDiffuse4, materialSpecular4);

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

			//projection
//			T.Ortho(-1, 1, -1, 1, -1, 1);  //Default
			// to avoid shape distortion because of reshaping the viewport
			//viewport aspect should be the same as the projection aspect
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

			// Convert right-hand to left-hand coordinate system
			//T.rotateX(-90);
			T.reverseZ();
			gl.glUniformMatrix4fv(Projection, 1, true, T.getTransformv(), 0);

		}

		public void createObject(GL3 gl, SObject object, int num, Vec4 materialAmbient, Vec4 materialDiffuse, Vec4 materialSpecular) {
			//This function is where the objects get bound to the shader, and the render is built.
			// each of the objects points are loaded into the renderer to then be buffered together,
			// followed by the implementation of the shader to
			float[] vertexArray = object.getVertices();
			float[] normalArray = object.getNormals();
			int[] vertexIndexs = object.getIndices();
			numElements[idPoint] = object.getNumIndices();

			bindObject(gl);

			FloatBuffer vertices = FloatBuffer.wrap(vertexArray);
			FloatBuffer normals = FloatBuffer.wrap(normalArray);

			// Create an empty buffer with the size we need
			// and a null pointer for the data values
			long vertexSize = vertexArray.length * (Float.SIZE / 8);
			long normalSize = normalArray.length * (Float.SIZE / 8);
			gl.glBufferData(GL_ARRAY_BUFFER, vertexSize + normalSize,
					null, GL_STATIC_DRAW); // pay attention to *Float.SIZE/8

			// Load the real data separately.  We put the colors right after the vertex coordinates,
			// so, the offset for colors is the size of vertices in bytes
			gl.glBufferSubData(GL_ARRAY_BUFFER, 0, vertexSize, vertices);
			gl.glBufferSubData(GL_ARRAY_BUFFER, vertexSize, normalSize, normals);

			IntBuffer elements = IntBuffer.wrap(vertexIndexs);

			long indexSize = vertexIndexs.length * (Integer.SIZE / 8);
			gl.glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexSize,
					elements, GL_STATIC_DRAW); // pay attention to *Float.SIZE/8

			ShaderProg shaderproc = new ShaderProg(gl, "Gouraud.vert", "Gouraud.frag");
			int program = shaderproc.getProgram();
			gl.glUseProgram(program);

			// Initialize the vertex position attribute in the vertex shader
			vPosition = gl.glGetAttribLocation(program, "vPosition");
			gl.glEnableVertexAttribArray(vPosition);
			gl.glVertexAttribPointer(vPosition, 3, GL_FLOAT, false, 0, 0L);

			// Initialize the vertex color attribute in the vertex shader.
			// The offset is the same as in the glBufferSubData, i.e., vertexSize
			// It is the starting point of the color data
			vNormal = gl.glGetAttribLocation(program, "vNormal");
			gl.glEnableVertexAttribArray(vNormal);
			gl.glVertexAttribPointer(vNormal, 3, GL_FLOAT, false, 0, vertexSize);

			//Get connected with the ModelView matrix in the vertex shader
			ModelView = gl.glGetUniformLocation(program, "ModelView");
			NormalTransform = gl.glGetUniformLocation(program, "NormalTransform");
			Projection = gl.glGetUniformLocation(program, "Projection");

			// The light position and type of light can be adjusted in this area.
			float[] lightPosition = {100.0f, 100.0f, 100.0f, 0.0f};
			Vec4 lightAmbient = new Vec4(1.0f, 1.0f, 1.0f, 1.0f);
			Vec4 lightDiffuse = new Vec4(1.0f, 1.0f, 1.0f, 1.0f);
			Vec4 lightSpecular = new Vec4(1.0f, 1.0f, 1.0f, 1.0f);

			// This ensures that each object gets its own shader variables.
			if(num==1) {
				Vec4 ambientProduct = lightAmbient.times(materialAmbient);
				ambient1 = ambientProduct.getVector();
				Vec4 diffuseProduct = lightDiffuse.times(materialDiffuse);
				diffuse1 = diffuseProduct.getVector();
				Vec4 specularProduct = lightSpecular.times(materialSpecular);
				specular1 = specularProduct.getVector();
			}else if(num==2){
				Vec4 ambientProduct = lightAmbient.times(materialAmbient);
				ambient2 = ambientProduct.getVector();
				Vec4 diffuseProduct = lightDiffuse.times(materialDiffuse);
				diffuse2 = diffuseProduct.getVector();
				Vec4 specularProduct = lightSpecular.times(materialSpecular);
				specular2 = specularProduct.getVector();
			}else if(num == 3){
				Vec4 ambientProduct = lightAmbient.times(materialAmbient);
				ambient3 = ambientProduct.getVector();
				Vec4 diffuseProduct = lightDiffuse.times(materialDiffuse);
				diffuse3 = diffuseProduct.getVector();
				Vec4 specularProduct = lightSpecular.times(materialSpecular);
				specular3 = specularProduct.getVector();
			}else{
				Vec4 ambientProduct = lightAmbient.times(materialAmbient);
				ambient4 = ambientProduct.getVector();
				Vec4 diffuseProduct = lightDiffuse.times(materialDiffuse);
				diffuse4 = diffuseProduct.getVector();
				Vec4 specularProduct = lightSpecular.times(materialSpecular);
				specular4 = specularProduct.getVector();
			}

			AmbientProduct = gl.glGetUniformLocation(program, "AmbientProduct");
			DiffuseProduct = gl.glGetUniformLocation(program, "DiffuseProduct");
			SpecularProduct = gl.glGetUniformLocation(program, "SpecularProduct");
			Shininess = gl.glGetUniformLocation(program, "Shininess");

			gl.glUniform4fv(gl.glGetUniformLocation(program, "LightPosition"), 1, lightPosition, 0);

		}

		public void bindObject(GL3 gl) {
			gl.glBindVertexArray(VAOs[idPoint]);
			gl.glBindBuffer(GL_ARRAY_BUFFER, VBOs[idBuffer]);
			gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBOs[idElement]);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// The camera movement is adjusted from this function.
			int x = e.getX();
			int y = e.getY();

			//left button down, move the object
			if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
				tx += (x-xMouse) * 0.01;
				ty -= (y-yMouse) * 0.01;
				xMouse = x;
				yMouse = y;
			}


			//right button down, rotate the object
			if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
				if((rx <= 90) && (rx >= -90)){
					//ry += (x - xMouse) * 1;
					rx -= (y - yMouse) * 1;
					//xMouse = x;
					yMouse = y;
					if(rx >= 90){
						rx = 90;
					}else if(rx <= -90){
						rx = -90;
					}
				}
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			xMouse = e.getX();
			yMouse = e.getY();
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
			// human movement is adjusted from here.
			if (e.getKeyCode() == 87 || e.getKeyCode() == 38) {  //W or up
				scale += 0.05f;
			}
			if (e.getKeyCode() == 65 || e.getKeyCode() == 37) { //A or left
				tx -= 0.05f;
			}
			if (e.getKeyCode() == 83 || e.getKeyCode() == 40) {//S or down
				scale -= 0.05f;
			}
			if(e.getKeyCode()==68||e.getKeyCode()==39) {//D or right
				tx += 0.05f;
			}

		}

		@Override
		public void keyReleased(KeyEvent e) {

		}
	}
}