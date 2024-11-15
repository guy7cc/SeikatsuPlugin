package io.github.guy7cc.seikatsu.item;

import io.github.guy7cc.seikatsu.util.TranslationUtil;
import net.md_5.bungee.api.chat.TranslatableComponent;

public enum CustomItemInteractionResult {
    UNAVAILABLE(false, null),
    IN_COOLDOWN(false, null),
    FAIL_NO_TARGET(true, new TranslatableComponent(TranslationUtil.chatKey("noTarget"))),
    SUCCESS(false, null);

    public final boolean shouldShowMessage;
    public final TranslatableComponent message;

    CustomItemInteractionResult(boolean shouldShowMessage, TranslatableComponent message) {
        this.shouldShowMessage = shouldShowMessage;
        this.message = message;
    }
}
