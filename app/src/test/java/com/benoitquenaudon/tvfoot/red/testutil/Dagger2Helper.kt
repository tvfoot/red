package com.benoitquenaudon.tvfoot.red.testutil

import java.lang.reflect.Method
import java.util.HashMap

object Dagger2Helper {
  private val methodsCache = HashMap<Class<*>, HashMap<Class<*>, Method>>()

  /**
   * This method is based on https://github.com/square/mortar/blob/
   * master/dagger2support/src/main/java/mortar/dagger2support/Dagger2.java
   * file that has been released with Apache License Version 2.0,
   * January 2004 http://www.apache.org/licenses/ by Square, Inc.
   *
   *
   * Magic method that creates a component with its dependencies set, by reflection. Relies on
   * Dagger2 naming conventions.
   */
  fun <T> buildComponent(componentClass: Class<T>, vararg dependencies: Any): T {
    buildMethodsCache(componentClass)

    val fqn = componentClass.name

    val packageName = componentClass.`package`.name
    // Accounts for inner classes, ie MyApplication$Component
    val simpleName = fqn.substring(packageName.length + 1)
    val generatedName = (packageName + ".Dagger" + simpleName).replace('$', '_')

    try {
      val generatedClass = Class.forName(generatedName)
      val builder = generatedClass.getMethod("builder").invoke(null)

      for (method in builder.javaClass.methods) {
        val params = method.parameterTypes
        if (params.size == 1) {
          val dependencyClass = params[0]
          for (dependency in dependencies) {
            if (dependencyClass.isAssignableFrom(dependency.javaClass)) {
              method.invoke(builder, dependency)
              break
            }
          }
        }
      }

      return builder.javaClass.getMethod("build").invoke(builder) as T
    } catch (e: Exception) {
      throw RuntimeException(e)
    }

  }

  private fun <T> buildMethodsCache(componentClass: Class<T>) {
    if (!Dagger2Helper.methodsCache.containsKey(componentClass)) {
      val methods = HashMap<Class<*>, Method>()
      for (method in componentClass.methods) {
        val params = method.parameterTypes
        if (params.size == 1) {
          methods.put(params[0], method)
        }
      }
      Dagger2Helper.methodsCache.put(componentClass, methods)
    }
  }
}
