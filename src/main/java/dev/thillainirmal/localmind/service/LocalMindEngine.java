package dev.thillainirmal.localmind.service;

import dev.thillainirmal.localmind.model.RoutingPlan;
import dev.thillainirmal.localmind.model.WorkloadRequest;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class LocalMindEngine {
    private final SafeToolRegistry tools;
    public LocalMindEngine(SafeToolRegistry tools){this.tools=tools;}

    public RoutingPlan route(WorkloadRequest request){
        List<String> trace=new ArrayList<>();
        trace.add("Classified data as "+request.dataClass());
        boolean cloudBlocked=request.dataClass()==WorkloadRequest.DataClass.SECRET
                || request.dataClass()==WorkloadRequest.DataClass.SENSITIVE;
        if(cloudBlocked) trace.add("Cloud egress denied by privacy policy");

        var tool=tools.match(request.prompt());
        if(tool.isPresent()){
            var call=tool.get();
            trace.add("Matched safe tool "+call.name()+" with "+call.safety()+" policy");
            return new RoutingPlan("tool","deterministic", "LOCAL",call.name(),call.output(),cloudBlocked,99,trace);
        }

        if(request.localRuntimeOnline() && (cloudBlocked || request.contextTokens()<=32_000)){
            String model=request.contextTokens()>16_000?"qwen2.5:32b":"llama3.2:8b";
            trace.add("Local runtime satisfies context and privacy constraints");
            return new RoutingPlan("ollama",model,"LOCAL",null,null,cloudBlocked,92,trace);
        }
        if(cloudBlocked){
            trace.add("Request blocked: private workload cannot leave the device");
            return new RoutingPlan("blocked","none","NONE",null,null,true,100,trace);
        }
        if(!request.allowCloudFallback()){
            trace.add("Request blocked: local runtime unavailable and cloud fallback disabled");
            return new RoutingPlan("blocked","none","NONE",null,null,false,100,trace);
        }
        trace.add("Local capacity insufficient; selected approved cloud fallback");
        return new RoutingPlan("cloud","gemini-2.5-pro","CLOUD",null,null,false,88,trace);
    }
}
