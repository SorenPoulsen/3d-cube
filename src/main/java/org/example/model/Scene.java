package org.example.model;

import org.example.render.RenderAction;
import org.example.shader.ShaderSource;

import java.util.List;

/**
 * @author Søren Thalbitzer Poulsen
 */
public class Scene {

    private final List<Model> models;
    private final Color backgroundColor;
    List<ShaderSource> shaderSources;
    List<RenderAction> renderActions;

    public Scene(List<Model> models, List<ShaderSource> shaderSources, List<RenderAction> renderActions, Color backgroundColor) {
        if (models == null || shaderSources == null || backgroundColor == null) {
            throw new IllegalArgumentException("All Scene parameters must be non-null");
        }
        this.models = models;
        this.shaderSources = shaderSources;
        this.renderActions = renderActions;
        this.backgroundColor = backgroundColor;
    }

    public List<Model> getModels() {
        return models;
    }

    public List<ShaderSource> getShaderSources() {
        return shaderSources;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public List<RenderAction> getRenderActions() {
        return renderActions;
    }
}
