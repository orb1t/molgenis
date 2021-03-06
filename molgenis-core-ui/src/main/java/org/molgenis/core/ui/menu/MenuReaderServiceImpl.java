package org.molgenis.core.ui.menu;

import static java.util.Objects.requireNonNull;

import com.google.gson.GsonBuilder;
import org.molgenis.security.core.runas.RunAsSystem;
import org.molgenis.settings.AppSettings;

public class MenuReaderServiceImpl implements MenuReaderService {
  private final AppSettings appSettings;

  public MenuReaderServiceImpl(AppSettings appSettings) {
    this.appSettings = requireNonNull(appSettings);
  }

  @Override
  @RunAsSystem
  public Menu getMenu() {
    String menuJson = appSettings.getMenu();
    return menuJson != null ? new GsonBuilder().create().fromJson(menuJson, Menu.class) : null;
  }
}
