package xyz.nkomarn.Phase;

import net.milkbowl.vault.economy.Economy;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.nkomarn.Kerosene.database.mongo.MongoDatabase;
import xyz.nkomarn.Kerosene.database.mongo.subscribers.SyncAsyncCollection;
import xyz.nkomarn.Phase.command.SetWarpCommand;
import xyz.nkomarn.Phase.command.WarpAdminCommand;
import xyz.nkomarn.Phase.command.WarpCommand;
import xyz.nkomarn.Phase.listener.InventoryClickListener;
import xyz.nkomarn.Phase.util.Search;

public class Phase extends JavaPlugin {
    private static Phase instance;
    private static SyncAsyncCollection<Document> warps;
    private static Economy economy = null;

    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        if (!initializeEconomy()) {
            return;
        }

        final String database = getConfig().getString("database");
        warps = MongoDatabase.getSyncAsyncCollection(database, "warps");
        Search.read();

        PluginCommand warpCommand = getCommand("warp");
        warpCommand.setExecutor(new WarpCommand());
        warpCommand.setTabCompleter(new WarpCommand());
        PluginCommand warpAdminCommand = getCommand("warpadmin");
        warpAdminCommand.setExecutor(new WarpAdminCommand());
        warpAdminCommand.setTabCompleter(new WarpAdminCommand());
        PluginCommand setWarpCommand = getCommand("setwarp");
        setWarpCommand.setExecutor(new SetWarpCommand());
        setWarpCommand.setTabCompleter(new SetWarpCommand());
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);

        getServer().getScheduler().scheduleSyncRepeatingTask(this, Search::sort, 0, 60 * 20);
    }

    public void onDisable() {

    }

    public static Phase getInstance() {
        return instance;
    }

    public static SyncAsyncCollection<Document> getCollection() {
        return warps;
    }

    public static Economy getEconomy() {
        return economy;
    }

    private boolean initializeEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().severe("Phase requires Vault to operate.");
            getServer().getPluginManager().disablePlugin(instance);
        }
        RegisteredServiceProvider<Economy> provider = getServer().getServicesManager()
                .getRegistration(Economy.class);
        if (provider == null) return false;
        economy = provider.getProvider();
        return true;
    }
}
