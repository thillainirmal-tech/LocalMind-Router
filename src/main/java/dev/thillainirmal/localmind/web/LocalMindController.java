package dev.thillainirmal.localmind.web;
import dev.thillainirmal.localmind.model.RoutingPlan;
import dev.thillainirmal.localmind.model.WorkloadRequest;
import dev.thillainirmal.localmind.service.LocalMindEngine;
import dev.thillainirmal.localmind.model.ExecutionResult;
import dev.thillainirmal.localmind.service.WorkloadExecutionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController @RequestMapping("/api")
public class LocalMindController {
    private final LocalMindEngine engine;
    private final WorkloadExecutionService executionService;
    public LocalMindController(LocalMindEngine engine, WorkloadExecutionService executionService){this.engine=engine;this.executionService=executionService;}
    @PostMapping("/route") public RoutingPlan route(@Valid @RequestBody WorkloadRequest request){return engine.route(request);}
    @PostMapping("/execute") public ExecutionResult execute(@Valid @RequestBody WorkloadRequest request){return executionService.execute(request);}
    @GetMapping("/demo") public Map<String,RoutingPlan> demo(){
        return Map.of("private",engine.route(new WorkloadRequest("Summarize secret acquisition notes", WorkloadRequest.DataClass.SECRET,6000,true,false)),
                "tool",engine.route(new WorkloadRequest("What is the current time?", WorkloadRequest.DataClass.COMPANY,200,true,false)));
    }
}
