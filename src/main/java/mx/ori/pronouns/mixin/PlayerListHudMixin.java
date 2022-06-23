package mx.ori.pronouns.mixin;

import mx.ori.pronouns.PronounsMod;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/PlayerListHud;getPlayerName(Lnet/minecraft/client/network/PlayerListEntry;)Lnet/minecraft/text/Text;"))
    public Text getPlayerName(PlayerListHud instance, PlayerListEntry entry) {
        var text = instance.getPlayerName(entry).getString();
        var pronouns = PronounsMod.pronounsMap.get(instance.getPlayerName(entry).getString());
        if(pronouns != null) {
            return Text.of(String.format("%s (%s)", text, pronouns));
        } else {
            return Text.of(text);
        }
    }
}
