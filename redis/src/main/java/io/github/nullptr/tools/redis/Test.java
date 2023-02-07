package io.github.nullptr.tools.redis;

import io.github.nullptr.tools.annotations.BuilderArgumentGenerator;
import io.github.nullptr.tools.annotations.BuilderGenerator;

@BuilderGenerator
public class Test {

    @BuilderArgumentGenerator(name = "manager", required = true, defaultValue = "RedisManager::new")
    private RedisManager manager;
}
