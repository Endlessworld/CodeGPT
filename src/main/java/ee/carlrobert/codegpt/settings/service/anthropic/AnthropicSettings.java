package ee.carlrobert.codegpt.settings.service.anthropic;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import ee.carlrobert.codegpt.credentials.AnthropicCredentialsManager;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

@State(name = "CodeGPT_AnthropicSettings", storages = @Storage("CodeGPT_AnthropicSettings.xml"))
public class AnthropicSettings implements PersistentStateComponent<AnthropicSettingsState> {

  private AnthropicSettingsState state = new AnthropicSettingsState();

  @Override
  @NotNull
  public AnthropicSettingsState getState() {
    return state;
  }

  @Override
  public void loadState(@NotNull AnthropicSettingsState state) {
    this.state = state;
  }

  public static AnthropicSettingsState getCurrentState() {
    return getInstance().getState();
  }

  public static AnthropicSettings getInstance() {
    return ApplicationManager.getApplication().getService(AnthropicSettings.class);
  }

  public boolean isModified(AnthropicSettingsForm form) {
    return !form.getCurrentState().equals(state)
        || !StringUtils.equals(
        form.getApiKey(),
        AnthropicCredentialsManager.getInstance().getCredential());
  }
}
