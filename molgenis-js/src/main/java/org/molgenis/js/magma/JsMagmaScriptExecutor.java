package org.molgenis.js.magma;

import static java.util.Objects.requireNonNull;

import java.util.Map;
import org.molgenis.data.Entity;
import org.molgenis.data.meta.model.AttributeFactory;
import org.molgenis.data.meta.model.EntityType;
import org.molgenis.data.meta.model.EntityTypeFactory;
import org.molgenis.data.support.DynamicEntity;
import org.springframework.stereotype.Service;

/** Executes a JavaScript using the Magma API */
@Service
public class JsMagmaScriptExecutor {
  private final JsMagmaScriptEvaluator jsMagmaScriptEvaluator;
  private final EntityTypeFactory entityTypeFactory;
  private final AttributeFactory attributeFactory;

  public JsMagmaScriptExecutor(
      JsMagmaScriptEvaluator jsMagmaScriptEvaluator,
      EntityTypeFactory entityTypeFactory,
      AttributeFactory attributeFactory) {
    this.jsMagmaScriptEvaluator = jsMagmaScriptEvaluator;
    this.entityTypeFactory = requireNonNull(entityTypeFactory);
    this.attributeFactory = requireNonNull(attributeFactory);
  }

  /** Execute a JavaScript using the Magma API */
  Object executeScript(String jsScript, Map<String, Object> parameters) {
    EntityType entityType = entityTypeFactory.create("entity");
    parameters
        .keySet()
        .forEach(key -> entityType.addAttribute(attributeFactory.create().setName(key)));
    Entity entity = new DynamicEntity(entityType);
    parameters.forEach((key, value) -> entity.set(key, value));
    return jsMagmaScriptEvaluator.eval(jsScript, entity);
  }
}
