package dev.thillainirmal.localmind.service;
import dev.thillainirmal.localmind.model.WorkloadRequest;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
class LocalMindEngineTest {
    private final LocalMindEngine engine=new LocalMindEngine(new SafeToolRegistry());
    @Test void secretsStayLocal(){var r=engine.route(new WorkloadRequest("summarize secret notes",WorkloadRequest.DataClass.SECRET,6000,true,false));assertThat(r.target()).isEqualTo("ollama");assertThat(r.cloudBlocked()).isTrue();}
    @Test void secretsAreBlockedWhenLocalIsOffline(){var r=engine.route(new WorkloadRequest("secret notes",WorkloadRequest.DataClass.SECRET,6000,false,true));assertThat(r.target()).isEqualTo("blocked");}
    @Test void longPublicContextFallsBackToCloud(){var r=engine.route(new WorkloadRequest("analyze public report",WorkloadRequest.DataClass.PUBLIC,64000,true,true));assertThat(r.target()).isEqualTo("cloud");}
    @Test void safeToolBypassesModel(){var r=engine.route(new WorkloadRequest("What is the current time?",WorkloadRequest.DataClass.COMPANY,200,true,false));assertThat(r.target()).isEqualTo("tool");assertThat(r.tool()).isEqualTo("current_time");assertThat(r.confidence()).isEqualTo(99);}
    @Test void localOfflineWithoutFallbackBlocks(){var r=engine.route(new WorkloadRequest("draft memo",WorkloadRequest.DataClass.PUBLIC,2000,false,false));assertThat(r.target()).isEqualTo("blocked");}
}
