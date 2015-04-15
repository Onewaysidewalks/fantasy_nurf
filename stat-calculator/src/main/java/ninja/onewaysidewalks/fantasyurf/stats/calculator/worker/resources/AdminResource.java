package ninja.onewaysidewalks.fantasyurf.stats.calculator.worker.resources;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("stats")
public interface AdminResource {
    @Path("/calculate")
    @POST
    void calculate(CalculateRequest request);
}
