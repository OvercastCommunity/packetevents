/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.retrooper.packetevents;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.util.TimeStampMode;
import com.github.retrooper.packetevents.util.adventure.AdventureLoader;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;
import java.util.Set;

public class PacketEventsPlugin extends JavaPlugin {

    private final ClassLoader injectionLoader;
    private final Set<Path> injectedJars;

    public PacketEventsPlugin() {
        // Bukkit initializes the plugin fields from JavaPlugin's constructor before this body runs.
        // Legacy Bukkit constructs every plugin before invoking any onLoad callback, so the shared
        // parent must be populated here before dependent plugins can resolve their bundled Adventure.
        ClassLoader pluginLoader = PacketEventsPlugin.class.getClassLoader();
        ClassLoader parentLoader = pluginLoader.getParent();
        this.injectionLoader = parentLoader != null ? parentLoader : pluginLoader;
        this.injectedJars = AdventureLoader.injectAll(this.injectionLoader,
                this.getDataFolder().toPath().resolve("libraries"), this.getLogger());
    }

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        PacketEvents.getAPI().getSettings().debug(false).checkForUpdates(true).timeStampMode(TimeStampMode.MILLIS).reEncodeByDefault(true);
        PacketEvents.getAPI().init();
    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
        AdventureLoader.uninjectAll(this.injectionLoader, this.injectedJars);
    }
}
