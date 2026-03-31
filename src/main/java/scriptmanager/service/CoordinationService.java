package scriptmanager.service;

import java.util.List;
import java.util.Map;

public interface CoordinationService {
    List<Map<String, Object>> getFullSchedule();
    List<Map<String, Object>> getResourceUsageByTime();
}
