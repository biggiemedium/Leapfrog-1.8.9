package dev.px.leapfrog.Client.Manager.Structures;

import dev.px.leapfrog.API.Event.Player.PlayerSendChatEvent;
import dev.px.leapfrog.API.Util.Render.ChatUtil;
import dev.px.leapfrog.Client.Command.Command;
import dev.px.leapfrog.Client.Command.Impl.ConfigCommand;
import dev.px.leapfrog.LeapFrog;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CommandManager {

    private ArrayList<Command> commands = new ArrayList<>();

    public CommandManager() {
        LeapFrog.EVENT_BUS.post(this);
        Add(new ConfigCommand());
    }

    private void Add(Command command) {
        this.commands.add(command);
    }

    public <T extends Command> T get(String name, Class<T> clazz) {
        return clazz.cast(commands.stream()
                .filter(command -> command.getName().equalsIgnoreCase(name))
                .findAny()
                .orElse(null));
    }

    public boolean isCommandRegistered(Class<? extends Command> clazz) {
        return commands.stream().anyMatch(cmd -> clazz.isInstance(cmd));
    }

    public List<Command> getCommands() {
        return Collections.unmodifiableList(commands);
    }

    /**
     * @author Rise Client
     */
    @EventHandler
    private Listener<PlayerSendChatEvent> sendChatEventListener = new Listener<>(event -> {

    });
}
