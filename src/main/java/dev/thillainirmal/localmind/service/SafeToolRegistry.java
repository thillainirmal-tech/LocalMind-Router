package dev.thillainirmal.localmind.service;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

@Service
public class SafeToolRegistry {
    public Optional<ToolResult> match(String prompt) {
        String value=prompt.toLowerCase(Locale.ROOT);
        if(value.contains("current time")||value.contains("what time")) {
            String now=DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(Instant.now().atZone(ZoneId.of("UTC")));
            return Optional.of(new ToolResult("current_time","READ_ONLY",now));
        }
        if(value.matches(".*\\bhealth|.*system status.*")) return Optional.of(new ToolResult("runtime_health","READ_ONLY","LOCAL_RUNTIME_READY"));
        if(value.contains("token estimate")) return Optional.of(new ToolResult("token_estimator","PURE_FUNCTION",String.valueOf(Math.max(1,prompt.length()/4))));
        return Optional.empty();
    }
    public record ToolResult(String name,String safety,String output){}
}
