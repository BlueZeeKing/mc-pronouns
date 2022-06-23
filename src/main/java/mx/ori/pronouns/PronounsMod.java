package mx.ori.pronouns;

import draylar.omegaconfig.OmegaConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.Style;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

public class PronounsMod implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("pronouns");
	public static final PronounsConfig CONFIG = OmegaConfig.register(PronounsConfig.class);

	public static final HashMap<String, String> pronounsMap = new HashMap<>();

	private static MinecraftClient client = null;

	@Override
	public void onInitializeClient() {
		ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
			if (screen instanceof TitleScreen) {
				var buttons = Screens.getButtons(screen);

				buttons.get(3).y += 12;
				buttons.get(4).y += 12;
				buttons.get(5).y += 12;
				buttons.get(6).y += 12;

				final int width = 200;
				var lit = ((MutableText)Text.of("Pronouns")).fillStyle(Style.EMPTY.withColor(0xFF7DBE));
				var button = new ButtonWidget(scaledWidth / 2 - width / 2, buttons.get(2).y + 24, width, 20, lit, ignore -> client.setScreen(new PronounsScreen(screen, client.options)));
				Screens.getButtons(screen).add(button);
			}
		});

		ClientPlayConnectionEvents.JOIN.register((handler, sender, cli) -> {
			client = cli;
			broadcast();
		});

		ClientPlayConnectionEvents.DISCONNECT.register((handler, cli) -> pronounsMap.clear());
	}

	public static void broadcast() {
		assert client.player != null;
		String fmt = CONFIG.pronouns == null ? "#pronouns" : "#pronouns %s";
		client.player.sendChatMessage(String.format(fmt, CONFIG.pronouns));
	}

	public static void whisper(String to) {
		assert client.player != null;
		if(!to.equals(client.player.getName().toString())) {
			String fmt = CONFIG.pronouns == null ? "/tell %s #pronouns" : "/tell %s #pronouns %s";
			client.player.sendCommand(String.format(fmt, to, CONFIG.pronouns));
		}
	}
}
