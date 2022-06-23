package mx.ori.pronouns.mixin;

import mx.ori.pronouns.PronounsMod;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.message.MessageSender;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(method="handleMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;onChatMessage(Lnet/minecraft/network/message/MessageType;Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSender;)V"), cancellable = true)
    public void handleMessage(MessageType type, SignedMessage message, MessageSender sender, CallbackInfo ci) {
        Matcher withPronouns = Pattern.compile("#pronouns (.*)").matcher(message.getContent().getString());
        Matcher withoutPronouns = Pattern.compile("#pronouns").matcher(message.getContent().getString());

        if (withPronouns.matches()) {
            PronounsMod.pronounsMap.put(sender.name().getString(), withPronouns.group(1));
            ci.cancel();
        } else if (withoutPronouns.matches()) {
            PronounsMod.pronounsMap.remove(sender.name().getString());
            ci.cancel();
        }
    }
}
