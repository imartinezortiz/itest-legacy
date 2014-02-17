package es.itest.engine.test.business.entity;

public interface Adaptable {
  <T> T getAdapter(Class<T> clazz);
}
