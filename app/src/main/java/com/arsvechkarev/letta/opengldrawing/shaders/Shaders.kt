package com.arsvechkarev.letta.opengldrawing.shaders

import java.util.HashMap

object Shaders {
  
  private val BRUSH_VSH = """
    precision highp float;
    uniform mat4 mvpMatrix;
    attribute vec4 inPosition;
    attribute vec2 inTexcoord;
    attribute float alpha;
    varying vec2 varTexcoord;
    varying float varIntensity;
    
    void main() {
      gl_Position = mvpMatrix * inPosition;
      varTexcoord = inTexcoord;
      varIntensity = alpha;
    }
    """.trimIndent()
  
  private val BRUSH_FSH = """
    precision highp float;
    varying vec2 varTexcoord;
    varying float varIntensity;
    uniform sampler2D texture;
    
    void main() {
      gl_FragColor = vec4(0, 0, 0, varIntensity * texture2D(texture, varTexcoord.st, 0.0).r);
    }
    """.trimIndent()
  
  private val BLIT_VSH = """
    precision highp float;
    uniform mat4 mvpMatrix;
    attribute vec4 inPosition;
    attribute vec2 inTexcoord;
    varying vec2 varTexcoord;
    
    void main() {
      gl_Position = mvpMatrix * inPosition;
      varTexcoord = inTexcoord;
    }
    """.trimIndent()
  
  private val BLIT_FSH = """
    precision highp float;
    varying vec2 varTexcoord;
    uniform sampler2D texture;

    void main() {
      vec4 tex = texture2D(texture, varTexcoord.st, 0.0);
      gl_FragColor = texture2D(texture, varTexcoord.st, 0.0);
      gl_FragColor.rgb *= gl_FragColor.a;
    }
    """.trimIndent()
  
  private val BLIT_WITH_MASK_FSH = """
    precision highp float;
    varying vec2 varTexcoord;
    uniform sampler2D texture;
    uniform sampler2D mask;
    uniform vec4 color;
    
    void main() {
      vec4 dst = texture2D(texture, varTexcoord.st, 0.0);
      float srcAlpha = color.a * texture2D(mask, varTexcoord.st, 0.0).a;
      float outAlpha = srcAlpha + dst.a * (1.0 - srcAlpha);
      gl_FragColor.rgb = (color.rgb * srcAlpha + dst.rgb * dst.a * (1.0 - srcAlpha)) / outAlpha;
      gl_FragColor.a = outAlpha;
      gl_FragColor.rgb *= gl_FragColor.a;
    }
    """.trimIndent()
  
  private val COMPOSITE_WITH_MASK_FSH = """
    precision highp float;
    varying vec2 varTexcoord;
    uniform sampler2D mask;
    uniform vec4 color;
    
    void main() {
      float alpha = color.a * texture2D(mask, varTexcoord.st, 0.0).a;
      gl_FragColor.rgb = color.rgb;
      gl_FragColor.a = alpha;
    }
    """.trimIndent()
  
  private val NON_PRE_MULTIPLIED_BLIT_FSH = """
    precision highp float;
    varying vec2 varTexcoord;
    uniform sampler2D texture;
    
    void main() {
      gl_FragColor = texture2D(texture, varTexcoord.st, 0.0);
    }
    """.trimIndent()
  
  private val availableShaders = createMap()
  
  private fun createMap(): HashMap<String, Map<String, Any>> {
    val result = HashMap<String, Map<String, Any>>()
    result[BRUSH] = setupValues(
      BRUSH_VSH, BRUSH_FSH, arrayOf(IN_POSITION, IN_TEXCOORD, ALPHA), arrayOf(MVP_MATRIX, TEXTURE)
    )
    result[BLIT] = setupValues(
      BLIT_VSH, BLIT_FSH, arrayOf(IN_POSITION, IN_TEXCOORD), arrayOf(MVP_MATRIX, TEXTURE)
    )
    result[BLIT_WITH_MASK] = setupValues(
      BLIT_VSH, BLIT_WITH_MASK_FSH, arrayOf(IN_POSITION, IN_TEXCOORD),
      arrayOf(MVP_MATRIX, TEXTURE, MASK, COLOR)
    )
    result[COMPOSITE_WITH_MASK] = setupValues(
      BLIT_VSH, COMPOSITE_WITH_MASK_FSH, arrayOf(IN_POSITION, IN_TEXCOORD),
      arrayOf(MVP_MATRIX, MASK, COLOR)
    )
    result[NON_PRE_MULTIPLIED_BLIT] = setupValues(
      BLIT_VSH, NON_PRE_MULTIPLIED_BLIT_FSH, arrayOf(IN_POSITION, IN_TEXCOORD),
      arrayOf(MVP_MATRIX, TEXTURE)
    )
    return result
  }
  
  @Suppress("UNCHECKED_CAST")
  fun setup(): Map<String, Shader> {
    val result: MutableMap<String, Shader> = HashMap()
    for ((key, value) in availableShaders) {
      val vertex = value[VERTEX] as String
      val fragment = value[FRAGMENT] as String
      val attributes = value[ATTRIBUTES] as Array<String>
      val uniforms = value[UNIFORMS] as Array<String>
      val shader = Shader(vertex, fragment, attributes, uniforms)
      result[key] = shader
    }
    return result
  }
  
  private fun setupValues(
    vertexShader: String,
    fragmentShader: String,
    attributes: Array<String>,
    uniforms: Array<String>
  ): Map<String, Any> {
    return mapOf(
      VERTEX to vertexShader,
      FRAGMENT to fragmentShader,
      ATTRIBUTES to attributes,
      UNIFORMS to uniforms
    )
  }
}