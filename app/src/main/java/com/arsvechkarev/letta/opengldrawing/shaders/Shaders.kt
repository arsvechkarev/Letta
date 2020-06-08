package com.arsvechkarev.letta.opengldrawing.shaders

import java.util.Collections
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
  
  fun setup(): Map<String, Shader> {
    val result = HashMap<String, Shader>()
    result[BRUSH] = setupValues(
      BRUSH_VSH,
      BRUSH_FSH,
      MVP_MATRIX,
      TEXTURE)
    result[BRUSH_LIGHT] = setupValues(
      BRUSH_VSH,
      BRUSH_LIGHT_FSH,
      MVP_MATRIX,
      TEXTURE)
    result[BLIT] = setupValues(
      BLIT_VSH,
      BLIT_FSH,
      MVP_MATRIX,
      TEXTURE)
    result[BLIT_WITH_MASK] = setupValues(
      BLIT_VSH,
      BLIT_WITH_MASK_FSH, MVP_MATRIX,
      TEXTURE,
      MASK,
      COLOR
    )
    result[BLIT_WITH_MASK_LIGHT] = setupValues(
      BLIT_VSH,
      BLIT_WITH_MASK_LIGHT_FSH,
      MVP_MATRIX,
      TEXTURE,
      MASK,
      COLOR
    )
    result[COMPOSITE_WITH_MASK] = setupValues(
      BLIT_VSH,
      COMPOSITE_WITH_MASK_FSH,
      MVP_MATRIX,
      MASK,
      COLOR
    )
    result[COMPOSITE_WITH_MASK_LIGHT] = setupValues(
      BLIT_VSH,
      COMPOSITE_WITH_MASK_LIGHT_FSH,
      MVP_MATRIX,
      TEXTURE,
      MASK,
      COLOR
    )
    result[NON_PRE_MULTIPLIED_BLIT] = setupValues(
      BLIT_VSH,
      NON_PRE_MULTIPLIED_BLIT_FSH,
      MVP_MATRIX,
      TEXTURE
    )
    return Collections.unmodifiableMap(result)
  }
  
  private fun setupValues(
    vertexShader: String,
    fragmentShader: String,
    vararg uniforms: String
  ): Shader {
    return Shader(vertexShader, fragmentShader, *uniforms)
  }
}