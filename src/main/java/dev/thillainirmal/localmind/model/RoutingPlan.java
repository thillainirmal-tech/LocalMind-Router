package dev.thillainirmal.localmind.model;
import java.util.List;
public record RoutingPlan(String target,String model,String execution,String tool,String toolResult,
                          boolean cloudBlocked,int confidence,List<String> trace) {}
