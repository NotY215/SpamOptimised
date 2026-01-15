package pro.noty.spam.gui;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;
import pro.noty.spam.SpamOptimised;

import java.util.Arrays;

public class ConfigGui implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.literal("Spam Optimised by NotY215"));

            ConfigCategory general = builder.getOrCreateCategory(Text.literal("Combat Settings"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            // 1. Global Toggle
            general.addEntry(entryBuilder.startBooleanToggle(Text.literal("Mod Enabled"), SpamOptimised.enabled)
                    .setDefaultValue(false)
                    .setSaveConsumer(newValue -> SpamOptimised.enabled = newValue)
                    .build());

            // 2. CPS Slider
            general.addEntry(entryBuilder.startIntSlider(Text.literal("Clicks Per Second (CPS)"), SpamOptimised.cps, 1, 20)
                    .setDefaultValue(10)
                    .setTooltip(Text.literal("Higher CPS may trigger Anti-Cheat on some servers."))
                    .setSaveConsumer(newValue -> SpamOptimised.cps = newValue)
                    .build());

            // 3. PVP Mode Selector
            general.addEntry(entryBuilder.startStringDropdownMenu(Text.literal("PVP Mode"), SpamOptimised.currentMode)
                    .setSelections(Arrays.asList("Sword", "Axe", "Mace", "Crystal", "Anchor", "Bow"))
                    .setDefaultValue("Sword")
                    .setSaveConsumer(newValue -> SpamOptimised.currentMode = newValue)
                    .build());

            // 4. Hit Level / Logic Type
            general.addEntry(entryBuilder.startBooleanToggle(Text.literal("Auto-Crit (Max Power)"), true)
                    .setDefaultValue(true)
                    .setTooltip(Text.literal("Forces critical hits even when standing still."))
                    .build());

            return builder.build();
        };
    }
}