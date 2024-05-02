#version 120

in vec2 TexCoord;
out vec4 FragColor;

uniform sampler2D texture;
uniform float blurRadius;
uniform float alpha;

// Function to calculate Gaussian blur
float gaussian(float x, float sigma) {
    return exp(-(x * x) / (2.0 * sigma * sigma)) / (sqrt(2.0 * 3.14159265358979323846) * sigma);
}

void main() {
    vec4 color = vec4(0.0);
    float totalWeight = 0.0;

    // Apply horizontal blur
    for (int i = -5; i <= 5; ++i) {
        float weight = gaussian(float(i), blurRadius);
        color += texture(texture, TexCoord + vec2(float(i) / textureSize(texture, 0).x, 0.0)) * weight;
        totalWeight += weight;
    }

    color /= totalWeight;

    // Apply vertical blur
    totalWeight = 0.0;
    for (int i = -5; i <= 5; ++i) {
        float weight = gaussian(float(i), blurRadius);
        color += texture(texture, TexCoord + vec2(0.0, float(i) / textureSize(texture, 0).y)) * weight;
        totalWeight += weight;
    }

    color /= totalWeight;

    // Set alpha value
    color.a = alpha;

    FragColor = color;
}