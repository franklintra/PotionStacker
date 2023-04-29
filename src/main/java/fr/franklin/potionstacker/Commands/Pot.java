package fr.franklin.potionstacker.Commands;

import fr.franklin.potionstacker.Msg;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.potion.PotionData;

import java.util.*;
import java.util.stream.IntStream;

/**
 * @author @franklintra (362694)
 * @project PotionStacker
 */
public class Pot implements CommandExecutor {

    public static Set<Material> potionMaterials = new HashSet<Material>(Arrays.asList(
            Material.POTION,
            Material.SPLASH_POTION,
            Material.LINGERING_POTION
    ));
    private enum commandType {
        SELF,
        OTHER,
        ALL,
        INVALID
    }


    private final Map<commandType, Permission> permissions;

    public Pot() {
        this.permissions = new EnumMap<commandType, Permission>(commandType.class);
        this.permissions.put(commandType.SELF, new Permission("potstacker.pot"));
        this.permissions.put(commandType.OTHER, new Permission("potstacker.pot.other"));
        this.permissions.put(commandType.ALL, new Permission("potstacker.pot.all"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        System.out.println("Main command called");
        Objects.requireNonNull(sender, "Sender cannot be null");
        Objects.requireNonNull(command, "Command cannot be null");
        Objects.requireNonNull(label, "Label cannot be null");
        Objects.requireNonNull(args, "Arguments cannot be null");
        commandType type = getCommandType(sender, args);
        switch (type) {
            case ALL:
                if (sender.hasPermission(permissions.get(commandType.ALL))) {
                    potAll(sender.getServer().getOnlinePlayers());
                }
                else {
                    Msg.sendChat((Player) sender, "You do not have permission to pot all players");
                }
                break;
            case SELF:
                if (sender.hasPermission(permissions.get(commandType.SELF))) {
                    potPlayer((Player) sender);
                }
                else {
                    Msg.sendChat((Player) sender, "You do not have permission to pot yourself");
                }
                break;
            case OTHER:
                if (sender.hasPermission(permissions.get(commandType.OTHER))) {
                    potPlayer(Objects.requireNonNull(sender.getServer().getPlayer(args[0])));
                }
                else {
                    Msg.sendChat((Player) sender , "You do not have permission to pot other players");
                }
                break;
            case INVALID:
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvalid arguments"));
                return false;
        }


        return false;
    }

    private commandType getCommandType(CommandSender sender, String[] args) {
        commandType type = null;
        if (args.length > 1) {
            type = commandType.INVALID;
        }
        if (args.length == 0) {
            type = commandType.SELF;
        }
        if (args.length == 1) {
            if ("all".equalsIgnoreCase(args[0])) {
                type = commandType.ALL;
            } else {
                Player target = sender.getServer().getPlayer(args[0]);
                if (target != null) {
                    if (target.equals(sender)) {
                        type = commandType.SELF;
                    }
                    else {
                        type = commandType.OTHER;
                    }
                }
                else {
                    type = commandType.INVALID;
                }
            }
        }
        return type;
    }

    private void potPlayer(Player player) {
        Msg.sendChat(player, "Potting player " + player.getName());
        ItemStack item = new ItemStack(Material.POTION, 10);
        Map<Material, Map<PotionMeta, Integer>> potions = new HashMap<>();
        //iterate through the player's inventory and add all potions to the collection and remove them from the player's inventory
        Arrays.stream(player.getInventory().getContents()).filter(Objects::nonNull).filter(i -> potionMaterials.contains(i.getType())).forEach(i -> {
            potions.merge(i.getType(), new HashMap<PotionMeta, Integer>() {{
                put((PotionMeta) i.getItemMeta(), i.getAmount());
            }}, (oldValue, newValue) -> {
                oldValue.merge((PotionMeta) i.getItemMeta(), i.getAmount(), Integer::sum);
                return oldValue;
            });
            player.getInventory().remove(i);
        });

        //iterate through the collection and add the potions back to the player's inventory

        potions.forEach((potion, metaAndValue) -> {
            metaAndValue.forEach((meta, amount) -> {
                int stacks = amount / 64;
                int remainder = amount % 64;
                ItemStack pot = new ItemStack(potion, 64);
                pot.setItemMeta(meta);
                IntStream.range(0, stacks).forEach(i -> {
                    player.getInventory().addItem(pot);
                });
                pot.setAmount(remainder);
                player.getInventory().addItem(pot);
                System.out.println("Added " + stacks + " stacks and " + remainder + " of " + meta.getBasePotionData().getType() + " to player " + player.getName());
            });
        });

        System.out.println(potions);
    }

    private void potAll(Collection<? extends Player> players) {
        players.forEach(this::potPlayer);
    }
}