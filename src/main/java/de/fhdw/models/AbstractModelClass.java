package de.fhdw.models;

import java.lang.reflect.Field;

public abstract interface AbstractModelClass {

   default public <T> T getChangesFrom(T newVersion) {
        try {
            for (Field field : newVersion.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                field.set(this, field.get(newVersion));
            }
        } catch (IllegalAccessException ignored) {
        }
        return (T) this;
    }
}
