package dev.px.leapfrog.Client.Command;

import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.Client.Module.Module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public abstract class Command {

    private String name, description;
    private String[] arguments;

    public Command() {
        this.name = getCommand().name();
        this.description = getCommand().description();
        this.arguments = getCommand().arguments();
    }

    public abstract void execute(String[] args);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getArguments() {
        return arguments;
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }

    protected Command.CommandInterface getCommand() {
        return getClass().getAnnotation(Command.CommandInterface.class);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface CommandInterface {

        String name();

        String description() default "";

        String[] arguments();

    }

}
