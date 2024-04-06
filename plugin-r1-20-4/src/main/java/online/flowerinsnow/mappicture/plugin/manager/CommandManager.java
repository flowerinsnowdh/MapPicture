package online.flowerinsnow.mappicture.plugin.manager;

import online.flowerinsnow.mappicture.plugin.MapPicturePlugin;
import online.flowerinsnow.mappicture.plugin.command.CommandMapPicture;
import online.flowerinsnow.mappicture.plugin.config.Messages;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.exception.InvalidCommandSenderException;
import org.incendo.cloud.exception.InvalidSyntaxException;
import org.incendo.cloud.exception.NoPermissionException;
import org.incendo.cloud.exception.handling.ExceptionController;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import org.jetbrains.annotations.NotNull;

public class CommandManager {
    @NotNull private final MapPicturePlugin plugin;
    @NotNull private final PaperCommandManager<CommandSender> commandManager;
    @NotNull private final AnnotationParser<CommandSender> annotationParser;

    public CommandManager(@NotNull MapPicturePlugin plugin) {
        this.plugin = plugin;
        this.commandManager = new PaperCommandManager<>(
                plugin, ExecutionCoordinator.simpleCoordinator(), SenderMapper.identity()
        );
        this.annotationParser = new AnnotationParser<>(commandManager, CommandSender.class);

        this.registerCommands();
        this.registerExceptionHandlers();
    }

    private void registerCommands() {
        this.annotationParser.parse(new CommandMapPicture(this.plugin));
    }

    private void registerExceptionHandlers() {
        ExceptionController<CommandSender> exceptionController = this.commandManager.exceptionController();
        exceptionController.registerHandler(InvalidSyntaxException.class, context ->
            Messages.Command.INVALID_SYNTAX.send(context.context().sender(), context.exception().correctSyntax())
        );
        exceptionController.registerHandler(InvalidCommandSenderException.class, context ->
            Messages.Command.INVALID_SENDER.send(context.context().sender(), context.exception().requiredSender().getTypeName())
        );
        exceptionController.registerHandler(NoPermissionException.class, context ->
            Messages.Command.NO_PERMISSION.send(context.context().sender(), context.exception().missingPermission().permissionString())
        );
    }
}
