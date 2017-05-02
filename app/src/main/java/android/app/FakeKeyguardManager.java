package android.app;

/**
 * Created by GGF on 17/5/1;
 */

public class FakeKeyguardManager extends KeyguardManager {
    @Override
    public boolean inKeyguardRestrictedInputMode() {
        return false;
    }
}
