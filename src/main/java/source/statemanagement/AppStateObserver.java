package POC.statemanagement;

import java.io.IOException;

public class AppStateObserver {
    public void addListener(AppStateStoreListener listener);
    public void removeListener(AppStateStoreListener listener);
    public void notifyListeners() throws IOException;
}
