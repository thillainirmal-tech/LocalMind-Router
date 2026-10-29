package dev.thillainirmal.localmind.model;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public record WorkloadRequest(@NotBlank @Size(max=5000) String prompt, DataClass dataClass,
                              int contextTokens, boolean localRuntimeOnline, boolean allowCloudFallback) {
    public enum DataClass { PUBLIC, COMPANY, SENSITIVE, SECRET }
    public WorkloadRequest {
        dataClass=dataClass==null?DataClass.COMPANY:dataClass;
        contextTokens=contextTokens<=0?2000:contextTokens;
    }
}
