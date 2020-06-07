package com.arsvechkarev.letta.opengldrawing.shaders

import java.util.Collections
import java.util.HashMap

object ShaderSet {
  
  private const val VERTEX = "vertex"
  private const val FRAGMENT = "fragment"
  private const val ATTRIBUTES = "attributes"
  private const val UNIFORMS = "uniforms"
  
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
  
  private val BRUSH_LIGHT_FSH = """
    precision highp float;
    varying vec2 varTexcoord;
    varying float varIntensity;
    uniform sampler2D texture;
    
    void main() {
      vec4 f = texture2D(texture, varTexcoord.st, 0.0);
      gl_FragColor = vec4(f.r * varIntensity, f.g, f.b, 0.0);
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
  
  private val BLIT_WITH_MASK_LIGHT_FSH = """
    precision highp float;
    varying vec2 varTexcoord;
    uniform sampler2D texture;
    uniform sampler2D mask;
    uniform vec4 color;
    
    void main() {
      vec4 dst = texture2D(texture, varTexcoord.st, 0.0);
      vec3 maskColor = texture2D(mask, varTexcoord.st, 0.0).rgb;
      float srcAlpha = clamp(0.78 * maskColor.r + maskColor.b + maskColor.g, 0.0, 1.0);
      vec3 borderColor = mix(color.rgb, vec3(1.0, 1.0, 1.0), 0.86);
      vec3 finalColor = mix(color.rgb, borderColor, maskColor.g);
      finalColor = mix(finalColor.rgb, vec3(1.0, 1.0, 1.0), maskColor.b);
      float outAlpha = srcAlpha + dst.a * (1.0 - srcAlpha);
      gl_FragColor.rgb = (finalColor * srcAlpha + dst.rgb * dst.a * (1.0 - srcAlpha)) / outAlpha;
      gl_FragColor.a = outAlpha;
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
  
  private val COMPOSITE_WITH_MASK_LIGHT_FSH = """
    precision highp float;
    varying vec2 varTexcoord;
    uniform sampler2D mask;
    uniform vec4 color;
    
    void main() {
      vec3 maskColor = texture2D(mask, varTexcoord.st, 0.0).rgb;
      float alpha = clamp(0.78 * maskColor.r + maskColor.b + maskColor.g, 0.0, 1.0);
      vec3 borderColor = mix(color.rgb, vec3(1.0, 1.0, 1.0), 0.86);
      vec3 finalColor = mix(color.rgb, borderColor, maskColor.g);
      finalColor = mix(finalColor.rgb, vec3(1.0, 1.0, 1.0), maskColor.b);
      gl_FragColor.rgb = finalColor;
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
  
  private fun createMap(): Map<String, Map<String, Any>> {
    val result: MutableMap<String, Map<String, Any>> = HashMap()
    var shader: MutableMap<String, Any> = HashMap()
    shader[VERTEX] = BRUSH_VSH
    shader[FRAGMENT] = BRUSH_FSH
    shader[ATTRIBUTES] = arrayOf("inPosition", "inTexcoord", "alpha")
    shader[UNIFORMS] = arrayOf("mvpMatrix", "texture")
    result["brush"] = Collections.unmodifiableMap(shader)
    shader = HashMap()
    shader[VERTEX] = BRUSH_VSH
    shader[FRAGMENT] = BRUSH_LIGHT_FSH
    shader[ATTRIBUTES] = arrayOf("inPosition", "inTexcoord", "alpha")
    shader[UNIFORMS] = arrayOf("mvpMatrix", "texture")
    result["brushLight"] = Collections.unmodifiableMap(shader)
    shader = HashMap()
    shader[VERTEX] = BLIT_VSH
    shader[FRAGMENT] = BLIT_FSH
    shader[ATTRIBUTES] = arrayOf("inPosition", "inTexcoord")
    shader[UNIFORMS] = arrayOf("mvpMatrix", "texture")
    result["blit"] = Collections.unmodifiableMap(shader)
    shader = HashMap()
    shader[VERTEX] = BLIT_VSH
    shader[FRAGMENT] = BLIT_WITH_MASK_LIGHT_FSH
    shader[ATTRIBUTES] = arrayOf("inPosition", "inTexcoord")
    shader[UNIFORMS] = arrayOf("mvpMatrix", "texture", "mask", "color")
    result["blitWithMaskLight"] = Collections.unmodifiableMap(shader)
    shader = HashMap()
    shader[VERTEX] = BLIT_VSH
    shader[FRAGMENT] = BLIT_WITH_MASK_FSH
    shader[ATTRIBUTES] = arrayOf("inPosition", "inTexcoord")
    shader[UNIFORMS] = arrayOf("mvpMatrix", "texture", "mask", "color")
    result["blitWithMask"] = Collections.unmodifiableMap(shader)
    shader = HashMap()
    shader[VERTEX] = BLIT_VSH
    shader[FRAGMENT] = COMPOSITE_WITH_MASK_FSH
    shader[ATTRIBUTES] = arrayOf("inPosition", "inTexcoord")
    shader[UNIFORMS] = arrayOf("mvpMatrix", "mask", "color")
    result["compositeWithMask"] = Collections.unmodifiableMap(shader)
    shader = HashMap()
    shader[VERTEX] = BLIT_VSH
    shader[FRAGMENT] = COMPOSITE_WITH_MASK_LIGHT_FSH
    shader[ATTRIBUTES] = arrayOf("inPosition", "inTexcoord")
    shader[UNIFORMS] = arrayOf("mvpMatrix", "texture", "mask", "color")
    result["compositeWithMaskLight"] = Collections.unmodifiableMap(shader)
    shader = HashMap()
    shader[VERTEX] = BLIT_VSH
    shader[FRAGMENT] = NON_PRE_MULTIPLIED_BLIT_FSH
    shader[ATTRIBUTES] = arrayOf("inPosition", "inTexcoord")
    shader[UNIFORMS] = arrayOf("mvpMatrix", "texture")
    result["nonPremultipliedBlit"] = Collections.unmodifiableMap(shader)
    return Collections.unmodifiableMap(result)
  }
  
  @Suppress("UNCHECKED_CAST")
  @JvmStatic
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
    return Collections.unmodifiableMap(result)
  }
}