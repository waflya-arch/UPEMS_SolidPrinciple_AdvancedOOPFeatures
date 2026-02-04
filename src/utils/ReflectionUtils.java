package utils;

import java.lang.reflect.*;
import java.util.Arrays;

public class ReflectionUtils {

    public static <T> void inspectClass(Class<T> clazz) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("REFLECTION INSPECTION FOR: " + clazz.getName());
        System.out.println("=".repeat(70));

        // Class information
        System.out.println("\n[CLASS INFORMATION]");
        System.out.println("Simple Name: " + clazz.getSimpleName());
        System.out.println("Package: " + clazz.getPackage().getName());
        System.out.println("Modifiers: " + Modifier.toString(clazz.getModifiers()));
        System.out.println("Is Abstract: " + Modifier.isAbstract(clazz.getModifiers()));
        System.out.println("Is Interface: " + clazz.isInterface());

        // Superclass
        System.out.println("\n[INHERITANCE]");
        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null) {
            System.out.println("Superclass: " + superclass.getName());
        } else {
            System.out.println("Superclass: None");
        }

        // Interfaces
        System.out.println("\n[INTERFACES IMPLEMENTED]");
        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length > 0) {
            for (Class<?> iface : interfaces) {
                System.out.println("  - " + iface.getName());
            }
        } else {
            System.out.println("  No interfaces implemented");
        }

        // Fields
        System.out.println("\n[FIELDS]");
        Field[] fields = clazz.getDeclaredFields();
        if (fields.length > 0) {
            for (Field field : fields) {
                System.out.printf("  %s %s %s%n",
                        Modifier.toString(field.getModifiers()),
                        field.getType().getSimpleName(),
                        field.getName());
            }
        } else {
            System.out.println("  No fields declared");
        }

        // Constructors
        System.out.println("\n[CONSTRUCTORS]");
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            System.out.print("  " + Modifier.toString(constructor.getModifiers()) + " ");
            System.out.print(clazz.getSimpleName() + "(");
            Class<?>[] paramTypes = constructor.getParameterTypes();
            for (int i = 0; i < paramTypes.length; i++) {
                System.out.print(paramTypes[i].getSimpleName());
                if (i < paramTypes.length - 1) System.out.print(", ");
            }
            System.out.println(")");
        }

        // Methods
        System.out.println("\n[METHODS]");
        Method[] methods = clazz.getDeclaredMethods();
        if (methods.length > 0) {
            for (Method method : methods) {
                System.out.printf("  %s %s %s(",
                        Modifier.toString(method.getModifiers()),
                        method.getReturnType().getSimpleName(),
                        method.getName());

                Class<?>[] paramTypes = method.getParameterTypes();
                for (int i = 0; i < paramTypes.length; i++) {
                    System.out.print(paramTypes[i].getSimpleName());
                    if (i < paramTypes.length - 1) System.out.print(", ");
                }
                System.out.println(")");
            }
        } else {
            System.out.println("  No methods declared");
        }

        System.out.println("\n" + "=".repeat(70) + "\n");
    }

    /**
     * Inspects an object instance and prints its current field values
     */
    public static void inspectObject(Object obj) {
        Class<?> clazz = obj.getClass();
        System.out.println("\n[OBJECT INSTANCE INSPECTION: " + clazz.getSimpleName() + "]");

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true); // Access private fields
            try {
                Object value = field.get(obj);
                System.out.printf("  %s = %s%n", field.getName(), value);
            } catch (IllegalAccessException e) {
                System.out.printf("  %s = [inaccessible]%n", field.getName());
            }
        }
        System.out.println();
    }

    public static String[] getMethodNames(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .map(Method::getName)
                .toArray(String[]::new);
    }

    public static String[] getFieldNames(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .map(Field::getName)
                .toArray(String[]::new);
    }
}