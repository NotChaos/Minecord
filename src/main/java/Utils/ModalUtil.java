package Utils;

import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class ModalUtil {
    public static TextInput createTextInput(String id, String label, TextInputStyle style, String placeholder, int minLength, boolean isRequired) {
        return TextInput.create(id, label, style)
                .setPlaceholder(placeholder)
                .setMinLength(minLength)
                .setRequired(isRequired)
                .build();
    }

    public static Modal createModal(String id, String title, TextInput input) {
        return Modal.create(id, title)
                .addActionRows(ActionRow.of(input))
                .build();
    }
}
