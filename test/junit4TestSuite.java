import com.java.controller.gameplay.ReinforcementTest;
import com.java.controller.gameplay.FortificationTest;
import com.java.controller.map.MapTest;
import com.java.controller.startup.StartUpPhaseTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ReinforcementTest.class, FortificationTest.class, StartUpPhaseTest.class, MapTest.class})
public class junit4TestSuite {
}
