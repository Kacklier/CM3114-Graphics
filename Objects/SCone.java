package Objects;

// Using the base code from SSphere, I made the shape of a cone, with a similar 0 point, and sloping sides.
public class SCone extends SObject {

    private int height;
    private float radius;
    private int slices;

    public SCone(int height, float radius) {
        super();
        init();
        this.height = height;
        this.radius = radius;
        update();
    }

    // Default values
    private void init() {
        this.height = 4;
        this.radius = 1;
        this.slices = 30;
    }

    @Override
    protected void genData() {
        double deltaLong = PI * 2 / slices;

        // Generate vertices coordinates, normal values, and texture coordinates
        numVertices = slices * height;
        vertices = new float[numVertices * 3];
        normals = new float[numVertices * 3];
        textures = new float[numVertices * 2];

        //generate vertices and normals for the point of the cone
        normals[0] = 0; normals[1] = 0; normals[2] = 1;
        vertices[0] = 0; vertices[1] = 0; vertices[2] = height;
        textures[0]= 0.5f; textures[1] = 1.0f;

        for (int i = 0; i < slices; i++) {
            // Normals and Vertices for sides
            normals[3 * i] = cos(deltaLong * i);
            normals[3 * i + 1] = 0;
            normals[3 * i + 2] = sin(deltaLong * i);
            vertices[3 * i] = radius * normals[3 * i];
            vertices[3 * i + 1] = 0;
            vertices[3 * i + 2] = radius * normals[3 * i + 2];

            normals[(i + numVertices / 2) * 3] = 0;
            normals[(i + numVertices / 2) * 3 + 1] = -1;
            normals[(i + numVertices / 2) * 3 + 2] = 0;
            vertices[(i + slices) * 3] = 0;
            vertices[(i + slices) * 3 + 1] = height;
            vertices[(i + slices) * 3 + 2] = 0;


            // Vertices and Normals for bottom face
            normals[(i + slices) * 3] = cos(deltaLong * i);
            normals[(i + slices) * 3 + 1] = 0;
            normals[(i + slices) * 3 + 2] = sin(deltaLong * i);
            vertices[(i + numVertices / 2) * 3] = radius * normals[(i + slices) * 3];
            vertices[(i + numVertices / 2) * 3 + 1] = 0;
            vertices[(i + numVertices / 2) * 3 + 2] = radius * normals[(i + slices) * 3 + 2];
        }

        numIndices = (slices-11)*slices*6;
        indices = new int[numIndices];

        // Indices for sides
        for (int i = 0; i < slices; i++) {
            indices[i * 6] = i + slices;
            indices[i * 6 + 1] = (i + 1) % slices;
            indices[i * 6 + 2] = i;
            indices[i * 6 + 3] = (i + 1) % slices;
            indices[i * 6 + 4] = i + slices;
            indices[i * 6 + 5] = (i + 1) % slices + slices;

            // Indices for bottom face and point
            if (i >= 2) {
            indices[slices * 6 + (i - 2) * 3] = numVertices / 2;
            indices[slices * 6 + (i - 2) * 3 + 1] = numVertices / 2 + i - 1;
            indices[slices * 6 + (i - 2) * 3 + 2] = numVertices / 2 + i;
            indices[slices * 6 + (slices + i - 4) * 3] = numVertices / 2 + slices + i;
            indices[slices * 6 + (slices + i - 4) * 3 + 1] = numVertices / 2 + slices + i - 1;
            indices[slices * 6 + (slices + i - 4) * 3 + 2] = numVertices / 2 + slices;
            }
        }
    }

    public void setHeight(int slices){
        this.slices = slices;
        updated = false;
    }

    public void setRadius(float radius){
        this.radius = radius;
        updated = false;
    }

    public void setSlices(int slices){
        this.slices = slices;
        updated = false;
    }

    public float getHeight() { return height; }
    public float getRadius() { return radius; }
    public int getSlices() { return slices; }
}